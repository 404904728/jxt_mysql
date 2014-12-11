package core.cq.hmq.cache;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.cq.hmq.util.tools.ResourceUtil;


public class CacheCookie {

	/**
	 * 建立一个无生命周期的cookie，即随着浏览器的关闭即消失的cookie
	 */
	public static void createSessionCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("cookiename", "cookievalue");
		response.addCookie(cookie);
	}

	/**
	 * 用户登录就把权限存到cookie,免得每次去数据库取
	 */
	public static void permissionCookie(HttpServletResponse response,
			String permission) {
		Cookie cookie = new Cookie(ResourceUtil.getSessionInfoName(),
				permission);
		response.addCookie(cookie);
	}

	/**
	 * 建立一个有生命周期的cookie,可以设置他的生命周期
	 */
	public static void createCookie(HttpServletResponse response, String cID,
			String cVal) {
		Cookie cookie = new Cookie(cID, cVal);
		cookie.setMaxAge(3600);
		// 设置路径，这个路径即该工程下都可以访问该cookie 如果不设置路径，那么只有设置该cookie路径及其子路径可以访问
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	/**
	 * 读取
	 */
	public static void readCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();// 这样便可以获取一个cookie数组
		for (Cookie cookie : cookies) {
			cookie.getName();// get the cookie name
			cookie.getValue(); // get the cookie value
		}
	}

}
