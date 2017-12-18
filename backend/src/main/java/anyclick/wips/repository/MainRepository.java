package anyclick.wips.repository;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

@Repository
public class MainRepository {

	@Autowired
	NamedParameterJdbcTemplate template;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void updateDatabase() {
		int isExist = template.queryForObject("SELECT COUNT(*) FROM global_config_tbl", Maps.newHashMap(), Integer.class);
		if (isExist == 0) {
			log.info("INIT DATABASE : global_config_tbl");
			String sql = "INSERT INTO global_config_tbl (ip_use) VALUES (0)";
			template.update(sql, Maps.newHashMap());
		}
	}

	public int getAdminLock(String $id) {
		Map param = Maps.newHashMap();
		param.put("id", $id);
		String sql = "SELECT isLock FROM admin_user_tbl WHERE user_id = :id";
		return template.queryForObject(sql, param, Integer.class);
	}

	public int getAdminLoginTryCnt(String $id) {
		Map param = Maps.newHashMap();
		param.put("id", $id);
		String sql = "SELECT try_count FROM admin_user_tbl WHERE user_id = :id";
		return template.queryForObject(sql, param, Integer.class);
	}

	public int updateAdminLoginTryCnt(String $id, int $try) {
		Map param = Maps.newHashMap();
		param.put("id", $id);
		param.put("try", $try);
		String sql = "UPDATE admin_user_tbl SET try_count = :try WHERE user_id = :id";
		return template.update(sql, param);
	}

	public int updateAdminLock(String $id, int $lock) {
		Map param = Maps.newHashMap();
		param.put("id", $id);
		param.put("lock", $lock);
		String sql = "UPDATE admin_user_tbl SET isLock = :lock WHERE user_id = :id";
		return template.update(sql, param);
	}
}
