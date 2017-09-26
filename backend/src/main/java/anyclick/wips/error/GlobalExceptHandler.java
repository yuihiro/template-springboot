package anyclick.wips.error;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	public Map except(HttpServletRequest request, Exception ex) {
		log.error("EXCEPTION : " + ex.getMessage() + " / " + request.getRequestURL() + " / " + ex.toString());
		String message = (ex.getMessage() != null) ? ex.getMessage() : ex.toString();
		ex.printStackTrace();
		Map result = new HashMap();
		result.put("url", request.getRequestURL().toString());
		result.put("message", message);
		result.put("status", HttpStatus.CONFLICT);
		return result;
	}

	//	ExceptionRepresentation handle(Exception exception) {
	//		ExceptionRepresentation body = new ExceptionRepresentation(exception.getLocalizedMessage());
	//		HttpStatus responseStatus = resolveAnnotatedResponseStatus(exception);
	//		return new ResponseEntity<ExceptionRepresentation>(body, responseStatus);
	//	}

	HttpStatus resolveAnnotatedResponseStatus(Exception exception) {
		ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);
		if (annotation != null) {
			return annotation.value();
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	//@ExceptionHandler(value = { Exception.class, RuntimeException.class })
	//@ResponseStatus(HttpStatus.BAD_REQUEST)
	//	public ModelAndView handleError(HttpServletRequest request, Exception ex) throws Exception {
	//		log.error("EXCEPTION : " + request.getRequestURL() + "/" + ex.getMessage() + "/" + ex.getStackTrace());
	//
	//		if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
	//			throw ex;
	//
	//		ModelAndView view = new ModelAndView();
	//		view.addObject("exception", ex);
	//		view.addObject("message", ex.getMessage());
	//		view.addObject("url", request.getRequestURL());
	//		view.setViewName("/error");
	//		return view;
	//	}

	/*
	@ModelAttribute("app_info")
	public Map getAppInfo(HttpServletRequest request) {
		String session_id = request.getSession().getId();
		String referer = request.getHeader("Referer");
		Map app_info = new HashMap<>();
	
		if (StringUtils.hasText(referer) == false) {
			referer = "/";
		}
	
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		String originalURL = urlPathHelper.getOriginatingRequestUri(request);
	
		app_info.put("session_id", session_id);
		app_info.put("referer", referer);
		return app_info;
	}	
	*/
}
