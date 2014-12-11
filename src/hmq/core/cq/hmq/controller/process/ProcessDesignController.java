package core.cq.hmq.controller.process;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;


@Controller
@RequestMapping("/processDesign")
public class ProcessDesignController extends BaseController {
	
	/**
	 * 页面跳转
	 * @return
	 */
	@RequestMapping(params = "page_")
	@ControllerAnn(toLogon=false)
	public String page(){
		return "core/workflow/workFlowDesign";
	}
	

	/**
	 * 页面跳转
	 * @return
	 */
	@RequestMapping(params = "pageeasy_")
	@ControllerAnn(toLogon=false)
	public String pageeasy(){
		return "res/flow/workflow/index";
	}
	

}
