package anyclick.wips.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anyclick.wips.service.LogService;

@RequestMapping("api")
@RestController
public class LogController {

	@Autowired
	LogService service;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping("getEventListCnt")
	public Long getEventListCnt(@RequestBody Map<String, Object> $param) {
		return service.getEventListCnt($param);
	}

	@PostMapping("getEventList")
	public List getEventList(@RequestBody Map<String, Object> $param) {
		return service.getEventList($param);
	}

	@PostMapping("getAdminLogListCnt")
	public Long getAdminLogListCnt(@RequestBody Map<String, Object> $param) {
		return service.getAdminLogListCnt($param);
	}

	@PostMapping("getAdminLogList")
	public List getAdminLogList(@RequestBody Map<String, Object> $param) {
		return service.getAdminLogList($param);
	}

	@PostMapping("getSystemLogListCnt")
	public Long getSystemLogListCnt(@RequestBody Map<String, Object> $param) {
		return service.getSystemLogListCnt($param);
	}

	@PostMapping("getSystemLogList")
	public List getSystemLogList(@RequestBody Map<String, Object> $param) {
		return service.getSystemLogList($param);
	}

}
