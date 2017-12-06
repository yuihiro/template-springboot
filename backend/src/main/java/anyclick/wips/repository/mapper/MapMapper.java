package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

import anyclick.wips.util.DateUtil;

public class MapMapper implements RowMapper {

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("server_id", rs.getInt("server_id"));
		vo.put("server_name", rs.getString("server_name"));
		vo.put("map_id", rs.getInt("map_id"));
		vo.put("map_name", rs.getString("location"));
		vo.put("parent_id", rs.getInt("parent_id"));
		vo.put("depth", rs.getInt("depth"));
		//		vo.put("sort_no", rs.getInt("sort_no"));
		vo.put("profile_id", rs.getInt("idx"));
		vo.put("profile_name", rs.getString("name"));
		vo.put("server_profile_id", rs.getInt("server_profile_id"));
		vo.put("profile_type", rs.getInt("type"));
		vo.put("profile_type_str", MapperHelper.policyType(rs.getInt("type")));
		vo.put("profile_reg_time", DateUtil.timeToStr(rs.getTimestamp("profile.reg_time")));
		vo.put("profile_chg_time", DateUtil.timeToStr(rs.getTimestamp("profile.chg_time")));
		return vo;
	}
}
