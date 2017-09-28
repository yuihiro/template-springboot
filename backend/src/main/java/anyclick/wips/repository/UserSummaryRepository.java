package anyclick.wips.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import anyclick.wips.repository.mapper.SummaryMapper;
import anyclick.wips.util.C3Util;
import anyclick.wips.util.DataUtil;
import anyclick.wips.util.QueryUtil;

@Repository
public class UserSummaryRepository {

	@Autowired
	NamedParameterJdbcTemplate template;

	public Map getUserSummaryByTime(Map<String, Object> $param) {
		String query = QueryUtil.getWhereQuery($param);
		query += QueryUtil.getTimeQuery($param, "AcctStartTime", false, query);
		String sql = "SELECT UserName as label, name as sub_label, COUNT(UserName) AS value, AcctStartTime AS date FROM radacct "
				+ "LEFT JOIN user_tbl ON radacct.UserName = user_tbl.user_id  " + query + " ";
		sql += "GROUP BY user_id, DATE_FORMAT(AcctStartTime,'%y-%m-%d') ";
		List<Map> source = template.query(sql, $param, new SummaryMapper());

		Map result = Maps.newHashMap();
		List date_lst = DataUtil.getDateList(source);
		List<Map> summary = DataUtil.parseSummaryData(source);
		List<Map> grid_data = DataUtil.parseGridData(source, summary, date_lst);
		result.put("pie_data", C3Util.parsePieChartData(summary));
		result.put("grid_data", grid_data);
		result.put("line_data", C3Util.parseLineChartData(grid_data, date_lst));
		return result;
	}
}
