<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<script type="text/javascript">

</script>
<table class="easyui-datagrid" title="流程菜单"
			 data-options="
			 	 singleSelect:true,
			 	 fit:true,toolbar:'#menuToolbar',
			 	 url:'menu.htm?findProcessMenu&id=1105'
		     ">
	<thead>
		<tr>
			<th data-options="field:'id',width:'100',align:'center'">菜单ID</th>
			<th data-options="field:'name',width:'100',align:'center'">菜单名字</th>
			<th data-options="field:'proName',width:'100',align:'center'">工作流名称</th>
			<th data-options="field:'proState',width:'100',align:'center'">工作流状态</th>
			<th data-options="field:'proVersion',width:'100',align:'center'">工作流版本</th>
			<th data-options="field:'permission',width:'250',align:'center'">权限名称</th>
			<th data-options="field:'use',width:'50',align:'center'">是否可用</th>
			<th data-options="field:'icon',width:'100',align:'center'">图标</th>
		</tr>
	</thead>
</table>
 <div id="menuToolbar" style="padding:5px;height:auto">
        <div style="margin-bottom:5px">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true">保存</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
        </div>
    </div>
</body>
</html>