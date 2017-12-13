package anyclick.wips.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import anyclick.wips.repository.AdminRepository;
import anyclick.wips.repository.CommonRepository;
import anyclick.wips.repository.ConfigRepository;

@Service
@Transactional
public class ConfigService {

	@Autowired
	ConfigRepository repo;

	@Autowired
	CommonRepository common_repo;

	@Autowired
	AdminRepository admin_repo;

	@Autowired
	CommonService common_service;

	public String ntp_file = "/opt/anyair/util/ntp.sh";
	public String snmp_file = "/etc/snmp/snmpd.conf";
	public String syslog_file = "/etc/rsyslog.conf";
	public String env_file = "/opt/anyair/.env";
	public String version_command = "rpm -qa Anyclick-AIR-Manager";

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public Map getAppData() {
		Map result = Maps.newHashMap();
		result.put("server_info", getServerInfo());
		result.put("ha_info", repo.getHaInfo());
		result.put("license_info", repo.getLicenseInfo());
		result.put("admin_lst", admin_repo.getAdminList(Maps.newHashMap(), "TINY"));
		result.put("server_lst", common_repo.getServerList());
		result.put("map_lst", common_repo.getMapList());
		log.debug("SERVER INFO");
		log.debug(result.get("server_info").toString());
		log.debug("HA INFO");
		log.debug(result.get("ha_info").toString());
		log.debug("LICENSE INFO");
		log.debug(result.get("license_info").toString());
		return result;
	}

	public Map getConfig() {
		Map result = Maps.newHashMap();
		result.putAll(repo.getConfig());
		result.putAll(repo.getLogConfig());
		result.putAll(getSnmpInfo());
		result.put("ntp_ip", getNtp());
		return result;
	}

	public Map getServerInfo() {
		Map result = Maps.newHashMap();
		result.put("server_mac", getServerMac());
		result.put("server_ip", getServerIp());
		result.put("rpm_name", common_service.execCommand(version_command));
		return result;
	}

