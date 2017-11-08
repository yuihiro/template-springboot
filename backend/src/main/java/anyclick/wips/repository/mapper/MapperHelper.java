package anyclick.wips.repository.mapper;

public class MapperHelper {

	static public String commandStatus(int value) {
		String label = "-";
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
		String label = "-";
		switch (value) {
		case 1:
			label = "프로파일";
			break;
		case 2:
			label = "AP/단말분류";
			break;
		default:
			break;
		}
		return label;
	}

	static public String commandSubType(int value, int sub_type) {
		String label = "-";
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
			label = "적용";
			break;
		default:
			break;
		}
		return label;
	}

	static public String policyType(int value) {
		String label = "-";
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
		String label = "-";
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
		String label = "-";
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
		String label = "-";
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
		String label = "-";
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

	static public String media(int value) {
		String label = "-";
		switch (value) {
		case 1:
			label = "a";
			break;
		case 2:
			label = "b";
			break;
		case 4:
			label = "g";
			break;
		case 6:
			label = "b/g";
			break;
		case 9:
			label = "a/n";
			break;
		case 12:
			label = "g/n";
			break;
		case 14:
			label = "b/g/n";
			break;
		case 17:
			label = "a/ac";
			break;
		case 24:
			label = "n/ac";
			break;
		case 25:
			label = "a/n/ac";
			break;
		default:
			label = "사용안함";
			break;
		}
		label = (label.equals("사용안함") != true) ? "802.11" + label : label;
		return label;
	}

	static public String cipher(int value) {
		String label = "-";
		switch (value) {
		case 0:
			label = "NONE";
			break;
		case 1:
			label = "WEP";
			break;
		case 2:
			label = "TKIP";
			break;
		case 3:
			label = "WRAP";
			break;
		case 4:
			label = "CCMP";
			break;
		default:
			label = "사용안함";
			break;
		}
		return label;
	}

	static public String network(int value) {
		String label = "-";
		switch (value) {
		case 1:
			label = "AP";
			break;
		case 2:
			label = "Ad hoc";
			break;
		case 3:
			label = "WDS";
			break;
		case 4:
			label = "Wi-Fi Direct";
			break;
		default:
			label = "사용안함";
			break;
		}
		return label;
	}

	static public String auth(int value) {
		String label = "-";
		switch (value) {
		case 0:
			label = "OPEN";
			break;
		case 1:
			label = "802.1X";
			break;
		case 2:
			label = "PSK";
			break;
		default:
			label = "사용안함";
			break;
		}
		return label;
	}

	static public String channel(int value) {
		String label = "-";
		switch (value) {
		case 1:
			label = "20MHz";
			break;
		case 2:
			label = "40MHz";
			break;
		case 3:
			label = "80MHz";
			break;
		case 4:
			label = "160MHz";
			break;
		case 5:
			label = "80MHz80MHz+";
			break;
		default:
			label = "사용안함";
			break;
		}
		return label;
	}
}
