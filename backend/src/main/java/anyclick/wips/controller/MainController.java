package anyclick.wips.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anyclick.wips.config.annotation.AuthCheck;
import anyclick.wips.service.MainService;

@RequestMapping("api")
@RestController
public class MainController {

	@Autowired
	MainService main_service;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping("login")
	@AuthCheck()
	public Map login(@RequestBody Map<String, Object> $param, HttpServletRequest $req) {
		String id = $param.get("id").toString();
		String pwd = $param.get("pwd").toString();
		return main_service.login(id, pwd, $req);
	}

	@PostMapping("logout")
	@AuthCheck()
	public void logout(@RequestBody Map<String, Object> $param, HttpServletRequest req) {
		String id = $param.get("id").toString();
		main_service.logout(id);
	}
}
