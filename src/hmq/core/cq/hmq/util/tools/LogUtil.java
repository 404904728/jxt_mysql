package core.cq.hmq.util.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 程序使用到了log统一通过这个获取
 * 
 * @since fan houjun 2008-11-11
 */
public class LogUtil {

	/**
	 * 程序使用到了log统一通过这个获取
	 */
	public static Log getLog(Class clazz) {
		return LogFactory.getLog(clazz);
	}

	public static Log getLog(String str) {
		return LogFactory.getLog(str);
	}
}
