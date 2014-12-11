package common.cq.hmq.controller.system;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.service.letter.PrivateLetterService;
import app.cq.hmq.service.notice.NoticeService;

import common.cq.hmq.pojo.sys.User;
import common.cq.hmq.service.UserService;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.SessionModal;
import core.cq.hmq.service.system.MenuService;
import core.cq.hmq.util.tools.ResourceUtil;
import core.cq.hmq.util.tools.StringUtil;

@Controller
@RequestMapping(value = "/home")
public class HomeController extends BaseController {

	@Resource
	private MenuService menuService;

	@Resource
	private PrivateLetterService privateLetterService;

	@Resource
	private NoticeService noticeService;

	/**
	 * 跳转主页
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping()
	@ControllerAnn(toLogon = false)
	public ModelAndView indexView(HttpSession session) {
		boolean isLogon = userService.isLogon(session);
		ModelAndView mav = new ModelAndView();
		if (isLogon) {
			// 如果登录过者直接跳转到后台主页
			mav.addObject("menus", menuService.findMenuByAce());
			/** 查看私信非本人回复的条数及未看通知条数 */
			SessionModal sm = currentSessionModel();
			mav.addObject(
					"privateLetterCount",
					privateLetterService.obtainPrivateCount(sm.getId(),
							sm.getUserType()));
			mav.addObject(
					"noticeCount",
					noticeService.obtainNoticeCount(sm.getId(),
							sm.getUserType()));
			mav.setViewName("home/home");
			return mav;
		}// 没登录就跳转登录
			// mav.setViewName("home/logon");
		mav.setViewName("home/mobileIndex");
		return mav;
	}

	/**
	 * 后台主页
	 * 
	 * @return
	 */
	@RequestMapping(params = "mobilelogon")
	@ControllerAnn(toLogon = false)
	public String mobilelogon() {
		return view("home/mobileIndex");
	}
	
	/**
	 * 二维码下载
	 *@describe
	 *@author
	 *@date 2014年9月18日  上午10:55:27
	 */
	@RequestMapping(params = "qrcod")
	@ControllerAnn(toLogon = false)
	public String qrcod(){
		return "home/appdown";
	}

	/**
	 * 后台主页
	 * 
	 * @return
	 */
	@RequestMapping(params = "home_")
	public String home_() {
		return view("home/home");
	}

	/**
	 * 登录
	 * 
	 * @return
	 */
	@RequestMapping(params = "logon_")
	@ControllerAnn(toLogon = false)
	public String logon_() {
		return view("home/mobileIndex");
	}

	@Autowired
	private UserService userService;

	/**
	 * 用户登录
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping(params = "logon", method = RequestMethod.POST)
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public AjaxMsg logon(User user, HttpSession session) {
		boolean isLogon = userService.isLogon(session);
		if (isLogon) {
			return new AjaxMsg(0);
		}
		return userService.logon(user, session);
	}

	/**
	 * 用户登录
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(params = "logonMethod", method = RequestMethod.POST)
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public AjaxMsg logonMethod(String no, String pwd, HttpSession session,
			String userType, String verifycode, Long uId, boolean mobile) {
		boolean isLogon = userService.isLogon(session);
		AjaxMsg msg = new AjaxMsg(0);
		if (isLogon) {
			return msg;
		}

		if (StringUtil.isEmpty(userType)) {
			msg.setType(AjaxMsg.WORN);
			msg.setMsg("请选择登录角色！");
			return msg;
		}
		if (!mobile) {
			if (StringUtil.isEmpty(verifycode)) {
				msg.setType(AjaxMsg.WORN);
				msg.setMsg("请输入验证码！");
				return msg;
			} else {
				if (!verifycode.equalsIgnoreCase(String.valueOf(session
						.getAttribute("AUTH_IMG_IN_SESSION")))) {
					msg.setType(AjaxMsg.ERROR);
					msg.setMsg("验证码有误，请重新输入验证码！");
					return msg;
				}
			}
		}
		return userService.StudentLogon(no, pwd, session, userType, uId);
	}

	/**
	 * 获取当前用户
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(params = "currentUser", method = RequestMethod.POST)
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public SessionModal currentUser(HttpSession session) {
		SessionModal sModal = (SessionModal) session.getAttribute(ResourceUtil
				.getSessionInfoName());
		return sModal;
	}

	/**
	 * 用户登录
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping(params = "logonImg", method = RequestMethod.POST)
	@ControllerAnn(toLogon = false)
	@ResponseBody
	public AjaxMsg logonImg(User user, HttpSession session, String authImg) {
		boolean isLogon = userService.isLogon(session);
		if (isLogon) {
			return new AjaxMsg(0, "登录成功");
		}
		return userService.logonImgAuth(user, session, authImg);
	}

	/**
	 * 用户退出
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping(params = "logout")
	@ControllerAnn(toLogon = true)
	public String logout(HttpSession session) {
		session.invalidate();
		// return "home/logon";
		return "index";
	}

	@RequestMapping(params = "modifyPwd")
	@ResponseBody
	public AjaxMsg modifyPwd(String oP, String nP) {
		return userService.modifyPwd(oP, nP);
	}
}
