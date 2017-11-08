package anyclick.wips.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import anyclick.wips.repository.mapper.ApMapper;
import anyclick.wips.repository.mapper.CommandMapper;
import anyclick.wips.repository.mapper.GeneralPolicyMapper;
import anyclick.wips.repository.mapper.MapMapper;
import anyclick.wips.repository.mapper.PolicyExceptMapper;
import anyclick.wips.repository.mapper.PolicyMapper;
import anyclick.wips.repository.mapper.ProfileMapper;
import anyclick.wips.repository.mapper.StationMapper;
import anyclick.wips.repository.mapper.VendorMapper;
import anyclick.wips.util.QueryUtil;

@Repository
public class PolicyRepository {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	NamedParameterJdbcTemplate template;

	public List getProfileList() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT *, server_info_tbl.name as server_name FROM event_policy_profile_tbl as policy ");
		sql.append("LEFT JOIN server_info_tbl ");
		sql.append("ON policy.server_id = server_info_tbl.server_id ");
		sql.append("ORDER BY policy.server_id ");
		List result = template.query(sql.toString(), new ProfileMapper());
		Collections.sort(result, new sortCompare());
		return result;
	}

	public List getPolicyList() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM event_policy_tbl");
		List result = template.query(sql.toString(), new PolicyMapper());
		return result;
	}

	public List getGeneralPolicyList() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT *, DATE_FORMAT(saved_time, '%Y-%m-%d') AS saved_time FROM event_policy_general_tbl ORDER BY idx");
		List<Map> result = template.query(sql.toString(), new GeneralPolicyMapper());
		for (Map item : result) {
			List<Map> vendor_lst = getGeneralVendorList(Integer.parseInt(item.get("id").toString()));
			item.put("vendor_lst", vendor_lst);
			if (vendor_lst.size() != 0) {
				item.put("vendor_str", vendor_lst.get(0).get("vendor_str") + " 포함 " + vendor_lst.size() + "개");
			} else {
				item.put("vendor_str", "사용안함");
			}
		}
		return result;
	}

	public List getGeneralVendorList(int $id) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM general_vendor_policy WHERE general_idx = " + $id);
		List result = template.query(sql.toString(), new VendorMapper("GENERAL"));
		return result;
	}

	public List getPolicyVendorList() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM policy_5_6_vendor_list");
		List result = template.query(sql.toString(), new VendorMapper("POLICY"));
		return result;
	}

	public List getPolicyExceptList() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM policy_except_list");
		List result = template.query(sql.toString(), new PolicyExceptMapper());
		return result;
	}

	public List getMapList() {
		String query = " ORDER BY server_name, depth, map.map_id";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT *, server.name as server_name FROM map_info_tbl as map ");
		sql.append("LEFT JOIN event_policy_profile_tbl as profile ");
		sql.append("ON map.profile_id = profile.idx ");
		sql.append("LEFT JOIN server_info_tbl as server ");
		sql.append("ON map.server_id = server.server_id ");
		sql.append(query);
		List result = template.query(sql.toString(), new MapMapper());
		return result;
	}

	public Map getProfile(long $id) {
		String sql = "SELECT * FROM event_policy_profile_tbl WHERE idx = :id";
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", $id);
		Map result = null;
		try {
			result = (Map) template.queryForObject(sql, param, new ProfileMapper());
		} catch (EmptyResultDataAccessException e) {

		}
		return result;
	}

	public long insertProfile(Map<String, Object> $param) {
		KeyHolder key = new GeneratedKeyHolder();
		$param.put("reg_time", new Timestamp(System.currentTimeMillis()));
		$param.put("chg_time", new Timestamp(System.currentTimeMillis()));
		SqlParameterSource vo = new MapSqlParameterSource($param);
		List<String> except = new ArrayList();
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO event_policy_profile_tbl ");
		sql.append("(`name`, `type`, `use`, `priority`, `alert`, `sound`, `mail`, `block`, `cable_block`, `reg_time`, `chg_time`) ");
		sql.append("VALUES ");
		sql.append("(:name, :type, :use, :priority, :alert, :sound, :mail, :block, :cable_block, :reg_time, :chg_time) ");
		int result = template.update(sql.toString(), vo, key);
		return key.getKey().longValue();
	}

	public int updateProfile(Map<String, Object> $param) {
		$param.put("chg_time", new Timestamp(System.currentTimeMillis()));
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE event_policy_profile_tbl SET ");
		sql.append(
				"`name` = :name, `type` = :type, `use` = :use, `priority` = :priority, `alert` = :alert, `sound` = :sound, `mail` = :mail, `block` = :block, `cable_block` = :cable_block, `chg_time` = :chg_time ");
		sql.append("WHERE idx = :id");
		return template.update(sql.toString(), $param);
	}

	public int deleteProfile(long $id) {
		String sql = "DELETE FROM event_policy_profile_tbl WHERE idx = :id";
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", $id);
		int result = 0;
		result = template.update(sql, param);
		template.update("DELETE FROM event_policy_tbl WHERE profile_idx = :id", param);
		clearProfile($id);
		return result;
	}

	public void clearProfile(long $id) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", $id);
		template.update("DELETE FROM event_policy_general_tbl WHERE profile_idx = :id", param);
		template.update("DELETE FROM general_vendor_policy WHERE profile_idx = :id", param);
		template.update("DELETE FROM policy_5_6_vendor_list WHERE profile_idx = :id", param);
		template.update("DELETE FROM policy_except_list WHERE profile_idx = :id", param);
	}

	public long insertGeneralPolicyList(long $id, List<Map> $general_lst) {
		int result = 0;
		for (Map item : $general_lst) {
			KeyHolder key = new GeneratedKeyHolder();
			Map param = Maps.newHashMap();
			param.put("profile_idx", $id);
			param.put("cipher", item.get("cipher"));
			param.put("media", item.get("media"));
			param.put("auth", item.get("auth"));
			param.put("broadcast", item.get("broad"));

			Boolean like_search = Boolean.parseBoolean(item.get("like_search").toString());
			param.put("ssid", (like_search == true) ? "%" + item.get("ssid") + "%" : item.get("ssid"));
			String channel = ("".equals(item.get("channel_lst").toString())) ? "사용안함" : item.get("channel_lst").toString();
			String data_rate = ("".equals(item.get("rate_lst").toString())) ? "사용안함" : item.get("rate_lst").toString();
			String mcs_set = ("".equals(item.get("mcs_lst").toString())) ? "사용안함" : item.get("mcs_lst").toString();
			param.put("channel", channel);
			param.put("data_rate", data_rate);
			param.put("mcs_set", mcs_set);
			List<Map> vendor_lst = (List) item.get("vendor_lst");
			param.put("vendor", (vendor_lst.size() >= 1) ? 1 : -1);

			String query = QueryUtil.getInsertQuery(param, null);
			String sql = "INSERT INTO event_policy_general_tbl " + query;
			template.update(sql, new MapSqlParameterSource(param), key);
			long general_id = key.getKey().longValue();
			if (vendor_lst.size() >= 1) {
				List vendors = Lists.newArrayList();
				for (Map vo : vendor_lst) {
					Map v = Maps.newHashMap();
					v.put("profile_idx", $id);
					v.put("general_idx", general_id);
					v.put("company", vo.get("company"));
					v.put("oui", vo.get("oui"));
					vendors.add(v);
				}
				insertVendorList(vendors);
			}
		}
		return result;
	}

	public int insertVendorList(List<Map> $vendor_lst) {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch($vendor_lst.toArray(new HashMap[$vendor_lst.size()]));
		Map item = (Map) $vendor_lst.get(0);
		String query = QueryUtil.getInsertQuery(item, null);
		String sql = "INSERT INTO general_vendor_policy " + query;
		int[] result = template.batchUpdate(sql, batch);
		return result.length;
	}

	public int insertPolicyVendorList(long $id, List<Map> $vendor_lst) {
		for (Map item : $vendor_lst) {
			item.put("profile_idx", $id);
		}
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch($vendor_lst.toArray(new HashMap[$vendor_lst.size()]));
		Map item = (Map) $vendor_lst.get(0);
		String query = QueryUtil.getInsertQuery(item, null);
		String sql = "INSERT INTO policy_5_6_vendor_list " + query;
		int[] result = template.batchUpdate(sql, batch);
		return result.length;
	}

	public int insertPolicyExceptList(long $id, List<Map> $except_lst) {
		for (Map item : $except_lst) {
			item.put("profile_idx", $id);
		}
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch($except_lst.toArray(new HashMap[$except_lst.size()]));
		Map item = (Map) $except_lst.get(0);
		String query = QueryUtil.getInsertQuery(item, null);
		String sql = "INSERT INTO policy_except_list " + query;
		int[] result = template.batchUpdate(sql, batch);
		return result.length;
	}

	public int insertPolicyList(long $id, List<Map> $policy_lst) {
		for (Map item : $policy_lst) {
			item.put("profile_idx", $id);
		}
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch($policy_lst.toArray(new HashMap[$policy_lst.size()]));
		Map param = (Map) $policy_lst.get(0);
		String query = QueryUtil.getInsertQuery(param, null);
		String sql = "INSERT INTO event_policy_tbl " + query;
		log.info(sql);
		int[] result = template.batchUpdate(sql, batch);
		return result.length;
	}

	public long insertPolicyCommand(int $type, int $sub_type, int $status, long $id, String $name) {
		KeyHolder key = new GeneratedKeyHolder();
		Map param = Maps.newHashMap();
		param.put("reg_time", new Timestamp(System.currentTimeMillis()));
		param.put("type", $type);
		param.put("sub_type", $sub_type);
		param.put("status", $status);
		param.put("target", $id);
		param.put("target_name", $name);
		SqlParameterSource vo = new MapSqlParameterSource(param);
		String query = QueryUtil.getInsertQuery(param, null);
		String sql = "INSERT INTO command_tbl " + query;
		int result = template.update(sql, vo, key);
		return key.getKey().longValue();
	}

	public int insertPolicyCommandDetail(List<Map> $details) {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch($details.toArray(new HashMap[$details.size()]));
		Map param = Maps.newHashMap();
		param.put("command_id", "");
		param.put("server_id", "");
		param.put("server_name", "");
		String query = QueryUtil.getInsertQuery(param, null);
		String sql = "INSERT INTO command_profile_tbl " + query;
		int[] result = template.batchUpdate(sql, batch);
		int sum = 0;
		for (int item : result) {
			sum += item;
		}
		return sum;
	}

	public List getPolicyCommandStatus(long $id) {
		// 테스트용
		updatePolicyCommandStatus($id);
		Map<String, Object> param = Maps.newHashMap();
		param.put("command_id", $id);
		param.put("status", 3);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM command_profile_tbl WHERE command_id = :command_id AND status >= :status");
		List result = template.query(sql.toString(), param, new CommandMapper("DETAIL"));
		return result;
	}

	public int updatePolicyCommandStatus(long $id) {
		long idx = template.queryForObject("SELECT id FROM command_profile_tbl WHERE status < 3 AND command_id = " + $id + " LIMIT 1", Maps.newHashMap(), Long.class);

		Map<String, Object> param = Maps.newHashMap();
		param.put("command_id", $id);
		param.put("status", 3);
		param.put("id", idx);
		String sql = "UPDATE command_profile_tbl SET status = :status WHERE command_id = :command_id AND id = :id";
		return template.update(sql, param);
	}

	public List getApList() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ap_mng_list");
		List result = template.query(sql.toString(), new ApMapper());
		return result;
	}

	public List getStationList() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM sta_mng_list");
		List result = template.query(sql.toString(), new StationMapper());
		return result;
	}

	public long insertApList(List<Map<String, Object>> $param) {
		template.update("DELETE FROM ap_mng_list WHERE server_id = 0", Maps.newHashMap());

		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch($param.toArray(new HashMap[$param.size()]));
		Map param = (Map) $param.get(0);
		String query = QueryUtil.getInsertQuery(param, null);
		String sql = "INSERT INTO ap_mng_list " + query;
		int[] result = template.batchUpdate(sql, batch);
		int sum = 0;
		for (int item : result) {
			sum += item;
		}
		return sum;
	}

	public long insertStationList(List<Map<String, Object>> $param) {
		template.update("DELETE FROM sta_mng_list WHERE server_id = 0", Maps.newHashMap());

		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch($param.toArray(new HashMap[$param.size()]));
		Map param = (Map) $param.get(0);
		String query = QueryUtil.getInsertQuery(param, null);
		String sql = "INSERT INTO sta_mng_list " + query;
		int[] result = template.batchUpdate(sql, batch);
		int sum = 0;
		for (int item : result) {
			sum += item;
		}
		return sum;
	}

	class sortCompare implements Comparator<Map> {

		@Override
		public int compare(Map arg0, Map arg1) {
			return arg0.get("sort_order").toString().compareTo(arg1.get("sort_order").toString());
		}
	}
}
