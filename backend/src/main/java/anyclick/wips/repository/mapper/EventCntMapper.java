package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

public class EventCntMapper implements RowMapper {

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("server_id", rs.getInt("server_id"));
		vo.put("name", rs.getString("name"));
		vo.put("status", rs.getString("status"));
		vo.put("type", rs.getInt("type"));
		vo.put("sub_type", rs.getInt("sub_type"));
		vo.put("value", rs.getInt("value"));
		return vo;
	}
}
