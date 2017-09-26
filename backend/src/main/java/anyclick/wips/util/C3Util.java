package anyclick.wips.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class C3Util {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	static public List<List> parsePieChartData(List<Map> source) {
		List<List> result = new ArrayList();
		List vo = null;
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
		vo.add("x");
		vo.addAll(date_lst);
		result.add(vo);
		for (Map item : source) {
			vo = new ArrayList();
			vo.add(item.get("label"));
			for (String date : date_lst) {
				vo.add(item.get(date));
			}
			result.add(vo);
		}
		return result;
	}
}
