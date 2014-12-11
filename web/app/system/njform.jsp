<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="./res/script_/date/datePicker/WdatePicker.js"></script>
<div class="row">
<form id="orgInfo_form" action="org/saveOrUpdate.htm" method="post" class="">
	<input name="id" class="hide" value="${org.id}"/>
	<input name="type" class="hide" value="2"/>
	<div class="profile-info-row">
		<div class="profile-info-name required">年级名称 </div>
		<div class="profile-info-value">
				<input name="name" class="editable" value="${org.name}"/>
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name required">属于阶段</div>
		<div class="profile-info-value" id="orgParentId">
			<input name="parent.id" class="editable hidden" value="${orgParent.id}"/>${orgParent.name}
		</div>
	</div>
<!-- 	<div class="profile-info-row"> -->
<!-- 		<div class="profile-info-name required">副班主任</div> -->
<!-- 		<div class="profile-info-value"> -->
<!-- 			 <select name="sLeader"> -->
<%-- 					<c:forEach items="${leaders}" var="leader"> --%>
<%-- 						<option value="${leader.id}">${leader.name}</option> --%>
<%-- 					</c:forEach> --%>
<!-- 				</select> -->
<!-- 		</div> -->
<!-- 	</div> -->

	<div class="profile-info-row">
		<div class="profile-info-name">电话</div>
		<div class="profile-info-value">
				<input name="tel" class="editable" value="${org.tel}"/>
		</div>
	</div>
	</form>
</div>
<div class="center">
	 <button class="btn btn-primary" onclick="$('#orgInfo_form').submit()">
		<i class="icon-save align-top bigger-125"></i>保存修改
	</button>
	<button class="btn" onclick="backInfoMsg()">
		<i class="icon-pencil align-top bigger-125"></i>关闭修改
	</button>
</div>
<script type="text/javascript">
jQuery(function($){
	$("#orgInfo_form").validACE({
		name:{required : true},date:{required : true},'parent.id':{required : true},
		mLeader:{required:true},code:{required:true}
	},function(data){
		alert(data.msg);
		if(data.type==0){
			$.hmqHomePage("menuPage.htm?studentPage");
		}
	});
})
</script>