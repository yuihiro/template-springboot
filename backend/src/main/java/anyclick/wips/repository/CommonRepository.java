package anyclick.wips.repository;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Maps;

import anyclick.wips.config.AppProperties;
import anyclick.wips.error.AuthException;

@Repository
public class CommonRepository {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	NamedParameterJdbcTemplate template;

	public HttpSession getSession() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
		HttpServletRequest request = attributes.getRequest();
		HttpServletResponse response = attributes.getResponse();
		HttpSession session = request.getSession(false);
		if (AppProperties.debug == true) {
			return session;
		}
		if (session == null) {
			throw new AuthException("NO SESSION");
		}
		return session;
	}

	public String getAdminID() {
		String result = null;
		if (getSession() != null) {
			result = getSession().getAttribute("id").toString();
		} else {
			if (AppProperties.debug == true) {
				result = "root";
			}
		}
		return result;
	}

	public int getAdminType() {
		return (int) getSession().getAttribute("type");
	}

	public int updateAdminLog(int type, String comment) {
		String sql = "INSERT INTO admin_log_tbl(type, admin, date, comment) VALUES(:type, :id, :date, :comment) ";
		int result = 0;
		Map param = Maps.newHashMap();
		param.put("type", type);
		param.put("id", getAdminID());
		param.put("date", new Date().getTime());
		param.put("comment", comment);
		result = template.update(sql, param);
		return result;
	}
}
