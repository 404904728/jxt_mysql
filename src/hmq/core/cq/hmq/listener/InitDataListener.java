package core.cq.hmq.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import common.cq.hmq.service.UserService;
import core.cq.hmq.dao.util.BeanUtil;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.service.InitDataService;
import core.cq.hmq.util.tools.LogUtil;
import core.cq.hmq.util.xml.XmlReadUtil;

/**
 * 数据初始化
 * 
 * @author hejian
 * 
 */
public class InitDataListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		// 不用处理容器结束方法
	}

	/**
	 * 容器启动时,用于启动数据
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		// UserService userService = (UserService)
		// BeanUtil.getBean("userService");
		// if (userService.isNull()) {
		// LogUtil.getLog("提示").info("数据初始化开始...");
		// initDataXmlSql(event);
		// LogUtil.getLog("提示").info("基础数据加载完成...");
		// } else {
		// LogUtil.getLog("提示").info("基础数据已经加载...");
		// }
		// updateDataXmlSql(event);
		InitDataService initDataService = (InitDataService) BeanUtil
				.getBean("initDataService");
		initDataService.createSequence();
		String path = event.getServletContext().getRealPath("/");
	}

	private void initDataXmlSql(ServletContextEvent event) {
		BaseService baseService = (BaseService) BeanUtil.getBean("baseService");
		LogUtil.getLog("提示").info("数据初始化开始XML...");
		String initString = java.util.ResourceBundle.getBundle("config")
				.getString("initdataXML");
		String[] initXML = initString.split(",");
		for (int i = 0; i < initXML.length; i++) {
			for (String sql : XmlReadUtil.read("initdata-" + initXML[i]
					+ ".xml")) {
				baseService.initData(sql);
			}
		}
	}

	private void updateDataXmlSql(ServletContextEvent event) {
		BaseService baseService = (BaseService) BeanUtil.getBean("baseService");
		LogUtil.getLog("提示").info(
				"数据-更新开始XML..."
						+ XmlReadUtil.read("initdata_update.xml").size());
		for (String sql : XmlReadUtil.read("initdata_update.xml")) {
			baseService.initData(sql);
		}
	}
}
