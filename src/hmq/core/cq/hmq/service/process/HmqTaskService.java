package core.cq.hmq.service.process;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import common.cq.hmq.pojo.sys.User;
import common.cq.hmq.service.UserService;
import common.cq.hmq.service.system.RoleService;

import core.cq.hmq.modal.AjaxMsg;
import core.cq.hmq.modal.TaskModal;
import core.cq.hmq.service.BaseService;


@Service(value = "hmqTaskService")
@Transactional
public class HmqTaskService extends BaseService {

	@Resource
	private RoleService roleService;

	@Resource
	private TaskService taskService;

	@Resource
	private HistoryService historyService;

	@Resource
	private UserService userService;
	
	@Resource
	private RuntimeService runtimeService;

	/**
	 * 取自己待办的任务
	 * 
	 * @return
	 */
	public List<TaskModal> queryTaskByUser() {
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> tasks = taskQuery.taskAssignee(currentUserId() + "").list();
		List<TaskModal> taskModalList = new ArrayList<TaskModal>();
		for (Task task : tasks) {
			TaskModal taskModal = new TaskModal();
			taskModal.setId(task.getId());
			taskModal.setName(task.getName());
			taskModal.setCreateDate(task.getCreateTime());
			taskModal.setDueDate(task.getDueDate());
			taskModalList.add(taskModal);
		}
		return taskModalList;
	}

	/**
	 * 查找自己已办结的任务
	 * 
	 * @return
	 */
	public List<TaskModal> queryTaskGoneByUser() {
		List<HistoricTaskInstance> listHistoricTask = historyService
				.createHistoricTaskInstanceQuery().taskAssignee(
						currentUserId() + "").finished().list();
		List<TaskModal> taskModalList = new ArrayList<TaskModal>();
		for (HistoricTaskInstance task : listHistoricTask) {
			TaskModal taskModal = new TaskModal();
			taskModal.setId(task.getId());
			taskModal.setName(task.getName());
			taskModal.setCreateDate(task.getEndTime());
			taskModal.setDueDate(task.getDueDate());
			taskModalList.add(taskModal);
		}
		return taskModalList;
	}

	/**
	 * 认领任务
	 * 
	 * @param taskId
	 * @param userId
	 * @return
	 */
	public AjaxMsg taskAllot(String taskId, Long userId) {
		AjaxMsg am = new AjaxMsg();
		try {
			taskService.claim(taskId, userId + "");
			am.setMsg("任务分配成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			am.setType(am.ERROR);
			am.setMsg("任务分配失败");
		}
		return am;
	}

	/**
	 * 完成任务
	 * 
	 * @param taskId
	 * @param userId
	 * @return
	 */
	public AjaxMsg taskComplete(String taskId) {
		AjaxMsg am = new AjaxMsg();
		try {
			List<IdentityLink> identityLinks = taskService
					.getIdentityLinksForTask(taskId);
			User user = userService.findUserById(Long.parseLong(identityLinks
					.get(0).getUserId()));
			taskService.complete(taskId);
			am.setMsg("办理成功,下一步"+user.getName()+"审核");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			am.setType(am.ERROR);
			am.setMsg("任务完成失败");
		}
		return am;
	}

	/**
	 * 取自己的待签收任务任务(通过当前用户角色ID来取)
	 * 
	 * @param group
	 * @return
	 */
	public List<TaskModal> querySignTaskByRole() {
		List<Long> roleIds = roleService.currentRoleByUserId(currentUserId());// 查询出当前用户的角色
		List<String> roleIdList = new ArrayList<String>();
		for (Long roleId : roleIds) {
			roleIdList.add(roleId + "");
		}
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskGroup = taskQuery.taskCandidateGroupIn(roleIdList)
				.list();
		List<TaskModal> taskModalList = new ArrayList<TaskModal>();
		for (Task task : taskGroup) {
			TaskModal taskModal = new TaskModal();
			taskModal.setId(task.getId());
			taskModal.setName(task.getName());
			taskModal.setCreateDate(task.getCreateTime());
			taskModal.setDueDate(task.getDueDate());
			taskModalList.add(taskModal);
		}
		return taskModalList;
	}

	/**
	 * 取自己的待签收任务任务(通过当前用户ID来取)
	 * 
	 * @return
	 */
	public List<TaskModal> querySignTaskByUserId() {
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskGroup = taskQuery
				.taskCandidateUser(currentUserId() + "").list();
		List<TaskModal> taskModalList = new ArrayList<TaskModal>();
		for (Task task : taskGroup) {
			TaskModal taskModal = new TaskModal();
			taskModal.setId(task.getId());
			taskModal.setName(task.getName());
			taskModal.setCreateDate(task.getCreateTime());
			taskModal.setDueDate(task.getDueDate());
			taskModalList.add(taskModal);
		}
		return taskModalList;
	}

}
