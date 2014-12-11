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
					<img id="avatar" class="editable img-responsive" alt="Alex's Avatar" src="${cj.stu.headPic.htmlUrl}" />
				</span>
				<div class="space-4"></div>
				<div class="width-80 label label-info label-xlg arrowed-in arrowed-in-right">
					<div class="inline position-relative">
						<a href="#" class="user-title-label dropdown-toggle" data-toggle="dropdown">
							<span class="white">${cj.stu.name}</span>
						</a>
					</div>
				</div>
			</div>

			<div class="space-6"></div>

			<div class="profile-contact-info">
				<div class="profile-contact-links align-left">
				</div>

				<div class="space-6"></div><!-- 空格 -->
				
			</div>
			<div class="hr hr12 dotted"></div>

			<div class="hr hr16 dotted"></div>
		</div>
		<div class="col-xs-12 col-sm-9">
			<div class="profile-user-info profile-user-info-striped">
			<form action="scoreManage.htm?oneScoreToUpdate_" method="post" class="form-horizontal" id="subFormToUpdate">
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">姓名:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="stu.name" id="name" value="${cj.stu.name}" readonly="readonly">
							<input type="hidden" class="editable" name="id" id="id" value="${cj.id}">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">学籍号:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="stu.studentCode" id="name" value="${cj.stu.studentCode}" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">考试科目:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="sub.name" id="sub.name" value="${cj.sub.name}" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">考试内容:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="ksnr" id="ksnr" value="${cj.ksnr}" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">考试成绩:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="chengJi" id="chengJi" value="${cj.chengJi}">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-12 col-sm-3 no-padding-right">所在班级:</label>
					<div class="col-xs-12 col-sm-9">
						<div class="clearfix">
							<input type="text" class="editable" name="org.name" id="org.name" value="${cj.org.name}" readonly="readonly">
						</div>
					</div>
				</div>

			</form>
			</div>
			<div class="space-20"></div>
			<!-- <div class="hr hr2 hr-double"></div> -->
				
			<div class="space-6"></div>
			<script type="text/javascript">
		
		$("#subFormToUpdate").validACE({
			chengJi:{number : true}
		},function(data){
			alert(data.msg);
			jQuery('#grid-table').jqGrid('setGridParam').trigger("reloadGrid");
		}); 
	 </script>
		</div>
	</div>
</div>

