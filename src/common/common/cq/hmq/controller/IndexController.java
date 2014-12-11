package common.cq.hmq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;


@Controller
@RequestMapping("/index")
public class IndexController extends BaseController{
	
	/**
	 * 跳转登陆页面主页
	 * @return
	 */
	@RequestMapping(value="logon")
	@ControllerAnn(toLogon=false)
	public String index(){
		return view("home/logon");
	}
}
