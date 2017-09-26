package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;

public class ConfigMapper implements RowCallbackHandler {

	public Map<String, Object> vo;

	public ConfigMapper(Map $vo) {
		vo = $vo;
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException {
		vo.put("snmp_ip", rs.getString("mail_ip"));
		vo.put("snmp_port", rs.getInt("mail_port"));
		vo.put("snmp_auth", rs.getString("mail_auth"));
		vo.put("ip_use", rs.getInt("ip_use"));
		vo.put("ip_token1", rs.getString("ip_token"));
		vo.put("ip_token2", rs.getString("ip_token2"));
		vo.put("login_max", rs.getInt("login_max"));
		vo.put("aus_use", rs.getInt("aus_use"));
		vo.put("aus_ip", rs.getString("aus_ip"));
		vo.put("aus_id", rs.getString("aus_id"));
		vo.put("aus_pass", rs.getString("aus_pass"));
		vo.put("syslog_use", rs.getInt("syslog_use"));
		vo.put("syslog_server1", rs.getString("syslog_server1"));
		vo.put("syslog_server2", rs.getString("syslog_server2"));
		vo.put("syslog_server3", rs.getString("syslog_server3"));
		vo.put("autoBackup", rs.getInt("autoBackup"));
		vo.put("autoLogout_interval", rs.getInt("autoLogout_interval"));
		vo.put("sensor_alone_run", rs.getInt("sensor_alone_run"));
		vo.put("block_code", rs.getInt("block_code"));
		vo.put("app_mode", rs.getInt("app_mode"));
		vo.put("country_id", rs.getInt("country_id"));
		vo.put("scan_channel", rs.getString("scan_channel"));
		vo.put("isInited", rs.getBoolean("inited"));
		vo.put("language", (rs.getString("language").equals("KOR")) ? 1 : 2);
		vo.put("auto_lock", rs.getInt("auto_lock"));
		vo.put("pwd_update", rs.getInt("pwd_update"));
	}

}
