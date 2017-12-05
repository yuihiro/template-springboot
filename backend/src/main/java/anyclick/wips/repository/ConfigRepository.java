package anyclick.wips.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import anyclick.wips.repository.mapper.ConfigMapper;
import anyclick.wips.repository.mapper.LicenseMapper;
import anyclick.wips.repository.mapper.LogConfigMapper;
import anyclick.wips.util.QueryUtil;

@Repository
public class ConfigRepository {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static final String SYSLOG_CFG_FILE = "/etc/syslog.conf";

	@Autowired
	NamedParameterJdbcTemplate template;

	public Map getConfig() {
		String sql = "SELECT * FROM global_config_tbl";
		Map<String, Object> result = (Map) template.queryForObject(sql, Maps.newHashMap(), new ConfigMapper());
		return result;
	}

	public Map getLogConfig() {
		String sql = "SELECT * FROM log_config_tbl";
		Map<String, Object> result = (Map) template.queryForObject(sql, Maps.newHashMap(), new LogConfigMapper());
		return result;
	}

	public Map getLicenseInfo() {
		String sql = "SELECT * FROM license_list WHERE mac IS NOT NULL AND type = 1 ORDER BY saved_time DESC LIMIT 1";
		Map<String, Object> result = (Map) template.queryForObject(sql, Maps.newHashMap(), new LicenseMapper());
		return result;
	}

	public int updateConfig(Map<String, Object> $param) {
		List<String> except = new ArrayList();
		except.add("type");
		String sql = "";
		String query = "";
		query = QueryUtil.getUpdateQuery($param, except);
		sql = "UPDATE global_config_tbl SET " + query + "";
		return template.update(sql, $param);
	}

	public int updateLogConfig(Map<String, Object> $param) {
		List<String> except = new ArrayList();
		except.add("type");
		String sql = "";
		String query = "";
		query = QueryUtil.getUpdateQuery($param, except);
		sql = "UPDATE log_config_tbl SET " + query + "";
		return template.update(sql, $param);
	}
}
