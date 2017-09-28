package anyclick.wips.error;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Maps;

//@Controller
public class GlobalErrorHandler implements ErrorController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ErrorAttributes errorAttributes;

	@RequestMapping("error")
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	Map error(HttpServletRequest request, HttpServletResponse response) {
		Map info = getErrorAttributes(request, true);
		log.error("ERROR : " + info.get("error"));
		Map result = Maps.newHashMap();
		result.put("url", info.get("path"));
		result.put("message", info.get("error"));
		result.put("status", info.get("status"));
		result.put("uri", request.getAttribute("uri"));
		result.put("controller", request.getAttribute("controller"));
		result.put("action", request.getAttribute("action"));
		return result;
	}

	@Override
	public String getErrorPath() {
		return "error";
	}

	private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
	}
}
