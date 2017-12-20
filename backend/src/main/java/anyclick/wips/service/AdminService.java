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
		$param.put("last_update", System.currentTimeMillis() / 1000);
		int result = repo.insertAdmin($param);
		common_repo.updateAdminLog(3, "관리자계정(" + $param.get("user_id").toString() + ")를 추가하였습니다.");
		return result;
	}

	public int updateAdmin(Map<String, Object> $param) {
		if ($param.get("is_pwd") != null) {
			$param.put("last_update", System.currentTimeMillis() / 1000);
		}
		int result = repo.updateAdmin($param);
		common_repo.updateAdminLog(3, "관리자계정(" + $param.get("user_id").toString() + ")를 수정하였습니다.");
		return result;
	}

	public int deleteAdmin(String $id) {
		int result = repo.deleteAdmin($id);
		common_repo.updateAdminLog(3, "관리자계정(" + $id + ")를 삭제하였습니다.");
		return result;
	}

	public int updateAdminPassword(Map<String, Object> $param) {
		$param.put("last_update", System.currentTimeMillis() / 1000);
		repo.updateAdmin($param);
		common_repo.updateAdminLog(3, "비밀번호를 변경하였습니다.");
		return 1;
	}

}
