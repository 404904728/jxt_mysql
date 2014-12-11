package core.cq.hmq.controller.process;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import core.cq.hmq.controller.core.BaseController;
import core.cq.hmq.service.process.ProcessService;


/**
 * 部署工作流
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/process")
public class ProcessController extends BaseController {

	/**
	 * 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "page_")
	public String page() {
		return "core/workflow/processManage";
	}

	@Resource
	private ProcessService processService;

	/**
	 * 获取部署的工作流
	 * 
	 * @return
	 */
	@RequestMapping(params = "findDeploy")
	@ResponseBody
	public List<Map<String, String>> findDeploy() {
		return processService.findProcessDefinition(null);
		// return processService.findProcess();
	}

	/**
	 * @return
	 */
	@RequestMapping(params = "deploy")
	@ResponseBody
	public String deploy(String proId) {
		boolean b = processService.deployProcess("leave", "请假流程");
		if (b) {
			return "部署成功";
		}
		return "部署失败";
	}

	/**
	 * 挂起工作流
	 * 
	 * @return
	 */
	@RequestMapping(params = "stop_")
	@ResponseBody
	public String stop(String proId) {
		String b = "";
		try {
			processService.suspendProcess(proId);
			b = "挂起成功";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			b = "挂起失败";
		}
		return b;
	}

	/**
	 * 激活工作流
	 * 
	 * @return
	 */
	@RequestMapping(params = "activation_")
	@ResponseBody
	public String activation(String proId) {
		String b = "";
		try {
			processService.activationProcess(proId);
			b = "激活成功";
		} catch (Exception e) {
			// TODO: handle exception
			b = "激活失败";
		}
		return b;
	}

	/**
	 * 启动流程
	 * 
	 * @return
	 */
	@RequestMapping(params = "start", method = RequestMethod.POST)
	@ResponseBody
	public String start() {
		boolean b = processService.processStart("leave");
		if (b) {
			return "启动成功";
		}
		return "启动失败";
	}
}
