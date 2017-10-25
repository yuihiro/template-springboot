package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

import anyclick.wips.util.Util;

public class LicenseMapper implements RowMapper {

	public LicenseMapper() {
	}

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("mac", rs.getString("mac"));
		vo.put("mac_str", Util.macToStr(rs.getLong("mac")));
		vo.put("sensor_cnt", rs.getInt("sensor_cnt"));
		vo.put("save_time", rs.getTimestamp("saved_time").toString().substring(0, 10));
		String start_date = (rs.getObject("expiry_start_date") != null) ? rs.getDate("expiry_start_date").toString().substring(0, 10) : null;
		String end_date = (rs.getObject("expiry_end_date") != null) ? rs.getDate("expiry_end_date").toString().substring(0, 10) : null;
		vo.put("expiry_start_date", (start_date != null) ? rs.getDate("expiry_start_date").getTime() : 0);
		vo.put("expiry_end_date", (end_date != null) ? rs.getDate("expiry_end_date").getTime() : 0);
		vo.put("expiry_date", start_date + " ~ " + end_date);
		vo.put("license_key", rs.getString("license_key"));
		vo.put("hw", rs.getString("hw"));
		vo.put("type", rs.getInt("type"));
		vo.put("type_str", (rs.getInt("type") == 1) ? "Official" : "Temporary");
		return vo;
	}
}
