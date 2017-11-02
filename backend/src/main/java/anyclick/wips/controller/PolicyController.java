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

import anyclick.wips.service.PolicyService;

@RequestMapping("api")
@RestController
public class PolicyController {

	@Autowired
	public PolicyService service;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	// 탐지차단정책관리
	@PostMapping("getProfileData")
	public Map data() {
		return service.getProfileData();
	}

	@PostMapping("insertProfile")
	public long insert(@RequestBody Map<String, Object> $param) {
		return service.insertProfile($param);
	}

	@PostMapping("updateProfile")
	public long update(@RequestBody Map<String, Object> $param) {
		return service.updateProfile($param);
	}

	@PostMapping("deletePolicy")
	public int delete(@RequestBody Map<String, Object> $param) {
		return service.deleteProfile($param);
	}

	@PostMapping("applyProfile")
	public long apply(@RequestBody Map<String, Object> $param) {
		return service.applyProfile($param);
	}

	@PostMapping("getPolicyProcess")
	public List<Map> process(@RequestBody Map<String, Object> $param) {
		long id = Long.parseLong($param.get("id").toString());
		return service.processProfile(id);
	}

	// AP/단말분류관리
	@PostMapping("getClassifyData")
	public Map getClassifyData() {
		return service.getClassifyData();
	}

	@PostMapping("applyClassify")
	public long applyClassify(@RequestBody Map<String, Object> $param) {
		return service.applyClassify($param);
	}
}
