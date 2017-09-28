package anyclick.wips.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import anyclick.wips.controller.view.DownloadView;
import anyclick.wips.error.AuthException;

@RequestMapping(value = "api")
@Controller
public class FileController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final String DOWNLOAD_PATH = "/WEB-INF/file";
	private final String UPLOAD_PATH = "/WEB-INF/file";

	private WebApplicationContext context = null;

	@GetMapping("download/{$file_name}")
	public ModelAndView downloadFile(@PathVariable String $file_name, HttpServletResponse response) {
		context = ContextLoader.getCurrentWebApplicationContext();
		String dir = context.getServletContext().getRealPath(DOWNLOAD_PATH);
		File file = new File(dir, $file_name);
		if (file.exists() == false) {
			throw new AuthException("파일이 존재하지않습니다.");
		}
		return new ModelAndView(new DownloadView(), "file", file);
	}

	@PostMapping("upload")
	public void uploadFile(@RequestParam("file") MultipartFile $file) {
		try {
			String file_name = $file.getOriginalFilename();
			String file_path = UPLOAD_PATH + File.separator + file_name;
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(file_path)));
			stream.write($file.getBytes());
			stream.close();
		} catch (Exception e) {
			throw new AuthException(e.getMessage());
		}
	}
}
