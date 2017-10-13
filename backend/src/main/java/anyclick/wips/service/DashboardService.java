package anyclick.wips.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import anyclick.wips.repository.DashboardRepository;
import anyclick.wips.repository.ServerRepository;

@Service
public class DashboardService {

	@Autowired
	DashboardRepository repo;

	@Autowired
	ServerRepository server_repo;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public Map getDashboardData(Map<String, Object> $param) {
		Map result = Maps.newHashMap();
		//		long start_time = Long.parseLong($param.get("start_time").toString());
		//		long last_event_id = Long.parseLong($param.get("last_event_id").toString());
		result.put("stats_data", repo.getStatsCnt($param));
		result.put("event_data", repo.getEventCnt($param));
		result.put("server_data", server_repo.getServerList(Maps.newHashMap()));
		//		result.put("last_event_id", last_event_id);
		result.put("update_time", System.currentTimeMillis());
		return result;
	}
}
