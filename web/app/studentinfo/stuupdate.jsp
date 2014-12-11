<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		
<div>
	<div class="widget-header header-color-blue">
		<h5 class="bigger lighter">
			<i class="icon-table">
				
			</i>
		</h5>
	</div>
	<br>
	<div id="user-profile-1" class="user-profile row">
		<div class="col-xs-12 col-sm-3 center">
			<div>
				<span class="profile-picture">
					<img id="avatar" class="editable img-responsive" alt="Alex's Avatar" src="${student.headPic.htmlUrl}" />
				</span>
				<div class="space-4"></div>
				<div class="width-80 label label-info label-xlg arrowed-in arrowed-in-right">
					<div class="inline position-relative">
						<a href="#" class="user-title-label dropdown-toggle" data-toggle="dropdown">
							<span class="white">${student.name}</span>
						</a>
					</div>
				</div>
			</div>

			<div class="space-6"></div>

			<div class="profile-contact-info">
				<!-- <div class="profile-contact-links align-left">
				</div>

				<div class="space-6"></div>空格 -->
				
			</div>
			<!-- <div class="hr hr12 dotted"></div>

			<div class="hr hr16 dotted"></div> -->
		</div>
		<div class="col-xs-12 col-sm-9">
			<div class="profile-user-info profile-user-info-striped">
			<form action="stuinfo.htm?oneStu_" method="post" class="form-horizontal" id="subFormToUpdate">
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">学生姓名:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="name" id="name" value="${student.name}">
							<input type="hidden" class="editable" name="id" id="id" value="${student.id}">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">性别:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<select id="sex" name="sex">
								<c:if test="${student.sex == 0}">
									<option value="0" selected="selected">女<option>
									<option value="1">男<option>
									<option value="2">保密<option>
								</c:if>								
								<c:if test="${student.sex == 1}">
									<option value="0">女<option>
									<option value="1" selected="selected">男<option>
									<option value="2">保密<option>
								</c:if>					
								<c:if test="${student.sex == 2}">
									<option value="0">女<option>
									<option value="1">男<option>
									<option value="2" selected="selected">保密<option>
								</c:if>								
							</select>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">家长姓名:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="parentName" id="parentName" value="${student.parentName}">
						</div>
					</div>
				</div>
				<%-- <div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">生日:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="birthday" id="birthday" value="${student.birthday}" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">家庭住址:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="address" id="address" value="${student.address}" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">职务:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="dutyPosition" id="dutyPosition" value="${student.dutyPosition}">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">学籍号:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="studentCode" id="studentCode" value="${student.studentCode}" readonly="readonly">
						</div>
					</div>
				</div> --%>
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">联系方式</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="no" id="no" value="${student.no}">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">所在班级:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="org.name" id="org.name" value="${student.org.name}" readonly="readonly">
						</div>
					</div>
				</div>

			</form>
			</div>
			<div class="space-20"></div>
			<!-- <div class="hr hr2 hr-double"></div> -->
				
			<div class="space-6"></div>
			<script type="text/javascript">
		
		/* 	$("#subFormToUpdate").validACE({
				name:{required : true},
				dutyPosition:{required : true}
			},function(data){
				alert(data.msg);
				jQuery('#grid-table').jqGrid('setGridParam').trigger("reloadGrid");
			}); */
	 </script>
		</div>
	</div>
</div>

