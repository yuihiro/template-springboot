package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import anyclick.wips.config.AppProperties;
import anyclick.wips.data.Enums.MapperType;
import anyclick.wips.util.CryptoUtil;
import anyclick.wips.util.DateUtil;
import anyclick.wips.util.Util;

public class ServerMapper implements RowMapper {

	MapperType type;

	public ServerMapper(MapperType $type) {
		type = $type;
	}

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("id", rs.getLong("server_id"));
		vo.put("name", rs.getString("name"));
		vo.put("ipaddr", rs.getString("ipaddr"));
		vo.put("port", rs.getString("port"));
		vo.put("macaddr", Util.macToStr(rs.getLong("macaddr")));
		if (type == MapperType.TINY) {
			return vo;
		}
		vo.put("secretkey", CryptoUtil.decrypt(rs.getString("secretkey"), AppProperties.CRYOTO_KEY));
		//		vo.put("pos_name", Util.toHyphen(rs.getString("pos_name")));
		vo.put("status", rs.getInt("status"));
		vo.put("status_str", MapperHelper.serverStatus(rs.getInt("status")));
		vo.put("reg_time", DateUtil.timeToStr(rs.getLong("reg_time")));
		vo.put("chg_time", DateUtil.timeToStr(rs.getLong("chg_time")));
		vo.put("cpu", rs.getInt("cpu") + "%");
		vo.put("temperature", rs.getInt("temperature") + "°");
		vo.put("mem_total", rs.getInt("mem_total"));
		vo.put("mem_space", rs.getInt("mem_space"));
		try {
			vo.put("memory", rs.getInt("mem_space") / rs.getInt("mem_total") * 100 + "%");
		} catch (ArithmeticException e) {
			vo.put("memory", "0%");
		}
		vo.put("disk_total", rs.getInt("disk_total"));
		vo.put("disk_space", rs.getInt("disk_space"));
		try {
			vo.put("disk", rs.getInt("disk_space") / rs.getInt("disk_total") * 100 + "%");
		} catch (ArithmeticException e) {
			vo.put("disk", "0%");
		}
		vo.put("hw_version", rs.getInt("hw_version"));
		vo.put("hw_version_str", rs.getString("hw_version_str"));
		if (type == MapperType.LIST) {
			return vo;
		}
		//		vo.put("pos_lat", rs.getLong("pos_lat"));
		//		vo.put("pos_lng", rs.getLong("pos_lng"));
		return vo;
	}
}
