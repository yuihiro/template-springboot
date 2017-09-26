package anyclick.wips.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import anyclick.wips.repository.CommonRepository;
import anyclick.wips.repository.PolicyRepository;

@Service
@Transactional
public class PolicyService {

	@Autowired
	PolicyRepository repo;

	@Autowired
	CommonRepository common_repo;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public Map getProfileData() {
		Map result = Maps.newHashMap();
		List profile_lst = repo.getProfileList();
		List map_lst = repo.getMapList();
		List policy_lst = repo.getPolicyList();
		result.put("profile_lst", profile_lst);
		result.put("map_lst", map_lst);
		result.put("policy_lst", policy_lst);
		return result;
	}

	public int applyProfile(Map<String, Object> $param) {
		log.debug($param.toString());
		long id = Long.parseLong($param.get("id").toString());
		String name = $param.get("name").toString();
		long command_id = repo.insertPolicyCommand(1, id, name);
		int result = 0;
		if (command_id > 0 && $param.get("map_data") != null) {
			List<Map> details = (List) $param.get("map_data");
			for (Map item : details) {
				item.put("command_id", command_id);
			}
			log.debug(details.toString());
			result = repo.insertPolicyCommandDetail(details);
		}
		common_repo.updateAdminLog(3, "정책프로파일(" + name + ")을 적용 시작하였습니다.");
		return result;
	}

	public int insertProfile(Map<String, Object> $param) {
		log.debug($param.toString());
		Map profile_info = (Map) $param.get("profile_info");
		String name = profile_info.get("name").toString();
		long id = repo.insertProfile(profile_info);
		List<Map> policy_lst = (List) $param.get("policy_lst");
		for (Map item : policy_lst) {
			item.put("server_id", -1);
			item.put("profile_id", id);
		}
		repo.deletePolicyList(id);
		repo.insertPolicyList(policy_lst);
		long command_id = repo.insertPolicyCommand(3, id, name);
		int result = common_repo.updateAdminLog(3, "정책프로파일(" + name + ")를 추가하였습니다.");
		return result;
	}

	public int updateProfile(Map<String, Object> $param) {
		log.debug($param.toString());
		Map profile_info = (Map) $param.get("profile_info");
		String name = profile_info.get("name").toString();
		long id = Long.parseLong(profile_info.get("id").toString());
		repo.updateProfile(profile_info);
		List<Map> policy_lst = (List) $param.get("policy_lst");
		for (Map item : policy_lst) {
			item.put("server_id", -1);
			item.put("profile_id", id);
		}
		repo.deletePolicyList(id);
		repo.insertPolicyList(policy_lst);
		long command_id = repo.insertPolicyCommand(2, id, name);
		int result = 0;
		if (command_id > 0 && $param.get("map_data") != null) {
			List<Map> details = (List) $param.get("map_data");
			for (Map item : details) {
				item.put("command_id", command_id);
			}
			log.debug(details.toString());
			result = repo.insertPolicyCommandDetail(details);
		}
		common_repo.updateAdminLog(3, "정책프로파일(" + name + ")을 수정하였습니다.");
		return result;
	}

	public int deleteProfile(Map<String, Object> $param) {
		long id = Long.parseLong($param.get("id").toString());
		String name = $param.get("name").toString();
		int result = repo.deleteProfile(id);
		repo.deletePolicyList(id);
		repo.insertPolicyCommand(4, id, name);
		common_repo.updateAdminLog(3, "정책프로파일(" + name + ")를 삭제하였습니다.");
		return result;
	}

	public List<Map> processProfile(long $id) {
		List<Map> result = repo.getPolicyCommandStatus($id);
		return result;
	}
}
