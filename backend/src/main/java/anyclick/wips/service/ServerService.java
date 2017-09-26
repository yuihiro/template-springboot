package anyclick.wips.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import anyclick.wips.repository.CommonRepository;
import anyclick.wips.repository.ServerRepository;

@Service
@Transactional
public class ServerService {

	@Autowired
	ServerRepository repo;

	@Autowired
	CommonRepository common_repo;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public long getServerListCnt(Map<String, Object> $param) {
		return repo.getServerListCnt($param);
	}

	public List getServerList(Map<String, Object> $param) {
		return repo.getServerList($param);
	}

	public Map getServer(long $id) {
		return repo.getServer($id);
	}

	public int insertServer(Map<String, Object> $param) {
		int result = repo.insertServer($param);
		if (result > 0) {
			common_repo.updateAdminLog(3, "서버(" + $param.get("name").toString() + ")를 추가하였습니다.");
		}
		return result;
	}

	public int updateServer(Map<String, Object> $param) {
		int result = repo.updateServer($param);
		if (result > 0) {
			common_repo.updateAdminLog(3, "서버(" + $param.get("name").toString() + ")를 수정하였습니다.");
		}
		return result;
	}

	public int deleteServer(long $id) {
		int result = repo.deleteServer($id);
		if (result > 0) {
			common_repo.updateAdminLog(3, "서버(" + $id + ")를 삭제하였습니다.");
		}
		return result;
	}

}
