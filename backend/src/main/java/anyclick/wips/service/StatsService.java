package anyclick.wips.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import anyclick.wips.repository.StatsRepository;

@Service
public class StatsService {

	@Autowired
	StatsRepository repo;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public List getEventStatsData(Map<String, Object> $param) {
		long start_date = Long.parseLong($param.get("start_date").toString());
		long end_date = Long.parseLong($param.get("end_date").toString());
		int type = Integer.parseInt($param.get("type").toString());
		return repo.getEventStatsList(type, start_date, end_date);
	}
}
