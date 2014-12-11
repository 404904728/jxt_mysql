package core.cq.hmq.interceptors;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 字符集拦截器
 * 
 * @author 何建
 * 
 */
public class EncodingInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 在controller后拦截
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在controller前拦截
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws Exception {
		// 获得请求的方式(1.post or 2.get),根据不同请求方式进行不同处理
		String method = request.getMethod();
		// 1.以post方式提交的请求,直接设置编码为UTF-8
		if (method.equalsIgnoreCase("post")||method.equalsIgnoreCase("POST")) {
			try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {// 2.以get方式提交的请求
				// 取出客户提交的参数集
			Enumeration<String> paramNames = request.getParameterNames();
			// 遍历参数集取出每个参数的名称及值
			while (paramNames.hasMoreElements()) {
				String name = paramNames.nextElement();// 取出参数名称
				String values[] = request.getParameterValues(name);// 根据参数名称取出其值
				// 如果参数值集不为空
				if (values != null) {
					// 如果参数值集中只有一个值
					if (values.length == 1) {
						try {
							// 调用toUTF(values[0])函数,(values[0]即第一个参数值)方法转换参数值的字元编码
							String vlustr = toUTF(values[0]);
							// 并将该值以属性的形式藏在request
							request.setAttribute(name, vlustr);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					} else {// 如果参数值集中有多个值
						// 遍历参数值集
						for (int i = 0; i < values.length; i++) {
							try {
								// 回圈依次将每个值调用toUTF(values[i])方法转换参数值的字元编码
								String vlustr = toUTF(values[i]);
								values[i] = vlustr;
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
						// 将该值以属性的形式藏在request
						request.setAttribute(name, values);
					}
				}
			}

		}
		// 设置响应方式和支持中文的字元集
		response.setContentType("text/html;charset=UTF-8");
		return super.preHandle(request, response, object);

	}

	private String toUTF(String inStr) throws UnsupportedEncodingException {
		String outStr = "";
		if (inStr != null) {
			// outStr=java.net.URLDecoder.decode(inStr);//不用decode了,到这的时候就已经自动decode过了
			// 将字符串转为UTF-8编码形式
			outStr = new String(inStr.getBytes("iso-8859-1"), "UTF-8");
		}
		return outStr;
	}

}
