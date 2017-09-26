package anyclick.wips;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import anyclick.wips.service.MainService;

@Component
public class SessionListener implements HttpSessionListener {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		log.info("SESSION CREATED : " + se.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		log.info("SESSION DESTROYED : " + se.getSession().getId());
		if (se.getSession().getAttribute("id") != null) {
			String id = se.getSession().getAttribute("id").toString();
			log.info("SESSION REMOVE : " + id);
			MainService.removeAdmin(id);
		}
	}
}