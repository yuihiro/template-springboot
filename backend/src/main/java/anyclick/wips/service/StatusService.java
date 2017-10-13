package anyclick.wips.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import anyclick.wips.repository.ServerRepository;
import anyclick.wips.repository.StatusRepository;

@Service
public class StatusService {

	@Autowired
	StatusRepository repo;

	@Autowired
	ServerRepository server_repo;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public Map getStatusData(Map<String, Object> $param) {
		//		long user_cnt = repo.getUserCnt(null);
		//		long nas_cnt = repo.getNasCnt(null);
		//		long user_request_cnt = repo.getUserRequestCnt(null);
		long start_time = Long.parseLong($param.get("start_time").toString());
		long last_event_id = Long.parseLong($param.get("last_event_id").toString());

		Map result = Maps.newHashMap();
		result.put("stats_data", repo.getStatsCnt($param));
		result.put("server_data", server_repo.getServerList(Maps.newHashMap()));
		Map param = Maps.newHashMap();
		param.put("start_time", start_time);
		param.put("last_event_id", last_event_id);
		param.put("offset", 0);
		param.put("limit", 5);
		List<Map> event_lst = repo.getStatusEventList(param);
		if (event_lst.size() > 0) {
			last_event_id = Long.parseLong(event_lst.get(0).get("id").toString());
		}
		result.put("event_data", event_lst);
		result.put("last_event_id", last_event_id);
		result.put("update_time", System.currentTimeMillis());
		//		log.debug(DateUtil.timeToStr(start_time) + " / " + DateUtil.timeToStr(System.currentTimeMillis()) + " / " + last_event_id);
		return result;
	}
}
