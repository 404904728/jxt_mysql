<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="row">
<form action="subject/saveOrUpdate.htm" method="post" class="form-horizontal" id="subjectform">
	<input type="hidden" value="${subject.id}" name="id"/>
	<div class="form-group">
		<label class="control-label col-xs-2  no-padding-right">科目名称:</label>
		<div class="col-xs-6">
			<div class="clearfix">
				<input type="text" name="name" class="col-xs-12 col-sm-5" value="${subject.name}"/>
			</div>
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-xs-2  no-padding-right">科目描述:</label>
		<div class="col-xs-6">
			<div class="clearfix">
				<input type="text" name="desc" class="col-xs-12 col-sm-5" value="${subject.desc}"/>
			</div>
		</div>
	</div>
</form>
	<script type="text/javascript">
		$("#subjectform").validACE({
			name:{required : true,},
			desc:{required : true}
		},function(data){
			alert(data.msg);
			if(data.type==0){
				$("#myDialogAce").dialog("destroy"); 
				jQuery("#grid-table-subject").trigger("reloadGrid"); //重新载入
			}
		});
	</script>
</div>