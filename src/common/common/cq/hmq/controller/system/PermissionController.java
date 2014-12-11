package common.cq.hmq.controller.system;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import core.cq.hmq.annotation.ControllerAnn;
import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.pojo.sys.Permission;
import core.cq.hmq.service.system.MenuService;
import core.cq.hmq.service.system.PermissionService;

@Controller()
@RequestMapping(value = "/permission")
public class PermissionController extends BaseController {

	@RequestMapping(params = "page")
	public String page() {
		return "core/roleAndpermission/permission";
	}

	@Resource
	private PermissionService permissionService;

	/**
	 * 根据角色id查找所有权限，且表示该角色已经有的权限
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "findData", method = RequestMethod.POST)
	@ResponseBody
	public List<Permission> findData(Long id) {
		return permissionService.findAll(id);
	}

	/**
	 * 
	 * @param id角色ID
	 * @param ids权限id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "roleGrantPermission", method = RequestMethod.POST)
	@ResponseBody
	@ControllerAnn(refPermission = true)
	public AjaxMsg roleGrantPermission(Long id, String ids) {
		AjaxMsg am = new AjaxMsg();
		try {
			permissionService.roleGantPer(id, ids);
			am.setMsg("权限分配成功");
		} catch (Exception e) {
			e.printStackTrace();
			am.setMsg("权限分配失败，请询问管理员");
			am.setType(am.ERROR);
		}
		return am;
	}

	/******************************************
	 * 家校通
	 ******************************************/

	@Autowired
	private MenuService menuService;

	/**
	 * 根据角色id查找所有权限，且表示该角色已经有的权限
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "findPermission", method = RequestMethod.POST)
	@ResponseBody
	public List<Permission> findPermission(Long rId) {
		return permissionService.findAll(rId);
	}

}
