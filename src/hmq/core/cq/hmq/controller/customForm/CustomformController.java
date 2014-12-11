package core.cq.hmq.controller.customForm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;


/**
 * 自定义表单
 * @author admin
 *
 */
@Controller
@RequestMapping("/customform")
public class CustomformController extends BaseController {

	@RequestMapping(params = "viewPage")
	@ControllerAnn(toLogon=false)
	public String viewPage() {
		return view("core/customform/index");
	}
}
