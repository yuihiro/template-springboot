package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

import anyclick.wips.util.Util;

public class VendorMapper implements RowMapper {

	String type = "POLICY";

	public VendorMapper(String $type) {
		type = $type;
	}

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("profile_id", rs.getInt("profile_idx"));
		if (type.equals("GENERAL")) {
			vo.put("general_id", rs.getInt("general_idx"));
		}
		vo.put("company", rs.getString("company"));
		vo.put("oui", rs.getLong("oui"));
		vo.put("oui_str", Util.ouiToStr(rs.getLong("oui")));
		vo.put("vendor_str", vo.get("company") + "(" + vo.get("oui_str") + ")");
		return vo;
	}
}
