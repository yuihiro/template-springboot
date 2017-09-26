package anyclick.wips.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import anyclick.wips.util.DateUtil;

public class SummaryMapper implements RowMapper {

	public SummaryMapper() {
	}

	@Override
	public Map mapRow(ResultSet rs, int row) throws SQLException {
		Map<String, Object> vo = new HashMap<String, Object>();
		vo.put("id", rs.getString("label"));
		vo.put("label", rs.getString("label") + "(" + rs.getString("sub_label") + ")");
		vo.put("value", rs.getInt("value"));
		vo.put("date", DateUtil.trimDateTime(rs.getString("date")));
		return vo;
	}
}
