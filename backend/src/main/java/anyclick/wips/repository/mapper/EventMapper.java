package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import anyclick.wips.util.DateUtil;
import anyclick.wips.util.Util;

public class EventMapper implements RowMapper {

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("id", rs.getLong("id"));
		vo.put("server_id", rs.getInt("server_id"));
		vo.put("map_id", rs.getInt("map_id"));
		vo.put("type", rs.getInt("type"));
		//		vo.put("type_str", MapperHelper.adminLogType(rs.getInt("type")));
		vo.put("sub_type", rs.getInt("sub_type"));
		//		vo.put("sub_type_str", MapperHelper.adminLogType(rs.getInt("type")));
		vo.put("bssid", Util.macToStr(rs.getLong("bssid")));
		vo.put("ssid", Util.toHyphen(rs.getString("bssid")));
		vo.put("ap_str", "");
		vo.put("sta", Util.macToStr(rs.getLong("sta")));
		vo.put("station_str", "");
		vo.put("reg_time", DateUtil.timeToStr(rs.getLong("l_date")));
		return vo;
	}
}
