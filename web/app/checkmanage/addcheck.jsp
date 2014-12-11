<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="row">
	<div class="col-xs-12">
		<div class="row">
			<div class="alert alert-success" style="height: 65px;">
				<form class="form-horizontal" role="form">
					<div class="form-group">
						<div class="col-sm-9">
							考勤标题：<span class="input-icon">
								<input id="form-icon-1" type="text">
								<i class="icon-leaf blue"></i>
							</span>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										 <button type="button" onclick="submitAddCheck()" class="btn btn-info btn-sm">确定
										<i class="icon-arrow-right icon-on-right bigger-110"></i></button>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										 <button type="button" class="btn btn-sm" onclick="addBack()">返回<i
										class="icon-reply icon-on-right bigger-110"></i></button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
	
		<div class="row">
				<input type="hidden" id="classId" value="${classId}">
				<div  id="mainDiv" class="col-xs-8 col-sm-11 pricing-span-body" style="">
				</div>
			<!-- PAGE CONTENT ENDS -->
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	initCheck();
});
var stuIdArray = ",";
var stuCount = 0;
var unReachCount = 0;
var resubmit = false;
function initCheck(){
	var classId = $.trim($("#classId").val());
	$.ajax({
		type: "POST",
		url: 'checkManage.htm?initaddCheckToqueryStu_&classId=' + classId,
		dataType: "json",
		cache: false,
		success: function(root){
			if(root.length == 0){
				alert("页面异常");
				return;
			}
			$("#mainDiv").empty();
			var htmlVal = '';
			stuCount = root.length;
			for(var i = 0; i < root.length; i++){
				var data = root[i];
				htmlVal += '<div class="pricing-span" onclick="changeColor(\'color'+ i +'\',\'hidden'+i+'\',\'colorflg'+ i +'\',\'status'+i+'\',\'stuId'+i+'\')" id="testId'+ i +'">' + 
				'<div class="widget-box pricing-box-small">';
				if(i%2 == 1){
					htmlVal += '<div class="widget-header header-color-blue4" id="color'+ i +'"><input type="hidden" id="hidden'+ i +'" value="'+ i +'"><input type="hidden" id="colorflg'+ i +'" value="1" ><input type="hidden" id="stuId'+ i +'" value="'+ data.id +'"><h5 class="bigger lighter"> </h5></div>';
				}else if(i%2 == 0){
					htmlVal += '<div class="widget-header header-color-blue5" id="color'+ i +'"><input type="hidden" id="hidden'+ i +'" value="'+ i +'"><input type="hidden" id="colorflg'+ i +'" value="1" ><input type="hidden" id="stuId'+ i +'" value="'+ data.id +'"><h5 class="bigger lighter"> </h5></div>';
				}
				htmlVal += '<div class="widget-body">' + 
				'<div class="widget-main no-padding">' +
				'<ul class="list-unstyled list-striped pricing-table">' + 
				'<li style="margin-top: 7px;"><dt>'+ data.name +'<dt><dt style="font-size: 12px; color: #777777;">'+data.no+'<dt></li></ul>'+
				'</div>'+
				'<a href="javascript:void(0)" style="cursor:default" class="btn btn-block btn-sm btn-white"><span id="status'+ i +'">&nbsp;</span></a></div></div></div>';
			}
			$("#mainDiv").append(htmlVal);

		}
	}); 
	
}

function removeElement(_this,id){
	$("#" + id).fadeOut("slow");
}
function addElement(_this,id){
	$("#" + id).fadeOut("slow");
}
function changeColor(id,hiid,flgid,status,stuid){
	var flg = $("#" + flgid).val();
/* 	alert($("#" + stuid).val()); */
	if(flg == "1"){// == 1 是指被选中状态，即缺勤
		$("#"+ id).removeClass($("#"+ id).attr("class"));
		$("#"+ id).addClass('widget-header header-color-orange');
		$("#" + status).text("未到");
		$("#" + flgid).val("2");
		stuIdArray += $("#" + stuid).val() + ",";
		unReachCount = unReachCount + 1;
	}
	if(flg == "2"){// == 2 取消缺勤
		$("#"+ id).removeClass($("#"+ id).attr("class"));
		var i = $("#" + hiid).val();
		if(i%2 == 0){
			$("#"+ id).addClass('widget-header header-color-blue5');
		}else if(i%2 == 1){
			$("#"+ id).addClass('widget-header header-color-blue4');
		}
		$("#" + status).html("&nbsp;");
		$("#" + flgid).val("1");
		stuIdArray = stuIdArray.replace($("#" + stuid).val()+ "," ,"");
		unReachCount = unReachCount - 1;
	}
	
}
function addBack(){
	$("#addCheckJsp").css('display','none');
	$("#chckmain").css('display','block');
}
function submitAddCheck(){
	var title = $.trim($("#form-icon-1").val());
	if("" == title){
		bootbox.alert("请输入考勤标题");
		return;
	}
	/* if("," == stuIdArray){
		stuIdArray = "";
		encodeURIComponent(status)
	} */
	if(resubmit){
		return false;
	}
	resubmit = true;
	$.ajax({
		type: "POST",
		url: 'checkManage.htm?saveCheckInfos_&stuId=' + stuIdArray + '&title=' + encodeURI(encodeURI(title)) + '&stuCount=' + stuCount + '&unReachCount=' + unReachCount + '&classId=' + $("#c_select option:selected").val(),
		dataType: "json",
		cache: false,
		success: function(root){
		resubmit = false;
			if(root == null){
				bootbox.alert("系统异常,请稍后在试!");
			}
			if(root.type == 0 || root.type == '0'){
				bootbox.alert("考勤成功!");
				addBack();
				stuIdArray = ",";
				stuCount = 0;
				unReachCount = 0;
				jQuery(grid_selector).jqGrid('setGridParam',{url: './checkManage.htm?findCheckManageInfos&classId='+g_classid}).trigger("reloadGrid");
			}
			if(root.type == 3 || root.type == '3'){
				bootbox.alert("考勤失败!");
				addBack();
				stuIdArray = ",";
				stuCount = 0;
				unReachCount = 0;
			}
		}
	});
}
</script>