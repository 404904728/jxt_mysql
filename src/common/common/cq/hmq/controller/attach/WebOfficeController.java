package common.cq.hmq.controller.attach;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.service.AttachService;

import core.cq.hmq.modal.AjaxMsg;


@Controller
@RequestMapping("/weboffice")
public class WebOfficeController {

	@Resource
	private AttachService attachService;

	/**
	 * 附件上传
	 * 
	 * weboffice上传或保存附件  还有乱码问题
	 * 
	 * @return
	 */
	@RequestMapping(value = "webofficeSave", produces = "application/json; charset=gbk")
	@ResponseBody
	public String webofficeSave(Long id, HttpServletRequest request) {
		AjaxMsg am = new AjaxMsg();
		if (id == null) {
			am = attachService.upload(request);
			return am.getMsg();
		} else {
			Attach attach = attachService.findById(id);
			try {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Map<String, MultipartFile> fileMap = multipartRequest
						.getFileMap();
				for (Map.Entry<String, MultipartFile> entity : fileMap
						.entrySet()) {
					MultipartFile mult = entity.getValue();
					FileCopyUtils.copy(mult.getBytes(), attach.getFilePath());
				}
				am.setMsg("保存成功");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				am.setMsg("保存失败");
			}
		}
		return am.getMsg();
	}

	/**
	 * webOffice 输出流(同getdoc.jsp)google还有点问题,进不了该方法
	 */
	@RequestMapping(value = "webofficeIo")
	public void webofficeIo(Long id, HttpServletResponse response) {
		Attach attach = attachService.getAttachWithFile(id);
		if (attach != null) {
			OutputStream ops;
			try {
				ops = response.getOutputStream();
				int i = 0;
				byte[] bt = new byte[8192];
				InputStream fis = attach.getFile();
				while ((i = fis.read(bt)) != -1) {
					ops.write(bt, 0, i);
				}
				fis.close();
				ops.flush();
				ops.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
