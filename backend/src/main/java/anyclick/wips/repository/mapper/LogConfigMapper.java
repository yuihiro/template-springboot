package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

public class LogConfigMapper implements RowMapper {

	public LogConfigMapper() {
	}

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("event_aging", rs.getInt("event_aging_day"));
		vo.put("policy_aging", rs.getInt("block_aging_day"));
		vo.put("admin_aging", rs.getInt("admin_aging_day"));
		vo.put("system_aging", rs.getInt("system_aging_day"));
		vo.put("notice_max", rs.getInt("file_notice_max"));
		vo.put("delete_max", rs.getInt("file_delete_max"));
		return vo;
	}
}
