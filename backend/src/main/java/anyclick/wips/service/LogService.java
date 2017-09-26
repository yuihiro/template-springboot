package anyclick.wips.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import anyclick.wips.repository.LogRepository;

@Service
@Transactional
public class LogService {

	@Autowired
	LogRepository repo;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public Long getEventListCnt(Map<String, Object> $param) {
		return repo.getEventListCnt($param);
	}

	public List getEventList(Map<String, Object> $param) {
		return repo.getEventList($param);
	}

	public Long getAdminLogListCnt(Map<String, Object> $param) {
		return repo.getAdminLogListCnt($param);
	}

	public List getAdminLogList(Map<String, Object> $param) {
		return repo.getAdminLogList($param);
	}

	public Long getSystemLogListCnt(Map<String, Object> $param) {
		return repo.getSystemLogListCnt($param);
	}

	public List getSystemLogList(Map<String, Object> $param) {
		return repo.getSystemLogList($param);
	}
}
