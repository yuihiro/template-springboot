package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

import anyclick.wips.util.DateUtil;
import anyclick.wips.util.Util;

public class StationMapper implements RowMapper {

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("server_id", rs.getInt("server_id"));
		vo.put("mac", rs.getLong("sta_mac"));
		vo.put("mac_str", Util.macToStr(rs.getLong("sta_mac")));
		vo.put("name", Util.toHyphen(rs.getString("user_name")));
		vo.put("reg_time", DateUtil.timeToStr(rs.getLong("reg_time")));
		return vo;
	}
}
