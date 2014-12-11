package core.cq.hmq.controller.process;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 流程任务页面跳转控制器
 * 
 * @author dxl
 * 
 */
@Controller
@RequestMapping("/taskPage")
public class TaskPageController {

	/**
	 * 待签收任务
	 * 
	 * @return
	 */
	@RequestMapping(params = "sign")
	public String sign() {
		return "core/workflow/taskSign";
	}

	/**
	 * 待办任务
	 * 
	 * @return
	 */
	@RequestMapping(params = "toDo")
	public String toDo() {
		return "core/workflow/taskToDo";
	}

	/**
	 * 已办任务
	 * 
	 * @return
	 */
	@RequestMapping(params = "hasToDo")
	public String hasToDo() {
		return "core/workflow/taskHasToDo";
	}

	/**
	 * 已办结任务
	 * 
	 * @return
	 */
	@RequestMapping(params = "gone")
	public String gone() {
		return "core/workflow/taskGone";
	}

}
