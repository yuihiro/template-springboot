package anyclick.wips.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping(value = "insertProfile", method = RequestMethod.POST)
	public long insert(@RequestBody Map<String, Object> $param) {
		return service.insertProfile($param);
	}

	@RequestMapping(value = "updateProfile", method = RequestMethod.POST)
	public long update(@RequestBody Map<String, Object> $param) {
		return service.updateProfile($param);
	}

	@RequestMapping(value = "deletePolicy", method = RequestMethod.POST)
	public int delete(@RequestBody Map<String, Object> $param) {
		return service.deleteProfile($param);
	}

	@RequestMapping(value = "applyProfile", method = RequestMethod.POST)
	public long apply(@RequestBody Map<String, Object> $param) {
		return service.applyProfile($param);
	}

	@RequestMapping(value = "getPolicyProcess", method = RequestMethod.POST)
	public List<Map> process(@RequestBody Map<String, Object> $param) {
		long id = Long.parseLong($param.get("id").toString());
		return service.processProfile(id);
	}
}
