package anyclick.wips.repository.mapper;

public class MapperHelper {

	static public String commandStatus(int value) {
		String label = "";
		switch (value) {
		case 1:
			label = "준비";
			break;
		case 2:
			label = "진행중";
			break;
		case 3:
			label = "완료";
			break;
		case 4:
			label = "에러";
			break;
		default:
			break;
		}
		return label;
	}

	static public String commandType(int value) {
		String label = "";
		switch (value) {
		case 1:
			label = "프로파일";
			break;
		case 2:
			label = "관리AP/RAP목록";
			break;
		default:
			break;
		}
		return label;
	}

	static public String commandSubType(int value, int sub_type) {
		String label = "";
		switch (value) {
		case 1:
			if (sub_type == 1) {
				label = "적용";
			} else if (sub_type == 2) {
				label = "수정";
			} else if (sub_type == 3) {
				label = "추가";
			} else {
				label = "삭제";
			}
			break;
		case 2:
			label = "LIST";
			break;
		default:
			break;
		}
		return label;
	}

	static public String policyType(int value) {
		String label = "";
		switch (value) {
		case 0:
			label = "일반";
			break;
		case 1:
			label = "강제";
			break;
		default:
			break;
		}
		return label;
	}

	static public String serverStatus(int value) {
		String label = "";
		switch (value) {
		case 0:
			label = "OFF";
			break;
		case 1:
			label = "ON";
			break;
		default:
			break;
		}
		return label;
	}

	static public String adminType(int value) {
		String label = "";
		switch (value) {
		case 0:
			label = "최고관리자";
			break;
		case 1:
			label = "일반관리자";
			break;
		default:
			break;
		}
		return label;
	}

	static public String adminLogType(int value) {
		String label = "";
		switch (value) {
		case 1:
			label = "접속";
			break;
		case 2:
			label = "정책";
			break;
		case 3:
			label = "관리";
			break;
		default:
			break;
		}
		return label;
	}

	static public String lock(int value) {
		String label = "";
		switch (value) {
		case 0:
			label = "정상";
			break;
		case 1:
			label = "잠김";
			break;
		default:
			break;
		}
		return label;
	}
}
