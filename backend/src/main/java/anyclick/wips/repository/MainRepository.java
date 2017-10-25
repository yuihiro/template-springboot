package anyclick.wips.repository;

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

	public void initDatabase() {
		int isExist = template.queryForObject("SELECT COUNT(*) FROM global_config_tbl", Maps.newHashMap(), Integer.class);
		if (isExist == 0) {
			log.info("INIT DATABASE : global_config_tbl");
			String sql = "INSERT INTO global_config_tbl (ip_use) VALUES (0)";
			template.update(sql, Maps.newHashMap());
		}
	}
}
