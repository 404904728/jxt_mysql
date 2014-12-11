package common.cq.hmq.controller.attach;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import common.cq.hmq.pojo.sys.Attach;
import common.cq.hmq.service.AttachService;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.EasyData;
import core.cq.hmq.modal.PageModel;


@Controller
@RequestMapping("/attach")
public class AttachController extends BaseController {

	/**
	 * 跳转附件页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "attachPage")
	public String showPage() {
		return view("core/attach/attach");
	}

	/**
	 * 跳转附件上传页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "attachUpLoad")
	public String attachUpLoad() {
		return view("core/attach/attachUpload");
	}

	@Resource
	private AttachService attachService;

	/**
	 * 获取附件信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "attachData")
	@ResponseBody
	public EasyData<Attach> getData(PageModel pageModel) {
		EasyData<Attach> ed = attachService.findAttach(pageModel);
		return ed;
	}

	@RequestMapping(params = "officeView_")
	public ModelAndView officeView(Long id, String docType) {
		ModelAndView modelAndView = modelAndView("core/attach/officeView");
		modelAndView.addObject("id", id);
		modelAndView.addObject("docType", docType);
		return modelAndView;
	}

	/**
	 * 删除附件
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public AjaxMsg delete(Long id) {
		return attachService.delete(id);
	}

}
