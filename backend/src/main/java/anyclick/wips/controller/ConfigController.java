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

import anyclick.wips.service.ConfigService;

@RequestMapping("api")
@RestController
public class ConfigController {

	@Autowired
	ConfigService service;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping("getConfig")
	public Map getConfig(HttpServletRequest $req) {
		Map result = service.getConfig();
		return result;
	}

	@PostMapping("updateConfig")
	public int updateConfig(@RequestBody Map<String, Object> $param) {
		return service.updateConfig($param);
	}

	@PostMapping("updateInitConfig")
	public int updateInitConfig(@RequestBody Map<String, Object> $param) {
		return service.updateInitConfig($param);
	}
}
