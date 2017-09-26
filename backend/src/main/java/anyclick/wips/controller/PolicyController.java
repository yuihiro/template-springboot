package anyclick.wips.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import anyclick.wips.service.PolicyService;

@RequestMapping("api")
@RestController
public class PolicyController {

	@Autowired
	public PolicyService service;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "getProfileData")
	public Map data() {
		return service.getProfileData();
	}

	@RequestMapping(value = "insertProfileInfo", method = RequestMethod.POST)
	public int insert(@RequestBody Map<String, Object> $param) {
		return service.insertProfile($param);
	}

	@RequestMapping(value = "updateProfileInfo", method = RequestMethod.POST)
	public int update(@RequestBody Map<String, Object> $param) {
		return service.updateProfile($param);
	}

	@RequestMapping(value = "deletePolicyInfo", method = RequestMethod.POST)
	public int delete(@RequestBody Map<String, Object> $param) {
		return service.deleteProfile($param);
	}

	@RequestMapping(value = "applyPolicyInfo", method = RequestMethod.POST)
	public int apply(@RequestBody Map<String, Object> $param) {
		return service.applyProfile($param);
	}

	@RequestMapping(value = "getPolicyProcess", method = RequestMethod.POST)
	public List<Map> process(@RequestParam("id") long $id) {
		return service.processProfile($id);
	}
}
