package app.cq.hmq.controller.appcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.cq.hmq.pojo.notice.Notice;
import app.cq.hmq.pojo.notice.SmsState;
import app.cq.hmq.service.appservice.AppPushService;
import app.cq.hmq.service.notice.NoticeService;
import app.cq.hmq.service.sms.SmsManageService;
import app.cq.hmq.service.stuinfo.StudentInfoService;
import app.cq.hmq.service.subject.SubjectMappingService;
import app.cq.hmq.service.teainfo.TeacherInfoService;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.service.OrgService;
import common.cq.hmq.service.UserService;
import common.cq.hmq.service.system.RoleService;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.dao.PageList;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.PageModel;

/**
 * 通知推送 手机接口
 * 
 * @author cqmonster
 */
@Controller
@RequestMapping("apppush")
public class AppPushController {

	private static int flg = 0;

	@Autowired
	private AppPushService appPushService;

	/**
	 * 根据老师ID 查找班级
	 * 
	 * @param tId
	 * @return [[1, 五年级一班]，[2，五年级2班]]
	 */
	@RequestMapping("/findClass")
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public List<Object[]> findclass(Long tId) {
		return appPushService.findClass(tId);
	}

	/**
	 * 根据老师Id查找所教班级下的学生
	 * 
	 * @param classId
	 * @return [[1, 王XX]，[2，李某某]]
	 */
	@RequestMapping("/homeworkPush")
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public List<Object[]> findstudent(Long classId) {
		return appPushService.findStudentByClassId(classId);
	}

	@Autowired
	private NoticeService noticeService;

	/**
	 * @param notice
	 *            title标题 content 通知内容 techerInfo.id 发送通知人id（老师id） grade
	 *            通知等级（0，普通通知 1：重要通知 2：紧急通知） genre 通知类型（ 0：作业通知 1：班级通知 2：教务处通知
	 *            3：学生处通知 4校内通告）
	 * @param classids
	 *            班级id组装
	 * @param orgids
	 *            老师组织id组装
	 * @param stuids
	 *            选择的学生id组装
	 * @param teaids
	 *            选择的老师id组装
	 * @param stunames
	 *            填写的学生名字组装
	 * @param teanames
	 *            填写的老师名字组装
	 * @param stutels
	 *            填写的学生电话号码组装
	 * @param teatels
	 *            填写的老师电话号码组装
	 * @return
	 */
	@RequestMapping("/push")
	@ResponseBody
	@ControllerAnn(toLogon = false, interval = 1)
	public AjaxMsg push(Notice notice, String classids, String orgids,
			String stuids, String teaids, String stunames, String teanames,
			String stutels, String teatels) {
		String error = "";
		try {
			error = noticeService.push(notice, classids, orgids, stuids,
					teaids, stunames, teanames, stutels, teatels).getMsg();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new AjaxMsg(error);
	}

	/**
	 * 收件箱列表
	 * 
	 * @param uid
	 * @param usertype
	 * @param model
	 * @return
	 */
	@RequestMapping("/receivepush")
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public PageList<List<Map<String, String>>> receivepush(Long uid,
			String usertype, PageModel model) {
		return noticeService.findreceivepush(uid, usertype, model);
	}

	/**
	 * 查找发送的通知
	 * 
	 * @param model
	 * @param uid
	 * @return
	 */
	@RequestMapping("/sendpush")
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public PageList<Object[]> sendpush(PageModel model, Long uid) {
		return noticeService.appFindSendNotices(model, uid);
	}

	@Autowired
	private SmsManageService smsManageService;

	/**
	 * 根据通知ID查找短信发送状态
	 * 
	 * @return
	 */
	@RequestMapping("/findnoticesmsstate")
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public JqGridData<SmsState> findNoticeSmsState(JqPageModel model,
			Long noticeid) {
		return smsManageService.findByNoticeId(model, noticeid);
	}

	@Autowired
	private OrgService orgService;

	@Autowired
	private SubjectMappingService subjectMappingService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	/**
	 * @param tid
	 * @return
	 */
	@RequestMapping(value = "/findtype")
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public List<Integer> findtype(Long tid) {
		// '作业通知',19
		// '班级通知',14
		// '教务处通知',15
		// '学生处通知',16
		// '校内通告',17
		// 通知 大类 0：作业通知 1：班级通知 2：教务处通知 3：学生处通知 4：年级组通知
		if (tid == null)
			return null;
		Set<String> set = userService.findPerByUserIdAndType(tid, true);
		List<Integer> list = new ArrayList<Integer>();
		if (set.contains("19")) {
			list.add(0);
		}
		if (set.contains("14")) {
			list.add(1);
		}
		if (set.contains("15")) {
			list.add(2);
		}
		if (set.contains("16")) {
			list.add(3);
		}
		if (set.contains("17")) {
			list.add(4);
		}
		if (set.contains("20")) {
			list.add(5);
		}
		if (set.contains("21")) {
			list.add(6);
		}
		if (set.contains("22")) {
			list.add(7);
		}
		return list;

	}

	/**
	 * @param tid
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/findobject")
	@ResponseBody
	@ControllerAnn(toLogon = false, interval = 1)
	public Map<String, Object> findObject(Long tid, Integer type,
			Integer teachers) {
		if (tid == null || type == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (type == 0) {// 作业通知
			map.put("class",
					subjectMappingService.findClassByTeacherIdObject(tid));// 班级
		} else if (type == 1) {// 班级通知
			map.put("leader", orgService.findLeader(tid));//
		} else if (type == 2 || type == 3 || type == 4) {// 教务处通知
			if (teachers == 1) {
				map.put("roles", roleService.findTeacherFindBack(tid, type));// 老师角色
			} else {
				map.put("class", orgService.findOrgByTypePush(tid,type));// 班级
			}
		} else if (type == 5 || type == 6 || type == 7) {
			map.put("roles", roleService.findTeacherFindBack(tid, type));// 老师角色
		}
		return map;
	}

	@Autowired
	private StudentInfoService studentInfoService;

	@Autowired
	private TeacherInfoService teacherInfoService;

	/**
	 * 根据老师组织ID查询老师数据
	 * 
	 * @param rid
	 * @return
	 */
	@RequestMapping(value = "/finduserbyrole")
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public List<Object[]> findUserByRole(Long rid) {
		if (rid == null)
			return null;
		return roleService.findByIdImpl(rid);
		// return teacherInfoService.findTeacherInfoByOrgId(rid);
	}

	/**
	 * 根据班级ID查询学生
	 * 
	 * @param clsid
	 * @return
	 */
	@RequestMapping(value = "/finduserbycls")
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public List<Object[]> findUserByCls(Long clsid) {
		if (clsid == null)
			return null;
		return studentInfoService.findStudentByClassIdAPP(clsid);
	}

	/**
	 * 更改通知查看状态
	 * 
	 * @param noticeid
	 *            通知ID
	 * @param userid
	 *            用户id
	 * @param teacher
	 *            true老师通知，false学生通知
	 * @return
	 */
	@RequestMapping(value = "/readnotice")
	@ResponseBody
	@ControllerAnn(toLogon = false)
	public AjaxMsg readNotice(Long noticeid, Long userid, Boolean teacher) {
		noticeService.changeLook(noticeid, teacher, userid);
		return new AjaxMsg("成功");
	}

}
