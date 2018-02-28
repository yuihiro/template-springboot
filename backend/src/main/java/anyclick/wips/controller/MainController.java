package anyclick.wips.controller;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import anyclick.wips.config.annotation.AuthCheck;
import anyclick.wips.service.MainService;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@RequestMapping("api")
@RestController
public class MainController {

	@Autowired
	MainService main_service;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping("login")
	@AuthCheck()
	public Map login(@RequestBody Map<String, Object> $param, HttpServletRequest $req) {
		main_service.removeInvalideAdmin();
		String id = $param.get("id").toString();
		String pwd = $param.get("pwd").toString();
		return main_service.login(id, pwd, $req);
	}

	@PostMapping("logout")
	@AuthCheck()
	public void logout(@RequestBody Map<String, Object> $param, HttpServletRequest req) {
		String id = $param.get("id").toString();
		main_service.logout(id);
	}

	@GetMapping("report")
	@AuthCheck()
	public void report(HttpServletRequest req) {
		JasperPrint print = null;
		JasperReport report = null;

		log.info("report");
		String path = this.getClass().getClassLoader().getResource("").getPath().split("/WEB-INF/classes/")[0];
		log.info(path);

		//		String export_dir = req.getSession().getServletContext().getRealPath("report");
		//		String jasper_file = req.getSession().getServletContext().getRealPath("report/RiskReport.jasper");
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("test", "go to home");
		//log.info(jasper_file);
		try {
			report = (JasperReport) JRLoader.loadObject(new File(path + "/report/report.jasper"));
		} catch (JRException e1) {
			e1.printStackTrace();
		}
		log.debug("-FILL-");
		try {
			print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
		} catch (JRException e) {
			e.printStackTrace();
		}

		try {
			JasperExportManager.exportReportToPdfFile(print, path + "/test.pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
