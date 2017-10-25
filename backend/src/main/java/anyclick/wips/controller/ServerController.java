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

import anyclick.wips.service.ServerService;

@RequestMapping("api")
@RestController
public class ServerController {

	@Autowired
	public ServerService service;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping("getServerListCnt")
	public long count(@RequestBody Map<String, Object> $param) {
		return service.getServerListCnt($param);
	}

	@PostMapping("getServerList")
	public List list(@RequestBody Map<String, Object> $param) {
		return service.getServerList($param);
	}

	@PostMapping("getServerInfo")
	public Map get(@RequestBody Map<String, Object> $param) {
		long id = Long.parseLong($param.get("id").toString());
		return service.getServer(id);
	}

	@PostMapping("insertServerInfo")
	public int insert(@RequestBody Map<String, Object> $param) {
		return service.insertServer($param);
	}

	@PostMapping("updateServerInfo")
	public int update(@RequestBody Map<String, Object> $param) {
		return service.updateServer($param);
	}

	@PostMapping("deleteServerInfo")
	public int delete(@RequestBody Map<String, Object> $param) {
		long id = Long.parseLong($param.get("id").toString());
		return service.deleteServer(id);
	}

	@PostMapping("getPolicyLogListByServer")
	public List getPolicyLogList(@RequestBody Map<String, Object> $param) {
		long id = Long.parseLong($param.get("id").toString());
		return service.getPolicyLogList(id);
	}
}
