<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="./res/script_/date/datePicker/WdatePicker.js"></script>
<script src="./res/ace/assets/js/select2.min.js"></script>	
<script src="./res/ace/assets/js/chosen.jquery.min.js"></script>	
<div class="row">
<form id="teacherInfo_form" action="teacherInfo/saveOrUpdate.htm" method="post" class="">
	<input name="id" class="hide" value="${teacherInfo.id}"/>
	<div class="profile-info-row">
		<div class="profile-info-name required">姓名 </div>
		<div class="profile-info-value">
				<input name="name" class="editable" value="${teacherInfo.name}"/>
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name required">性别 </div>
		<div class="profile-info-value">
				<input name="gender" type="radio" class="ace" ${teacherInfo.gender==1?'checked':''} value="1"/>
				<span class="lbl">男</span>
				<input name="gender" type="radio" class="ace" ${teacherInfo.gender==0?'checked':''} value="0"/>
				<span class="lbl">女</span>
				<input name="gender" type="radio" class="ace" ${teacherInfo.gender==2?'checked':''} value="2"/>
				<span class="lbl">未知</span>
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name">生日</div>
		<div class="profile-info-value">
				<input class="Wdate" name="birthday" value="${teacherInfo.birthday}" type="text" onClick="WdatePicker()">
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
		<div class="profile-info-name">教师电话</div>
		<div class="profile-info-value">
				<input name="telePhone" class="editable" value="${teacherInfo.telePhone}"/>
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name required">所属部门</div>
		<div class="profile-info-value">
			<select id="select_orgid" name="org.id">
				<c:forEach items="${orgs}" var="o">
					<option value="${o[0]}" ${teacherInfo.org.id==o[0]?'selected':''}>${o[1]}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="profile-info-row">
		<div class="profile-info-name required">所属组织</div>
		<div class="profile-info-value">
			<select name="roleIds" multiple="" class="width-40 chosen-select" data-placeholder="请选择组织...">
				<c:forEach items="${roles}" var="o">
					<option value="${o[0]}" ${null != o[2]?'selected':''}>${o[1]}</option>
				</c:forEach>
			</select>
		</div>
	</div>

	<!--
	<div class="profile-info-row">
		<div class="profile-info-name">教师资格证</div>
		<div class="profile-info-value">
			 <input name="teachNo" class="editable" value="${teacherInfo.teachNo}"/>
		</div>
	</div>
	-->
	<div class="profile-info-row">
		<div class="profile-info-name visible-md visible-lg hidden-sm hidden-xs action-buttons">
		<a class="red" href="javascript:configsubjectMapping()" title="配置"><i class="icon-pencil bigger-130"></i></a>所教班级
		</div>
		<div class="profile-info-value" id="subjectclass">
			<c:forEach items="${teacherInfoClass}" var="tcls">
			   <a onclick="$(this).remove()">
				   	<i class="icon-flag bigger-110 red"></i>
					<input type="hidden" name="subjectClass" value="${tcls.org.id},${tcls.subjectInfo.id}"/>
					<span class="editable" >&nbsp;${tcls.org.name}（${tcls.subjectInfo.name}）</span>
			   </a>
			</c:forEach>
			&nbsp;
		</div>
	</div>
	</form>
</div>
<div class="center">
	 <button class="btn btn-primary" onclick="saveupdate()">
		<i class="icon-save align-top bigger-125"></i>保存修改
	</button>
	<button class="btn" onclick="backInfoMsg()">
		<i class="icon-pencil align-top bigger-125"></i>关闭修改
	</button>
</div>
<script type="text/javascript">
jQuery(function($){
	$("#teacherInfo_form").validACE({
		name: "required",gender:"required",'org.id':"required",'roleIds':"required"
	},function(data){
		alert(data.msg);
	});

	$("#select_orgid").css('width','210px').select2({allowClear:true})
	.on('change', function(){
		$(this).closest('form').validate().element($(this));
	}); 

	$(".chosen-select").chosen(); 
})

function saveupdate(){
	$('#teacherInfo_form').submit()
}

function configsubjectMapping(){
	$.dialogACE({
		url:"teacherInfo/configsubject.htm",
		title:"配置班级科目老师关系",
		width:600,
		height:400,
		callBack:function(dialog){
			if($.isEmpty($("select[name='orgId'] option:selected").val())){
				alert("请选择班级");
				return;
			}
			if($.isEmpty($("select[name='subjectId'] option:selected").val())){
				alert("请选择科目");
				return;
			}
			var html="";
			html+='<a onclick="$(this).remove()"><i class="icon-flag bigger-110 red"></i>';
			html+='<input type="hidden" name="subjectClass" value="'+$("select[name='orgId'] option:selected").val()+','+$("select[name='subjectId'] option:selected").val()+'"/>'
			html+='<span class="editable" >'+$("select[name='orgId'] option:selected").text()+'（'+$("select[name='subjectId'] option:selected").text()+'）</span></a>'
			$("#subjectclass").append(html);
			$(dialog).dialog("destroy");
		}
	})
}
</script>
