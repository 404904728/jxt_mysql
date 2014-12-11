package core.cq.hmq.controller.process.leave;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import common.cq.hmq.pojo.sys.Menu;
import common.cq.hmq.pojo.sys.User;
import common.cq.hmq.service.UserService;

import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.service.process.ProcessService;
import core.cq.hmq.service.system.MenuService;
import core.cq.hmq.util.tools.DateUtil;


/**
 * 请假流程
 * 
 * @author hejian
 * 
 */
@Controller
@RequestMapping("/leavePro")
public class LeaveProController extends BaseController {

	@Resource
	private FormService formService;

	@Resource
	private ProcessService processService;

	@Resource
	private RuntimeService runtimeService;

	@Resource
	private MenuService menuService;

	/** 列表页面 */
	@RequestMapping(params = "viewTask_")
	public ModelAndView viewTask(Long menuId) {
		ModelAndView mav = new ModelAndView("core/process/proTaskList");
		mav.addObject("menu", menuService.findOne(menuId));
		return mav;
	}

	/** 列表页面 */
	@RequestMapping(params = "leaveForm_")
	public ModelAndView leaveForm(Long menuId) {
		ModelAndView mav = new ModelAndView("core/process/leaveForm");
		mav.addObject("menuId", menuId);
		return mav;
	}

	@Resource
	private UserService userService;

//	/** 列表页面 */
//	@RequestMapping(params = "leaveSave_")
//	@ResponseBody
//	public String leaveSave(Leave leave, Long menuId) {
//		Map<String, String> mapLeave = new HashMap<String, String>();
//		mapLeave.put("title", leave.getTitle());
//		mapLeave.put("start", DateUtil.format(leave.getStart()));
//		Menu menu = menuService.findOne(menuId);
//		ProcessInstance processInstance = formService.submitStartFormData(menu
//				.getProId(), mapLeave);
//		List<IdentityLink> identityLinks = runtimeService
//				.getIdentityLinksForProcessInstance(processInstance.getId());
//		User user = userService.findUserById(Long.parseLong(identityLinks
//				.get(0).getUserId()));
//		return "申请成功,下一步任务人" + user.getName();
//
//	}
}
