package core.cq.hmq.util.tools;

import javax.servlet.http.HttpSession;

/**
 * 项目参数工具类
 * @author 孙宇
 * 
 */
public class ResourceUtil {
	
	/**
	 * 获得sessionInfo名字
	 * 
	 * @return
	 */
	public static String getSessionInfoName() {
		return java.util.ResourceBundle.getBundle("config").getString(
				"sessionModalName");
	}
	
	/**
	 * 获得sessionInfo名字
	 * 
	 * @return
	 */
	public static String getPermissionCookie() {
		return java.util.ResourceBundle.getBundle("config").getString(
				"permissionCookie");
	}
	/**
	 * 获得附件上传地址
	 * 
	 * @return
	 */
	public static String getUploadPath() {
		return java.util.ResourceBundle.getBundle("config").getString(
				"uploadPath");
	}
	
	/**
	 * 获取项目web目录路径
	 * @return
	 */
	public static String getProjectWebPath(HttpSession session){
		return session.getServletContext().getRealPath("/");
	}
	
	public static void main(String[] args) {
		System.out.println(java.util.ResourceBundle.getBundle("notice").getString(
				"1042"));
	}

}
