<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="./res/script_/date/datePicker/WdatePicker.js"></script>
<div class="row">
<form id="teacherInfo_form_add" action="teacherInfo/insert.htm" method="post" class="">
	<div class="profile-info-row">
		<div class="profile-info-name required">姓名 </div>
		<div class="profile-info-value">
				<input name="name" class="editable"/>
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name required">性别 </div>
		<div class="profile-info-value">
				<input name="gender" type="radio" class="ace" value="1"/>
				<span class="lbl">男</span>
				<input name="gender" type="radio" class="ace" value="0"/>
				<span class="lbl">女</span>
				<input name="gender" type="radio" class="ace" value="2"/>
				<span class="lbl">未知</span>
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name">生日</div>
		<div class="profile-info-value">
				<input class="Wdate" name="birthday"  type="text" onClick="WdatePicker({maxDate:'%y-%M-%d'})">
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name">职称</div>
		<div class="profile-info-value">
				<input name="zhiCheng" class="editable" value="${teacherInfo.zhiCheng}"/>
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name">邮件</div>
		<div class="profile-info-value">
				<input name="email" class="editable" value="${teacherInfo.email}"/>
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name required">所属组织</div>
		<div class="profile-info-value">
			<input type="hidden" name="roleids" value="${roleids}"/>${rolenames}
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name required">所属部门</div>
		<div class="profile-info-value">
			<select name="org.id" >
				<c:forEach items="${orgs}" var="o">
					<option value="${o[0]}">${o[1]}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name">教师资格证</div>
		<div class="profile-info-value">
			 <input name="teachNo" class="editable"/>
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name required">教师电话</div>
		<div class="profile-info-value">
				<input name="telePhone" class="editable"/>
		</div>
	</div>
	</form>
</div>
<script type="text/javascript">
jQuery(function($){
	$("#teacherInfo_form_add").validACE({
		name: "required cname",gender:"required",'telePhone':{required:true,phone:true}
	},function(data){
		alert(data.msg);
		$("#myDialogAce").dialog("destroy");
	})
})
</script>
