package common.cq.hmq.controller.attach;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.service.AttachService;

import core.cq.hmq.util.tools.Servlet;

@SuppressWarnings("serial")
public class DownLoadServlet extends Servlet {

	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ApplicationContext ac = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
		final AttachService attachService = ac.getBean(AttachService.class);
		Attach attach = attachService.getAttachWithFile(parseId(request
				.getRequestURI()));
		long time = new Date().getTime();
		if (attach.getDate() != null) {
			time = attach.getDate().getTime();
		}
		InputStream fis = attach.getFile();
		if (fis == null)
			return;
		response.setDateHeader("Last-Modified", time);
		response.setDateHeader("Expires", time + 2400 * 60 * 60 * 1000);
		response.setHeader("Content-Disposition", "attachment");
		response.setHeader("Cache-Control", "public");
		response.setHeader("Pragma", "Pragma");
		// response.setContentType(attach.getContentType());
		response.setCharacterEncoding("utf-8");
		ServletOutputStream sos = response.getOutputStream();
		response.setContentLength(attach.getSize().intValue());
		int i = 0;
		byte[] bt = new byte[8192];
		while ((i = fis.read(bt)) != -1) {
			sos.write(bt, 0, i);
		}
		fis.close();
		sos.flush();
		sos.close();
	}

	private Long parseId(String url) {
		return Long.valueOf(url.substring(url.lastIndexOf('/') + 1,
				url.lastIndexOf('.')));
	}

}
