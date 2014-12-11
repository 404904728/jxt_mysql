<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
function formatterOperFlowSing(value,rowData,rowIndex){
	var html="";
	html+="<a class='btn' href='javascript:hmqFlowSign("+rowData.id+")'>签收</a>";
	return html;
}
</script>
<table class="easyui-datagrid" title="签收任务"
			 data-options="
			 	 singleSelect:true,fit:true,
			 	 url:'task.htm?findSign',
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
				 	<th data-options="field:'operate',width:'200',align:'center',formatter:formatterOperFlowSing">操作</th>
				</tr>
			</thead>
</table>