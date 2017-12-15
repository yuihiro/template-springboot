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

	@PostMapping("getManageApListCnt")
	public long getManageApListCnt(@RequestBody Map<String, Object> $param) {
		return service.getManageApListCnt();
	}

	@PostMapping("getManageApList")
	public List<Map> getManageApList(@RequestBody Map<String, Object> $param) {
		return service.getManageApList($param);
	}

	@PostMapping("getManageStationListCnt")
	public long getManageStationListCnt(@RequestBody Map<String, Object> $param) {
		return service.getManageStationListCnt();
	}

	@PostMapping("getManageStationList")
	public List<Map> getManageStationList(@RequestBody Map<String, Object> $param) {
		return service.getManageStationList($param);
	}

	@PostMapping("insertManageApList")
	public long insertManageApList(@RequestBody List<Map> $param) {
		return service.insertManageApList($param);
	}

	@PostMapping("insertManageStationList")
	public long insertManageStationList(@RequestBody List<Map> $param) {
		return service.insertManageStationList($param);
	}

	@PostMapping("deleteManageApList")
	public long deleteManageApList(@RequestBody List<String> $param) {
		return service.deleteManageApList($param);
	}

	@PostMapping("deleteManageStationList")
	public long deleteManageStationList(@RequestBody List<String> $param) {
		return service.deleteManageStationList($param);
	}

	@PostMapping("applyClassify")
	public long applyClassify(@RequestBody Map<String, Object> $param) {
		return service.applyClassify($param);
	}
}
