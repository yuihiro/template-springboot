package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

import anyclick.wips.util.DateUtil;
import anyclick.wips.util.Util;

public class AdminLogMapper implements RowMapper {

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("id", rs.getString("admin"));
		vo.put("type", rs.getInt("type"));
		vo.put("type_str", MapperHelper.adminLogType(rs.getInt("type")));
		vo.put("comment", Util.toHyphen(rs.getString("comment")));
		vo.put("reg_time", DateUtil.timeToStr(rs.getLong("date")));
		return vo;
	}
}
