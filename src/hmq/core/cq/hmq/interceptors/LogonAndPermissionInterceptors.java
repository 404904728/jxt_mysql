package core.cq.hmq.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import common.cq.hmq.service.UserService;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.dao.util.BeanUtil;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.util.tools.ResourceUtil;

/**
 * 验证登录与否的拦截器
 * 
 * @author 何建
 * 
 */
public class LogonAndPermissionInterceptors extends HandlerInterceptorAdapter {

	/**
	 * 最后执行 用于资源释放
	 */
	@Override
	public void afterCompletion(HttpServletRequest req,
			HttpServletResponse res, Object obj, Exception ex) throws Exception {
		HandlerMethod hMethod = (HandlerMethod) obj;
		ControllerAnn conAnn = hMethod.getMethodAnnotation(ControllerAnn.class);
		if (conAnn != null) {
			if (conAnn.refPermission()) {// 如果有刷新权限
				UserService userService = (UserService) BeanUtil
						.getBean("userService");
				SessionModal sessionModal = (SessionModal) req.getSession()
						.getAttribute(ResourceUtil.getSessionInfoName());
				userService.savePerByUserId(req.getSession(),
						sessionModal.getId());
			}
		}

	}

	/**
	 * 生成视图之前,可以修改 modelAndView
	 */
	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj, ModelAndView modelAndView) throws Exception {
	}

	/** 方法执行之前 */
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj) throws Exception {
		SessionModal sm = (SessionModal) req.getSession().getAttribute(
				ResourceUtil.getSessionInfoName());
		HandlerMethod hMethod = (HandlerMethod) obj;
		debug(hMethod);
		ControllerAnn conAnn = hMethod.getMethodAnnotation(ControllerAnn.class);
		if (conAnn != null) {// 没写该注解的情况下，是必须先登录
			if (conAnn.toLogon()) {// 判断进入该方法是否需要登录
				if (sm == null || sm.getSessionId() == null) {// 如果session里没用户信息则跳转到登录页面
					res.sendRedirect(logonJsp());
					return false;
				}
				return super.preHandle(req, res, obj);
			}
			return super.preHandle(req, res, obj);
		} else {
			if (sm == null || sm.getSessionId() == null) {// 如果session里没用户信息则跳转到登录页面
				res.sendRedirect(logonJsp());
				return false;
			}
		}
		return super.preHandle(req, res, obj);
	}

	public String logonJsp() {
		return java.util.ResourceBundle.getBundle("config").getString(
				"noSession");
	}

	private void debug(HandlerMethod hMethod) {
		if (java.util.ResourceBundle.getBundle("config")
				.getString("debug_method").equals("true")) {
			String[] method = hMethod.getMethod().toGenericString().split(" ");
			if (method.length > 3) {
				System.out.println("调试  进入方法:" + method[3]);
			} else {
				System.out.println("调试  进入方法:" + method[2]);
			}

		}
	}
}
