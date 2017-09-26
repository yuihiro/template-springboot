package anyclick.wips.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import anyclick.wips.config.AppProperties;
import anyclick.wips.config.annotation.AuthCheck;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (request.getRequestURI().equals("/error") == false) {
			saveRequestInfo(request, handler);
		}
		log.info(request.getRequestURI() + " / " + AppProperties.debug + " / " + request.getSession(false));
		if (AppProperties.debug || handler instanceof HandlerMethod == false) {
			return super.preHandle(request, response, handler);
		}
		AuthCheck annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthCheck.class);
		//sslCheckAnnotation = (sslCheckAnnotation == null) ? handlerMethod.getBeanType().getAnnotation(SslCheck.class) : sslCheckAnnotation;
		if (annotation == null) {
			if (request.getSession(false).getAttribute("id") == null) {
				response.setStatus(401);
				return false;
			}
		}
		return super.preHandle(request, response, handler);
		/*
		NoAuthCheck auth = ((HandlerMethod) handler).getMethodAnnotation(NoAuthCheck.class);
		HttpSession session = request.getSession(false);
		
		if (auth != null) {
			return super.preHandle(request, response, handler);
		} else if (session != null) {
			Object authInfo = session.getAttribute("auth_info");
			if (authInfo != null) {
				return super.preHandle(request, response, handler);
			}
		}
		
		response.sendRedirect("/login");
		return false;
		*/
	}

	public void saveRequestInfo(HttpServletRequest request, Object handler) {
		String uri = request.getRequestURI();
		String query = request.getQueryString();
		String controller = null;
		String action = null;
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			controller = handlerMethod.getBeanType().getSimpleName().replace("Controller", "");
			action = handlerMethod.getMethod().getName();
		}
		if (query == null) {
			query = "";
		} else {
			query = "?" + query;
		}
		request.setAttribute("uri", uri + query);
		request.setAttribute("controller", controller);
		request.setAttribute("action", action);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}
}
