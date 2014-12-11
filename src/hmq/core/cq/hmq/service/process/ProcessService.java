package core.cq.hmq.service.process;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;

import core.cq.hmq.modal.FlowModal;
import core.cq.hmq.service.BaseService;
import core.cq.hmq.util.xml.XMLRead;


@Service(value = "processService")
public class ProcessService extends BaseService {
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private RepositoryService repositoryService;

	/** 读取BPMN，分解为任务以便前台展示流程进度图 */
	@SuppressWarnings("static-access")
	public List<FlowModal> processProgress(String xmlPath) {
		return new XMLRead().read(xmlPath);
	}

	/**
	 * 启动一个流程
	 * 
	 * @param proId
	 *            流程ID <process id="流程ID" name="XXX" isExecutable="true">
	 */
	public boolean processStart(String proId) {
		boolean b = true;
		try {
			runtimeService.startProcessInstanceByKey(proId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			b = false;
		}
		return b;
	}

	/**
	 * 查询部署的工作流
	 * 
	 * @param proId
	 *            如果流程ID为空查询所有,
	 * @return
	 */
	public List<Map<String, String>> findProcessDefinition(String proId) {
		ProcessDefinitionQuery processDefinitionQuery = null;
		if (proId == null) {
			processDefinitionQuery = repositoryService
					.createProcessDefinitionQuery();
		} else {
			processDefinitionQuery = repositoryService
					.createProcessDefinitionQuery().processDefinitionId(proId);
		}
		List<ProcessDefinition> processDefinitions = processDefinitionQuery
				.list();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (ProcessDefinition processDefinition : processDefinitions) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", processDefinition.getId());// 部署ID
			map.put("deploymentId", processDefinition.getDeploymentId());
			map.put("key", processDefinition.getKey());
			map.put("name", processDefinition.getName());
			map.put("category", processDefinition.getCategory());
			map.put("version", processDefinition.getVersion() + "");
			map.put("state", processDefinition.isSuspended() ? "挂起" : "激活");
			list.add(map);
		}
		return list;
	}

	/**
	 *发布工作流
	 */
	public boolean deployProcess(String ProcessKey, String processName) {
		try {
			Deployment deployment = repositoryService.createDeployment()
					.addClasspathResource(
							"/activiti/" + ProcessKey + "/" + ProcessKey
									+ ".bpmn20.xml").name(processName).deploy();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 挂起一个流程
	 */
	public void suspendProcess(String proId) {
		repositoryService.suspendProcessDefinitionById(proId);
	}

	/**
	 * 激活一个流程
	 * 
	 * @param key
	 */
	public void activationProcess(String proId) {
		repositoryService.activateProcessDefinitionById(proId);
	}

	/**
	 * 获取流程图不是监控中的流程图
	 * 
	 * @param flowKey
	 * @return
	 * @throws IOException
	 */
	public void processPng(String flowKey, HttpServletResponse response)
			throws IOException {
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().processDefinitionKey(flowKey)
				.singleResult();
		String diagramResourceName = processDefinition.getDiagramResourceName();
		InputStream imageStream = repositoryService.getResourceAsStream(
				processDefinition.getDeploymentId(), diagramResourceName);
		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
		// 流程跟踪
		// ProcessInstance processInstance = runtimeService
		// .createProcessInstanceQuery().processInstanceId(executionId)
		// .singleResult();
		// BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance
		// .getProcessDefinitionId());
		// List<String> activeActivityIds = runtimeService
		// .getActiveActivityIds(flowKey);
		// // 不使用spring请使用下面的两行代码
		// // ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl)
		// // ProcessEngines.getDefaultProcessEngine();
		// //
		// Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());
		// // 使用spring注入引擎请使用下面的这行代码
		// Context.setProcessEngineConfiguration(processEngine
		// .getProcessEngineConfiguration());
		//
		// InputStream imageStream = ProcessDiagramGenerator.generateDiagram(
		// bpmnModel, "png", activeActivityIds);
		// // 输出资源内容到相应对象
		// byte[] b = new byte[1024];
		// int len;
		// while ((len = imageStream.read(b, 0, 1024)) != -1) {
		// response.getOutputStream().write(b, 0, len);
		// }
	}

	public void getProcessMap(String processInstanceId) {
		// try {
		// Execution execution = runtimeService.createExecutionQuery()
		// .executionId(processInstanceId).singleResult();// 执行实例
		// Object property = PropertyUtils.getProperty(execution, "activityId");
		// String activityId = "";
		// if (property != null) {
		// activityId = property.toString();
		// }
		// ProcessInstance processInstance = runtimeService
		// .createProcessInstanceQuery().processInstanceId(
		// processInstanceId).singleResult();
		// ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)
		// ((RepositoryServiceImpl) repositoryService)
		// .getDeployedProcessDefinition(processInstance
		// .getProcessDefinitionId());
		// List<ActivityImpl> activitiList =
		// processDefinition.getActivities();// 获得当前任务的所有节点
		// ActivityImpl activity = null;
		// for (ActivityImpl activityImpl : activitiList) {
		// String id = activityImpl.getId();
		// if (id.equals(activityId)) {// 获得执行到那个节点
		// activity = activityImpl;
		// break;
		// }
		// }
		// Map<String, Object> activityImageInfo = new HashMap<String,
		// Object>();
		// activityImageInfo.put("x", activity.getX());
		// activityImageInfo.put("y", activity.getY());
		// activityImageInfo.put("width", activity.getWidth());
		// activityImageInfo.put("height", activity.getHeight());
		//
		// Struts2Utils.renderJson(activityImageInfo);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return null;
	}

}
