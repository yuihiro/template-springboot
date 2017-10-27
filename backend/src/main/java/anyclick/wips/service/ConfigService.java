package anyclick.wips.service;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import anyclick.wips.repository.ConfigRepository;

@Service
@Transactional
public class ConfigService {

	@Autowired
	ConfigRepository repo;

	@Autowired
	CommonService common_service;

	public String ntp_file = "/opt/anyair/util/ntp.sh";
	public String env_file = "/opt/anyair/.env";
	public String version_command = "rpm -qa Anyclick-AIR";

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public Map getConfig() {
		Map result = repo.getConfig();
		//result.put("rad_info", FileUtil.getConfigFile("src/main/resources/radiusd.conf"));
		result.put("server_mac", getServerMac());
		result.put("server_ip", getServerIp());
		result.put("ntp_ip", getNtpIp());
		result.put("rpm_name", common_service.execCommand("rpm -qa Anyclick-AIR"));
		//result.put("rpm_name", common_service.executeCommand("rpm -qa Anyclick-AIR"));
		return result;
	}

	public String getServerMac() {
		String mac = "";
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(env_file));
			mac = props.getProperty("HWADDR");
		} catch (Exception e) {
		}
		return mac;
	}

	public String getServerIp() {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
		}
		return ip;
	}

	public String getNtpIp() {
		String ip = "";
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(ntp_file));
			ip = props.getProperty("NTP_SERVER");
		} catch (Exception e) {
		}
		return ip;
	}

	public int updateConfig(Map<String, Object> $param) {
		return repo.updateConfig($param);
	}
}
