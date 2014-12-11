<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="./res/ace/assets/js/select2.min.js"></script>	
<script src="res/script_/hmq/stuinfo/sevform.js"></script>

<h4 class="blue">
	<i class="green icon-envelope bigger-110"></i>
	你可以在下面填写报修设备信息
</h4>
<form id="id-message-form" name="id-message-form" enctype="multipart/form-data" action="requimentRepair.htm?submitFormInfo_" method="post"
	class="form-horizontal message-form  col-xs-12">
	<!-- <div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"
			for="form-field-subject">报修主题:</label>
		<div class="col-sm-6 col-xs-12">
			<div class="input-icon block col-xs-12 no-padding">
				<input maxlength="100" type="text" class="col-xs-12" name="repairTitle"
					id="form-field-subject" placeholder="请输入报修主题.." /> <i
					class="icon-comment-alt"></i>
			</div>
		</div>
	</div> -->
	<div class="hr hr-18 dotted"></div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"
			for="form-field-subject">报修等级:</label>
		<div class="col-sm-6 col-xs-12">
			<div class="input-icon block col-xs-12 no-padding">
				<div class="profile-info-row">
						<div class="profile-info-value">
							<span class="editable">
								<input name="level" type="radio" class="ace" value="统一" checked="checked"/>
								<span class="lbl">统一</span>
								<input name="level" type="radio" class="ace" value="立即"/>
								<span class="lbl">立即</span>
							</span>
						</div>
					</div>
				<!-- <input name="level" type="radio" checked="checked" value="统一"/>统一
				<input name="level" type="radio" value="立即"/>立即 -->
			</div>
		</div>
	</div>
	<div class="hr hr-18 dotted"></div>

	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"> <span
			class="inline space-24 hidden-480"></span> 内容:
		</label>
		<div class="col-sm-8">
			<textarea  style="width: 99%;height:120px" id="repairContent" name="repairContent">时间、地点、问题...</textarea>
		</div>
	</div>
	<div class="hr hr-18 dotted"></div>
	<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"
			for="form-field-subject">文件上传:</label>
			<div class="col-sm-4">
				<input type="file" name="fileselect[]" value="123" id="id-input-file2" onchange="regPicformat(this)"/>
			</div>
	</div>
	<div class="hr hr-18 dotted"></div>
	<input type="hidden" value="" name="draft"/>
	<div class="messagebar-item-right">
		<span class="inline btn-send-message">
			<button type="button" onclick="submitFormValue()" class="btn btn-sm btn-primary no-border">
				<span class="bigger-120">发送</span>
				<i class="icon-arrow-right icon-on-right"></i>
			</button>
		</span>
	</div>
	<div class="space"></div>
</form>
<script type="text/javascript">
$('#id-input-file2').ace_file_input({
	no_file:'没有文件 ...',
	btn_choose:'选择',
	btn_change:'更改',
	droppable:false,
	onchange:null,
	thumbnail:false //| true | large
	//whitelist:'gif',
	
});


$(".select2").css('width','210px').select2({allowClear:true})
.on('change', function(){
	$(this).closest('form').validate().element($(this));
});
function submitFormValue(){
	//var title = $.trim($("#form-field-subject").val());
	
	var area = $.trim($("#repairContent").val());
	var file = $.trim($("#id-input-file2").val());
	var level = $("input:radio[name='level']:checked").val();
	/* if(null == title || "" == title){
		bootbox.alert("报修主题不能为空");
		return;
	} */
	if(null == level || "" == level){
		bootbox.alert("请选择报修等级");
		return;
	}
	
	if(null == area || "" == area){
		bootbox.alert("发送内容不能为空");
		return;
	}
	if(null == file || "" == file){
		bootbox.alert("请选择上传文件");
		return;
	}
	
	$('#id-message-form').ajaxSubmit({
		type:"POST",
		dataType: "json",
		cache: false,
  		success: function(msg){
			if(msg.flg == 1){
				bootbox.alert("请重新上传文件");
			}else if(msg.flg == 0){
				bootbox.alert("发送失败,请重新发送");
				$("#testid").css('display','block');
				$("#write").css('display','none');
			}else if(msg.flg == 2){
				bootbox.alert("发送成功");
				/* $("li.active").parent().find("")eq(0).addClass("active");
				$("li.active")..parent().eq(2).removeClass('active'); */
				$(".nav-tabs").children().eq(0).addClass("active");
				$(".nav-tabs").children().eq(1).removeClass("active");
				$(".nav-tabs").children().eq(2).removeClass("active");
				
				$("#testid").css('display','block');
				$("#write").css('display','none');
				init('','2','');
			}
  		}
  	});
}
function regPicformat(_this){
	strtype = $(_this).val().toLowerCase(); 
	if(!/\.(jpg|png)$/.test(strtype)){
		bootbox.alert("请上传jpg或png类型文件!");
		var title = $("#form-field-subject").val();
		var count = $("#repairContent").val();
		document.forms["id-message-form"].reset();
		if(null != title && "" != title){
			$("#form-field-subject").val(title);
		}
		if(null != count && "" != count){
			$("#repairContent").val(count);
		}
		
	}
}
</script>
