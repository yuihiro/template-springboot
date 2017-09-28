package anyclick.wips.repository;

import java.util.List;
import java.util.Map;

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
		String query = QueryUtil.getWhereQuery($param);
		query += QueryUtil.getTimeQuery($param, "l_date", false, query);
		query += QueryUtil.getOrderQuery("l_date desc");
		query += QueryUtil.getLimitQuery($param);
		String sql = "SELECT * FROM event_tbl " + query;
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
		String sql = "SELECT * FROM command_profile_tbl " + query;
		List result = template.query(sql, $param, new CommandMapper("DETAIL"));
		return result;
	}
}
