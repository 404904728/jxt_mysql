<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="page-header">
	<h1>
		个人资料
		<small>
			<i class="icon-double-angle-right"></i>
			个人信息
		</small>
	</h1>
</div><!-- /.page-header -->

<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->

		<div class="clearfix">
			<div class="pull-left alert alert-success no-margin">
				<button type="button" class="close" data-dismiss="alert">
					<i class="icon-remove"></i>
				</button>
				<i class="icon-umbrella bigger-120 blue"></i>
				点击下面的图片，或在个人资料栏位进行编辑 ...
			</div>
		</div>

		<div class="hr dotted"></div>

		<div>
			<div id="user-profile-1" class="user-profile row">
				<div class="col-xs-12 col-sm-3 center">
					<div>
						<span class="profile-picture">
							<img id="avatar" class="editable img-responsive" alt=""  style="height:225px" src="${teacher.headpic.htmlUrl}" />
						</span>
					</div>


					<div class="profile-contact-info">
						<div class="profile-contact-links align-left">
							<a href="javascript:changeHeadImg()" class="btn btn-sm btn-block btn-success">
								<i class="icon-plus-sign bigger-120"></i>
								<span class="bigger-110">更换头像</span>
							</a>

							<a href="#" class="btn btn-sm btn-block btn-primary">
								<i class="icon-envelope-alt bigger-110"></i>
								<span class="bigger-110">发送私信</span>
							</a>
						</div>
					</div>
				</div>

				<!-- 按钮 -->
				<div class="col-xs-12 col-sm-9">

					<div class="space-12"></div>

					<div class="profile-user-info profile-user-info-striped">
						<div class="profile-info-row">
							<div class="profile-info-name"> 姓名 </div>
							<div class="profile-info-value">
								<span class="editable" id="username">${teacher.name}</span>
							</div>
						</div>
						<div class="profile-info-row">
							<div class="profile-info-name">所属部门</div>
							<div class="profile-info-value">
								<span class="editable" id="about">${teacher.org.name}&nbsp;</span>
							</div>
						</div>
						<div class="profile-info-row">
							<div class="profile-info-name">职务</div>
							<div class="profile-info-value">
								<span class="editable" id="country">${teacher.zhiCheng}&nbsp;</span>
							</div>
						</div>

						<div class="profile-info-row">
							<div class="profile-info-name"> 出生生日 </div>
							<div class="profile-info-value">
								<span class="editable" id="">${teacher.birthday}&nbsp;</span>
							</div>
						</div>

						<div class="profile-info-row">
							<div class="profile-info-name">邮件</div>

							<div class="profile-info-value">
								<span class="editable" id="login">${teacher.email}&nbsp;</span>
							</div>
						</div>

						<!--<div class="profile-info-row">
							<div class="profile-info-name">教师资格证</div>

							<div class="profile-info-value">
								<span class="editable" id="about">${teacher.teachNo}&nbsp;</span>
							</div>
						</div>
						--><div class="profile-info-row">
							<div class="profile-info-name">教师电话</div>

							<div class="profile-info-value">
								<span class="editable" id="about">${teacher.telePhone}</span>
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
											<span class="editable" id="about">&nbsp;${tcls.org.name}(${tcls.subjectInfo.name})</span>
										</a>
									</c:forEach>
									&nbsp;
								</div>
					 </div>
					</div>
				</div>
			</div>
		</div>
<!-- 		<div class="hr hr2 hr-double"></div> -->
	</div><!-- /.col -->
</div><!-- /.row -->