package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

public class PolicyExceptMapper implements RowMapper {

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("profile_id", rs.getInt("profile_idx"));
		vo.put("type", rs.getInt("type"));
		vo.put("sub_type", rs.getInt("sub_type"));
		vo.put("value", rs.getString("value"));
		return vo;
	}
}
