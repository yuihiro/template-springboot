package anyclick.wips.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import anyclick.wips.repository.mapper.AdminMapper;
import anyclick.wips.util.QueryUtil;

@Repository
public class AdminRepository {

	@Autowired
	NamedParameterJdbcTemplate template;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public Map getAdmin(String $id) {
		String sql = "SELECT * FROM admin_user_tbl WHERE user_id = :user_id";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("user_id", $id);
		Map result = null;
		try {
			result = (Map) template.queryForObject(sql, param, new AdminMapper("INFO"));
		} catch (EmptyResultDataAccessException e) {

		}
		return result;
	}

	public List getAdminList(Map<String, Object> $param, String $type) {
		String sql = "SELECT * FROM admin_user_tbl";
		List result = template.query(sql, $param, new AdminMapper($type));
		return result;
	}

	public int insertAdmin(Map<String, Object> $param) {
		List<String> except = new ArrayList();
		except.add("idx");
		String sql = "";
		String query = "";
		query = QueryUtil.getInsertQuery($param, except);
		sql = "INSERT INTO admin_user_tbl " + query;
		return template.update(sql, $param);
	}

	public int updateAdmin(Map<String, Object> $param) {
		List<String> except = new ArrayList();
		except.add("idx");
		String sql = "";
		String query = "";
		query = QueryUtil.getUpdateQuery($param, except);
		sql = "UPDATE admin_user_tbl SET " + query + " WHERE user_idx = :idx";
		return template.update(sql, $param);
	}

	public int deleteAdmin(String $id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("user_id", $id);
		String sql = "DELETE FROM admin_user_tbl WHERE user_id = :user_id";
		int result = 0;
		result = template.update(sql, param);
		return result;
	}

}
