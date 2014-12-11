package core.cq.hmq.util.tools;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义Servlet供系统中几个特殊的Servlet类继承，只需实现execute方法。
 * 
 * @since hejian
 */
public abstract class Servlet extends HttpServlet {

	private static final ThreadLocal<Object[]> tl = new ThreadLocal<Object[]>();

	public static void threadLocal(HttpServletRequest request,
			HttpServletResponse response) {
		tl.set(new Object[] { request, response });
	}

	public static void remove() {
		tl.remove();
	}

	protected final void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		threadLocal(request, response);
		try {
			execute(request, response);
		} finally {
			remove();
		}
	}

	public static Object[] getReq() {
		return tl.get();
	}

	protected abstract void execute(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;
}
