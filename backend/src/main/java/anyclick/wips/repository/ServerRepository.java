package anyclick.wips.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import anyclick.wips.data.Enums.MapperType;
import anyclick.wips.repository.mapper.CommandMapper;
import anyclick.wips.repository.mapper.EventCntMapper;
import anyclick.wips.repository.mapper.EventMapper;
import anyclick.wips.repository.mapper.ServerMapper;
import anyclick.wips.util.QueryUtil;

@Repository
public class ServerRepository {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	NamedParameterJdbcTemplate template;

	public long getServerListCnt(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		String sql = "SELECT COUNT(*) FROM server_info_tbl " + query;
		Long result = template.queryForObject(sql, $param, Long.class);
		return result;
	}

	public List getServerList(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		query += " ORDER BY status desc, name desc";
		query += QueryUtil.getLimitQuery($param);
		String sql = "SELECT * FROM server_info_tbl LEFT JOIN server_stats_tbl ON server_stats_tbl.server_id = server_info_tbl.server_id " + query;
		List result = template.query(sql, $param, new ServerMapper(MapperType.LIST));
		return result;
	}

	public Map getServer(long $id) {
		String sql = "SELECT * FROM (SELECT * FROM server_info_tbl WHERE server_id = :id) as server_info_tbl LEFT JOIN server_stats_tbl ON server_stats_tbl.server_id = server_info_tbl.server_id";
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", $id);
		Map result = null;
		try {
			result = (Map) template.queryForObject(sql, param, new ServerMapper(MapperType.INFO));
		} catch (EmptyResultDataAccessException e) {

		}
		return result;
	}

	public List getEventCnt(long $id) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", $id);
		String sql = "SELECT * FROM (SELECT * FROM dash_event_summary WHERE server_id = :id) as dash_event_summary LEFT JOIN server_info_tbl ON server_info_tbl.server_id = dash_event_summary.server_id ORDER BY server_info_tbl.server_id, TYPE, sub_type";
		List result = template.query(sql, param, new EventCntMapper());
		return result;
	}

	public int insertServer(Map<String, Object> $param) {
		KeyHolder key = new GeneratedKeyHolder();
		$param.put("chg_time", new Date().getTime());
		List<String> except = new ArrayList();
		except.add("id");
		String sql = "";
		String query = "";
		$param.put("reg_time", new Date().getTime());
		query = QueryUtil.getInsertQuery($param, except);
		sql = "INSERT INTO server_info_tbl " + query;
		int result = template.update(sql, new MapSqlParameterSource($param), key);
		return key.getKey().intValue();
	}

	public int insertServerStats(int $id) {
		Map param = Maps.newHashMap();
		param.put("id", $id);
		String sql = "INSERT INTO server_stats_tbl (server_id) VALUES (:id)";
		return template.update(sql, param);
	}

	public int updateServer(Map<String, Object> $param) {
		$param.put("chg_time", new Date().getTime());
		List<String> except = new ArrayList();
		except.add("id");
		String sql = "";
		String query = "";
		query = QueryUtil.getUpdateQuery($param, except);
		sql = "UPDATE server_info_tbl SET " + query + " WHERE server_id = :id";
		return template.update(sql, $param);
	}

	public int deleteServer(long $id) {
		String sql = "DELETE FROM server_info_tbl WHERE server_id = :id";
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", $id);
		int result = template.update(sql, param);
		return result;
	}

	public int deleteServerStats(long $id) {
		String sql = "DELETE FROM server_stats_tbl WHERE server_id = :id";
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", $id);
		int result = template.update(sql, param);
		return result;
	}

	public int deleteServerMap(long $id) {
		String sql = "DELETE FROM map_info_tbl WHERE server_id = :id";
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", $id);
		int result = template.update(sql, param);
		return result;
	}

	public int deleteServerPolicy(long $id) {
		String sql = "DELETE FROM event_policy_tbl WHERE server_id = :id";
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", $id);
		int result = template.update(sql, param);
		sql = "DELETE FROM event_policy_profile_tbl WHERE server_id = :id";
		template.update(sql, param);
		sql = "DELETE FROM event_policy_general_tbl WHERE server_id = :id";
		template.update(sql, param);
		return result;
	}

	public long getEventListCnt(Map<String, Object> $param) {
		String sql = "SELECT COUNT(*) FROM event_tbl WHERE server_id = :id";
		Long result = template.queryForObject(sql, $param, Long.class);
		return result;
	}

	public List getEventList(Map<String, Object> $param) {
		String query = QueryUtil.getOrderQuery("l_date desc");
		query += QueryUtil.getLimitQuery($param);
		String sql = "SELECT * FROM (SELECT * FROM event_tbl WHERE server_id = :server_id) as event_tbl ";
		sql += "LEFT JOIN server_info_tbl ON event_tbl.server_id = server_info_tbl.server_id ";
		sql += "LEFT JOIN map_info_tbl ON event_tbl.map_id = map_info_tbl.map_id " + query;
		List result = template.query(sql, $param, new EventMapper());
		return result;
	}

	public List getPolicyLogList(Map<String, Object> $param) {
		String query = QueryUtil.getOrderQuery("reg_time desc");
		query += QueryUtil.getLimitQuery($param);
		String sql = "SELECT * FROM (SELECT * FROM command_profile_tbl WHERE server_id = :server_id) AS command_profile_tbl ";
		sql += "LEFT JOIN (SELECT id, type, sub_type, target, target_name from command_tbl) as command_tbl ";
		sql += "ON command_profile_tbl.command_id = command_tbl.id ";
		List result = template.query(sql, $param, new CommandMapper("SENSOR"));
		return result;
	}
}
