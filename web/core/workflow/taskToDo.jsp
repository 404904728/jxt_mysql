<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
function formatterOperFlowToDo(value,rowData,rowIndex){
	var html="";
	html+="<a class='btn' href='javascript:hmqTaskComplete("+rowData.id+")'>办理</a>";
	return html;
}
</script>
<table class="easyui-datagrid" title="待办任务"
			 data-options="
			 	 singleSelect:true,fit:true,
			 	 url:'task.htm?findToDo',
			 	 onLoadSuccess:function(data){
			 	 	$('.btn').linkbutton({
					    iconCls: 'icon-flow-sign',
					    plain:true
					});
			 	 }
			 ">
			<thead>
				<tr>
					<th data-options="field:'id',width:'100',align:'center'">任务编号</th>
					<th data-options="field:'name',width:'150',align:'center'">任务名字</th>
					<th data-options="field:'createDate',width:'150',align:'center'">任务创建时间</th>
				 	<th data-options="field:'operate',width:'200',align:'center',formatter:formatterOperFlowToDo">操作</th>
				</tr>
			</thead>
</table>