	public String getServerMac() {
		String mac = "";
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(env_file));
			mac = props.getProperty("HWADDR").toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mac;
	}

	public String getServerIp() {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	public String getNtp() {
		String ip = "";
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(ntp_file));
			ip = props.getProperty("NTP_SERVER");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	public Map getSnmpInfo() {
		Map result = Maps.newHashMap();
		result.put("sysname", "");
		result.put("syscontact", "");
		result.put("syslocation", "");
		try {
			Scanner scanner = new Scanner(new BufferedReader(new FileReader(snmp_file)));
			String token;
			while (scanner.hasNextLine()) {
				token = scanner.nextLine();
				if (token.contains("sysname") == true) {
					result.put("sysname", token.split(" ")[1]);
				}
				if (token.contains("syscontact") == true) {
					result.put("syscontact", token.split(" ")[1]);
				}
				if (token.contains("syslocation") == true) {
					result.put("syslocation", token.split(" ")[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int updateConfig(Map<String, Object> $param) {
		int type = Integer.parseInt($param.get("type").toString());
		int result = -1;
		String title = "";
		String comment = "";
		if (type == 1) {
			title = "접근제한";
			comment += "자동로그아웃:" + $param.get("autoLogout_interval").toString();
			comment += ",로그인실패허용횟수:" + $param.get("login_max").toString();
			comment += ",장시간미접속시계정자동잠금:" + $param.get("auto_lock").toString();
			comment += ",계정패스워드변경:" + $param.get("pwd_update").toString();
			repo.updateConfig($param);
		} else if (type == 2) {
			title = "로그설정";
			comment += "이벤트에이징:" + $param.get("event_aging_day").toString();
			comment += ",정책로그에이징:" + $param.get("block_aging_day").toString();
			comment += ",관리자로그에이징:" + $param.get("admin_aging_day").toString();
			comment += ",시스템로그에이징:" + $param.get("system_aging_day").toString();
			comment += ",디스크사용량알림임계치:" + $param.get("file_notice_max").toString();
			comment += ",디스크사용량최대임계치:" + $param.get("file_delete_max").toString();
			repo.updateLogConfig($param);
		} else if (type == 3) {
			title = "외부서버설정-NTP";
			updateNtp($param.get("ntp_ip").toString());
		} else if (type == 4) {
			title = "외부서버설정-SMTP";
			repo.updateConfig($param);
		} else if (type == 5) {
			title = "외부서버설정-SNMP";
			String name = $param.get("sysname").toString();
			String location = $param.get("syslocation").toString();
			String contact = $param.get("syscontact").toString();
			updateSnmp(name, location, contact);
			common_service.execCommand("/etc/init.d/snmpd restart");
		} else if (type == 6) {
			title = "외부서버설정-SYSLOG";
			int use = Integer.parseInt($param.get("syslog_use").toString());
			String server1 = $param.get("syslog_server1").toString();
			String server2 = $param.get("syslog_server2").toString();
			String server3 = $param.get("syslog_server3").toString();
			repo.updateConfig($param);
			log.debug($param.toString());
			updateSyslog(use, server1, server2, server3);
			common_service.execCommand("/etc/init.d/rsyslog restart");
			title = "SNMP";
			//String comment = "sysname:" + $item.get("sysname") + ",syslocation:" + $item.get("syslocation") + ",syscontact:" + $item.get("syscontact");
		}
		common_repo.updateAdminLog(3, "환경설정-" + title + "(" + comment + ")를 수정하였습니다.");
		return result;
	}

	public int updateNtp(String $value) {
		final String ntp_var = "NTP_SERVER=" + $value + "\n";
		final String ntpdate = "/usr/sbin/ntpdate $NTP_SERVER\n";
		final String hwclock = "/sbin/hwclock --systohc\n";
		int result = -1;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(ntp_file));
			bw.write(ntp_var);
			bw.write(ntpdate);
			bw.write(hwclock);
			bw.flush();
			bw.close();
			result = 1;
			common_service.execCommand(ntp_file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int updateSyslog(int $use, String $server1, String $server2, String $server3) {
		ArrayList<String> temp = new ArrayList<String>();
		int idx = 0;
		int result = -1;
		try {
			Scanner fileScan = new Scanner(new BufferedReader(new FileReader(syslog_file)));
			while (fileScan.hasNextLine()) {
				temp.add(fileScan.nextLine());
				idx++;
			}
			PrintWriter out = new PrintWriter(new FileWriter(syslog_file));
			for (int i = 0; i < 78; i++) {
				out.println(temp.get(i));
			}
			out.println("");
			if ($use == 1) {
				if ($server1.length() > 0) {
					out.println("local0.*                    @" + $server1);
				}
				if ($server2.length() > 0) {
					out.println("local0.*                    @" + $server2);
				}
				if ($server3.length() > 0) {
					out.println("local0.*                    @" + $server3);
				}
			}
			fileScan.close();
			out.close();
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int updateSnmp(String $name, String $location, String $contact) {
		ArrayList<String> temp = new ArrayList<String>();
		int idx = 0;
		int result = -1;
		int flag_row = 0;
		try {
			Scanner scanner = new Scanner(new BufferedReader(new FileReader(snmp_file)));
			while (scanner.hasNextLine()) {
				String token = scanner.nextLine();
				temp.add(token);
				if (token.contains("# variables through the snmpd.conf file:") == true) {
					flag_row = idx;
				}
				idx++;
			}
			int cnt = 0;
			int total = temp.size();
			String token;
			PrintWriter out = new PrintWriter(new FileWriter(snmp_file));
			for (int i = 1; i <= total; i++) {
				token = temp.get(i - 1);
				cnt = i - 1;
				if (token.contains("syscontact") == true) {
					out.println("syscontact " + $contact);
				} else if (token.contains("syslocation") == true) {
					out.println("syslocation " + $location);
				} else if (token.contains("sysname") == true) {
					out.println("sysname " + $name);
				} else {
					out.println(temp.get(i - 1));
				}
			}
			scanner.close();
			out.close();
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//	String rpm_name = "";
	//	DefaultExecutor executor = new DefaultExecutor();
	//	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	//	PumpStreamHandler streamHandler = new PumpStreamHandler(baos);
	//	executor.setStreamHandler(streamHandler);
	//	try {
	//		CommandLine cmd = CommandLine.parse(version_command);
	//		executor.execute(cmd);
	//		rpm_name = baos.toString();
	//		log.info("RPM : " + rpm_name);
	//	} catch (ExecuteException e) {
	//		e.printStackTrace();
	//	} catch (IOException e) {
	//		e.printStackTrace();
	//	}
	//	result.put("rpm_name", rpm_name);
}
