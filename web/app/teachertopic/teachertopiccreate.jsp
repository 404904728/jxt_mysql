<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script src="res/script_/hmq/stuinfo/sevform.js"></script>
<style type="text/css">
.ace-file-input .add {
	position: absolute;
	right: -38px;
	top: 6px;
	width: 17px;
	text-align: center;
	height: 17px;
	line-height: 15px;
	font-size: 11px;
	font-weight: normal;
	background-color: #FA920D;
    border-radius: 100%;
	color: #FFF;
	text-decoration: none;
	z-index: 2\0/
}

</style>

<h4 class="blue">
	<i class="green icon-envelope bigger-110"></i>
    <span>请输入信息:</span>
</h4>
<form id="id-message-form" enctype="multipart/form-data" action="teachertopic.htm?saveTeacherTopic_" method="post"
	class="form-horizontal message-form  col-xs-12">
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"
			for="form-field-subject">标&nbsp;&nbsp;题:</label>
		<div class="col-sm-6 col-xs-12">
			<div class="input-icon block col-xs-12 no-padding">
				<input type="hidden" name="classId" id="classIdVlaue" value="${classId}">
				<input maxlength="100" type="text" class="col-xs-12" name="title"
					id="form-field-subject" placeholder="请输入心语标题.." />
					<i class="icon-comment-alt"></i>
			</div>
		</div>
	</div>
	<div class="hr hr-18 dotted"></div>

	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"> <span
			class="inline space-24 hidden-480"></span> 内&nbsp;&nbsp;容:
		</label>
		<div class="col-sm-8">
			<textarea  style="width: 99%;height:120px" id="repairContent" name="content"></textarea>
		</div>
	</div>
	<div class="hr hr-18 dotted"></div>
	<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"
			for="form-field-subject">附&nbsp;&nbsp;件:</label>
			<div id="addinputdiv" class="col-sm-4" >
					<input type="file" name="file2" id="id-input-file-2" onchange="regPicformat(this)">
			</div>
	</div>
	<div class="hr hr-18 dotted"></div>
		<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right"></label>
		<div class="col-sm-4">
		<span class="inline btn-send-message">
			<button type="button" onclick="submitFormValue()" class="btn btn-sm btn-primary no-border">
				<span class="bigger-120">发布</span>
				<i class="icon-arrow-up icon-on-right"></i>
			</button>
		</span>
		&nbsp;&nbsp;&nbsp;
		<span class="inline btn-send-message">
			<button type="button" onclick="backlist()" class="btn btn-sm btn-primary no-border">
				<span class="bigger-120">返回</span>
				<i class="icon-reply icon-on-right"></i>
			</button>
		</span>
		</div>
	</div>
</form>
<script type="text/javascript">
var resubmit = false;
$("#form-field-subject").focus().select();
var i = 3;
function addinoput(){
	if((i-3) > 3){
		bootbox.alert("最多上传5张附件");
		return;
	}
	var v = '<input type="file" name="file'+i+'" id="id-input-file-'+ i +'" multiple="true" onchange="regPicformat(this)"/>';
	$("#addinputdiv").append(v);
	eval('$("#id-input-file-'+i+'").ace_file_input({ no_file:"没有文件 ...", btn_choose:"选择",btn_change:"更改",droppable:true,onchange:null,thumbnail:false })');
	i++;
}
$('#id-input-file-2').ace_file_input({
	no_file:'没有文件 ...',
	btn_choose:'选择',
	btn_change:'更改',
	droppable:true,
	onchange:null,
	thumbnail:false //| true | large
	//whitelist:'gif',
	
});

function submitFormValue(){
	var title = $.trim($("#form-field-subject").val());
	var area = $.trim($("#repairContent").val());
	if(null == title || "" == title){
		bootbox.alert("心语标题不能为空");
		return;
	}
	if(null == area || "" == area){
		bootbox.alert("心语内容不能为空");
		return;
	}
	if(resubmit){
		return false;
	}
	resubmit = true;
	$('#id-message-form').ajaxSubmit({
		type:"POST",
		dataType: "json",
		cache: false,
  		success: function(msg){
			resubmit = false;
			if(msg.flg == 1){
				bootbox.alert("请重新上传文件"); 
			}else if(msg.flg == 0){
				if(msg.msg){
					bootbox.alert(msg.msg);
				}else{
					bootbox.alert("发送失败,请重新发送");
				}
				$("#teachertopic").css('display','block');
				$("#addTeacherTopic").css('display','none');
			}else if(msg.flg == 2){
				bootbox.alert("发送成功");
				$("#teachertopic").css('display','block');
				$("#addTeacherTopic").css('display','none');
				jQuery("#grid-table_s").jqGrid('setGridParam',{url: './teachertopic.htm?findAllTopic&classId='+ $("#classIdVlaue").val()}).trigger("reloadGrid");
				i = 3;
			}
  		}
  	});
}
function backlist(){
	$("#teachertopic").css('display','block');
	$("#addTeacherTopic").css('display','none');
}

function regPicformat(_this){
	var name = $(_this).val();
	if(!/\.(jpg|png)$/.test(name)){
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

$("div .ace-file-input").append('<a class="add" onclick="addinoput()" href="#"><i class="icon-plus"></i></a>');
</script>
