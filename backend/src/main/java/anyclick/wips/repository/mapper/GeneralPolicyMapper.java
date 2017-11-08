package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.google.common.collect.Maps;

public class GeneralPolicyMapper implements RowMapper {

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = Maps.newHashMap();
		vo.put("profile_id", rs.getInt("profile_idx"));
		vo.put("id", rs.getInt("idx"));
		vo.put("media", rs.getInt("media"));
		vo.put("media_str", MapperHelper.media(rs.getInt("media")));
		vo.put("cipher", rs.getInt("cipher"));
		vo.put("cipher_str", MapperHelper.cipher(rs.getInt("cipher")));
		vo.put("auth", rs.getInt("auth"));
		vo.put("auth_str", MapperHelper.auth(rs.getInt("auth")));
		vo.put("broad", rs.getInt("broadcast"));
		vo.put("broad_str", (rs.getInt("broadcast") == 1) ? "Broadcast 사용" : (rs.getInt("broadcast") == 0) ? "Broadcast 미사용" : "사용안함");
		vo.put("saved_time", rs.getString("saved_time"));

		String ssid = rs.getString("ssid");
		if (ssid.equals("사용안함") == false) {
			if (ssid.substring(0, 1).equals("%") == true && ssid.substring(ssid.length() - 1, ssid.length()).equals("%") == true) {
				vo.put("ssid_str", ssid.replaceAll("%", "") + "(옵션사용)");
				vo.put("like_search", true);
			} else {
				vo.put("ssid_str", "사용안함");
				vo.put("like_search", false);
			}
			vo.put("ssid", ssid.replaceAll("%", ""));
		} else {
			vo.put("ssid", "사용안함");
			vo.put("ssid_str", "사용안함");
			vo.put("like_search", false);
		}

		if (rs.getString("channel").equals("사용안함") == true) {
			vo.put("channel_str", "사용안함");
			vo.put("channel_lst", "");
		} else {
			vo.put("channel_str", rs.getString("channel").toString().split(",").length + "개");
			vo.put("channel_lst", rs.getString("channel"));
		}

		vo.put("rate_lst", (rs.getString("data_rate").equals("사용안함")) ? "" : rs.getString("data_rate"));
		vo.put("mcs_lst", (rs.getString("mcs_set").equals("사용안함")) ? "" : rs.getString("mcs_set"));
		String rate_mcs_str = "";
		if (vo.get("rate_lst").toString().length() > 0) {
			rate_mcs_str = "Data rate : " + vo.get("rate_lst").toString().split(",").length + "개";
			vo.put("rate_str", vo.get("rate_lst").toString().split(",").length + "개");
		} else {
			vo.put("rate_str", "사용안함");
		}
		if (vo.get("mcs_lst").toString().length() > 0) {
			if (rate_mcs_str.length() > 0) {
				rate_mcs_str += " / MCS set : " + vo.get("mcs_lst").toString().split(",").length + "개";
				vo.put("mcs_str", vo.get("mcs_lst").toString().split(",").length + "개");
			} else {
				rate_mcs_str = "MCS set : " + vo.get("mcs_lst").toString().split(",").length + "개";
			}
		} else {
			vo.put("mcs_str", "사용안함");
		}
		if (rate_mcs_str.length() > 0) {
			vo.put("rate_mcs_str", rate_mcs_str);
		} else {
			vo.put("rate_mcs_str", "사용안함");
		}
		return vo;
	}
}
