package anyclick.wips.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import anyclick.wips.repository.mapper.EventMapper;
import anyclick.wips.util.QueryUtil;

@Repository
public class StatusRepository {

	@Autowired
	NamedParameterJdbcTemplate template;

	public long getManageApCnt() {
		String sql = "SELECT COUNT(*) FROM ap_mng_list";
		return template.queryForObject(sql, Maps.newHashMap(), Long.class);
	}

	public long getManageStationCnt() {
		String sql = "SELECT COUNT(*) FROM sta_mng_list";
		return template.queryForObject(sql, Maps.newHashMap(), Long.class);
	}

	public Map getServerStatsCnt() {
		String sql = "SELECT SUM(sensor_on_cnt) AS sensor_on_cnt, SUM(sensor_off_cnt) AS sensor_off_cnt, " + "SUM(rogue_ap_cnt) AS rogue_ap_cnt, "
		//+ "SUM(manage_ap_cnt) AS manage_ap_cnt, SUM(unmanage_ap_cnt) AS unmanage_ap_cnt, SUM(rogue_ap_cnt) AS rogue_ap_cnt, SUM(external_ap_cnt) AS external_ap_cnt, "
		//+ "SUM(manage_sta_cnt) AS manage_sta_cnt, SUM(unmanage_sta_cnt) AS unmanage_sta_cnt, SUM(external_sta_cnt) AS external_sta_cnt, "
				+ "SUM(high_event_cnt) AS high_event_cnt, SUM(medium_event_cnt) AS medium_event_cnt, SUM(low_event_cnt) AS low_event_cnt, SUM(unconfirm_event_cnt) AS unconfirm_event_cnt "
				+ "FROM server_stats_tbl";
		Map result = template.queryForMap(sql, Maps.newHashMap());
		return result;
	}

	public List getStatusEventList(Map<String, Object> $param) {
		String query = "";
		boolean first = (Long.parseLong($param.get("last_event_id").toString()) == -1) ? true : false;
		if (first) {
			query += "WHERE l_date >= " + $param.get("start_time").toString();
			query += QueryUtil.getOrderQuery("l_date desc");
			query += QueryUtil.getLimitQuery($param);
		} else {
			query += "WHERE l_date >= " + $param.get("start_time").toString();
			query += QueryUtil.getOrderQuery("l_date desc");
		}
		String sql = "SELECT * FROM event_tbl LEFT JOIN server_info_tbl ON event_tbl.server_id = server_info_tbl.server_id LEFT JOIN map_info_tbl ON event_tbl.server_id = map_info_tbl.server_id AND event_tbl.map_id = map_info_tbl.map_id "
				+ query;
		List result = template.query(sql, $param, new EventMapper());
		return result;
	}
}
