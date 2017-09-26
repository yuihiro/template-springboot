package anyclick.wips.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import anyclick.wips.repository.AdminRepository;
import anyclick.wips.repository.CommonRepository;

@Service
@Transactional
public class AdminService {

	@Autowired
	AdminRepository repo;

	@Autowired
	CommonRepository common_repo;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public List getAdminList(Map<String, Object> $param) {
		return repo.getAdminList($param, "LIST");
	}

	public Map getAdmin(String $id) {
		return repo.getAdmin($id);
	}

	public int insertAdmin(Map<String, Object> $param) {
		int result = repo.insertAdmin($param);
		if (result > 0) {
			common_repo.updateAdminLog(3, "관리자(" + $param.get("user_id").toString() + ")를 추가하였습니다.");
		}
		return result;
	}

	public int updateAdmin(Map<String, Object> $param) {
		int result = repo.updateAdmin($param);
		if (result > 0) {
			common_repo.updateAdminLog(3, "관리자(" + $param.get("user_id").toString() + ")를 수정하였습니다.");
		}
		return result;
	}

	public int deleteAdmin(String $id) {
		int result = repo.deleteAdmin($id);
		if (result > 0) {
			common_repo.updateAdminLog(3, "관리자(" + $id + ")를 삭제하였습니다.");
		}
		return result;
	}
}
