package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class PolicyMapper implements RowMapper {

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("server_id", rs.getInt("server_id"));
		vo.put("profile_id", rs.getInt("profile_id"));
		vo.put("type", rs.getInt("type"));
		vo.put("sub_type", rs.getInt("sub_type"));
		vo.put("use", rs.getInt("use"));
		vo.put("priority", rs.getInt("priority"));
		vo.put("alert", rs.getInt("alert"));
		vo.put("sound", rs.getInt("sound"));
		vo.put("mail", rs.getInt("mail"));
		vo.put("block", rs.getInt("block"));
		vo.put("cable_block", rs.getInt("cable_block"));
		vo.put("threshold", rs.getInt("threshold"));
		vo.put("option", rs.getInt("option"));
		vo.put("option2", rs.getInt("option2"));
		vo.put("rss", rs.getInt("rss"));
		return vo;
	}
}
