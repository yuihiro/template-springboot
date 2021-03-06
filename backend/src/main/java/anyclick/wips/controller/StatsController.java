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

import anyclick.wips.service.StatsService;

@RequestMapping("api")
@RestController
public class StatsController {

	@Autowired
	StatsService service;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping("getEventStatsData")
	public List getEventStatsData(@RequestBody Map<String, Object> $param) {
		return service.getEventStatsData($param);
	}
}
