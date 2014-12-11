<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<script type="text/javascript">
function processLeaveCreate(){
	$("#homeWin").window("setTitle","流程");
	$("#homeWin").window("open").window("refresh",'leavePro.htm?leaveForm_&menuId='+${menu.id});
}
</script>
<table class="easyui-datagrid" title="${menu.name}"
			 data-options="
			 	 singleSelect:true,
			 	 fit:true,toolbar:'#proTaskToolbar',
			 	 url:''
		     ">
	<thead>
		<tr>
			<th data-options="field:'id',width:'100',align:'center'">编号</th>
			<th data-options="field:'name',width:'250',align:'center'">流程名称</th>
			<th data-options="field:'icon',width:'200',align:'center'">活动名称</th>
			<th data-options="field:'url',width:'150',align:'center'">提交人</th>
			<th data-options="field:'permission',width:'150',align:'center'">提交时间</th>
		</tr>
	</thead>
</table>
 <div id="proTaskToolbar" style="padding:5px;height:auto">
        <div style="margin-bottom:5px">
            <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="processLeaveCreate()" plain="true">创建</a>|
            <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true">查看</a>
            <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
        </div>
    </div>
</body>
</html>