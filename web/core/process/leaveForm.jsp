<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<script type="text/javascript">
function saveLeave(){
	$("#leaveForm").formAjax(function(data) {
		alert(data);
	},false);
}
function onSelect(date){
	alert(data);
}
</script>
<form id="leaveForm" action="leavePro.htm?leaveSave_">
<table class="form">
	<input type="hidden" name="menuId" value="${menuId}"/>
	<tr>
		<th>标题</th>
		<td><input type="text" name="title" /></td>
	</tr>
	<tr>
		<th>类型</th>
		<td><select name="type">
			<option value="0">病假</option>
			<option value="1">事假</option>
			<option value="2">调休</option>
			<option value="3">年假</option>
		</select></td>
	</tr>
</table>
</form>
<input type="button" onclick="saveLeave()" value="提交"/>
</body>
</html>