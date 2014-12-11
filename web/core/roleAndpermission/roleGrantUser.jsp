<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="hmq" uri="/hmq-tags"%>
<script type="text/javascript">
var roleId=${roleId};
$(function(){
	$("#roleGrantUser_orgTree").tree({  
	    url:'user.htm?findOrgUser',
	    onClick:function(node){
	    	var idtype=node.id.split(":")[0];
	    	if(idtype=="org"){
	    		//alert("部门"+node.id.split(":")[1])
		    }else if(idtype=="user"){
		    	$.hmqAJAX("role.htm?addUser",function(data){
		    		Easy.hmqDialog(data,function(){
		    			$("#rolegrantPerTable").datagrid("load");
			    	});
		    	},{"userId":node.id.split(":")[1],"roleId":roleId});
			}
	    },onBeforeExpand:function(){
			//alert(1);
		}
	});
})
function rolegrantUser_findPermission(){
	return "role.htm?findUser&id="+roleId;
}
function delRoleGrantUser(userId){
	$.hmqAJAX("role.htm?delUser",function(data){
		Easy.hmqDialog(data,function(){
			$("#rolegrantPerTable").datagrid("load");
    	});
	},{"userId":userId,"roleId":roleId})
}
</script>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'west',split:true" title="组织" style="width:300px;">
		<ul id="roleGrantUser_orgTree"></ul>
	</div>
	<div data-options="region:'center',title:'已选择人员'">
		<table id="rolegrantPerTable" class="easyui-datagrid" 
			data-options="
				collapsible:true,rownumbers:true,
				fit:true,url:rolegrantUser_findPermission(),
				onLoadSuccess:function(data){
				
				}
			">
			<thead>
				<tr>
					<th data-options="field:'id',width:80,sortable:true">用户ID</th>
					<th data-options="field:'name',width:120,sortable:true">用户名称</th>
					<th data-options="field:'org',width:120,sortable:true,formatter:function(value,rowData){
						return value.name;
					}">所属部门</th>
					<th data-options="field:'oper',width:100,formatter:function(value,rowData){
					var html='';
					html+=$.addIcon('scissors','移除','delRoleGrantUser('+rowData.id+')');
					return html;
				}">操作</th>
				</tr>
			</thead>
		</table>
	</div>
</div>