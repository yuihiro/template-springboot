package anyclick.wips.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandUtil {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static String execUnixCommand(int subType, String cmd) throws Exception {
		String packetData = null;
		TcpUtil tcpClient = new TcpUtil();

		try {
			tcpClient.connect("127.0.0.1", "8991");
			tcpClient.setHeader(tcpClient.HEADER_LENGTH + cmd.getBytes().length, 14, subType, 1);
			///패킷을 보내고, 응답을 받는다.
			packetData = tcpClient.getPacketDataWithFailLog(cmd);
			if (packetData.lastIndexOf("\n") != -1) {
				packetData = packetData.substring(0, packetData.length() - 1); //결과값에 개행문자가 포함되어 있으면 없앰.
			}
		} catch (Exception e) {
		} finally {
			try {
				if (tcpClient != null)
					tcpClient.close();
			} catch (Exception e) {
			}
		}
		return packetData;
	}

	public static int execUnixCommand(String cmd) throws Exception {
		try {
			int i = 0;

			try {
				execUnixCommand(1, cmd);
			} catch (Exception e) {
				e.printStackTrace();
				i = -1;
			}

			if (i != 0) {
				throw new Exception("execUnixCommand failed :" + cmd);
			}
			return i;
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getRunningTime() throws Exception {
		try {
			if (!isRunning()) {
				return "0";
			}
			String xauthHome = getHome();
			String startup = xauthHome.concat("/var/run/radiusd/startup.txt");
			FileInputStream fis = new FileInputStream(startup);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String tm = br.readLine();

			br.close();
			fis.close();
			// System.out.println ("Startup time is : " + tm);
			Long startTime = new Long(tm);
			Date date = new Date(startTime.longValue() * 1000);
			SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			return df.format(date);
			/* calculate time difference */
		} catch (FileNotFoundException e) {
			return "";
		} catch (Exception e) {
			throw e;
		}
	}

	public static boolean isRunning() throws Exception {
		try {
			String pid = getPid();
			if (pid == null)
				return false;

			String cmd = "kill -0 " + pid;
			int ret = execUnixCommand(cmd);

			if (ret == 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			/* kill command fails if server is not running */
			return false;
		}
	}

	public static String getHome() throws Exception {
		// return System.getProperty("XAUTH_HOME");
		return System.getProperty("unet.anyclick.ia.home.dir");
	}

	public static String getPid() throws Exception {
		try {
			String ret = null;
			String xauthHome = "";
			FileInputStream fis = new FileInputStream(xauthHome + "/var/run/radiusd/radiusd.pid");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			ret = br.readLine();
			if (ret == null) {
				return null;
			}
			br.close();
			fis.close();
			return ret;
		} catch (FileNotFoundException e) {
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	public static float getCPUInfo() throws Exception {
		FileInputStream fis = null;
		BufferedReader br = null;
		float cpuUsage = 0;
		try {
			long[][] cpuinfo = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
			long[] total = { 0, 0 };
			long diffTotal = 0;
			for (int i = 0; i < 2; i++) {

				fis = new FileInputStream("/proc/stat");
				br = new BufferedReader(new InputStreamReader(fis));

				String line;
				while ((line = br.readLine()) != null) {
					if ((line.substring(0, 4)).equals("cpu ")) {
						break;
					}
				}
				br.close();
				fis.close();

				String[] token = line.split("\\s");
				int y = 0;
				for (int x = 1; y < 4; x++) {
					if (token[x] != null && token[x].length() > 0) {
						cpuinfo[i][y] = Long.parseLong(token[x]);
						y++;
					}
				}

				if (cpuinfo[i].length < 4)
					return -1;

				for (int x = 0; x < cpuinfo[i].length; x++)
					total[i] += cpuinfo[i][x];

				Thread.sleep(1000);

			}

			diffTotal = total[1] - total[0];
			cpuUsage = 100 - (((cpuinfo[1][3] - cpuinfo[0][3]) * 100) / diffTotal);
			System.out.println("CPU = " + cpuUsage);
			return cpuUsage;
		}

		catch (Exception e) {
			throw e;
		} finally {
			br.close();
			fis.close();
		}
	}

	public String getHostname() throws Exception {
		String hostname = null;
		try {
			hostname = System.getProperty("xaim.svcsvr.svcsvrmgmt.control2.AuthServer.hostname");
			if (hostname == null) {
				hostname = execUnixCommand(1, "hostname");
				System.setProperty("xaim.svcsvr.svcsvrmgmt.control2.AuthServer.hostname", hostname);
			}
			return hostname;
		} catch (Exception e) {
		}
		return hostname;
	}

	public String getServerRpmVer() throws Exception {
		String rpmversion = null;
		try {
			rpmversion = System.getProperty("xaim.svcsvr.svcsvrmgmt.control2.AuthServer.rpmver");
			if (rpmversion == null) {
				rpmversion = execUnixCommand(1, "rpm -q anyclick");
				System.setProperty("xaim.svcsvr.svcsvrmgmt.control2.AuthServer.rpmver", rpmversion);
			}
		} catch (Exception e) {
		}
		return rpmversion;
	}
}
