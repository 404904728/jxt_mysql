package common.cq.hmq.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.cq.hmq.service.leave.LeaveInfoService;
import app.cq.hmq.service.notice.NoticeService;
import app.cq.hmq.service.stuinfo.StudentInfoService;
import app.cq.hmq.service.subject.SubjectMappingService;
import app.cq.hmq.service.teainfo.TeacherInfoService;

import common.cq.hmq.pojo.sys.User;

import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.PageModel;
import core.cq.hmq.modal.SessionModal;

/**
 * 一级菜单跳转
 * 
 * @author cqmonster
 * 
 */
@Controller
@RequestMapping("/menuPage")
public class MenuPageController extends BaseController {

	@Resource
	private LeaveInfoService leaveInfoService;

	@Resource
	private StudentInfoService studentInfoService;

	@Resource
	private TeacherInfoService teacherInfoService;

	/**************************************************
	 * ********************系统管理模块********************
	 ***************************************************/

	/**
	 * 学生个人信息信息跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "studentSelfPage")
	public ModelAndView studentSelfPage() {
		ModelAndView mav = new ModelAndView("app/system/studentSelfPage");
		mav.addObject("student", studentInfoService.currentStudent());
		return mav;
	}

	/**
	 * 老师个人信息跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "teacherSelfPage")
	public ModelAndView teacherSelfPage() {
		ModelAndView mav = new ModelAndView("app/system/teacherSelfPage");
		mav.addObject("teacherInfoClass", subjectMappingService
				.findSubjectMapingByTeacherId(currentSessionModel().getId()));
		mav.addObject("teacher", teacherInfoService.currentTeacher());
		return mav;
	}

	/**
	 * 所有教职工信息跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "teacherPage")
	public ModelAndView teacherPage() {
		ModelAndView mav = new ModelAndView("app/system/teacherPage");
		return mav;
	}

	/**
	 * 所有学生信息跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "studentPage")
	public ModelAndView studentPage() {
		ModelAndView mav = new ModelAndView("app/system/studentPage");
		return mav;
	}

	/**
	 * 角色管理跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "roleInfoPage")
	public ModelAndView roleInfoPage() {
		ModelAndView mav = new ModelAndView("app/system/roleInfoPage");
		return mav;
	}

	/**
	 * 科目管理跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "subjectInfoPage")
	public ModelAndView subjectInfoPage() {
		ModelAndView mav = new ModelAndView("app/subject/subjectInfoPage");
		return mav;
	}

	/**************************************************
	 * ********************系统管理模块结束********************
	 ***************************************************/

	/**************************************************
	 * ********************通知模块跳转开始********************
	 ***************************************************/

	@Autowired
	private NoticeService noticeService;

	/**
	 * 通知信息，发送通知
	 * 
	 * @return
	 */
	@RequestMapping(params = "noticeInfoPage")
	public ModelAndView noticeInfoPage(PageModel model, int type) {
		ModelAndView mav = new ModelAndView("app/notice/noticeInfoPage");
		mav.addObject("sendlist", noticeService.findSendList(model, type));
		mav.addObject("draftlist", noticeService.findDraftList(model, type));
		mav.addObject("noticeType", type);
		return mav;
	}

	/**
	 * 通知信息,接收通知
	 * 
	 * @return
	 */
	@RequestMapping(params = "noticereceivePage")
	public ModelAndView noticereceivePage(PageModel model) {
		ModelAndView mav = new ModelAndView("app/notice/noticereceivePage");
		if (currentSessionModel().getUserType().equals("2")) {
			mav.addObject("receivelist",
					noticeService.findteaReceiveList(model));
		} else {
			mav.addObject("receivelist",
					noticeService.findstuReceiveList(model));
		}
		return mav;
	}

	/**************************************************
	 * ********************通知模块跳转结束********************
	 ***************************************************/

