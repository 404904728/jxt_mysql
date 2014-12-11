package core.cq.hmq.controller.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.TaskModal;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.service.process.HmqTaskService;
import core.cq.hmq.service.process.ProcessService;


@Controller
@RequestMapping("/task")
public class TaskController extends BaseService {
	@Resource
	private ProcessService flowService;

	@Resource
	private HmqTaskService hmqTaskService;

	// 查询代办任务
	@RequestMapping(params = "findToDo", method = RequestMethod.POST)
	@ResponseBody
	public List<TaskModal> findToDo() {
		return hmqTaskService.queryTaskByUser();
	}

	// 查询已办
	@RequestMapping(params = "findGone", method = RequestMethod.POST)
	@ResponseBody
	public List<TaskModal> findGone() {
		return hmqTaskService.queryTaskGoneByUser();
	}

	// 查询待认领的任务（待签收）
	@RequestMapping(params = "findSign", method = RequestMethod.POST)
	@ResponseBody
	public List<TaskModal> findSign() {
		return hmqTaskService.querySignTaskByRole();
	}

	// 签收任务
	@RequestMapping(params = "sign", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMsg sign(String taskId) {
		return hmqTaskService.taskAllot(taskId, currentUserId());
	}

	// 办理任务
	@RequestMapping(params = "taskComplete", method = RequestMethod.POST)
	@ResponseBody
	public AjaxMsg taskComplete(String taskId) {
		return hmqTaskService.taskComplete(taskId);
	}

}
