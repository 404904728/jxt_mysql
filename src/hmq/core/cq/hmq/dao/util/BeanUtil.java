package core.cq.hmq.dao.util;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BeanUtil{

	public static Object getBeanByServletContext(ServletContext sc,
			String beanId) {
		ApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		return ctx.getBean(beanId);
	}

	public static Object getBeanByXml(String beanId) {
		ApplicationContext ac = new FileSystemXmlApplicationContext(
				"spring-.xml");
		return ac.getBean(beanId);
	}

	public static Object getBeanByXmlTest(String beanId) {
		ApplicationContext ac = new FileSystemXmlApplicationContext(
				"/web/WEB-INF/classes/test-spring.xml");
		return ac.getBean(beanId);
	}

	/**
	 * 默认容器下获取bean
	 * 
	 * @param beanId
	 * @return
	 */
	public static Object getBean(String beanId) {
		WebApplicationContext wac = ContextLoader
				.getCurrentWebApplicationContext();
		return wac.getBean(beanId);
	}
}
