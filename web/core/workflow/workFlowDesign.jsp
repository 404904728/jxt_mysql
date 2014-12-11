<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head>
<script type="text/javascript" src="./res/script_/hmq/commonEasy.js"></script>
<link href="./res/css/flow/flowIcon.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="./res/script_/raphael/raphael-min.js"></script>
<script type="text/javascript" src="./res/script_/raphael/flowUtil.js"></script>
<script>
$(function(){
	HMQ_R.drawPaper("paper");
	HMQ_R.drawTask(50,50,"测试");
	HMQ_R.drawTask(200,200,"测试1");
})
</script>
</head>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'west',split:true,title:'工具栏'" style="width:150px;">
		<div class="easyui-accordion" data-options="fit:true,border:false">
				<div id="event" title="事件" iconCls="palette-menu-icon" class="palette-menu">
					<a href="##" onclick="HMQ_R.drawTask(200,200,'开始');" class="easyui-linkbutton" data-options="plain:true,iconCls:'flow-start'">开始</a><br>
					<a href="##" onclick="HMQ_R.drawTask(200,200,'结束');" class="easyui-linkbutton" plain="true" iconCls="flow-end">结束</a><br>
				</div>
				<div id="task" title="任务" iconCls="palette-menu-icon">
					<a href="#" onclick="HMQ_R.drawTask(200,200,'用户任务');" class="easyui-linkbutton" data-options="plain:true,iconCls:'flow-task-user'">用户任务</a><br>
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="flow-task-manual" wfModel="ManualTask">手工任务</a><br>
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="flow-task-service">服务任务</a><br>
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="flow-task-script">脚本任务</a><br>
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="flow-task-mail">邮件任务</a><br>
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="flow-task-receive">接受任务</a><br>
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="flow-task-business">规则任务</a><br>
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="flow-task-subprocess">子流程</a><br>
					<a href="#" class="easyui-linkbutton" plain="true" iconCls="flow-task-call">呼叫活动</a><br>
				</div>
				<div id="gateway" title="连接" iconCls="palette-menu-icon" class="palette-menu">
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="flow-link-parallel">分支</a><br>
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="flow-link-exclusive">合并</a><br>
				</div>
				<div id="boundary-event" title="域事件" iconCls="palette-menu-icon" class="palette-menu">
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="flow-event-time">时间相关事件</a><br>
					<a href="##" class="easyui-linkbutton" plain="true" iconCls="flow-event-error">错误相关事件</a><br>
				</div>
		</div>
	</div>
	<div data-options="region:'center',split:true">
		<div id="desginXml" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',split:true,title:'工具栏'">
				<div class="easyui-tabs" data-options="fit:true">  
				    <div title="流程">  
				        <div id="paper" oncontextmenu="return false" onmouseover="$('#paper').unbind('contextmenu')">
				        	<!-- 屏蔽浏览器右键 -->
				        </div>
				    </div>  
				    <div title="XML">  
				        
				    </div>  
				</div>  
			</div>
			<div data-options="region:'south',title:'属性'" style="height:250px;">
				
			</div>
		</div>
	</div>
<div id="flowMenu" class="easyui-menu" style="width:120px;" >
		<div data-options="iconCls:'icon-print'">线条</div>
		<div data-options="iconCls:'icon-save'" onclick="HMQ_R.objectRemove()">删除</div>
		<div data-options="iconCls:'icon-print',disabled:true">属性</div>
	</div>
</div>
