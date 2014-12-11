<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
	<div id="user-profile-1" class="user-profile row">
		<div class="col-xs-12">
			<div class="profile-user-info profile-user-info-striped">
	
				<div class="profile-info-row">
					<div class="profile-info-name">所教班级</div>
					<div class="profile-info-value">
						<span class="editable">
							<select  name="orgId">
								<option value="">&nbsp;</option>
								<c:forEach items="${clss}" var="cls">
									<option value="${cls.id}">${cls.name}</option>
								</c:forEach>
							</select>
						</span>
					</div>
				</div>
				<div class="profile-info-row">
					<div class="profile-info-name">科目</div>
					<div class="profile-info-value">
						<span class="editable">
							<select  name="subjectId" style="width:145px">
								<option value="">&nbsp;</option>
								<c:forEach items="${subject}" var="subj">
									<option value="${subj.id}">${subj.name}</option>
								</c:forEach>
							</select>
						</span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
