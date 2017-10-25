package anyclick.wips.util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.Maps;

public class FileUtil {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	static private ApplicationContext context;

	static public Map getConfigFile(String file) {
		return getConfigFile(file, "=");
	}

	//	static public Map findFile(String file) {
	//		aa.getResourceAsStream("other/some.properties");    
	//		
	//		InputStream inputStream = classPathResource.getInputStream();
	//		File somethingFile = File.createTempFile("", file);
	//		try {
	//			FileU
	//			FileUtils.copyInputStreamToFile(inputStream, somethingFile);
	//		} finally {
	//			IOUtils.closeQuietly(inputStream);
	//		}
	//	}

	static public Map getConfigFile(String file, String delimter) {
		if (delimter == "=") {
			Properties props = new Properties();
			try {
				props.load(new FileInputStream(new File(file)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return propToMap(props);
		} else {
			return getFile(file);
		}
	}

	static private Map getFile(String file) {
		Map result = Maps.newHashMap();
		Path path = Paths.get("", file);
		try {
			List<String> lines = Files.readAllLines(path);
			for (String line : lines) {
				line = line.trim();
				if (line.length() == 0 || line.charAt(0) == '#' || line.charAt(0) == '$') {
					continue;
				}
				String[] token = line.split("\\s+");
				result.put(token[0], token[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	static public Map propToMap(Properties data) {
		Stream<Entry<Object, Object>> stream = data.entrySet().stream();
		return stream.collect(Collectors.toMap(e -> String.valueOf(e.getKey()), e -> String.valueOf(e.getValue())));
	}

}
