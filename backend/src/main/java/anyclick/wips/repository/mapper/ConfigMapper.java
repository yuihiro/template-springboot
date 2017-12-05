package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

import anyclick.wips.util.Util;

public class ConfigMapper implements RowMapper {

	public ConfigMapper() {
	}

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("ip_use", rs.getInt("ip_use"));
		vo.put("login_max", rs.getInt("login_max"));
		vo.put("auto_logout", rs.getInt("autoLogout_interval"));
		vo.put("auto_lock", rs.getInt("auto_lock"));
		vo.put("pwd_update", rs.getInt("pwd_update"));
		vo.put("auto_backup", rs.getInt("autoBackup"));
		vo.put("inited", rs.getBoolean("inited"));
		vo.put("language", (rs.getString("language").equals("KOR")) ? 1 : 2);

		vo.put("smtp_ip", Util.toEmpty(rs.getString("mail_ip")));
		vo.put("smtp_port", rs.getInt("mail_port"));
		vo.put("smtp_auth", Util.toEmpty(rs.getString("mail_auth")));

		vo.put("sys_use", rs.getInt("syslog_use"));
		vo.put("sys_server1", Util.toEmpty(rs.getString("syslog_server1")));
		vo.put("sys_server2", Util.toEmpty(rs.getString("syslog_server2")));
		vo.put("sys_server3", Util.toEmpty(rs.getString("syslog_server3")));

		return vo;
	}
}
