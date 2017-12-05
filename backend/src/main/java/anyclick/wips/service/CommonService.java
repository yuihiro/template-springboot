package anyclick.wips.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import anyclick.wips.util.DateUtil;

@Service
public class CommonService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public String execCommand(String command) {
		StringBuffer output = new StringBuffer();
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(command);
			//			p.getErrorStream().close(); 
			//			p.getInputStream().close(); 
			//			p.getOutputStream().close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
			reader.close();
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (p != null) {
			p.destroy();
		}
		return output.toString();
	}

	public List<Map> getFileList(String path, String ex, Boolean isChilden) {
		String path_str = path;
		File dir = new File(path_str);
		File[] children = dir.listFiles();
		log.debug(dir.getAbsolutePath());
		List dirList = new ArrayList();
		if (children == null) {
		} else {
			for (int i = 0; i < children.length; i++) {
				Map<String, Object> item = new HashMap<String, Object>();
				if (children[i].isFile() == true) {
					if (ex == null || ex.equals(FilenameUtils.getExtension(children[i].getName()))) {
						item.put("file_name", children[i].getName());
						item.put("reg_date", DateUtil.timeToStr(children[i].lastModified()));
						item.put("path", dir.getAbsoluteFile().getName());
						dirList.add(item);
					}
				} else {
					if (isChilden) {
						dirList.addAll(getFileList(children[i].getAbsolutePath(), ex, false));
					}
				}
			}
		}
		return dirList;
	}
}
