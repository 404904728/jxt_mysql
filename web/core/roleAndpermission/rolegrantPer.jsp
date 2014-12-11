<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="hmq" uri="/hmq-tags"%>
<html>
<body>
<script type="text/javascript">
$(function(){
	
})
function rolegrantPer_save(){
	var obj=$("#rolegrantPerTable").datagrid("getSelections");
	var ids="";
	for(var i=0;i<obj.length;i++){
		ids+=obj[i].id+",";
	}
	$.hmqAJAX("permission.htm?roleGrantPermission",function(data){
		Easy.hmqDialog(data,function(){
			if(data.type==0){
				$('#roleWin').window('close');
				//$("#roleTable").datagrid("reload");不用刷新了
			}
		})
	},{"ids":ids,"id":${role.id}})
}
function rolegrantPer_clear(){
	
}
//根据角色id查找所有权限，且表示该角色已经有的权限
function rolegrantPer_findPermission(){
	return "permission.htm?findData&id="+${role.id};
}
</script>
<table id="rolegrantPerTable" class="easyui-datagrid" 
			data-options="
				collapsible:true,rownumbers:true,
				toolbar:'#toolbarRole2Permission',
				fit:true,url:rolegrantPer_findPermission(),
				onLoadSuccess:function(data){
					var data=data.rows;
					for(var i=0;i<data.length;i++){
						console.log(data[i].check);
						if(data[i].check){
							$('#rolegrantPerTable').datagrid('selectRow',i);
						}
					}
				}
		">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'name',width:200,sortable:true">权限名称</th>
				<th data-options="field:'desc',width:300,sortable:true">权限描述</th>
			</tr>
		</thead>
</table>
<div id="toolbarRole2Permission">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="rolegrantPer_save()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="rolegrantPer_clear()">清空</a>
    </div>
</body>
</html>