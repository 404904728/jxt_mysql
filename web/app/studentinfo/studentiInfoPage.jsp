<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
function rollBackStuInfos(){
	$("#studentByClassMess").css('display','block');
	$("#readStuMeaasge").css('display','none');
}

</script>
<div>
	<div class="alert alert-success" style="margin-bottom: 5px">
		<i class="icon-reply green"></i>
		<button class="btn btn-link" onclick="rollBackStuInfos()">返回学生信息</button>
	</div>
	
	<div class="widget-header header-color-blue">
		<h5 class="bigger lighter">
			<i class="icon-table">
				信息
			</i>
		</h5>
	</div>
	<div id="user-profile-1" style="margin-left: 30px; margin-top:100px; width: 1000px;" class="user-profile row">
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
					<a href="javascript:void(0)" style="cursor:default" class="btn btn-sm btn-block btn-success">
						
						<span class="bigger-110">&nbsp;</span>
					</a>

					<a href="javascript:void(0)" style="cursor:default" class="btn btn-sm btn-block btn-primary">
						
						<span class="bigger-110">&nbsp;</span>
					</a>
				</div> -->

				<!-- <div class="space-6"></div> --><!-- 空格 -->
				
			</div>


			<div class="hr hr16 dotted"></div>
		</div>

		<div class="col-xs-12 col-sm-9">
			<div class="profile-user-info profile-user-info-striped">
				<div class="profile-info-row">
					<div class="profile-info-name">学籍号</div>
					<div class="profile-info-value">
						<span class="editable" id="age">${student.studentCode}&nbsp;</span>
					</div>
				</div>
				<div class="profile-info-row">
					<div class="profile-info-name"> 姓名 </div>

					<div class="profile-info-value">
						<span class="editable" id="username">${student.name}</span>
					</div>
				</div>

				<div class="profile-info-row">
					<div class="profile-info-name"> 性别 </div>
					<div class="profile-info-value">
						<span class="editable" id=gender>
							<c:if test="${student.sex==0}">
								女
							</c:if>
							<c:if test="${student.sex==1}">
								男
							</c:if>
							<c:if test="${student.sex==2}">
								未知
							</c:if>
						</span>
					</div>
				</div>
				<div class="profile-info-row">
					<div class="profile-info-name">职务</div>

					<div class="profile-info-value">
						<span class="editable" id="about">&nbsp;${student.dutyPosition}</span>
					</div>
				</div>
				
				<div class="profile-info-row">
					<div class="profile-info-name">生日</div>
					<div class="profile-info-value">
						<span class="editable" id="birthday">&nbsp;${student.birthday}</span>
					</div>
				</div>
				<div class="profile-info-row">
					<div class="profile-info-name">民族</div>
					<div class="profile-info-value">
						<span class="editable" id="age">${student.nation}&nbsp;</span>
					</div>
				</div>
				<div class="profile-info-row">
					<div class="profile-info-name">籍贯</div>
					<div class="profile-info-value">
						<i class="icon-map-marker light-orange bigger-110"></i>
						<span class="editable" id="country">${student.address}&nbsp;</span>
					</div>
				</div>
				<div class="profile-info-row">
					<div class="profile-info-name">家庭住址</div>
					<div class="profile-info-value">
						<i class="icon-map-marker light-orange bigger-110"></i>
						<span class="editable" id="country">${student.address}</span>
					</div>
				</div>
				<div class="profile-info-row">
					<div class="profile-info-name">监护人信息</div>

					<div class="profile-info-value">
						<span class="editable" id="login">关系：${student.parentRelation}</span>
						<span class="editable" id="login">姓名：${student.parentName}</span>
						<span class="editable" id="login">电话：${student.selftel}</span>
					</div>
				</div>
				
				<%-- <div class="profile-info-row">
					<div class="profile-info-name">家长姓名</div>
					<div class="profile-info-value">
						<span class="editable" id="about">&nbsp;${student.parentName}</span>
					</div>
				</div>
				<div class="profile-info-row">
					<div class="profile-info-name">家长关系</div>
					<div class="profile-info-value">
						<span class="editable" id="about">&nbsp;${student.parentRelation}</span>
					</div>
				</div>
				<div class="profile-info-row">
					<div class="profile-info-name">联系方式</div>
	
					<div class="profile-info-value">
						<span class="editable" id="about">&nbsp;${student.selftel}</span>
					</div>
				</div> --%>
				<div class="profile-info-row">
					<div class="profile-info-name">身份证号</div>
	
					<div class="profile-info-value">
						<span class="editable" id="about">&nbsp;${student.idCardNo}</span>
					</div>
				</div>
				<div class="profile-info-row">
					<div class="profile-info-name">所属班级</div>

					<div class="profile-info-value">
						<span class="editable" id="about">[${student.org.parent.name}<a>${student.org.name}</a>]</span>
					</div>
				</div>
				<%-- <div class="profile-info-row">
					<div class="profile-info-name">班级主任</div>
	
					<div class="profile-info-value">
						<span class="editable" id="about">${student.leader}&nbsp;</span>
					</div>
				</div> --%>
			</div>
			
			<div class="space-20"></div>

			<div class="hr hr2 hr-double"></div>

			<div class="space-6"></div>

		</div>
	</div>
</div>