package common.cq.hmq.controller.system;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import common.cq.hmq.model.JqGridData;
import common.cq.hmq.model.JqPageModel;
import common.cq.hmq.pojo.sys.User;
import common.cq.hmq.service.UserService;
import common.cq.hmq.service.system.RoleService;
import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.pojo.sys.Role;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Resource
	private UserService userService;

	@RequestMapping(params = "page")
	public String page() {
		return "core/roleAndpermission/roles";
	}

	@Resource
	private RoleService roleService;

	/**
	 * 查询角色数据
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "findData", method = RequestMethod.POST)
	@ResponseBody
	public JqGridData<Role> findData(JqPageModel model) {
		return roleService.findAll(model);
	}

	/**
	 * 查找老师
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "findTeacherRole", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> findTeacherRole(String id) {
		return roleService.findTeacherRoleZtree(id);
	}

	/**
	 * 查找老师角色
	 * 
	 * @return
	 */
	@RequestMapping(params = "findTeacherFindBack", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> findTeacherFindBack(int noticeType) {
		return roleService.findTeacherFindBack(currentSessionModel().getId(),
				noticeType);
	}

	/**
	 * 家校通
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "pageform")
	public ModelAndView pageform(String id) {
		ModelAndView mav = new ModelAndView("app/system/roleform");
		if (id != null) {
			mav.addObject("role", roleService.findById(Long.parseLong(id)));
		}
		return mav;
	}

	/**
	 * 家校通 角色人员绑定页面跳转
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "configrole")
	public ModelAndView configrole(String id) {
		ModelAndView mav = new ModelAndView("app/system/configrole");
		if (id != null) {
			mav.addObject("role2user",
					roleService.findUserByRoleId(Long.parseLong(id)));
			mav.addObject("role", roleService.findById(Long.parseLong(id)));
		}
		return mav;
	}

	/**
	 * 家校通 角色人员绑定页面跳转
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "configpermission")
	public ModelAndView configpermission(String id) {
		ModelAndView mav = new ModelAndView("app/system/configpermission");
		mav.addObject("rId", id);
		return mav;
	}

	/**
	 * 家校通 根据角色ID与用户ID进行添加
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/roleuserbinding", method = RequestMethod.POST)
	@ResponseBody
	@ControllerAnn(refPermission = true)
	public AjaxMsg roleuserbinding(Long roleId, String teacherIds) {
		return roleService.roleuserbinding(roleId, teacherIds);
	}

	@RequestMapping(params = "saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMsg saveOrUpdate(Role role) {
		if (role.getId() == null) {
			return roleService.save(role);
		} else {
			return roleService.update(role);
		}
	}

	@RequestMapping(params = "del", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMsg del(String id) {
		return roleService.del(Long.parseLong(id));
	}

	/**
	 * 根据角色ID，查找该角色下的用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "findUser", method = RequestMethod.POST)
	@ResponseBody
	public List<User> findUserByRoleId(Long id) {
		return userService.findUserByRoleId(id);
	}

	/**
	 * 根据角色ID与用户ID进行添加
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "addUser", method = RequestMethod.POST)
	@ResponseBody
	@ControllerAnn(refPermission = true)
	public AjaxMsg addUserByRoleId(Long roleId, Long userId) {
		return roleService.addUser(userId, roleId);
	}

	/**
	 * 根据角色ID与用户ID进行删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "delUser", method = RequestMethod.POST)
	@ResponseBody
	@ControllerAnn(refPermission = true)
	public AjaxMsg delUserByRoleId(Long roleId, Long userId) {
		return roleService.delUser(userId, roleId);
	}

	/**
	 * 权限分配到角色
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "grantPerPage")
	public ModelAndView grantPerPage(String id) {
		ModelAndView mav = new ModelAndView(
				"core/roleAndpermission/rolegrantPer");
		if (id != null) {
			mav.addObject("role", roleService.findById(Long.parseLong(id)));
		}
		return mav;
	}

	/**
	 * 角色分配给权限
	 * 
	 * @return
	 */
	@RequestMapping(params = "grantPermission", method = RequestMethod.POST)
	@ResponseBody
	public String grantPermission() {
		return null;
	}

	/**
	 * 页面跳转，角色分配给用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "grantUserPage")
	public ModelAndView grantUserPage(Long id) {
		ModelAndView mav = new ModelAndView(
				"core/roleAndpermission/roleGrantUser");
		mav.addObject("roleId", id);
		return mav;
	}

}
