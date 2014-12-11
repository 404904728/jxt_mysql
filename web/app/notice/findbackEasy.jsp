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
	$.hmqAJAX('org/findClassData.htm',function(data){
		$("#studentgrid").datagrid({
			data:data,
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
				{field:'name',title:'班级名称',sortable:true,width:200,align:'center'},  
				{field:'cls',title:'年级',sortable:true,width:200,align:'center'}
			]]
		});
	})
	
})
function getSelectIds(){
	var obj =$("#studentgrid").datagrid("getChecked");
	var ids="";
	for(var i=0;i<obj.length;i++){
		ids+=obj[i].id+",";
	}
	return ids;
}
</script>
</head>
<body>
<table id="studentgrid"></table>
</body>
</html>