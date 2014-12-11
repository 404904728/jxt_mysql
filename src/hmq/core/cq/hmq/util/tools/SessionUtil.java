package core.cq.hmq.util.tools;

import javax.servlet.http.HttpSession;

import common.cq.hmq.pojo.sys.User;
import common.cq.hmq.service.UserService;

import core.cq.hmq.dao.util.BeanUtil;
import core.cq.hmq.modal.SessionModal;


public class SessionUtil {

	public static SessionModal saveSession(User user, HttpSession session) {
		SessionModal sessionModal = new SessionModal();
		sessionModal.setId(user.getId());
		sessionModal.setNo(user.getNo());
		sessionModal.setName(user.getName());
		sessionModal.setDate(DateUtil.format(user.getDate(),
				DateUtil.DATETIME_PATTERN));
		sessionModal.setSessionId(user.getId() + ":" + user.getNo());
		session.setAttribute(ResourceUtil.getSessionInfoName(), sessionModal);// 将一些信息存到session中
		return sessionModal;
	}

	public static SessionModal findSessionModal(HttpSession session) {
		return (SessionModal) session.getAttribute(ResourceUtil
				.getSessionInfoName());
	}

	/**
	 * 刷新权限
	 * 
	 * @param session
	 */
	public static void refPermission(HttpSession session) {
		UserService userService = (UserService) BeanUtil.getBean("userService");
		userService.savePerByUserId(session, SessionUtil.findSessionModal(
				session).getId());// 刷新权限
	}
}
