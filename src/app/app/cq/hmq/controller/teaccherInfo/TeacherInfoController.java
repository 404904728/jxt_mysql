package app.cq.hmq.controller.teaccherInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import common.cq.hmq.service.OrgService;
import common.cq.hmq.service.system.RoleService;
import app.cq.hmq.pojo.teacherinfo.TeacherInfo;
import app.cq.hmq.service.subject.SubjectMappingService;
import app.cq.hmq.service.teainfo.TeacherInfoService;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.util.tools.StringUtil;

/**
 * 老师操作相关控制器
 * 
 * @author cqmonster
 * 
 */
@Controller
@RequestMapping("/teacherInfo")
public class TeacherInfoController {

	@Autowired
	private TeacherInfoService teacherInfoService;

	/**
	 * 重置老师密码
	 * 
	 * @param tId
	 * @return
	 */
	@RequestMapping(params = "resetPwd")
	@ResponseBody
	public AjaxMsg resetPwd(Long tId) {
		return teacherInfoService.resetPwd(tId);
	}

	@Autowired
	private SubjectMappingService subjectMappingService;

	/**
	 * 老师信息页面跳转
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pageform")
	public ModelAndView pageform(Long id) {
		ModelAndView mav = new ModelAndView("/app/system/teacherInfoform");
		if (id != null) {
			mav.addObject("teacherInfo", teacherInfoService.findById(id));
			mav.addObject("teacherInfoClass",
					subjectMappingService.findSubjectMapingByTeacherId(id));
			mav.addObject("roles", teacherInfoService.getRoleByType(1L,id));
		}
		mav.addObject("orgs", orgService.findOrgByType(4));
		return mav;
	}

	@Autowired
	private RoleService roleService;

	/**
	 * 添加老师信息页面跳转
	 * 
	 * @param 角色id
	 * @return
	 */
	@RequestMapping(value = "/addpageform")
	public ModelAndView addpageform(String ids, String names) {
		ModelAndView mav = new ModelAndView("/app/system/teacherInfoformAdd");
		mav.addObject("roleids", ids);
		mav.addObject("rolenames", names);
		mav.addObject("orgs", orgService.findOrgByType(4));
		return mav;
	}

	@Autowired
	private OrgService orgService;

	/**
	 * 配置 老师 班级 科目关系
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/configsubject")
	public ModelAndView configsubject() {
		ModelAndView mav = new ModelAndView("/app/subject/subjectfindback");
		mav.addObject("clss", orgService.findAllClass());
		mav.addObject("subject", subjectMappingService.findSubjectAll());
		return mav;
	}

	/**
	 * 老师信息新增或修改
	 * 
	 * @param teacherInfo
	 * @param roleIds
	 *            角色ids
	 * @return
	 */
	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public AjaxMsg saveOrUpdate(TeacherInfo teacherInfo, String[] subjectClass,
			String roleIds) {
		AjaxMsg am = new AjaxMsg();
		try {
			teacherInfoService.saveOrUpdate(teacherInfo, roleIds);
			subjectMappingService
					.bindSubject(subjectClass, teacherInfo.getId());
			am.setMsg("操作成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			am.setType(am.ERROR);
			am.setMsg("操作失败");
		}
		return am;
	}
	
	/**
	 * 老师信息新增或修改
	 * 
	 * @param teacherInfo
	 * @param roleIds
	 *            角色ids
	 * @return
	 */
	@RequestMapping(value = "/insert")
	@ResponseBody
	public AjaxMsg insert(TeacherInfo teacherInfo,String roleids) {
		AjaxMsg am = new AjaxMsg();
		try {
			teacherInfoService.saveOrUpdate(teacherInfo, roleids);
			am.setMsg("操作成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			am.setType(am.ERROR);
			am.setMsg("操作失败");
		}
		return am;
	}

	/**
	 * 删除老师信息
	 * 
	 * @param sId
	 * @return
	 */
	@RequestMapping(params = "deleteById")
	@ResponseBody
	public AjaxMsg deleteById(Long id) {
		AjaxMsg am = new AjaxMsg();
		try {
			teacherInfoService.deleteById(id);
			am.setMsg("删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			am.setType(am.ERROR);
			am.setMsg("删除失败");
		}
		return am;
	}

	/**
	 * 老师信息查找带回页面跳转
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/teacherPage")
	public ModelAndView teacherPage(String id) {
		ModelAndView mav = new ModelAndView("/app/notice/findbacktea");
		mav.addObject("teachers",
				teacherInfoService.findTeacherInfoByOrgId(Long.parseLong(id)));
		return mav;
	}

	/**
	 * 根据角色id查找老师
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findTeacherByRole")
	@ResponseBody
	public List<Map<String, Object>> findTeacherByRole(String id) {
		if (StringUtil.isEmpty(id)) {
			return new ArrayList<Map<String, Object>>();
		}
		return orgService.findTeacherByRoles(id);
	}

}
