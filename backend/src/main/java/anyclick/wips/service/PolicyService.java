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
import anyclick.wips.repository.ServerRepository;

@Service
@Transactional
public class PolicyService {

	@Autowired
	PolicyRepository repo;

	@Autowired
	ServerRepository server_repo;

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

	public long applyProfile(Map<String, Object> $param) {
		log.debug($param.toString());
		long id = Long.parseLong($param.get("id").toString());
		String name = $param.get("name").toString();
		long command_id = repo.insertPolicyCommand(1, 1, 1, id, name);
		List<Map> details = (List) $param.get("map_lst");
		for (Map item : details) {
			item.put("command_id", command_id);
		}
		log.debug(details.toString());
		repo.insertPolicyCommandDetail(details);
		common_repo.updateAdminLog(3, "정책프로파일(" + name + ")을 적용하였습니다.");
		return command_id;
	}

	public long insertProfile(Map<String, Object> $param) {
		log.debug($param.toString());
		Map profile_info = (Map) $param.get("profile_info");
		String name = profile_info.get("name").toString();
		long id = repo.insertProfile(profile_info);
		List<Map> policy_lst = (List) $param.get("policy_lst");
		for (Map item : policy_lst) {
			item.put("server_id", 0);
			item.put("profile_idx", id);
		}
		repo.deletePolicyList(id);
		repo.insertPolicyList(policy_lst);
		long command_id = repo.insertPolicyCommand(1, 3, 3, id, name);
		common_repo.updateAdminLog(3, "정책프로파일(" + name + ")를 추가하였습니다.");
		return id;
	}

	public long updateProfile(Map<String, Object> $param) {
		log.debug($param.toString());
		Map profile_info = (Map) $param.get("profile_info");
		String name = profile_info.get("name").toString();
		long id = Long.parseLong(profile_info.get("id").toString());
		repo.updateProfile(profile_info);
		List<Map> policy_lst = (List) $param.get("policy_lst");
		for (Map item : policy_lst) {
			item.put("server_id", 0);
			item.put("profile_idx", id);
		}
		repo.deletePolicyList(id);
		repo.insertPolicyList(policy_lst);
		common_repo.updateAdminLog(3, "정책프로파일(" + name + ")을 수정하였습니다.");
		long command_id = -1;
		if ($param.get("map_lst") != null) {
			command_id = repo.insertPolicyCommand(1, 2, 3, id, name);
			List<Map> details = (List) $param.get("map_lst");
			for (Map item : details) {
				item.put("command_id", command_id);
			}
			log.debug(details.toString());
			repo.insertPolicyCommandDetail(details);
			//			common_repo.updateAdminLog(3, "정책프로파일(" + name + ")을 적용하였습니다.");
		}
		return command_id;
	}

	public int deleteProfile(Map<String, Object> $param) {
		long id = Long.parseLong($param.get("id").toString());
		String name = $param.get("name").toString();
		int result = repo.deleteProfile(id);
		repo.deletePolicyList(id);
		repo.insertPolicyCommand(1, 4, 3, id, name);
		common_repo.updateAdminLog(3, "정책프로파일(" + name + ")를 삭제하였습니다.");
		return result;
	}

	public List<Map> processProfile(long $id) {
		List<Map> result = repo.getPolicyCommandStatus($id);
		return result;
	}

	public Map getClassifyData() {
		Map result = Maps.newHashMap();
		List server_lst = server_repo.getServerList(Maps.newHashMap());
		List ap_lst = repo.getApList();
		List station_lst = repo.getStationList();
		result.put("server_lst", server_lst);
		result.put("ap_lst", ap_lst);
		result.put("station_lst", station_lst);
		return result;
	}

	public long applyClassify(Map<String, Object> $param) {
		repo.insertApList((List) $param.get("ap_lst"));
		repo.insertStationList((List) $param.get("station_lst"));

		long command_id = repo.insertPolicyCommand(2, 1, 1, -1, null);
		List<Map> details = (List) $param.get("map_lst");
		for (Map item : details) {
			item.put("command_id", command_id);
		}
		log.debug(details.toString());
		repo.insertPolicyCommandDetail(details);
		common_repo.updateAdminLog(3, "AP/단말분류를 적용하였습니다.");
		return command_id;
	}
}
