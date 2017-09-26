package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import anyclick.wips.config.AppProperties;
import anyclick.wips.util.CryptoUtil;
import anyclick.wips.util.DateUtil;

public class AdminMapper implements RowMapper {

	public String type = "LIST";

	public AdminMapper(String $type) {
		type = $type;
	}

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("idx", rs.getLong("user_idx"));
		vo.put("id", rs.getString("user_id"));
		vo.put("name", rs.getString("name"));
		vo.put("type", rs.getInt("admin_kinds"));
		vo.put("type_str", MapperHelper.adminType(rs.getInt("admin_kinds")));
		if (type == "TINY") {
			return vo;
		}
		vo.put("lock", rs.getInt("isLock"));
		vo.put("lock_str", MapperHelper.lock(rs.getInt("isLock")));
		vo.put("try_cnt", rs.getInt("try_count"));
		vo.put("permit_ip", rs.getString("permit_ip"));
		vo.put("pwd", CryptoUtil.decrypt(rs.getString("pwd"), AppProperties.CRYOTO_KEY));
		vo.put("phone", rs.getString("phone"));
		vo.put("email", rs.getString("email"));
		vo.put("email_pwd", rs.getString("email_pwd"));
		vo.put("comment", rs.getString("comment"));
		vo.put("last_login", DateUtil.timeToStr(rs.getLong("last_login")));
		return vo;
	}
}
