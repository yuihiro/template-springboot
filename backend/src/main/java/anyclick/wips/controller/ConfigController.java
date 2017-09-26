package anyclick.wips.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
	public Map getConfig() {
		return service.getConfig();
	}

}
