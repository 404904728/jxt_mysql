package app.cq.hmq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import core.cq.hmq.annotation.ControllerAnn;

@Controller
@RequestMapping()
public class MobileController {

	@RequestMapping(value = "/app_download")
	@ControllerAnn(toLogon = false)
	public String page() {
		return "mobile/mobile";
	}
}
