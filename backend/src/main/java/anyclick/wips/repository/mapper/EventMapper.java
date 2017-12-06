package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

import anyclick.wips.util.DateUtil;
import anyclick.wips.util.Util;

public class EventMapper implements RowMapper {

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("id", rs.getLong("id"));
		vo.put("server_id", rs.getInt("server_id"));
		vo.put("server_str", rs.getString("name"));
		vo.put("map_id", rs.getInt("map_id"));
		vo.put("map_str", rs.getString("location"));
		vo.put("server", rs.getString("name") + "(" + rs.getString("location") + ")");
		vo.put("type", rs.getInt("type"));
		vo.put("type_str", MapperHelper.eventType(rs.getInt("type")));
		vo.put("sub_type", rs.getInt("sub_type"));
		vo.put("sub_type_str", MapperHelper.eventSubType(rs.getInt("type"), rs.getInt("sub_type")));
		vo.put("bssid", Util.macToStr(rs.getLong("bssid")));
		vo.put("ssid", Util.toHyphen(rs.getString("bssid")));
		vo.put("ap_str", "");
		vo.put("sta", Util.macToStr(rs.getLong("sta")));
		vo.put("station_str", "");
		vo.put("block", rs.getInt("block"));
		vo.put("block_str", MapperHelper.block(rs.getInt("block")));
		vo.put("reg_time", DateUtil.timeToStr(rs.getLong("l_date")));
		return vo;
	}
}
