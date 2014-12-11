<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="row">
<form action="role.htm?saveOrUpdate" method="post" class="form-horizontal" id="roleform">
	<input type="hidden" value="${role.id}" name="id"/>
	<input type="hidden" value="0" name="type"/>
	<div class="form-group">
		<label class="control-label col-xs-2  no-padding-right">角色名称:</label>
		<div class="col-xs-6">
			<div class="clearfix">
				<input type="text" name="name" class="col-xs-12 col-sm-5" value="${role.name}"/>
			</div>
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-xs-2  no-padding-right">是否可用:</label>
		<div class="col-xs-6">
			<div class="clearfix">
				<input name="use" type="radio" ${role.use==true?'checked':''} class="ace" value="true"/>
				<span class="lbl">可用</span>
				<input name="use" type="radio" ${role.use==false?'checked':''} class="ace" value="false"/>
				<span class="lbl">不可用</span>
			</div>
		</div>
	</div>
<!-- 	<div class="form-group" style="display: none"> -->
<!-- 		<label class="control-label col-xs-2  no-padding-right">角色类型:</label> -->
<!-- 		<div class="col-xs-6"> -->
<!-- 			<div class="clearfix"> -->
<%-- 				<input name="type" type="radio" ${role.type==0?'checked':''} class="ace" value="0"/> --%>
<!-- 				<span class="lbl">普通角色</span> -->
<%-- 				<input name="type" type="radio" ${role.type==1?'checked':''} class="ace" value="1"/> --%>
<!-- 				<span class="lbl">教师角色</span> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
	<div class="form-group">
		<label class="control-label col-xs-2  no-padding-right">角色描述:</label>
		<div class="col-xs-6">
			<div class="clearfix">
				<input type="text" name="desc" width="100%" class="col-xs-12 col-sm-5" value="${role.desc}"/>
			</div>
		</div>
	</div>
</form>
	<script type="text/javascript">
		$("#roleform").validACE({
			name:{required : true},
			use:{required : true},
			desc:{required : true}
		},function(data){
			alert(data.msg);
			if(data.type==0){
				$("#myDialogAce").dialog("destroy"); 
				jQuery("#grid-table").trigger("reloadGrid"); //重新载入
			}
		});
	</script>
</div>