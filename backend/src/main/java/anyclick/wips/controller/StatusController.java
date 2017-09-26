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

import anyclick.wips.service.StatusService;

@RequestMapping("api")
@RestController
public class StatusController {

	@Autowired
	StatusService service;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping("getStatusData")
	public Map getStatusData(@RequestBody Map<String, Object> $param, HttpServletRequest $req) {
		Map result = service.getStatusData($param);
		//		result.put("session_id", $req.getSession(false).getId());
		return result;
	}
}
