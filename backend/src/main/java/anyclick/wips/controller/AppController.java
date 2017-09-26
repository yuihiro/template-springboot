package anyclick.wips.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ServletContextAware;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import anyclick.wips.config.AppProperties;

@RequestMapping("dev")
@Controller
public class AppController implements ServletContextAware {

	@Autowired
	private Environment env;

	private ServletContext servletContext;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void setServletContext(ServletContext servletCtx) {
		this.servletContext = servletCtx;
	}

	@GetMapping("info")
	@ResponseBody
	public Map info() throws IOException {
		Map result = new HashMap<>();
		result.put("name", AppProperties.name);
		result.put("version", AppProperties.version);
		result.put("buildTime", AppProperties.buildTime);
		result.put("debug", AppProperties.debug);
		return result;
	}

	@GetMapping("test")
	@ResponseBody
	public long test() throws Exception {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MMM-dd");

		Map a = new HashMap();
		a.put("label", "aa");
		a.put("date", "2017-03-24");
		List<Map> source = new ArrayList();
		source.add(a);
		a = new HashMap();
		a.put("label", "aa");
		a.put("date", "2017-04-24");
		source.add(a);
		a = new HashMap();
		a.put("label", "bb");
		a.put("date", "2017-02-24");
		source.add(a);

		long result = 0;
		source.stream().mapToLong(it -> {
			try {
				return f.parse(it.get("date").toString()).getTime();
			} catch (Exception e) {
				return 0;
			}
		}).max().getAsLong();
		return result;

		//return DateUtil.getDateFromTwoDate("17-03-24", "17-04-24");
	}

	@GetMapping("hi")
	@ResponseBody
	public String printHi() {
		return "hi";
	}

	@GetMapping("jsp")
	public String viewJsp(Model model) {
		model.addAttribute("message", "하이");
		return "test";
	}

	@GetMapping("http")
	public ResponseEntity returnHttpStatus() {
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@GetMapping("exception")
	public String returnException() {
		throw new IllegalArgumentException("custom exception");
	}

	@GetMapping("csv")
	public void downloadCSV(HttpServletResponse response) throws IOException {
		String file_name = "books.csv";
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file_name));

		List<String[]> data = new ArrayList<String[]>();
		data.add(new String[] { "1", "wild", "010-123-1234" });
		data.add(new String[] { "2", "chris", "011-234-1231" });
		data.add(new String[] { "3", "jack", "010-123-2341" });

		ICsvListWriter csvWriter = new CsvListWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] header = { "Title", "Description", "Author" };
		csvWriter.writeHeader(header);
		for (String[] item : data) {
			csvWriter.write(item);
		}
		csvWriter.flush();
		csvWriter.close();
	}

	@Autowired
	ConfigurableWebApplicationContext subContext;

	/**
	 * 현재 profile 보기
	 * 
	 * @param model
	 */
	@GetMapping("profile")
	public String profile(Model model) {
		return currentProfile();
	}

	/**
	 * 운영/개발 profile 토글
	 * 
	 * @return
	 */
	@GetMapping("toogleProfile")
	public String changeProfile() {
		String toChangeProfile = currentProfile().equals("product") ? "development" : "product";

		ConfigurableWebApplicationContext rootContext = (ConfigurableWebApplicationContext) subContext.getParent();

		// root, sub 싹다 엑티브 프로파일을 바꾼후 리프레쉬 해야 적용됨

		// Refreshing Root WebApplicationContext
		rootContext.getEnvironment().setActiveProfiles(toChangeProfile);
		rootContext.refresh();

		// Refreshing Spring-servlet WebApplicationContext
		subContext.getEnvironment().setActiveProfiles(toChangeProfile);
		subContext.refresh();

		return "redirect:/profile";
	}

	/**
	 * 현재 프로파일 가져오기
	 * 
	 * @return
	 */
	private String currentProfile() {
		String[] profiles = subContext.getEnvironment().getActiveProfiles();

		if (profiles.length == 0) {
			profiles = subContext.getEnvironment().getDefaultProfiles();
		}

		return profiles[0];
	}

}
