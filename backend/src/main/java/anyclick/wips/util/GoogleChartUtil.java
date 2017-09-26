package anyclick.wips.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoogleChartUtil {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	static public List<List> parsePieChartData(List<Map> source) {
		List<List> result = new ArrayList();
		String[] columns = new String[] { "label", "total", "id" };
		List vo = new ArrayList();
		for (String column : columns) {
			vo.add(column);
		}
		result.add(vo);

		for (Map item : source) {
			vo = new ArrayList();
			vo.add(item.get("label"));
			vo.add(item.get("total"));
			vo.add(item.get("id"));
			result.add(vo);
		}
		return result;
	}

	static public List parseLineChartData(List<Map> source, List<String> date_lst) {
		List<List> result = new ArrayList();
		List vo = new ArrayList();
		vo.add("date");
		for (Map item : source) {
			vo.add(item.get("label"));
		}
		result.add(vo);

		for (String date : date_lst) {
			vo = new ArrayList();
			vo.add(date);
			for (Map item : source) {
				vo.add(item.get(date));
			}
			result.add(vo);
		}
		return result;
	}
}
