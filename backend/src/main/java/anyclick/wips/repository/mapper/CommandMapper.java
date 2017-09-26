package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import anyclick.wips.util.DateUtil;

public class CommandMapper implements RowMapper {

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("id", rs.getLong("id"));
		vo.put("command_id", rs.getInt("command_id"));
		vo.put("server_id", rs.getInt("server_id"));
		vo.put("server_name", rs.getString("server_name"));
		vo.put("map_id", rs.getInt("map_id"));
		vo.put("map_name", rs.getString("map_name"));
		vo.put("status", rs.getInt("status"));
		vo.put("status_str", MapperHelper.commandStatus(rs.getInt("status")));
		vo.put("result", rs.getString("result"));
		vo.put("reg_time", DateUtil.timeToStr(rs.getTimestamp("reg_time")));
		vo.put("chg_time", DateUtil.timeToStr(rs.getTimestamp("chg_time")));
		return vo;
	}
}
