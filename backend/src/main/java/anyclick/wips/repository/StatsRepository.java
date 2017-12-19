package anyclick.wips.repository;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import anyclick.wips.repository.mapper.EventStatsMapper;

@Repository
public class StatsRepository {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	NamedParameterJdbcTemplate template;

	public List getEventStatsList(int type, long start_date, long end_date) {
		String query = "'%y-%m-%d'";
		if (type == 1 || type == 2) {
			query = "'%y-%m-%d'";
		} else if (type == 3) {
			query = "'%y-%m'";
		} else if (type == 4) {
			query = "'%Y'";
		}
		Map param = Maps.newHashMap();
		param.put("start_date", start_date);
		param.put("end_date", end_date);
		String sql = "SELECT l_date, type, sub_type, count(*) as value" + " FROM (SELECT from_unixtime(l_date/1000, " + query
				+ " ) as l_date, id, type, sub_type FROM event_tbl WHERE l_date >= :start_date AND l_date <= :end_date ) as data" + " GROUP BY type, sub_type, l_date"
				+ " ORDER BY type, sub_type, l_date";
		List result = template.query(sql, param, new EventStatsMapper());
		return result;
	}
}
