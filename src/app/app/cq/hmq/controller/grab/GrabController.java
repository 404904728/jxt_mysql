package app.cq.hmq.controller.grab;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.pojo.grab.Grab;
import app.cq.hmq.service.grab.GrabService;
import core.cq.hmq.annotation.ControllerAnn;

/**
 * 网页抓取
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/grab")
public class GrabController {

	@ControllerAnn(toLogon = false)
	@RequestMapping(value = "/find")
	@ResponseBody
	public List<Grab> grab(Integer type) throws HttpException, IOException {
		if (type == null) {
			return null;
		}
		return grabService.findList(type);
	}

	@Autowired
	private GrabService grabService;

	@ControllerAnn(toLogon = false)
	@RequestMapping(params = "showpage")
	public ModelAndView showpage(Long id) throws HttpException, IOException {
		ModelAndView mav = new ModelAndView("app/grab/grab");
		mav.addObject("map", grabService.find(id));
		return mav;
	}

}
