package anyclick.wips.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataUtil {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	static public List<Map> parseSummaryData(List<Map> source) {
		Map<String, Integer> temp = source.stream()
				.collect(Collectors.groupingBy(it -> it.get("id").toString() + "|" + it.get("label").toString(), Collectors.summingInt(item -> (int) item.get("value"))));

		List<Map> result = new ArrayList();
		Map vo = null;
		String[] token = null;
		for (String key : temp.keySet()) {
			vo = new HashMap();
			token = StringUtils.split(key, "|");
			vo.put("id", token[0]);
			vo.put("label", token[1]);
			vo.put("total", temp.get(key));
			result.add(vo);
		}
		return result;
	}

	static public List<Map> getDateList(List<Map> source) {
		Map<String, String> around_date = getAroundDate(source);
		return DateUtil.getDateFromTwoDate(around_date.get("start"), around_date.get("end"));
	}

	static public List<Map> parseGridData(List<Map> source, List<Map> summary, List<String> date_lst) {
		List<Map> result = new ArrayList();
		Map vo = null;
		String id = null;
		String label = null;
		int total = 0;
		for (Map item : summary) {
			id = item.get("id").toString();
			label = item.get("label").toString();
			total = Integer.parseInt(item.get("total").toString());
			vo = new TreeMap();
			for (String date : date_lst) {
				vo.put(date, getColumnValue(source, label, date));
			}
			vo.put("id", id);
			vo.put("label", label);
			vo.put("total", total);

			result.add(vo);
		}
		return result;
	}

	static public int getColumnValue(List<Map> source, String label, String date) {
		for (Map item : source) {
			if (label.equals(item.get("label").toString()) && date.equals(item.get("date").toString())) {
				return Integer.parseInt(item.get("value").toString());
			}
		}
		return 0;
	}

	static public Map<String, String> getAroundDate(List<Map> source) {
		String max = source.stream().map(it -> it.get("date")).max(Comparator.comparing(String::valueOf)).get().toString();
		String min = source.stream().map(it -> it.get("date")).min(Comparator.comparing(String::valueOf)).get().toString();
		Map result = new HashMap();
		result.put("start", min);
		result.put("end", max);
		return result;
	}

}
