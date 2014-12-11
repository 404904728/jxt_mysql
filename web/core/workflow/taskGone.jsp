<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<table class="easyui-datagrid" title="已办结任务"
			 data-options="
			 	 singleSelect:true,
			 	 fit:true,
			 	 url:'task.htm?findGone'
			 ">
			<thead>
				<tr>
					<th data-options="field:'id',width:'100',align:'center'">任务编号</th>
					<th data-options="field:'name',width:'150',align:'center'">任务名字</th>
					<th data-options="field:'createDate',width:'150',align:'center'">办理时间</th>
<!--				<th data-options="field:'category',width:'100',align:'center'">操作</th>-->
				</tr>
			</thead>
</table>