<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文件上传</title>
<style type="text/css">
body {
	margin: 10px;
	font: 62% Tahoma, Arial, sans-serif;
}
#main_container{
	font-size: 1.4em;
}
</style>
<link href="./res/script_/fileuploader/css/ui-lightness/jquery-ui-1.8.14.custom.css" rel="stylesheet" type="text/css" />
<link href="./res/script_/fileuploader/css/fileUploader.css" rel="stylesheet" type="text/css" />
<script src="./res/script_/hmq/jquery.min.js" type="text/javascript"></script>
<script src="./res/script_/fileuploader/js/jquery-ui-1.8.14.custom.min.js" type="text/javascript"></script>
<script src="./res/script_/fileuploader/js/jquery.fileUploader.js" type="text/javascript"></script>
<script src="./res/script_/hmq/hmq.js" type="text/javascript"></script>
<script type="text/javascript">
var attachId=null;
$(function(){
	$('.fileUpload').fileUploader({
		afterEachUpload:function(formId,status,id,data){
			if("success"==status){
				if(attachId!=null)
					deleteAttach(attachId);
				attachId=id;
			}
		},
		onFileRemove:function(li){
			deleteAttach($(li).attr("attachid"))
		}
	});
})
function deleteAttach(id){
	$.hmqAJAX("attach.htm?delete",function(data){},{"id":id})
}
</script>
</head>
<body>
<div id="main_container">
	<form action="uploader/up.htm" method="post" enctype ="multipart/form-data">
		<input type="file" name="upload" class="fileUpload"/>
<!-- 		<button id="px-submit" type="button">上传</button> -->
<!-- 		<button id="px-clear" type="reset">清除</button> -->
	</form>
</div>
</body>
</html>
