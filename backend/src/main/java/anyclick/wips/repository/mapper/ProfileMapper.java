package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import anyclick.wips.util.DateUtil;

public class ProfileMapper implements RowMapper {

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("id", rs.getLong("id"));
		vo.put("name", rs.getString("name"));
		vo.put("server_id", rs.getInt("server_id"));
		vo.put("server_profile_id", rs.getInt("server_profile_id"));
		vo.put("type", rs.getInt("type"));
		vo.put("type_str", MapperHelper.policyType(rs.getInt("type")));
		vo.put("reg_time", DateUtil.timeToStr(rs.getTimestamp("reg_time")));
		vo.put("chg_time", DateUtil.timeToStr(rs.getTimestamp("chg_time")));
		vo.put("server_name", (rs.getString("server_name") == null) ? "통합" : (rs.getString("server_name")));
		vo.put("use", rs.getInt("use"));
		vo.put("priority", rs.getInt("priority"));
		vo.put("alert", rs.getInt("alert"));
		vo.put("sound", rs.getInt("sound"));
		vo.put("mail", rs.getInt("mail"));
		vo.put("block", rs.getInt("block"));
		vo.put("cable_block", rs.getInt("cable_block"));
		return vo;
	}
}
