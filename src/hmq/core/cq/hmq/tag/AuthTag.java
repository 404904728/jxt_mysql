package core.cq.hmq.tag;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import core.cq.hmq.util.tools.ResourceUtil;
import core.cq.hmq.util.tools.StringUtil;


/***
 * doStartTag()方法返回一个整数值，用来决定程序的后续流程。
 * A.Tag.SKIP_BODY：表示<prefix:someTag>…</prefix:someTag>之间的内容被忽略
 * B.Tag.EVAL_BODY_INCLUDE：表示标签之间的内容被正常执行
 * 
 * .doEndTag：但JSP容器遇到自定义标签的结束标志，就会调用doEndTag()方法。
 * doEndTag()方法也返回一个整数值，用来决定程序后续流程。
 * A.Tag.SKIP_PAGE：表示立刻停止执行网页，网页上未处理的静态内容和JSP程序均被 忽。略任何已有的输出内容立刻返回到客户的浏览器上。
 * B.Tag_EVAL_PAGE：表示按照正常的流程继续执行JSP网页
 * 
 * @author monster
 * 
 */
public class AuthTag extends TagSupport {

	private Long permissionId;

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {
		// TODO Auto-generated method stub
		Set<String> setPermission = (Set<String>) pageContext.getSession()
				.getAttribute(ResourceUtil.getPermissionCookie());
		if (StringUtil.isEmpty(setPermission)) {
			return SKIP_BODY;
		}
		if (setPermission.contains(permissionId.toString())) {
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
		// SessionModal sModal = (SessionModal) pageContext.getSession()
		// .getAttribute(ResourceUtil.getSessionInfoName());
		// UserService userService = (UserService) getBean("userService",
		// pageContext.getServletContext());
		// if (userService.canPermissionByUserId(sModal.getId(), permissionId))
		// {
		// return EVAL_BODY_INCLUDE;
		// } else {
		// return SKIP_BODY;
		// }
	}

	// 获取bean
	public static Object getBean(String beanId, ServletContext sc) {
		ApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(sc);
		return ctx.getBean(beanId);
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
}
