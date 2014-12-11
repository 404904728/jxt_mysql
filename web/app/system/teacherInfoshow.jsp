<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="profile-user-info profile-user-info-striped">
	<div class="profile-info-row">
		<div class="profile-info-name"> 姓名 </div>
	
		<div class="profile-info-value">
			<span class="editable" id="username">${teacherInfo.name}</span>
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name"> 性别 </div>
		<div class="profile-info-value">
			<span class="editable" id=gender>
				<c:if test="${teacherInfo.gender==0}">
					女
				</c:if>
				<c:if test="${teacherInfo.gender==1}">
					男
				</c:if>
				<c:if test="${teacherInfo.gender==2}">
					未知
				</c:if>
				&nbsp;
			</span>
		</div>
	</div>
	
	
	<div class="profile-info-row">
		<div class="profile-info-name">生日</div>
		<div class="profile-info-value">
			<span class="editable" id="">${teacherInfo.birthday}&nbsp;</span>
		</div>
	</div>
	
	<div class="profile-info-row">
				<div class="profile-info-name">职务</div>
				<div class="profile-info-value">
					<span class="editable" id="country">${teacherInfo.zhiCheng}&nbsp;</span>
				</div>
			</div>
	
			<div class="profile-info-row">
				<div class="profile-info-name">邮件</div>
	
				<div class="profile-info-value">
					<span class="editable" id="login">${teacherInfo.email}&nbsp;</span>
				</div>
			</div>
	
			<div class="profile-info-row">
				<div class="profile-info-name">所属部门</div>
				<div class="profile-info-value">
					<span class="editable" id="about">${teacherInfo.org.name}</span>
				</div>
			</div>
			<div class="profile-info-row">
				<div class="profile-info-name">所属组织</div>
				<div class="profile-info-value">
					<span class="editable" id="about">${zuZhiName}&nbsp;</span>
				</div>
			</div>
			<div class="profile-info-row">
				<div class="profile-info-name">教师电话</div>
				<div class="profile-info-value">
					<span class="editable" id="about">${teacherInfo.telePhone}</span>
				</div>
			</div>
			<div class="profile-info-row">
			<div class="profile-info-name visible-md visible-lg hidden-sm hidden-xs action-buttons">
				所教班级
			</div>
			<div class="profile-info-value">
				<c:forEach items="${teacherInfoClass}" var="tcls">
					<a href="#">
						<i class="icon-flag bigger-110 red"></i>
						<span class="editable" id="about">&nbsp;${tcls.org.name}（${tcls.subjectInfo.name}）</span>
					</a>
				</c:forEach>
				&nbsp;
			</div>
		</div>
</div>