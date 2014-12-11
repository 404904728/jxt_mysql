<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="./res/script_/hmq/jquery.min.js"></script>
<script type="text/javascript" src="./res/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript" src="./res/script_/hmq/hmq.js"></script>
<script type="text/javascript" src="./res/script_/hmq/hmqKindEditor.js"></script>
<script type="text/javascript" src="./res/kindeditor/lang/zh_CN.js"></script>
<style type="text/css">
.ke-icon-hmqInput{
	background-image: url(./res/kindeditor/themes/default/default.png);
	background-position: 0px -672px;
	width: 16px;
	height: 16px;
}
.ke-icon-hmqSelect{
	background-image: url(./res/kindeditor/themes/default/default.png);
	background-position: 0px -672px;
	width: 16px;
	height: 16px;
}
.ke-icon-hmqProty{
	background-image: url(./res/kindeditor/themes/default/default.png);
	background-position: 0px -672px;
	width: 16px;
	height: 16px;
}
</style>
<script type="text/javascript">
var editor;
$(function(){
	editor = KindEditor.create('textarea[name="content"]', {
		allowFileManager : true,
		items : ['preview','source', 'removeformat', 'hmqInput', 'code','table','flash','hmqSelect','link','|','hmqProty']
	});
})
function getHtml(){
	alert(editor.html());
}
</script>

<title>自定义表单</title>
</head>
<body>
<input type="button" onclick="getHtml()"/>
<form action="">
<textarea name="content"  style="width:800px;height:400px;visibility:hidden;">KindEditor</textarea>
</form>
</body>
</html>