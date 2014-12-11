package core.cq.hmq.controller.process;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import core.cq.hmq.modal.FlowModal;
import core.cq.hmq.service.process.ProcessService;


@Controller
@RequestMapping("/processPng")
public class ProcessPngController {
	@Resource
	private ProcessService processService;


	/** 读取BPMN，分解为任务以便前台展示流程进度图 */
	@RequestMapping(params = "flowData")
	@ResponseBody
	public List<FlowModal> getData(String xmlPath) {
		return processService.processProgress(xmlPath);
	}

	/** 获取流程图不是监控中的流程图 */
	@RequestMapping(params = "processPic")
	public void flowPng(HttpServletResponse response) throws IOException {
		processService.processPng("leave", response);
	}

}
