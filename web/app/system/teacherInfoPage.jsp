<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
<!-- 	<div class="alert alert-info"> -->
<!-- 		在该页面可以重置老师的登录密码，可以修改学校教职工的信息 -->
<!-- 		<button class="btn btn-xs btn-yellow bigger" onclick="backInfoMsg()"> -->
<!-- 			<i class="icon-arrow-left"></i>返回列表 -->
<!-- 	    </button> -->
<!-- 	</div> -->
	<div id="user-profile-1" class="user-profile row">
		<div class="col-xs-12 col-sm-3 center">
			<div>
				<span class="profile-picture">
					<img id="avatar" class="editable img-responsive" alt="Alex's Avatar" src="${teacherInfo.headpic.htmlUrl}" />
				</span>
			</div>
			<div class="profile-contact-info">
				<div class="profile-contact-links align-left">
					<a href="javascript:resetPwd(${teacherInfo.id})" class="btn btn-sm btn-block btn-success">
						<i class=" icon-unlock bigger-120"></i>
						<span class="bigger-110">重置密码</span>
					</a>
					<a href="javascript:updateTeacherInfo(${teacherInfo.id})" class="btn btn-sm btn-block btn-primary">
						<i class="icon-envelope-alt bigger-110"></i>
						<span class="bigger-110">修改信息</span>
					</a>
				</div>

				<div class="space-6"></div><!-- 空格 -->
				
			</div>
		</div>

		<div id="teacherInfoForm" class="col-xs-12 col-sm-9">
			<%@include file="teacherInfoshow.jsp" %>
			<div class="center">
				<button class="btn" onclick="backInfoMsg()">
					<i class="icon-pencil align-top bigger-125"></i>返回列表
				</button>
			</div>
		</div>
		</div>
	</div>
<script type="text/javascript">
<!--
function updateTeacherInfo(tId){
	$.hmqRefreshPage("teacherInfoForm","teacherInfo/pageform.htm?id="+tId)
}
function resetPwd(tId){
	bootbox.confirm("您确定重置该老师的密码?", function(result) {
		if(result) {
			$.hmqAJAX("teacherInfo.htm?resetPwd",function(data){
				alert(data.msg);
			},{"tId":tId});
		}
	});
}
//-->
</script>