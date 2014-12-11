/**
 * 签收任务
 * @param taskId
 * @return
 */
function hmqFlowSign(taskId){
	$.hmqAJAX("task.htm?sign",function(data){
		Easy.hmqDialog(data);
	},{"taskId":taskId});
}
/**
 * 办理任务
 * @param taskId
 * @return
 */
function hmqTaskComplete(taskId){
	$.hmqAJAX("task.htm?taskComplete",function(data){
		Easy.hmqDialog(data);
	},{"taskId":taskId});
}