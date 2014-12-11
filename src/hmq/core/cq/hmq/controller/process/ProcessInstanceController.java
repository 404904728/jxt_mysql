package core.cq.hmq.controller.process;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import core.cq.hmq.dao.PageList;


@Controller
@RequestMapping(value = "/workflow/processinstance")
public class ProcessInstanceController {

	@Autowired
	private RuntimeService runtimeService;

	@RequestMapping(value = "running")
	public ModelAndView running(Model model, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/workflow/running-manage");
		PageList<ProcessInstance> page = new PageList<ProcessInstance>(0,
				PageList.DEFAULT_PAGE_SIZE);
		ProcessInstanceQuery processInstanceQuery = runtimeService
				.createProcessInstanceQuery();
		List<ProcessInstance> list = processInstanceQuery.listPage(0, 2);
		page.setResult(list);
		page.setTotalCount((int) processInstanceQuery.count());
		mav.addObject("page", page);
		return mav;
	}

	/**
	 * 挂起、激活流程实例
	 */
	@RequestMapping(value = "update/{state}/{processInstanceId}")
	public String updateState(@PathVariable("state") String state,
			@PathVariable("processInstanceId") String processInstanceId,
			RedirectAttributes redirectAttributes) {
		if (state.equals("active")) {
			redirectAttributes.addFlashAttribute("message", "已激活ID为["
					+ processInstanceId + "]的流程实例。");
			runtimeService.activateProcessInstanceById(processInstanceId);
		} else if (state.equals("suspend")) {
			runtimeService.suspendProcessInstanceById(processInstanceId);
			redirectAttributes.addFlashAttribute("message", "已挂起ID为["
					+ processInstanceId + "]的流程实例。");
		}
		return "redirect:/workflow/processinstance/running";
	}
}
