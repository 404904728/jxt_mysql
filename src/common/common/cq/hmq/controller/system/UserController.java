package common.cq.hmq.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import app.cq.hmq.pojo.stuinfo.StudentInfo;
import app.cq.hmq.service.stuinfo.StudentInfoService;
import app.cq.hmq.service.teainfo.TeacherInfoService;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.Org;
import common.cq.hmq.pojo.sys.User;
import common.cq.hmq.service.OrgService;
import common.cq.hmq.service.UserService;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;

/**
 * 用户
 * 
 * @author monster
 * 
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	/**
	 * 教师信息组织
	 * 
	 * @return
	 */
	@RequestMapping(params = "showPage")
	public String indexView() {
		return view("core/system/user");
	}

	/**
	 * 学生信息组织
	 * 
	 * @return
	 */
	@RequestMapping(params = "showStudent")
	public String showStudent() {
		return view("core/system/students");
	}

	@Resource
	private OrgService orgService;

	/**
	 * 获取部门，用户数据
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params = "findOrgUser", method = RequestMethod.POST)
	public List<Map<String, String>> findOrgUser(String id) {
		if (id == null) {
			return orgService.findOrgs();
		} else {
			return orgService.findChildOrg(id);
		}
	}

	@Resource
	private UserService userService;

	/**
	 * 验证账号是否存在
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params = "validNo", method = RequestMethod.POST)
	@ControllerAnn(toLogon = false)
	public boolean validNo(String no) {
		return userService.validNo(no) == 0 ? false : true;
	}

	@ResponseBody
	@RequestMapping(params = "register", method = RequestMethod.POST)
	@ControllerAnn(toLogon = false)
	public AjaxMsg register(User user, HttpSession session) {
		String pwd = user.getPwd();
		Org org = new Org();
		org.setId(Long.parseLong("2"));// 大众用户部门
		AjaxMsg am = userService.insert(user);
		if (am.getType() == 0) {
			user.setPwd(pwd);
			userService.logon(user, session);
			return am;
		} else {
			return am;
		}
	}

	@ResponseBody
	@RequestMapping(params = "update", method = RequestMethod.POST)
	public AjaxMsg update(User user) {
		return userService.update(user);
	}

	@ResponseBody
	@RequestMapping(params = "pwdchange", method = RequestMethod.POST)
	public AjaxMsg pwdchange(String oldPwd, String pwd, Long id) {
		return userService.pwdChange(id, oldPwd, pwd);
	}

	@Autowired
	private StudentInfoService studentInfoService;

	/**
	 * 查找对应班级的学生
	 * 
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params = "findStudent", method = RequestMethod.POST)
	public JqGridData<StudentInfo> findStudent(JqPageModel model, Long cId) {
		return studentInfoService.findStudentByClassId(model, cId);
	}

	@Autowired
	private TeacherInfoService teacherInfoService;

	/**
	 * 查找所有老师信息
	 * 
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params = "findTeacher", method = RequestMethod.POST)
	public JqGridData<Map> findTeacher(JqPageModel model) {
		return teacherInfoService.findAll(model);
	}

	/**
	 * 修改用户头像
	 * 
	 * @param tId
	 * @return
	 */
	@RequestMapping(value = "/changeImg")
	@ResponseBody
	public AjaxMsg changeImg(Long attachId, HttpSession session) {
		return userService.changeImg(attachId, session);
	}

	@RequestMapping(params = "findBackTeacherPage")
	public String findBackTeacherPage() {
		return "/app/system/findbackTeacher";
	}

	@RequestMapping(params = "findBackTeacher")
	@ResponseBody
	public List<Map<String, Object>> findBackTeacher() {
		List<Object[]> list = teacherInfoService.findTeacherInfoByRoleBZR();
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (Object[] objects : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", objects[0]);
			map.put("name", objects[1]);
			map.put("orgname", objects[2]);
			listMap.add(map);
		}
		return listMap;
	}

	/**
	 * 修改班级主任
	 * 
	 * @param orgId
	 * @param leaderId
	 * @return
	 */
	@RequestMapping(params = "updateLeader")
	@ResponseBody
	public AjaxMsg updateLeader(Long orgId, String leaderIds) {
		AjaxMsg am = new AjaxMsg();
		try {
			orgService.updateLeader(orgId, leaderIds);
			am.setMsg("修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			am.setType(AjaxMsg.ERROR);
			am.setMsg("修改失败");
		}
		return am;
	}
}
