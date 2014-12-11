package core.cq.hmq.controller.core;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.service.util.DateEditor;
import core.cq.hmq.util.tools.ResourceUtil;

@Controller
@RequestMapping("/dateBaseController")
public class DateBaseController {
	
	@InitBinder
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new DateEditor());
	}

	/**
	 * 页面返回
	 * 
	 * @Deprecated
	 * @return
	 */
	protected String view(String pagePath) {
		return pagePath;
	}

	protected ModelAndView modelAndView(String pagePath) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(pagePath);
		return modelAndView;
	}

	/**
	 * 获取当前用户
	 * 
	 * @param session
	 * @return
	 */
	protected SessionModal currentSessionModel() {
		HttpSession session1 = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();
		SessionModal sModal = (SessionModal) session1.getAttribute(ResourceUtil
				.getSessionInfoName());
		return sModal;
	}

}
