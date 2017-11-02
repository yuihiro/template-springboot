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

import com.google.common.collect.Maps;

import anyclick.wips.repository.mapper.ApMapper;
import anyclick.wips.repository.mapper.CommandMapper;
import anyclick.wips.repository.mapper.MapMapper;
import anyclick.wips.repository.mapper.PolicyMapper;
import anyclick.wips.repository.mapper.ProfileMapper;
import anyclick.wips.repository.mapper.StationMapper;
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
		template.update("delete from event_policy_general_tbl where profile_idx = :id", param);
		template.update("delete from general_vendor_policy where profile_id = :id", param);
		template.update("delete from policy_5_6_vendor_list where profile_id = :id", param);
		template.update("delete from policy_except_list where profile_id = :id", param);
		return result;
	}

	public int deletePolicyList(long $id) {
		String sql = "DELETE FROM event_policy_tbl WHERE profile_idx = :profile_id";
		Map<String, Object> param = Maps.newHashMap();
		param.put("profile_id", $id);
		int result = 0;
		result = template.update(sql, param);
		return result;
	}

	public int insertPolicyList(List<Map> $policy_lst) {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch($policy_lst.toArray(new HashMap[$policy_lst.size()]));
		Map param = (Map) $policy_lst.get(0);
		String query = QueryUtil.getInsertQuery(param, null);
		String sql = "INSERT INTO event_policy_tbl " + query;
		int[] result = template.batchUpdate(sql, batch);
		int sum = 0;
		for (int item : result) {
			sum += item;
		}
		return sum;
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
