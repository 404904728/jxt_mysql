<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function(){
	
})
function createFlow(){
	window.open("processDesign.htm?page_");
	/**$('#flowWin').window({  
	    width:$(self).width()*0.8,height:$(self).height()*0.8,
	    title:'流程设计',modal:true,
	    minimizable:false,
	    href:'flowDesign.do?page'
	});**/
}
function createFlowMini(){
	//window.open("./res/flow/myflow-min/demo4.html");
	//window.open("flowDesign.htm?pageeasy_");
	window.open("./res/flow/workflow/index.html");
}
function processDeploy(){
	$.hmqAJAX("process.htm?deploy",function(data){
		alert(data);
	});
}
function processStart(){
	$.hmqAJAX("process.htm?start",function(data){
		alert(data);
	});
}
function stopProcess(proId){
	$.hmqAJAX("process.htm?stop_",function(data){
		alert(data);
	},{"proId":proId});
}
function activationProcess(proId){
	$.hmqAJAX("process.htm?activation_",function(data){
		alert(data);
	},{"proId":proId});
}
function formatterProcessManage(value,rowdata){
	var html='';
	html+=$.addIcon('pen','挂起','stopProcess("'+rowdata.id+'")');
	html+=$.addIcon('scissors','激活','activationProcess("'+rowdata.id+'")');
	return html;
}
</script>
<table class="easyui-datagrid" title="流程管理"
			 data-options="
			 	 singleSelect:true,
			 	 fit:true,
				 tools:'#tt',
			 	 url:'process.htm?findDeploy'
			 ">
			<thead>
				<tr>
					<th data-options="field:'id',width:'100',align:'center'">流程ID</th>
					<th data-options="field:'deploymentId',width:'100',align:'center'">部署ID</th>
					<th data-options="field:'key',width:'100',align:'center'">KEY</th>
					<th data-options="field:'name',width:'150',align:'center'">流程名字</th>
					<th data-options="field:'version',width:'100',align:'center '">流程版本</th>
					<th data-options="field:'state',width:'100',align:'center '">状态</th>
					<th data-options="field:'oper',width:350,formatter:formatterProcessManage">操作</th>
				</tr>
			</thead>
		</table>
<div id="tt">
	<a href="javascript:void(0)" class="icon-add" onclick="createFlow()" title="创建新流程"></a>
	<a href="javascript:void(0)" class="icon-add" onclick="createFlowMini()" title="创建新流程flow"></a>
	<a href="javascript:void(0)" class="icon-edit" onclick="javascript:processDeploy()"></a>
	<a href="javascript:void(0)" class="icon-cut" onclick="javascript:processStart()"></a>
	<a href="javascript:void(0)" class="icon-help" onclick="javascript:alert('help')"></a>		
</div>
<div id="flowWin"></div>
