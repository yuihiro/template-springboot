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
		Map config_data = config_service.getSystemConfig();
		int log_max = Integer.parseInt(config_data.get("login_max").toString());
		int ip_use = Integer.parseInt(config_data.get("ip_use").toString());
		int inited = Integer.parseInt(config_data.get("inited").toString());
		int auto_lock = Integer.parseInt(config_data.get("auto_lock").toString());
		int pwd_update = Integer.parseInt(config_data.get("pwd_update").toString());
		int auto_logout = Integer.parseInt(config_data.get("auto_logout").toString());
		String status = "SUCCESS";
		// 초기 접속 
		if (inited == 1 && $id.equals(AppProperties.INIT_ID) && pwd.equals(AppProperties.INIT_PWD)) {
			login_data = Maps.newHashMap();
			login_data.put("id", $id);
			login_data.put("pwd", pwd);
			status = "INIT";
		} else {
			login_data = admin_repo.getAdmin($id);
			if (login_data != null) {
				// 자동 계정 잠김
				int admin_type = Integer.parseInt(login_data.get("type").toString());
				if (admin_type != 0 && auto_lock != 0) {
					if (isUpdateAutoLock(login_data, auto_lock) == true) {
						main_repo.updateAdminLock($id, 1);
					}
				}
				// 로그인 시도 횟수				
				int try_cnt = main_repo.getAdminLoginTryCnt($id);
				main_repo.updateAdminLoginTryCnt($id, try_cnt + 1);
				if (try_cnt + 1 > log_max) {
					main_repo.updateAdminLock($id, 1);
				}
				int lock = main_repo.getAdminLock($id);
				if (lock == 1) {
					status = "LOCK";
				} else {
					// 로그인 성공
					if (pwd.equals(login_data.get("pwd").toString())) {
						// 강제 패스워드 변경
						if (pwd_update != 0) {
							long last_update = Long.parseLong(login_data.get("last_update").toString());
							if (last_update != 0) {
								long epoch = System.currentTimeMillis() / 1000;
								long diff = epoch - last_update;
								if (diff / (24 * 60 * 60) >= pwd_update) {
									status = "PWD_UPDATE";
								}
							}
						}
						// 중복접속
						HttpSession exist = isExistAdmin($id);
						if (exist != null) {
							status = "DUPLICATE";
						}
						// 로그인 실패(패스워드)					
					} else {
						status = "FAIL_PWD";
					}
				}
				// 로그인 실패(아이디)				
			} else {
				login_data = Maps.newHashMap();
				status = "FAIL_ID";
			}
		}
		login_data.put("login_type", status);
		login_data.remove("pwd");
		Map app_data = Maps.newHashMap();
		if ("SUCCESS".equals(status)) {
			HttpSession session = $req.getSession(true);
			session.setAttribute("id", login_data.get("id"));
			session.setAttribute("type", login_data.get("type"));
			session.setMaxInactiveInterval(AppProperties.SESSION_TIMEOUT);
			login_data.put("session_id", session.getId());
			login_data.put("login_type", "SUCCESS");
			login_user_lst.add(session);
			log.info("LOGIN SUCCESS : " + $id + " / " + session.getId());
			log.info("LOGIN LENGTH : " + login_user_lst.size());
			//				printLoginAdminList();
			common_repo.updateAdminLog(1, "login 했습니다");
			main_repo.updateAdminLoginTryCnt($id, 0);
			main_repo.updateDatabase();
			app_data = config_service.getAppData();
		}
		Map result = Maps.newHashMap();
		result.put("login_data", login_data);
		result.put("app_data", app_data);
		result.put("config_data", config_data);
		return result;
	}

	public HttpSession isExistAdmin(String $id) {
		for (HttpSession item : login_user_lst) {
			String id = null;
			try {
				id = item.getAttribute("id").toString();
			} catch (Exception e) {

			}
			if ($id.equals(id)) {
				return item;
			}
		}
		return null;
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
				log.info("LOGOUT : " + $id + " / " + item.getId());
				log.info("LOGIN LENGTH " + login_user_lst.size());
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

	public void removeInvalideAdmin() {
		log.info("REMOVE INVALIDE ADMIN");
		for (HttpSession item : login_user_lst) {
			if (item.getAttribute("id") == null) {
				log.info("DELETE : " + item.getId());
				login_user_lst.remove(item);
			}
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

	public boolean isUpdateAutoLock(Map data, int auto_lock) {
		if (Integer.parseInt(data.get("lock").toString()) == 0) {
			if (Integer.parseInt(data.get("last_login").toString()) != 0) {
				long last_login = Long.parseLong(data.get("last_login").toString());
				if (last_login != 0) {
					long epoch = System.currentTimeMillis() / 1000;
					long diff = epoch - last_login;
					if (diff / (24 * 60 * 60) >= auto_lock) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
