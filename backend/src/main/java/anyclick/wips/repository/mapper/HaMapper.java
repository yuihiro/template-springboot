package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

import anyclick.wips.util.QueryUtil;
import anyclick.wips.util.Util;

public class HaMapper implements RowMapper {

	static private final Logger log = LoggerFactory.getLogger(QueryUtil.class);

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		if (rs.getInt("use") == 1) {
			try {
				vo.put("master_mac", Util.macToLong(rs.getString("master_mac")));
				vo.put("master_mac_str", Util.toHyphen(rs.getString("master_mac")).toUpperCase());
			} catch (NullPointerException e) {
				vo.put("master_mac", 0);
				vo.put("master_mac_str", null);
			}
			try {
				vo.put("slave_mac", Util.macToLong(rs.getString("slave_mac")));
				vo.put("slave_mac_str", Util.toHyphen(rs.getString("slave_mac")).toUpperCase());
			} catch (NullPointerException e) {
				vo.put("slave_mac", 0);
				vo.put("slave_mac_str", null);
			}
			vo.put("master_ip", Util.toHyphen(rs.getString("master_ip")));
			vo.put("slave_ip", Util.toHyphen(rs.getString("slave_ip")));
			vo.put("vip", Util.toHyphen(rs.getString("vip")));

			vo.put("status", Util.toHyphen(rs.getString("status")));
			vo.put("update_time", rs.getDate("update_time").toString());
		}
		vo.put("use", rs.getInt("use"));
		return vo;
	}
}
