<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="./res/script_/date/datePicker/WdatePicker.js"></script>
<div class="row">
<form id="orgteacher_form" action="role.htm?saveOrUpdate" method="post" class="">
	<input name="type" class="hide" value="1"/>
	<input name="id" class="hide" value="${role.id}"/>
	<div class="profile-info-row">
		<div class="profile-info-name required">组织名称</div>
		<div class="profile-info-value">
				<input name="name" class="editable" value="${role.name}"/>
		</div>
	</div>
<!-- 	<div class="profile-info-row"> -->
<!-- 		<div class="profile-info-name required">组长</div> -->
<!-- 		<div class="profile-info-value"> -->
<%-- 				<input name="name" class="editable" value="${role.name}"/> --%>
<!-- 		</div> -->
<!-- 	</div> -->
	<div class="profile-info-row">
		<div class="profile-info-name">电话</div>
		<div class="profile-info-value">
				<input name="tel" class="editable" value="${role.tel}"/>
		</div>
	</div>
	</form>
</div>
<div class="center">
	 <button class="btn btn-primary" onclick="$('#orgteacher_form').submit()">
		<i class="icon-save align-top bigger-125"></i>保存
	</button>
	<button class="btn" onclick="backInfoMsg()">
		<i class="icon-pencil align-top bigger-125"></i>取消
	</button>
</div>
<script type="text/javascript">
jQuery(function($){
	$("#orgteacher_form").validACE({
		name:{required:true}
	},function(data){
		alert(data.msg);
		if(data.type==0){
			backInfoMsg();
		}
	});
})
</script>