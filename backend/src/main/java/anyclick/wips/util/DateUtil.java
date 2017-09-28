package anyclick.wips.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class DateUtil {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static SimpleDateFormat second_sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

	public static SimpleDateFormat minute_sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
	public static SimpleDateFormat date_sdf = new SimpleDateFormat("yy-MM-dd");

	public static Map<String, String> getTodayTime() {
		String date = date_sdf.format(new Date(System.currentTimeMillis()));
		String start_time = date + " 00:00:00";
		String end_time = date + " 23:59:59";

		Map<String, String> result = Maps.newHashMap();
		result.put("start_time", start_time);
		result.put("end_time", end_time);
		return result;
	}

	public static String timeToStr(Long time) {
		Date date = new Date(time);
		return second_sdf.format(date);
	}

	public static String timeToStr(Timestamp time) {
		if (time == null)
			return "-";
		return second_sdf.format(time);
	}

	public static String timeToLong(String time, boolean begin) {
		time += (begin) ? " 00:00:00" : " 23:59:59";
		try {
			Date date = second_sdf.parse(time);
			return String.valueOf(date.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-";
	}

	public static String addTimeStr(String time, boolean begin) {
		if (time.length() == 10) {
			return time += (begin) ? " 00:00:00" : " 23:59:59";
		}
		return time;
	}

	public static String trimDateTime(String time) {
		if (time == null) {
			return "-";
		}
		String result = "-";
		try {
			result = date_sdf.format(date_sdf.parse(time));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String secondToTime(long time) {
		String result = LocalTime.MIN.plusSeconds(time).toString();
		if (result.equals("00:00")) {
			result = "-";
		}
		return result;
	}

	public static List getDateFromTwoDate(String start, String end) {
		List result = new ArrayList();
		result.add(start);
		Date start_date = null;
		Date end_date = null;

		try {
			start_date = date_sdf.parse(start);
			end_date = date_sdf.parse(end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(start_date);
		while (cal.getTime().before(end_date)) {
			cal.add(Calendar.DATE, 1);
			result.add(date_sdf.format(cal.getTime()));
		}
		//		if (result.size() == 2) {
		//			result.add(date_sdf.format(cal.getTime()));
		//		}
		//
		return result;
	}
}
