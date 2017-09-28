package anyclick.wips.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import anyclick.wips.data.Enums.MapperType;
import anyclick.wips.repository.mapper.ServerMapper;
import anyclick.wips.util.QueryUtil;

@Repository
public class ServerRepository {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	NamedParameterJdbcTemplate template;

	public long getServerListCnt(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		String sql = "SELECT COUNT(*) FROM server_info_tbl " + query;
		Long result = template.queryForObject(sql, $param, Long.class);
		return result;
	}

	public List getServerList(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		query += " ORDER BY status desc, name desc";
		query += QueryUtil.getLimitQuery($param);
		String sql = "SELECT * FROM server_info_tbl " + query;
		List result = template.query(sql, $param, new ServerMapper(MapperType.LIST));
		return result;
	}

	public Map getServer(long $id) {
		String sql = "SELECT * FROM server_info_tbl WHERE server_id = :id";
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", $id);
		Map result = null;
		try {
			result = (Map) template.queryForObject(sql, param, new ServerMapper(MapperType.INFO));
		} catch (EmptyResultDataAccessException e) {

		}
		return result;
	}

	public int insertServer(Map<String, Object> $param) {
		$param.put("chg_time", new Date().getTime());
		List<String> except = new ArrayList();
		except.add("id");
		String sql = "";
		String query = "";
		$param.put("reg_time", new Date().getTime());
		query = QueryUtil.getInsertQuery($param, except);
		sql = "INSERT INTO server_info_tbl " + query;
		return template.update(sql, $param);
	}

	public int updateServer(Map<String, Object> $param) {
		$param.put("chg_time", new Date().getTime());
		List<String> except = new ArrayList();
		except.add("id");
		String sql = "";
		String query = "";
		query = QueryUtil.getUpdateQuery($param, except);
		sql = "UPDATE server_info_tbl SET " + query + " WHERE server_id = :id";
		return template.update(sql, $param);
	}

	public int deleteServer(long $id) {
		String sql = "DELETE FROM server_info_tbl WHERE server_id = :id";
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", $id);
		int result = 0;
		result = template.update(sql, param);
		return result;
	}
}
