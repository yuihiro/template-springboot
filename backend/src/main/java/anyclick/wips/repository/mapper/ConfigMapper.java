package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

public class ConfigMapper implements RowMapper {

	public ConfigMapper() {
	}

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("ip_use", rs.getInt("ip_use"));
		vo.put("login_max", rs.getInt("login_max"));
		vo.put("autoLogout_interval", rs.getInt("autoLogout_interval"));
		vo.put("auto_lock", rs.getInt("auto_lock"));
		vo.put("pwd_update", rs.getInt("pwd_update"));

		vo.put("syslog_use", rs.getInt("syslog_use"));
		vo.put("syslog_server1", rs.getString("syslog_server1"));
		vo.put("syslog_server2", rs.getString("syslog_server2"));
		vo.put("syslog_server3", rs.getString("syslog_server3"));
		vo.put("autoBackup", rs.getInt("autoBackup"));
		vo.put("isInited", rs.getBoolean("inited"));
		vo.put("language", (rs.getString("language").equals("KOR")) ? 1 : 2);
		return vo;
	}
}
