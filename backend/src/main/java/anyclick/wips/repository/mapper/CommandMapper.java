package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

import anyclick.wips.util.DateUtil;
import anyclick.wips.util.Util;

public class CommandMapper implements RowMapper {

	String type;

	public CommandMapper(String $type) {
		type = $type;
	}

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("id", rs.getLong("id"));
		vo.put("status", rs.getInt("status"));
		vo.put("status_str", MapperHelper.commandStatus(rs.getInt("status")));
		vo.put("reg_time", DateUtil.timeToStr(rs.getTimestamp("reg_time")));
		vo.put("done_time", DateUtil.timeToStr(rs.getTimestamp("done_time")));
		if (type.equals("COMMAND")) {
			vo.put("type", rs.getInt("type"));
			vo.put("type_str", MapperHelper.commandType(rs.getInt("type")));
			vo.put("sub_type", rs.getInt("sub_type"));
			vo.put("sub_type_str", MapperHelper.commandSubType(rs.getInt("type"), rs.getInt("sub_type")));
			vo.put("target", rs.getInt("target"));
			vo.put("target_name", Util.toHyphen(rs.getString("target_name")));
			vo.put("detail", rs.getString("command_id") == null ? false : true);
		} else if (type.equals("DETAIL")) {
			vo.put("command_id", rs.getLong("command_id"));
			vo.put("server_id", rs.getInt("server_id"));
			vo.put("server_name", rs.getString("server_name"));
			vo.put("map_id", rs.getInt("map_id"));
			vo.put("map_name", Util.toHyphen(rs.getString("map_name")));
			vo.put("result", rs.getString("result"));
			String server_name = Util.toHyphen(rs.getString("server_name"));
			String map_name = Util.toHyphen(rs.getString("map_name"));
			String location = (map_name.equals("-")) ? server_name : server_name + "-" + map_name;
			vo.put("location", location);
		} else if (type.equals("SENSOR")) {
			vo.put("type", rs.getInt("type"));
			vo.put("type_str", MapperHelper.commandType(rs.getInt("type")));
			vo.put("sub_type", rs.getInt("sub_type"));
			vo.put("sub_type_str", MapperHelper.commandSubType(rs.getInt("type"), rs.getInt("sub_type")));
			vo.put("target", rs.getInt("target"));
			vo.put("target_name", Util.toHyphen(rs.getString("target_name")));
			String server_name = Util.toHyphen(rs.getString("server_name"));
			String map_name = Util.toHyphen(rs.getString("map_name"));
			String location = (map_name.equals("-")) ? server_name : server_name + "-" + map_name;
			vo.put("location", location);
		}
		return vo;
	}
}
