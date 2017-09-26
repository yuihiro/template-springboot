package anyclick.wips.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import anyclick.wips.repository.mapper.EventMapper;
import anyclick.wips.util.QueryUtil;

@Repository
public class StatusRepository {

	@Autowired
	NamedParameterJdbcTemplate template;

	public Long getOnlineUserCnt(Map<String, Object> $param) {
		String sql = "SELECT COUNT(*) FROM radonline";
		Long result = template.queryForObject(sql, $param, Long.class);
		return result;
	}

	public Long getAuthLogCnt(Map<String, Object> $param) {
		String sql = "SELECT COUNT(id) FROM radauthlog WHERE Result = :success AND TimeStamp BETWEEN :start_time AND :end_time";
		Long result = template.queryForObject(sql, $param, Long.class);
		return result;
	}

	public List getStatusEventList(Map<String, Object> $param) {
		String query = "";
		boolean first = (Long.parseLong($param.get("last_event_id").toString()) == -1) ? true : false;
		if (first) {
			query += QueryUtil.getOrderQuery("l_date desc");
			query += QueryUtil.getLimitQuery($param);
		} else {
			query += "WHERE l_date >= " + $param.get("start_time").toString();
			query += QueryUtil.getOrderQuery("l_date desc");
		}
		String sql = "SELECT * FROM event_tbl " + query;
		List result = template.query(sql, $param, new EventMapper());
		return result;
	}
}
