package anyclick.wips.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import anyclick.wips.repository.mapper.EventCntMapper;
import anyclick.wips.repository.mapper.EventMapper;
import anyclick.wips.util.QueryUtil;

@Repository
public class DashboardRepository {

	@Autowired
	NamedParameterJdbcTemplate template;

	public List getStatsCnt(Map<String, Object> $param) {
		String sql = "SELECT server_stats_tbl.server_id, `name`, SUM(sensor_on_cnt) AS sensor_on_cnt, SUM(sensor_off_cnt) AS sensor_off_cnt, "
				+ "SUM(manage_ap_cnt) AS manage_ap_cnt, SUM(unmanage_ap_cnt) AS unmanage_ap_cnt, SUM(rogue_ap_cnt) AS rogue_ap_cnt, SUM(external_ap_cnt) AS external_ap_cnt, "
				+ "SUM(manage_sta_cnt) AS manage_sta_cnt, SUM(unmanage_sta_cnt) AS unmanage_sta_cnt, SUM(external_sta_cnt) AS external_sta_cnt, "
				+ "SUM(high_event_cnt) AS high_event_cnt, SUM(medium_event_cnt) AS medium_event_cnt, SUM(low_event_cnt) AS low_event_cnt, SUM(unconfirm_event_cnt) AS unconfirm_event_cnt "
				+ "FROM server_stats_tbl LEFT JOIN server_info_tbl ON server_info_tbl.server_id = server_stats_tbl.server_id GROUP BY server_stats_tbl.server_id ORDER BY server_stats_tbl.server_id";
		List result = template.queryForList(sql, $param);
		return result;
	}

	public List getEventCnt(Map<String, Object> $param) {
		String sql = "SELECT * FROM dash_event_summary LEFT JOIN server_info_tbl ON server_info_tbl.server_id = dash_event_summary.server_id ORDER BY server_info_tbl.server_id, TYPE, sub_type";
		List result = template.query(sql, $param, new EventCntMapper());
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
