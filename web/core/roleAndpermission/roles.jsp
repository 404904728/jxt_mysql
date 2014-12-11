<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function(){
	
})
function updateRole(id){
	$("#homeWin").window("setTitle","角色修改");
	$('#homeWin').window('open').window('refresh','role.htm?pageform&id='+id);
}
function deleteRole(id){
	Easy.hmqConfirm("警告","您确定删除该记录吗?",function(){
		$.hmqAJAX('role.htm?del',function(data){
			 $("#roleTable").datagrid("reload");
			 Easy.hmqDialog(data);
		},{"id":id});
	})
}
function grantPermission(id){
	$("#homeWin").window("setTitle","权限分配");
	$("#homeWin").window("open").window("refresh",'role.htm?grantPerPage&id='+id);
}
function grantUser(id){
	$("#homeWin").window("setTitle","人员分配");
	$("#homeWin").window("open").window("refresh",'role.htm?grantUserPage&id='+id);
}
</script>
<table id="roleTable" class="easyui-datagrid" 
			data-options="
				singleSelect:true,
				collapsible:true,rownumbers:true,
				fit:true,pagination:true,
				url:'role.htm?findData',
				onRowContextMenu:function(e, rowIndex, rowData){
					$('#roleTable').datagrid('selectRow',rowIndex);
					e.preventDefault();
					$('#rightMenuRole').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
				},
				pagerBtns:[{
						iconCls:'icon-add',
						handler:function(){
							$('#homeWin').window('open').window('refresh','role.htm?pageform');;
						}
					},'-',{
					iconCls:'icon-save',
					handler:function(){
						
					}
				}]
		">
		<thead>
			<tr>
				<th data-options="field:'name',width:200,sortable:true">角色名称</th>
				<th data-options="field:'use',width:100,sortable:true,formatter:function(value){return value?'是':'否'}">是否可用</th>
				<th data-options="field:'desc',width:300,sortable:true">角色描述</th>
				<th data-options="field:'oper',width:350,formatter:function(value,rowData){
					var html='';
					html+=$.addIcon('pen','修改','updateRole('+rowData.id+')');
					html+=$.addIcon('scissors','删除','deleteRole('+rowData.id+')');
					html+=$.addIcon('system','配置权限','grantPermission('+rowData.id+')');
					html+=$.addIcon('popel','人员配置','grantUser('+rowData.id+')');
					return html;
				}">操作</th>
			</tr>
		</thead>
	</table>
	<div id="rightMenuRole" class="easyui-menu" style="width:100px;">
		<div data-options="iconCls:'icon-color-pen'" onclick="">修改</div>
		<div data-options="iconCls:'icon-color-scissors'" onclick="">删除</div>
		<div class="menu-sep"></div>
		<div data-options="iconCls:'icon-color-system'" onclick="">权限配置</div>
		<div data-options="iconCls:'icon-color-popel'" onclick="">人员配置</div>
	</div>