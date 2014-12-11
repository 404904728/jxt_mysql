<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="./res/script_/hmq/commonEasy.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
$(function(){
	$("#teacherInfoGrid").datagrid({
		//pagination:true,
		fit:true,fitColumns:true,
		rownumbers:true,
		//singleSelect:true,
		pagerBtns:[{
			iconCls:'icon-add',
			handler:function(){alert('add')}
		},'-',{
			iconCls:'icon-save',
			handler:function(){alert('save')}
		}],
		columns:[[
			{field:'id',title:'id',checkbox:true,width:50,align:'center'},  
			{field:'name',title:'老师名字',sortable:true,width:200,align:'center'},  
			{field:'orgname',title:'组织',sortable:true,width:200,align:'center'}
		]]
	});
	
	setTimeout(function(){
		$("#teacherInfoGrid").datagrid("loading");
		$.hmqAJAX('user.htm?findBackTeacher',function(data){
			$("#teacherInfoGrid").datagrid("loadData",data);
			$("#teacherInfoGrid").datagrid("loaded");
		})
	},500)
	
})
function getSelectIds(){
	var obj =$("#teacherInfoGrid").datagrid("getChecked");
	return obj;
}
</script>
</head>
<body>
<table id="teacherInfoGrid"></table>
</body>
</html>