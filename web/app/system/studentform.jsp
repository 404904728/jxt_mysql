<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
	<form id="studentform" class="form-horizontal" action="stuinfo/saveOrUpdate.htm" method="post">
		<input type="hidden" name="id" value="${student.id}"/>
		<div id="user-profile-1" class="user-profile row">
			<div class="col-xs-12">
				<div class="profile-user-info profile-user-info-striped">
					<div class="profile-info-row">
						<div class="profile-info-name required">账号</div>
						<div class="profile-info-value">
							<span class="editable">
								<input type="text" name="no" value="${student.no}"/>
							</span>
						</div>
					</div>
					<div class="profile-info-row">
						<div class="profile-info-name required">名称</div>
	
						<div class="profile-info-value">
							<span class="editable">
								<input type="text" name="name" value="${student.name}"/>
							</span>
						</div>
					</div>
					
					<div class="profile-info-row">
						<div class="profile-info-name">性别</div>
						<div class="profile-info-value">
							<span class="editable">
								<input name="sex" type="radio" class="ace" value="1"/>
								<span class="lbl">男</span>
								<input name="sex" type="radio" class="ace" value="0"/>
								<span class="lbl">女</span>
							</span>
						</div>
					</div>
					<div class="profile-info-row">
						<div class="profile-info-name">生日</div>
	
						<div class="profile-info-value">
							<span class="editable ">
								<input class="Wdate" style="height: 90%" name="date" value="${student.birthday}" type="text" onClick="WdatePicker()">
							</span>
						</div>
					</div>
					
	
					<div class="profile-info-row">
						<div class="profile-info-name required">所属班级</div>
						<div class="profile-info-value">
							<span class="editable">
								<select  name="org.id" class="select2" data-placeholder="选择...">
									<option value="">&nbsp;</option>
									<c:forEach items="${clss}" var="cls">
										<option  value="${cls.id}"  ${cls.id==student.org.id?'selected':''}>${cls.name}</option>
									</c:forEach>
								</select>
							</span>
						</div>
					</div>
	
					
					<div class="profile-info-row">
						<div class="profile-info-name required">监护人关系</div>
						<div class="profile-info-value">
							<span class="editable">
								<input type="text" name="parentRelation" value="${student.parentRelation}"/>
							</span>
						</div>
					</div>
					
					<div class="profile-info-row">
						<div class="profile-info-name required">监护人姓名</div>
						<div class="profile-info-value">
							<span class="editable">
								<input type="text" name="parentName" value="${student.parentName}"/>
							</span>
						</div>
					</div>
					
					<div class="profile-info-row">
						<div class="profile-info-name">联系方式</div>
						<div class="profile-info-value">
							<input type="text" name="selftel" value="${student.selftel}"/>
						</div>
					</div>
				</div>
				<div class="center">
					<a href="#" onclick="$('#studentform').submit()"  class="btn btn-app btn-info btn-sm">
						保存
					</a>
					<a href="#" onclick="$('#studentform')[0].reset()" class="btn btn-app btn-danger btn-sm">
						重置
					</a>
				</div>
				<div class="space-20"></div>
	
				<div class="hr hr2 hr-double"></div>
	
				<div class="space-6"></div>
			</div>
		</div>
	</form>
	<script src="./res/ace/assets/js/jquery.maskedinput.min.js"></script>
	<script src="./res/ace/assets/js/select2.min.js"></script>
	<script language="javascript" type="text/javascript" src="./res/script_/date/datePicker/WdatePicker.js"></script>
	<script type="text/javascript">
		$(".select2").css('width','190px').select2({allowClear:true})
		.on('change', function(){
			$(this).closest('form').validate().element($(this));
		}); 
		$("#studentform").validACE({
			"no":{required : true,phone:true},
			"name":{required : true},
			//"org.id":{required : true},
			"parentRelation":{required : true},
			"parentName":{required : true},
			"org.id":{required : true}
		},function(data){
			alert(data.msg)
			
		});
	</script>
</div>
