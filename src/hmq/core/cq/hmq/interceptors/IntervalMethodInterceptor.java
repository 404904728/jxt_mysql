package core.cq.hmq.interceptors;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.dao.util.BeanUtil;
import core.cq.hmq.pojo.IntervalMethod;
import core.cq.hmq.service.util.IntervalService;
import core.cq.hmq.util.tools.DateUtil;

public class IntervalMethodInterceptor implements HandlerInterceptor {

	/**
	 * 在controller后拦截
	 */
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception)
			throws Exception {
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在controller前拦截
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws Exception {
		HandlerMethod hMethod = (HandlerMethod) object;
		ControllerAnn conAnn = hMethod.getMethodAnnotation(ControllerAnn.class);
		if (conAnn != null && conAnn.interval() != 0) {
			String iBean = hMethod.getBean().getClass().getName();
			String iMethod = hMethod.getMethod().getName();
			IntervalService intervalService = (IntervalService) BeanUtil
					.getBean("intervalService");
			IntervalMethod im = intervalService.findBefor(iBean, iMethod);
			if (im == null) {
				IntervalMethod inM = new IntervalMethod();
				inM.setBeanName(iBean);
				inM.setMethodName(iMethod);
				inM.setDate(new Date());
				intervalService.insert(inM);
			} else {
				if ((Math.abs(DateUtil.differenceInSeconds(im.getDate())) - conAnn
						.interval()) > 0) {
					System.out.println("拦截方法-----"+iBean);
					im.setDate(new Date());
					intervalService.update(im);
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

}
