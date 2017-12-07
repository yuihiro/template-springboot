package anyclick.wips.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import anyclick.wips.config.AppProperties;
import anyclick.wips.repository.AdminRepository;
import anyclick.wips.repository.CommonRepository;
import anyclick.wips.repository.MainRepository;
import anyclick.wips.util.CryptoUtil;

@Service
@Transactional
public class MainService {

	@Autowired
	AdminRepository admin_repo;

	@Autowired
	CommonRepository common_repo;

	@Autowired
	MainRepository main_repo;

	@Autowired
	ConfigService config_service;

	private static final Logger log = LoggerFactory.getLogger("MainService");
	public static List<HttpSession> login_user_lst = Lists.newCopyOnWriteArrayList();

	public Map login(String $id, String $pwd, HttpServletRequest $req) {
		String pwd = CryptoUtil.decrypt($pwd, AppProperties.CRYOTO_KEY);
		Map login_data = null;
		if ($id.equals(AppProperties.INIT_ID) && pwd.equals(AppProperties.INIT_PWD)) {
			login_data = Maps.newHashMap();
			login_data.put("id", $id);
			login_data.put("pwd", pwd);
			login_data.put("type", 0);
		} else {
			login_data = admin_repo.getAdmin($id);
		}
		if (login_data != null) {
			if (pwd.equals(login_data.get("pwd").toString())) {
				HttpSession session = $req.getSession(true);
				session.setAttribute("id", login_data.get("id"));
				session.setAttribute("type", login_data.get("type"));
				session.setMaxInactiveInterval(AppProperties.SESSION_TIMEOUT);
				login_data.put("session_id", session.getId());
				login_data.put("login_type", "SUCCESS");
				login_user_lst.add(session);
				common_repo.updateAdminLog(1, "login 했습니다");
				log.info("login : " + $id + " / " + session.getId());
				log.info("login length : " + login_user_lst.size());
				//				printLoginAdminList();
			} else {
				login_data.put("login_type", "FAIL_PWD");
			}
		} else {
			login_data = Maps.newHashMap();
			login_data.put("login_type", "FAIL_ID");
		}
		login_data.remove("pwd");
		Map app_data = Maps.newHashMap();
		if ("SUCCESS".equals(login_data.get("login_type"))) {
			main_repo.initDatabase();
			app_data = config_service.getAppData();
		}
		Map result = Maps.newHashMap();
		result.put("login_data", login_data);
		result.put("app_data", app_data);
		return result;
	}

	public void logout(String $id) {
		for (HttpSession item : login_user_lst) {
			String id = null;
			try {
				id = item.getAttribute("id").toString();
			} catch (Exception e) {

			}
			if ($id.equals(id)) {
				common_repo.updateAdminLog(1, "logout 했습니다");
				item.invalidate();
				log.info("logout : " + $id + " / " + item.getId());
				log.info("login length " + login_user_lst.size());
				//				printLoginAdminList();
				return;
			}
		}
	}

	public void printLoginAdminList() {
		for (HttpSession item : login_user_lst) {
			log.debug(item.getId() + " / " + item.getAttribute("id"));
		}
	}

	public static void removeAdmin(String $id) {
		for (HttpSession item : login_user_lst) {
			String id = null;
			try {
				id = item.getAttribute("id").toString();
			} catch (Exception e) {

			}
			if ($id.equals(id)) {
				log.info("DELETE : " + $id);
				login_user_lst.remove(item);
				return;
			}
		}
	}
}
