package anyclick.wips.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import anyclick.wips.repository.mapper.ConfigMapper;
import anyclick.wips.repository.mapper.LicenseMapper;

@Repository
public class ConfigRepository {

	public static final String SYSLOG_CFG_FILE = "/etc/syslog.conf";

	@Autowired
	NamedParameterJdbcTemplate template;

	public Map getConfig() {
		Map result = getAppConfig();
		//result.put("license", getLicenseInfo());
		return result;
	}

	private Map getAppConfig() {
		String sql = "SELECT * FROM global_config_tbl";
		Map<String, Object> result = (Map) template.queryForObject(sql, Maps.newHashMap(), new ConfigMapper());
		return result;
	}

	public Map getLicenseInfo() {
		String sql = "SELECT * FROM license_list WHERE mac IS NOT NULL AND type = 1 ORDER BY saved_time DESC LIMIT 1";
		Map<String, Object> result = (Map) template.queryForObject(sql, Maps.newHashMap(), new LicenseMapper());
		return result;
	}
}
