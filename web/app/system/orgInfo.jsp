<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<div id="user-profile-1" class="user-profile row">
		<div class="col-xs-12">
			<div class="profile-user-info profile-user-info-striped">
				<div class="profile-info-row">
					<div class="profile-info-name">名称</div>

					<div class="profile-info-value">
						<span class="editable" id="name">${orgInfo.name}</span>
					</div>
				</div>

				<div class="profile-info-row">
					<div class="profile-info-name">上级组织 </div>
					<div class="profile-info-value">
						<span class="editable" id="parent">
							${orgInfo.parentName}&nbsp;
						</span>
					</div>
				</div>

				
				<div class="profile-info-row">
					<div class="profile-info-name">
						<a class="red" href="javascript:updateOrgLeader(${orgInfo.id})" title="配置人员">
						<i class="icon-cog bigger-130"></i></a>
						<script type="text/javascript">
							function updateOrgLeader(orgId){
								$.dialogACE({
									url:"user.htm?findBackTeacherPage",
									frame:true,frameId:'teacherframe',
									title:'查找老师',width:300,
									callBack:function(dialog){
										var obj=document.getElementById("teacherframe").contentWindow.getSelectIds();
										$(dialog).dialog( "destroy"); 
										var mleader="";
										var mleaderName="";
										for(var i=0;i<obj.length;i++){
											mleader+=obj[i].id+",";
											mleaderName+=obj[i].name+"  ";
										}
										$.hmqAJAX("user.htm?updateLeader",function(data){
											alert(data.msg);
											if(data.type==0){
												$("#leader").html(mleaderName);
											}
										},{"orgId":orgId,"leaderIds":mleader});
									}
								});
							}
						</script>
						班级主任
					</div>
					<div class="profile-info-value">
						<span class="editable" id="leader">${orgInfo.leader}&nbsp;</span>
					</div>
				</div>
				
				<div class="profile-info-row">
					<div class="profile-info-name">任课老师</div>
					<div class="profile-info-value">
						<c:forEach items="${orgInfo.teacher}" var="teacher">
							<span class="editable" id="teacher">${teacher}</span>
						</c:forEach>
						&nbsp;
					</div>
				</div>
				
				<div class="profile-info-row">
					<div class="profile-info-name">联系方式</div>
	
					<div class="profile-info-value">
						<span class="editable" id="about">&nbsp;</span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>