	/**
	 * 学生信息一级菜单跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "studentInfoPage")
	public ModelAndView studentInfoPage(Long mId) {
		ModelAndView mav = new ModelAndView("app/studentinfo/studentinfos");
		mav.addObject("students", "5年级一班");
		return mav;
	}

	/**
	 * 教師通訊一级菜单跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "teacherCommPage")
	public ModelAndView teacherCommPage(Long mId) {
		ModelAndView mav = new ModelAndView("app/teacherComm/teacherComms");
		return mav;
	}

	@Resource
	private SubjectMappingService subjectMappingService;

	/**
	 * 请假申请一级菜单跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "leaveInfo")
	public ModelAndView leaveInfo(Long mId, String searchKey) {
		SessionModal sm = currentSessionModel();
		String type = sm.getUserType();
		ModelAndView mav = new ModelAndView("app/leave/leaveinfos");

		if (User.TEACHERTYPE.equals(type)) {
			mav.addObject("classLeave", subjectMappingService
					.findClassByTeacherId(currentSessionModel().getId()));
		}
		if (User.STUDENTTYPE.equals(type)) {
			mav.addObject("studentId", sm.getId());
			mav.addObject("teachers",
					leaveInfoService.findMyClassBanzhuren(sm.getOrgId()));
			mav.addObject("pageList", leaveInfoService.findLeaveInfoList(0,
					PageList.DEFAULT_PAGE_SIZE, searchKey, type, sm.getId(),
					null));
		}
		mav.addObject("usertype", type);
		return mav;
	}

	/**
	 * 成绩管理一级菜单跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "cjManageInfoPage")
	public ModelAndView cjManageInfoPage(HttpServletRequest request) {
		SessionModal sessionModal = currentSessionModel();
		ModelAndView mav = null;
		if (User.TEACHERTYPE.equals(sessionModal.getUserType())) {
			Map<String, Object> resizeteaType = teacherInfoService
					.resizeteaType(sessionModal.getId());
			if (null != resizeteaType && resizeteaType.size() != 0) {
				if (null != resizeteaType.get("bjzr")) {
					//mav = new ModelAndView("app/cjmanage/cjpublish");
					mav = new ModelAndView("app/cjmanage/cjtitlepublish");
					mav.addObject("bjzr", resizeteaType.get("bjzr"));
					mav.addObject("drqx", resizeteaType.get("drqx"));
					mav.addObject("userType", sessionModal.getUserType());
					return mav;
				}else if(null != resizeteaType.get("drqx")) {
					mav = new ModelAndView("app/cjmanage/cjmanage");
					mav.addObject("drqx", resizeteaType.get("drqx"));
					mav.addObject("userType", sessionModal.getUserType());
					return mav;
				} else if (null == resizeteaType.get("bjzr")
						&& null == resizeteaType.get("drqx")) {
					/** 任课教师 */
					mav = new ModelAndView("app/cjmanage/cjquery");
					mav.addObject("zrflg", "0");
					mav.addObject("userType", sessionModal.getUserType());
					return mav;
				}
			}
		} else if (User.STUDENTTYPE.equals(sessionModal.getUserType())) {
			mav = new ModelAndView("app/cjmanage/cjquery");
			mav.addObject("zrflg", "1");
			mav.addObject("userType", sessionModal.getUserType());
			mav.addObject("jzclassid",sessionModal.getOrgId());
			return mav;
		}
		return null;
	}

	/**
	 * 成绩管理一级菜单跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "queryCJByclass")
	public ModelAndView queryCJByclass(String zrflg) {
		SessionModal sessionModal = currentSessionModel();
		ModelAndView mav = new ModelAndView("app/cjmanage/cjquery");
		mav.addObject("zrflg", zrflg);
		mav.addObject("userType", sessionModal.getUserType());
		return mav;
	}

	/**
	 * 设备报修一级菜单跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "equiRepair")
	public ModelAndView equiRepair(HttpServletRequest request) {
		SessionModal sessionModal = (core.cq.hmq.modal.SessionModal) request
				.getSession().getAttribute("sessionModal");
		ModelAndView mav = new ModelAndView("app/equirepair/equirepair");
		mav.addObject("teaValue", sessionModal.getUserType());
		//studentInfoService.queryMysqlData();
		return mav;
	}

	/**
	 * 私信留言列表
	 * 
	 * @return
	 */
	@RequestMapping(params = "privateLetter")
	public ModelAndView privateLetter() {
		ModelAndView mav = new ModelAndView("app/letter/privateLetters");
		return mav;
	}

	/**
	 * 考勤列表
	 * 
	 * @return
	 */
	@RequestMapping(params = "checkManage")
	public ModelAndView checkManage() {
		ModelAndView mav = new ModelAndView("app/checkmanage/checkmanage");
		SessionModal sessionModal = currentSessionModel();
		mav.addObject("type", sessionModal.getUserType());
		if (User.TEACHERTYPE.equals(sessionModal.getUserType())) {
			mav.addObject("banjis", subjectMappingService
					.findClassByTeacherId(sessionModal.getId()));
		} else if (User.STUDENTTYPE.equals(sessionModal.getUserType())) {
			mav.addObject("banjiName", sessionModal.getOrgName());
			mav.addObject("banjiId", sessionModal.getOrgId());
		}
		return mav;
	}

	/**
	 * 教师心语
	 * 
	 * @return
	 */
	@RequestMapping(params = "teachertopic")
	public ModelAndView teacherTopic() {
		ModelAndView mav = new ModelAndView("app/teachertopic/topicmain");
		SessionModal currentSessionModel = currentSessionModel();
		mav.addObject("userType", currentSessionModel.getUserType());
		if (User.TEACHERTYPE.equals(currentSessionModel.getUserType())) {
			mav.addObject("banjis", subjectMappingService
					.findClassByTeacherId(currentSessionModel.getId()));
		} else if (User.STUDENTTYPE.equals(currentSessionModel.getUserType())) {
			mav.addObject("banjiName", currentSessionModel.getOrgName());
			mav.addObject("banjiId", currentSessionModel.getOrgId());
		}
		return mav;
	}
}
