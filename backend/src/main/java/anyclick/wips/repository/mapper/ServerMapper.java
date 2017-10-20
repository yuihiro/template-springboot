package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

import anyclick.wips.config.AppProperties;
import anyclick.wips.data.Enums.MapperType;
import anyclick.wips.util.CryptoUtil;
import anyclick.wips.util.DateUtil;
import anyclick.wips.util.Util;

public class ServerMapper implements RowMapper {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	MapperType type;

	public ServerMapper(MapperType $type) {
		type = $type;
	}

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("id", rs.getLong("server_id"));
		vo.put("name", rs.getString("name"));
		vo.put("ipaddr", rs.getString("ipaddr"));
		vo.put("port", rs.getString("port"));
		vo.put("macaddr", Util.macToStr(rs.getLong("macaddr")));
		if (type == MapperType.TINY) {
			return vo;
		}
		vo.put("secretkey", CryptoUtil.decrypt(rs.getString("secretkey"), AppProperties.CRYOTO_KEY));
		//		vo.put("pos_name", Util.toHyphen(rs.getString("pos_name")));
		vo.put("status", rs.getInt("status"));
		vo.put("status_str", MapperHelper.serverStatus(rs.getInt("status")));
		vo.put("reg_time", DateUtil.timeToStr(rs.getLong("reg_time")));
		vo.put("chg_time", DateUtil.timeToStr(rs.getLong("chg_time")));
		vo.put("cpu", rs.getInt("cpu"));
		//vo.put("temperature", rs.getInt("temperature") + "°");
		vo.put("mem_total", rs.getInt("mem_total"));
		vo.put("mem_space", rs.getInt("mem_space"));

		double m = (double) (rs.getLong("mem_total") - rs.getLong("mem_space")) / rs.getLong("mem_total") * 100;
		if (Double.isNaN(m)) {
			m = 0;
		}
		vo.put("memory", Math.round(m));
		vo.put("disk_total", rs.getLong("disk_total"));
		vo.put("disk_space", rs.getLong("disk_space"));

		m = (double) (rs.getLong("disk_total") - rs.getLong("disk_space")) / rs.getLong("disk_total") * 100;
		if (Double.isNaN(m)) {
			m = 0;
		}
		vo.put("disk", Math.round(m));
		vo.put("svn_revision", rs.getInt("svn_revision"));
		vo.put("server_version", Util.toHyphen(rs.getString("server_version")));
		vo.put("sensor_cnt", rs.getInt("sensor_on_cnt") + rs.getInt("sensor_off_cnt"));
		vo.put("sensor_on_cnt", rs.getInt("sensor_on_cnt"));
		vo.put("sensor_off_cnt", rs.getInt("sensor_off_cnt"));
		vo.put("manage_ap_cnt", rs.getInt("manage_ap_cnt"));
		vo.put("unmanage_ap_cnt", rs.getInt("unmanage_ap_cnt"));
		vo.put("rogue_ap_cnt", rs.getInt("rogue_ap_cnt"));
		vo.put("external_ap_cnt", rs.getInt("external_ap_cnt"));
		vo.put("manage_sta_cnt", rs.getInt("manage_sta_cnt"));
		vo.put("unmanage_sta_cnt", rs.getInt("unmanage_sta_cnt"));
		vo.put("external_sta_cnt", rs.getInt("external_sta_cnt"));
		vo.put("unconfirm_event_cnt", rs.getInt("unconfirm_event_cnt"));
		vo.put("high_event_cnt", rs.getInt("high_event_cnt"));
		vo.put("medium_event_cnt", rs.getInt("medium_event_cnt"));
		vo.put("low_event_cnt", rs.getInt("low_event_cnt"));
		//		vo.put("pos_lat", rs.getLong("pos_lat"));
		//		vo.put("pos_lng", rs.getLong("pos_lng"));
		return vo;
	}
}
