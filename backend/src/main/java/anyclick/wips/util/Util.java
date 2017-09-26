package anyclick.wips.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	static public Map deepMerge(Map original, Map newMap) {
		for (Object key : newMap.keySet()) {
			if (newMap.get(key) instanceof Map && original.get(key) instanceof Map) {
				Map originalChild = (Map) original.get(key);
				Map newChild = (Map) newMap.get(key);
				original.put(key, deepMerge(originalChild, newChild));
			} else {
				original.put(key, newMap.get(key));
			}
		}
		return original;
	}

	public static Map getParameterMap(HttpServletRequest $request) {
		Enumeration name_list = $request.getParameterNames();
		Map result = new HashMap();
		while (name_list.hasMoreElements()) {
			String name = name_list.nextElement().toString();
			String value = $request.getParameter(name);
			result.put(name, value);
		}
		return result;
	}

	public static List getListFromEnum(Enum[] items) {
		List result = new ArrayList();
		for (Enum item : items) {
			Map vo = new HashMap();
			vo.put("label", item.name());
			vo.put("value", item);
			result.add(vo);
		}
		return result;
	}

	public static String getStringFromObject(Object data) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(data);
	}

	public static Boolean toBoolean(Object value) {
		if (value instanceof Integer) {
			if (Integer.parseInt(value.toString()) == 1) {
				return true;
			}
		} else if (value instanceof String) {
			if (value.toString().equalsIgnoreCase("Y") || value.toString().equalsIgnoreCase("1")) {
				return true;
			}
		}
		return false;
	}

	public static String toHyphen(Object value) {
		if (value == null) {
			return "-";
		}
		if (value instanceof String) {
			if (((String) value).trim().length() == 0) {
				return "-";
			}
		}
		return value.toString();
	}

	public static String toNullStr(Object value) {
		if (value == null) {
			return "null";
		}
		if (value instanceof String) {
			if (((String) value).trim().length() == 0) {
				return "null";
			}
		}
		return value.toString();
	}

	public static Boolean isEmpty(Object value) {
		if (value == null) {
			return true;
		}

		if (value instanceof String) {
			String str = (String) value;
			if (str.trim().length() == 0 || str.endsWith("-") || str.equalsIgnoreCase("null")) {
				return true;
			}
		}
		return false;
	}

	public static Map getEmptyMap() {
		return new HashMap() {
		};
	}

	public static String macToStr(Long mac_addr_number) {
		String mac_addr = "";
		String temp = mac_addr_number.toHexString(mac_addr_number);
		for (int len = 0; len < 12 && temp.length() < 12; len++) {
			temp = "0" + temp;
		}
		mac_addr = temp.substring(0, 2) + ":" + temp.substring(2, 4) + ":" + temp.substring(4, 6) + ":" + temp.substring(6, 8) + ":" + temp.substring(8, 10) + ":"
				+ temp.substring(10, 12);
		if (mac_addr.toUpperCase().equals("FF:FF:FF:FF:FF:FF") == true || mac_addr.toUpperCase().equals("00:00:00:00:00:00") == true) {
			return "-";
		}
		return mac_addr.toUpperCase();
	}

	public static Long macToLong(String mac_addr) {
		mac_addr = mac_addr.substring(0, 2) + mac_addr.substring(3, 5) + mac_addr.substring(6, 8) + mac_addr.substring(9, 11) + mac_addr.substring(12, 14)
				+ mac_addr.substring(15, 17);
		BigInteger bi = new BigInteger(mac_addr, 16);
		return bi.longValue();
	}

	//	public static Object[] convertToObjectArray(Object array) {
	//		Class ofArray = array.getClass().getComponentType();
	//		if (ofArray.isPrimitive()) {
	//			List ar = new ArrayList();
	//			int length = Array.getLength(array);
	//			for (int i = 0; i < length; i++) {
	//				ar.add(Array.get(array, i));
	//			}
	//			return ar.toArray();
	//		} else {
	//			return (Object[]) array;
	//		}
	//	}

}
