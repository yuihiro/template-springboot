package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

public class EventStatsMapper implements RowMapper {

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("date", rs.getString("l_date"));
		vo.put("value", rs.getLong("value"));
		vo.put("type", rs.getInt("type"));
		vo.put("sub_type", rs.getInt("sub_type"));
		vo.put("type_str", MapperHelper.eventType(rs.getInt("type")));
		vo.put("sub_type_str", MapperHelper.eventSubType(rs.getInt("type"), rs.getInt("sub_type")));
		return vo;
	}
}
