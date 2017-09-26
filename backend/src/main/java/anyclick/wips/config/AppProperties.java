package anyclick.wips.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config.properties")
@ConfigurationProperties
public class AppProperties {

	public static String name;
	public static String version;
	public static boolean debug;
	public static String buildTime;

	public static final String INIT_ID = "anyclick";
	public static final String INIT_PWD = "adminme00!";
	public static final String CRYOTO_KEY = "ANYANGEL";

	public static final int SESSION_TIMEOUT = 10 * 60;

	public void setName(String name) {
		this.name = name;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setBuildTime(String time) {
		this.buildTime = time;
	}
}
