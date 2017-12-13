package anyclick.wips.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import anyclick.wips.repository.mapper.MapMapper;

@Repository
public class MapRepository {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	NamedParameterJdbcTemplate template;

	public List getMapList() {
		String sql = "SELECT *, server.name as server_name FROM map_info_tbl as map ";
		sql += "LEFT JOIN server_info_tbl as server ";
		sql += "ON map.server_id = server.server_id ";
		sql += "ORDER BY server_name, depth, map.map_id ";
		List result = template.query(sql, new MapMapper("TINY"));
		return result;
	}
}
