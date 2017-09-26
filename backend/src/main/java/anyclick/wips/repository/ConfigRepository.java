package anyclick.wips.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import anyclick.wips.util.FileUtil;
import anyclick.wips.util.Util;

@SuppressWarnings("unchecked")
@Repository
public class ConfigRepository {

	public static final String SYSLOG_CFG_FILE = "/etc/syslog.conf";

	@Autowired
	NamedParameterJdbcTemplate template;

	public Map getConfig() {
		Map result = getSystemConfig();
		result.put("rad_info", FileUtil.getConfigFile("src/main/resources/radiusd.conf"));
		return result;
	}

	private Map getSystemConfig() {
		String sql = "SELECT * FROM env_tbl";
		List<Map<String, Object>> list = (List<Map<String, Object>>) template.queryForList(sql, Maps.newHashMap());
		Map result = new HashMap();
		for (Map item : list) {
			result.put(item.get("name"), Util.toHyphen(item.get("value")) + "|" + Util.toHyphen(item.get("kname")) + "|" + Util.toHyphen(item.get("seq")));
		}
		return result;
	}

	private List getPermitIpList() {
		String sql = "SELECT INET_NTOA(from_ip), INET_NTOA(to_ip), enabled, etc FROM webconsole_accessible_ip_tbl";
		List result = template.queryForList(sql, Maps.newHashMap());
		return result;
	}

}
