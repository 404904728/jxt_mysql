<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文件上传</title>
<link href="./res/script_/fileuploader/css/ui-lightness/jquery-ui-1.8.14.custom.css" rel="stylesheet" type="text/css" />
<link href="./res/script_/fileuploader/css/fileUploader.css" rel="stylesheet" type="text/css" />
<script src="./res/script_/hmq/jquery.min.js" type="text/javascript"></script>
<script src="./res/script_/fileuploader/js/jquery-ui-1.8.14.custom.min.js" type="text/javascript"></script>
<script src="./res/script_/fileuploader/js/jquery.fileUploader.js" type="text/javascript"></script>
<script src="./res/script_/hmq/hmq.js" type="text/javascript"></script>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
var attachId=null;
$(function(){
	$('.fileUpload').fileUploader({
		allowedExtension: 'xls',
		limit:1,
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
<table>
<c:if test="${bjzr != null && njzr != null}">
<tr><td>
<input type="radio" name="usertype"  value="${bjzr}">班级考试</input>
<input type="radio" name="usertype"  checked="checked" value="${njzr}">年级统考</input>
</td></tr>
</c:if>
<tr><td>
<input type="radio" name="scoretype" value="1">周考</input>
<input type="radio" name="scoretype" checked="checked" value="2">月考</input>
</td></tr>
<tr><td>
<div id="main_container" style="margin: 10px 0px 10px 0px">
	<form action="uploader/up.htm" method="post" enctype ="multipart/form-data">
		<input type="file" name="upload" class="fileUpload"/>
<!-- 		<button id="px-submit" type="button">上传</button> -->
<!-- 		<button id="px-clear" type="reset">清除</button> -->
	</form>
</div>
</td></tr>

</table>
<div>
</div>
</body>
</html>
