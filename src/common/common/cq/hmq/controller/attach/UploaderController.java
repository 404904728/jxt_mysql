package common.cq.hmq.controller.attach;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import common.cq.hmq.service.AttachService;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;


@Controller
@RequestMapping("/uploader")
public class UploaderController extends BaseController {

	/**
	 * File Uploader 上传插件跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "show_")
	@ControllerAnn(toLogon = false)
	public String show() {
		return "res/script_/fileuploader/fileupload";
	}

	/**
	 * 上传附件出错跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "error")
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public String error() {
		StringBuffer sb = new StringBuffer();
		sb.append("<div id='status'>error</div>");
		sb.append("<div id='message'>您上传的文件太大，上传失败</div>");// 存入返回信息
		return sb.toString();
	}

	@Resource
	private AttachService attachService;

	/**
	 * 附件上传
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "up")
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public String upload(HttpServletRequest request) {
		AjaxMsg am = attachService.upload(request);
		StringBuffer sb = new StringBuffer();
		sb.append("<div id='status'>"
				+ ((am.getType() == am.SUCCESS) ? "success" : "error")
				+ "</div>");
		sb.append("<div id='message'>" + am.getMsg() + "</div>");// 存入返回信息
		sb.append("<div id='attachId'>" + am.getId() + "</div>");// 存入附件ID
		return sb.toString();
	}


}
