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

import anyclick.wips.service.AdminService;

@RequestMapping("api")
@RestController
public class AdminController {

	@Autowired
	public AdminService service;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping("getAdminList")
	public List list(@RequestBody Map<String, Object> $param) {
		return service.getAdminList($param);
	}

	@PostMapping("getAdminInfo")
	public Map get(@RequestBody Map<String, Object> $param) {
		String id = $param.get("id").toString();
		return service.getAdmin(id);
	}

	@PostMapping("insertAdminInfo")
	public int insert(@RequestBody Map<String, Object> $param) {
		return service.insertAdmin($param);
	}

	@PostMapping("updateAdminInfo")
	public int update(@RequestBody Map<String, Object> $param) {
		return service.updateAdmin($param);
	}

	@PostMapping("deleteAdminInfo")
	public int delete(@RequestBody Map<String, Object> $param) {
		String id = $param.get("id").toString();
		return service.deleteAdmin(id);
	}

	@PostMapping("updateAdminPassword")
	public int updateAdminPassword(@RequestBody Map<String, Object> $param) {
		return service.updateAdminPassword($param);
	}

}
