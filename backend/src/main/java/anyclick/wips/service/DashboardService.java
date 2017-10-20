package anyclick.wips.service;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import anyclick.wips.repository.DashboardRepository;
import anyclick.wips.repository.ServerRepository;
import snmp.SNMPObject;
import snmp.SNMPSequence;
import snmp.SNMPVarBindList;
import snmp.SNMPv1CommunicationInterface;

@Service
public class DashboardService {

	@Autowired
	DashboardRepository repo;

	@Autowired
	ServerRepository server_repo;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public Map getDashboardData(Map<String, Object> $param) {
		Map result = Maps.newHashMap();
		//		long start_time = Long.parseLong($param.get("start_time").toString());
		//		long last_event_id = Long.parseLong($param.get("last_event_id").toString());

		Map<String, Object> server_info = new HashMap<String, Object>();
		server_info.put("cpu", new Random().nextInt(100));
		server_info.put("memory", new Random().nextInt(100));
		server_info.put("disk", new Random().nextInt(100));
		//		result.put("server_info", server_info);
		result.put("server_info", getServerInfo("localhost", "airsnmpinfo"));
		result.put("stats_data", repo.getStatsCnt($param));
		result.put("event_data", repo.getEventCnt($param));
		result.put("server_data", server_repo.getServerList(Maps.newHashMap()));
		//		result.put("last_event_id", last_event_id);
		result.put("update_time", System.currentTimeMillis());
		log.info("result");
		return result;
	}

	public Map<String, Object> getServerInfo(String $host, String $community) {
		int cpuUsePercent = 0;
		int memUsePercent = 0;
		int diskUsePercent = 0;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cpu", cpuUsePercent);
		result.put("memory", memUsePercent);
		result.put("disk", diskUsePercent);
		SNMPv1CommunicationInterface comInterface1 = null;
		log.info($host + " / " + $community);
		try {
			InetAddress hostAddress = InetAddress.getByName($host);
			int version = 1; // SNMPv1	(ver 0=1, 1=2v, 2=3)
			comInterface1 = new SNMPv1CommunicationInterface(version, hostAddress, $community);
			if (comInterface1 != null) {
				log.info("2");
				comInterface1.setSocketTimeout(1000);

				int ssCpuUser = getSNMPInt(comInterface1, "1.3.6.1.4.1.2021.11.9.0");
				int ssCpuSystem = getSNMPInt(comInterface1, "1.3.6.1.4.1.2021.11.10.0");
				cpuUsePercent = ssCpuUser + ssCpuSystem;

				int dskPercent = getSNMPInt(comInterface1, "1.3.6.1.4.1.2021.9.1.9.1");
				diskUsePercent = dskPercent;

				double memTotalReal = getSNMPDouble(comInterface1, "1.3.6.1.4.1.2021.4.5.0");
				double memAvailReal = getSNMPDouble(comInterface1, "1.3.6.1.4.1.2021.4.6.0");
				double memBuffer = getSNMPDouble(comInterface1, "1.3.6.1.4.1.2021.4.14.0");
				double memCached = getSNMPDouble(comInterface1, "1.3.6.1.4.1.2021.4.15.0");
				memUsePercent = 100 - (int) (((memAvailReal + memBuffer + memCached) / memTotalReal) * 100);
				comInterface1.closeConnection();

				log.info("3");
				log.info(ssCpuUser + "");
				log.info(ssCpuSystem + "");
				result.put("cpu", cpuUsePercent);
				result.put("memory", memUsePercent);
				result.put("disk", diskUsePercent);
			}
		} catch (Exception e) {
			log.info("20");
			e.printStackTrace();
		} finally {
			try {
				if (comInterface1 != null)
					comInterface1.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private double getSNMPDouble(SNMPv1CommunicationInterface comInterface, String itemID) {
		try {
			SNMPVarBindList newVars = comInterface.getMIBEntry(itemID);
			SNMPSequence pair = (SNMPSequence) (newVars.getSNMPObjectAt(0));
			SNMPObject snmpObj = pair.getSNMPObjectAt(1);
			if (snmpObj.toString().equals(""))
				return 0;
			return Double.parseDouble(snmpObj.getValue().toString());
		} catch (Exception e) {
			return 0;
		}
	}

	private int getSNMPInt(SNMPv1CommunicationInterface comInterface, String itemID) {
		try {
			SNMPVarBindList newVars = comInterface.getMIBEntry(itemID);
			SNMPSequence pair = (SNMPSequence) (newVars.getSNMPObjectAt(0));
			SNMPObject snmpObj = pair.getSNMPObjectAt(1);
			if (snmpObj.toString().equals(""))
				return 0;
			return Integer.parseInt(snmpObj.getValue().toString());
		} catch (Exception e) {
			return 0;
		}
	}
}
