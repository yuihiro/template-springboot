package anyclick.wips.repository;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import anyclick.wips.repository.mapper.AdminLogMapper;
import anyclick.wips.repository.mapper.CommandMapper;
import anyclick.wips.repository.mapper.EventMapper;
import anyclick.wips.repository.mapper.SystemLogMapper;
import anyclick.wips.util.QueryUtil;

@Repository
public class LogRepository {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	NamedParameterJdbcTemplate template;

	public long getEventListCnt(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		query += QueryUtil.getTimeQuery($param, "l_date", false, query);
		String sql = "SELECT COUNT(*) FROM event_tbl " + query;
		Long result = template.queryForObject(sql, $param, Long.class);
		return result;
	}

	public List getEventList(Map<String, Object> $param) {
		if ($param.containsKey("server_id")) {
			$param.put("event_tbl.server_id", $param.get("server_id"));
		}
		$param.remove("server_id");
		String query = QueryUtil.getWhereQuery($param);
		query += QueryUtil.getTimeQuery($param, "l_date", false, query);
		query += QueryUtil.getOrderQuery("l_date desc");
		query += QueryUtil.getLimitQuery($param);
		String sql = "SELECT * FROM event_tbl LEFT JOIN server_info_tbl ON event_tbl.server_id = server_info_tbl.server_id LEFT JOIN map_info_tbl ON map_info_tbl.server_id = event_tbl.server_id AND map_info_tbl.map_id = event_tbl.map_id "
				+ query;
		List result = template.query(sql, $param, new EventMapper());
		return result;
	}

	public long getAdminLogListCnt(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		query += QueryUtil.getTimeQuery($param, "date", false, query);
		String sql = "SELECT COUNT(*) FROM admin_log_tbl " + query;
		Long result = template.queryForObject(sql, $param, Long.class);
		return result;
	}

	public List getAdminLogList(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		query += QueryUtil.getTimeQuery($param, "date", false, query);
		query += QueryUtil.getOrderQuery("date desc");
		query += QueryUtil.getLimitQuery($param);
		String sql = "SELECT * FROM admin_log_tbl " + query;
		List result = template.query(sql, $param, new AdminLogMapper());
		return result;
	}

	public long getSystemLogListCnt(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		query += QueryUtil.getTimeQuery($param, "l_date", false, query);
		String sql = "SELECT COUNT(*) FROM system_log_tbl " + query;
		Long result = template.queryForObject(sql, $param, Long.class);
		return result;
	}

	public List getSystemLogList(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		query += QueryUtil.getTimeQuery($param, "l_date", false, query);
		query += QueryUtil.getOrderQuery("l_date desc");
		query += QueryUtil.getLimitQuery($param);
		String sql = "SELECT * FROM system_log_tbl " + query;
		List result = template.query(sql, $param, new SystemLogMapper());
		return result;
	}

	public long getPolicyLogListCnt(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		query += QueryUtil.getTimeQuery($param, "reg_time", true, query);
		String sql = "SELECT COUNT(*) FROM command_tbl " + query;
		Long result = template.queryForObject(sql, $param, Long.class);
		return result;
	}

	public List getPolicyLogList(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		query += QueryUtil.getTimeQuery($param, "reg_time", true, query);
		query += QueryUtil.getOrderQuery("reg_time desc");
		query += QueryUtil.getLimitQuery($param);
		String sql = "SELECT * FROM (SELECT * FROM command_tbl " + query + ") as command_tbl ";
		sql += "LEFT JOIN ";
		sql += "(SELECT command_id FROM command_profile_tbl GROUP BY command_id) AS command_profile_tbl ";
		sql += "ON command_profile_tbl.command_id = command_tbl.id " + query;
		List result = template.query(sql, $param, new CommandMapper("COMMAND"));
		return result;
	}

	public List getPolicyDetailLogList(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		query += QueryUtil.getTimeQuery($param, "reg_time", true, query);
		query += QueryUtil.getOrderQuery("reg_time desc");
		String sql = "SELECT *, status as detail_status FROM command_profile_tbl " + query;
		List result = template.query(sql, $param, new CommandMapper("DETAIL"));
		return result;
	}
}
