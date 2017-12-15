package anyclick.wips.util;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryUtil {

	static private final Logger log = LoggerFactory.getLogger(QueryUtil.class);

	static public String getWhereQuery(Map<String, Object> $param) {
		if ($param == null) {
			return "";
		}
		String sql = "";
		Set<Entry<String, Object>> entries = $param.entrySet();
		int i = 1;
		int total = entries.size();
		boolean first = true;
		for (Entry<String, Object> entry : entries) {
			i++;
			if (Util.isEmpty(entry.getValue())) {
				continue;
			}
			if (entry.getKey().equals("offset") || entry.getKey().equals("limit")) {
				continue;
			}
			if (entry.getKey().equals("start_time") || entry.getKey().equals("end_time") || entry.getKey().equals("time_column") || entry.getKey().equals("time_epoch")) {
				continue;
			}
			if (entry.getKey().equals("order")) {
				continue;
			}
			if (StringUtils.contains(entry.getKey(), "|")) {
				continue;
			}

			if (sql.length() == 0) {
				sql += " WHERE ";
			} else {
				if (!first) {
					sql += " AND ";
				}
			}
			first = false;
			//if (entry.getKey().equalsIgnoreCase("id") || entry.getKey().equalsIgnoreCase("user_id") || entry.getKey().equalsIgnoreCase("name")
			//	|| entry.getKey().equalsIgnoreCase("user_name")) {

			if (StringUtils.contains(entry.getValue().toString(), "%")) {
				if (StringUtils.contains(entry.getKey().toString(), "sta") || StringUtils.contains(entry.getKey().toString(), "mac")
						|| StringUtils.contains(entry.getKey().toString(), "bssid")) {
					String value = entry.getValue().toString();
					entry.setValue(value.replaceAll(":", ""));
					sql += " HEX(" + entry.getKey() + ") LIKE :" + entry.getKey();
				} else {
					//entry.setValue("%" + entry.getValue() + "%");
					sql += " " + entry.getKey() + " LIKE :" + entry.getKey();
				}
			} else {
				if (StringUtils.contains(entry.getKey().toString(), "sta") || StringUtils.contains(entry.getKey().toString(), "mac")
						|| StringUtils.contains(entry.getKey().toString(), "bssid")) {
					String value = entry.getValue().toString();
					entry.setValue(value.replaceAll(":", ""));
					sql += " HEX(" + entry.getKey() + ") = :" + entry.getKey();
				} else {
					sql += " " + entry.getKey() + " = :" + entry.getKey();
				}
			}
		}
		sql += getKeyQuery($param, sql);
		return sql;
	}

	static public String getKeyQuery(Map<String, Object> $param, String $sql) {
		if ($param == null) {
			return "";
		}
		String sql = "";
		Set<Entry<String, Object>> entries = $param.entrySet();
		boolean first = true;
		for (Entry<String, Object> entry : entries) {
			if (StringUtils.contains(entry.getKey(), "|") && Util.isEmpty(entry.getValue()) == false) {
				if (Util.isEmpty($sql)) {
					sql += " WHERE ";
				} else {
					sql += " AND ";
				}
				sql += " ( ";
				String[] tokens = StringUtils.split(entry.getKey(), "|");
				for (int i = 1; i <= tokens.length; i++) {
					sql += " " + tokens[i - 1] + " LIKE :" + tokens[i - 1];
					if (i != tokens.length) {
						sql += " OR ";
					}
					$param.put(tokens[i - 1], entry.getValue());
				}
				sql += " ) ";
				return sql;
			}
		}
		return sql;
	}

	static public String getTimeQuery(Map<String, Object> $param, String $time_column, boolean $timestamp, String $sql) {
		if ($param == null || $param.get("start_time") == null) {
			return "";
		}
		String sql = "";
		if (Util.isEmpty($sql)) {
			sql += " WHERE ";
		} else {
			sql += " AND ";
		}
		String column = $time_column;
		String start_time = $param.get("start_time").toString();
		String end_time = $param.get("end_time").toString();
		Boolean isString = StringUtils.contains(start_time, "-") ? true : false;
		if (isString) {
			if ($timestamp) {
				start_time = DateUtil.addTimeStr(start_time, true);
				end_time = DateUtil.addTimeStr(end_time, false);
			} else {
				start_time = DateUtil.timeToLong(start_time, true);
				end_time = DateUtil.timeToLong(end_time, false);
			}
		}
		if (start_time != null && end_time != null) {
			sql += " " + column + " BETWEEN '" + start_time + "' AND '" + end_time + "' ";
		} else if (start_time != null) {
			sql += " '" + start_time + "' <= " + column + " ";
		} else {
			sql += " " + column + " <= '" + end_time + "' ";
		}
		return sql;
	}

	static public String getGroupQuery(Map<String, Object> $param) {
		if ($param == null || $param.get("offset") == null || $param.get("limit") == null) {
			return "";
		}
		Integer offset = Integer.parseInt($param.get("offset").toString());
		Integer limit = Integer.parseInt($param.get("limit").toString());
		return " LIMIT " + offset + "," + limit;
	}

	static public String getOrderQuery(String $param) {
		if ($param == null) {
			return "";
		}
		return " ORDER BY " + $param;
	}

	static public String getOrderQuery(Map $param) {
		if ($param == null || $param.get("order") == null) {
			return "";
		}
		return " ORDER BY " + $param.get("order").toString();
	}

	static public String getLimitQuery(Map<String, Object> $param) {
		if ($param == null || $param.get("offset") == null || $param.get("limit") == null) {
			return "";
		}
		Integer offset = Integer.parseInt($param.get("offset").toString());
		Integer limit = Integer.parseInt($param.get("limit").toString());
		return " LIMIT " + offset + "," + limit;
	}

	static public String getInsertQuery(Map<String, Object> $param, List<String> $except) {
		String key_sql = " ( ";
		String value_sql = " ( ";
		String sql = "";

		Set<Entry<String, Object>> entries = $param.entrySet();
		int i = 0;
		int total = entries.size();
		for (Entry<String, Object> entry : entries) {
			i++;
			if (isContain($except, entry.getKey()) == false) {
				key_sql += "`" + entry.getKey() + "`";
				value_sql += ":" + entry.getKey();
				if (i != total) {
					key_sql += ", ";
					value_sql += ", ";
				} else {
					key_sql += " ) ";
					value_sql += " ) ";
				}

			}
		}
		sql = key_sql + " VALUES " + value_sql;
		return sql;
	}

	static public String getUpdateQuery(Map<String, Object> $param, List<String> $except) {
		String sql = "";
		Set<Entry<String, Object>> entries = $param.entrySet();
		int i = 0;
		int total = entries.size();
		for (Entry<String, Object> entry : entries) {
			if (isContain($except, entry.getKey()) == false) {
				i++;
				if (i != 1) {
					sql += ", ";
				}
				sql += "`" + entry.getKey() + "` = :" + entry.getKey();
			} else {
				total--;
			}
		}
		return sql;
	}

	static public String getInQuery(List<String> $param) {
		String sql = "(";
		int total = $param.size();
		for (int i = 1; i <= total; i++) {
			sql += $param.get(i - 1);
			if (i != total) {
				sql += ",";
			}
		}
		sql += ")";
		return " IN " + sql;
	}

	static public String getJoinQuery(Object $column, String $query) {
		return (Util.isEmpty($column) == false) ? $query : "";
	}

	static public Boolean isContain(List<String> $data, String $value) {
		if ($data == null) {
			return false;
		}
		for (String item : $data) {
			if ($value.equals(item)) {
				return true;
			}
		}
		return false;
	}
}
