package anyclick.wips.controller.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

@Component
public class DownloadView extends AbstractView {

	public DownloadView() {
		setContentType("applicaiton/download;charset=utf-8");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			this.setResponseContentType(request, response);
			File downloadFile = (File) model.get("file");
			this.setDownloadFileName(downloadFile.getName(), request, response);
			response.setContentLength((int) downloadFile.length());
			this.downloadFile(downloadFile, request, response);
		} catch (Exception e) {
			throw e;
		}
	}

	private void setDownloadFileName(String fileName, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String userAgent = request.getHeader("User-Agent");

		boolean isIe = userAgent.indexOf("MSIE") != -1;

		if (isIe) {
			fileName = URLEncoder.encode(fileName, "utf-8");
		} else {
			fileName = new String(fileName.getBytes("utf-8"));
		}

		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
	}

	private void downloadFile(File downloadFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(downloadFile);

		try {
			FileCopyUtils.copy(in, out);
			out.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException ioe) {
			}
			try {
				if (out != null)
					out.close();
			} catch (IOException ioe) {
			}
		}
	}
}
