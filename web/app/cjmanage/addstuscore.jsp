<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
		<link rel="stylesheet" href="res/ace/assets/css/chosen.css" />
		<link rel="stylesheet" href="res/ace/assets/css/bootstrap-timepicker.css" />
		<link rel="stylesheet" href="res/ace/assets/css/daterangepicker.css" />
		<link rel="stylesheet" href="res/ace/assets/css/colorpicker.css" />


		<!-- ace styles -->
		

		<script src="res/ace/assets/js/date-time/bootstrap-timepicker.min.js"></script>
		<script src="res/ace/assets/js/date-time/moment.min.js"></script>
		<script src="res/ace/assets/js/date-time/daterangepicker.min.js"></script>
		<script src="res/script_/hmq/stuinfo/sevform.js"></script>
<div class="alert alert-warning">
	<input type="hidden" id="addClass-id" value="">
	<input type="hidden" id="addSub-id" value="">
	<input type="hidden" id="addKSNR-id" value="">
	<input type="hidden" id="addKSDate-id" value="">
	<table id="classMessage" style="display: block;">
		<tr>
			<td><label for="form-field-1">班级列表&nbsp;&nbsp;&nbsp;</label></td>
			<td><select class="form-control" id="form-field-1"
				style="width: 200px" onchange="queryKaoShiSub_add(this)">
					<option value=''>--请选择--</option>
					<c:forEach items="${classInfo}" var="ks">
						<option value="${ks.flgId}">${ks.flgValue}</option>
					</c:forEach>
			</select></td>
			<td><label for="form-field-select-2">&nbsp;&nbsp;考试科目&nbsp;&nbsp;</label></td>

			<td><select class="form-control" id="form-field-2"
				style="width: 200px">
					<option value=''>--请选择--</option>
			</select></td>
			<td>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right">考试内容</label>
					<div class="col-sm-9">
						<span class="input-icon"> <input id="form-field-icon-1"
							type="text"> <i class="icon-leaf blue"></i>
						</span>
					</div>
				</div>
			</td>
			<td>
				<label for="form-field-1">&nbsp;&nbsp;考试时间&nbsp;&nbsp;</label>
			</td>
			<td>
				<div class="row">
					<div class="col-xs-8 col-sm-11">
						<div class="input-group">
							<input id="id-date-picker-1" class="form-control date-picker" readonly="readonly"
								type="text" data-date-format="yyyy-mm-dd"> <span
								class="input-group-addon"> <i
								class="icon-calendar bigger-110"></i>
							</span>
						</div>
					</div>
				</div>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<button id="bootbox-options" class="btn btn-success" onclick="addStuScore()">开始新增
					<i class="icon-plus smaller-75"></i>
				</button>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<button id="bootbox-options" class="btn btn-prev" onclick="closeAddStuScore()">关闭新增
					<i class="icon-arrow-left"></i>
				</button>
			</td>
		</tr>
	</table>
	<!-- <div id="home_message" style=" width:200px;margin-left: 5px;display: none;">
				</div> -->
</div>
<div>
					
	<table id="grid-table-add"></table>
	
	<div id="grid-pager-add"></div>
</div>
<script type="text/javascript">
jQuery(function($) {
	$('.date-picker').datepicker({autoclose:true}).next().on(ace.click_event, function(){
		$(this).prev().focus();
	}); 
	var testUrl = "";
	var grid_selector = "#grid-table-add";
	var pager_selector = "#grid-pager-add";
	jQuery(grid_selector).jqGrid({
		url:'',
		datatype: "json",
		height: 500,
		colNames:[ '','姓名', '班级ID','科目ID','考试内容','考试时间','成绩','学生ID', '备注', '操作'],
		colModel:[
			{name:'id',index:'id',align:'center', width:50,editable: true,hidden:true},
			{name:'stu.name',index:'stu.name',align:'center', width:50,editable: false},
			{name:'org.id',index:'org.id',align:'center', width:50,editable: true,hidden:true},
			{name:'sub.id',index:'sub.id',align:'center', width:50,editable: true,hidden:true},
			{name:'ksnr',index:'ksnr',align:'center', width:50,editable: true,hidden:true},
			{name:'kSTime',index:'kSTime',align:'center', width:50, editable: true,hidden:true},
			{name:'chengJi',index:'chengJi', align:'center',width:50,editable: true,editoptions:{size:"20",maxlength:"30"}},
			{name:'stu.id',index:'stu.id', width:150,align:'center',editable: true,hidden:true},
			{name:'reCommon',index:'reCommon', align:'center',width:150, editable: true,editoptions:{size:"100",maxlength:"30"}},
			{name:'myac',index:'', width:80, fixed:true, sortable:false, resize:false,hidden:false,
				formatter:'actions', 
				formatoptions:{
					keys:true,
					delbutton:false,
					delOptions:{recreateForm: true, beforeShowForm:beforeDeleteCallback},
					//editformbutton:true, editOptions:{recreateForm: true, beforeShowForm:beforeEditCallback}
				}
			}
		], 

		viewrecords : true,
		rowNum:40,
		rowList:[40,50,60],
		pager : pager_selector,
		altRows: true,
		
		viewrecords:true,
		onSelectRow: function(id) {
			idValue = id;
		},
		
		editurl: "scoreManage.htm?saveData_",//nothing is saved
		caption: " ",
		autowidth: true

	});
	
});

/**
 * 选择班级下拉框后通过此方法查询考试科目
 */
function queryKaoShiSub_add(_this){
	var classValue = $(_this).val();
	$("#classId_toStu").val(classValue);
	if("" != classValue && null != classValue){
		querywarnkinds('form-field-2','scoreManage.htm?querySub_&status=2&classId=' + classValue);
	}else if("" == classValue || null == classValue){
		$("#form-field-2").empty();
		var cityoption = "<option value=''>--请选择--</option>";
		$("#form-field-2").append(cityoption);
	}
}
/**
 * 通用方法 加载下拉框
 */
function querywarnkinds(unid, url){
	$.ajax({
		type: "POST",
		url: url,
		dataType: "json",
		cache: false,
		success: function(msg){
			if(msg != null && msg.length != 0){
				$("#" + unid).empty();
				var cityoption = "<option value='' selected='selected'>--请选择--</option>";
				for(var len = 0; len < msg.length; len++){
						var typeId = msg[len].flgId;
						var typeName = msg[len].flgValue;
						cityoption = cityoption + "<option value='" + typeId + "'>" + typeName + "</option>";
				}
				 $("#" +unid).append(cityoption);
			}else{
				$("#" +unid).empty();
				var cityoption = "<option value=''>--请选择--</option>";
				$("#" +unid).append(cityoption);
			}
	    }
	});
}

function aceSwitch( cellvalue, options, cell ) {
	setTimeout(function(){
		$(cell) .find('input[type=checkbox]')
				.wrap('<label class="inline" />')
			.addClass('ace ace-switch ace-switch-5')
			.after('<span class="lbl"></span>');
	}, 0);
}


//enable datepicker
function pickDate( cellvalue, options, cell ) {
	setTimeout(function(){
		$(cell) .find('input[type=text]')
				.datepicker({format:'yyyy-mm-dd' , autoclose:true}); 
	}, 0);
}


function style_edit_form(form) {
}

function style_delete_form(form) {
	var buttons = form.next().find('.EditButton .fm-button');
	buttons.addClass('btn btn-sm').find('[class*="-icon"]').remove();//ui-icon, s-icon
	buttons.eq(0).addClass('btn-danger').prepend('<i class="icon-trash"></i>');
	buttons.eq(1).prepend('<i class="icon-remove"></i>')
}

function style_search_filters(form) {
	form.find('.delete-rule').val('X');
	form.find('.add-rule').addClass('btn btn-xs btn-primary');
	form.find('.add-group').addClass('btn btn-xs btn-success');
	form.find('.delete-group').addClass('btn btn-xs btn-danger');
}
function style_search_form(form) {
	var dialog = form.closest('.ui-jqdialog');
	var buttons = dialog.find('.EditTable')
	buttons.find('.EditButton a[id*="_reset"]').addClass('btn btn-sm btn-info').find('.ui-icon').attr('class', 'icon-retweet');
	buttons.find('.EditButton a[id*="_query"]').addClass('btn btn-sm btn-inverse').find('.ui-icon').attr('class', 'icon-comment-alt');
	buttons.find('.EditButton a[id*="_search"]').addClass('btn btn-sm btn-purple').find('.ui-icon').attr('class', 'icon-search');
}

function beforeDeleteCallback(e) {
	var form = $(e[0]);
	if(form.data('styled')) return false;
	
	form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
	style_delete_form(form);
	
	form.data('styled', true);
}

function beforeEditCallback(e) {
	var form = $(e[0]);
	form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
	style_edit_form(form);
}





function updatePagerIcons(table) {
	var replacement = 
	{
		'ui-icon-seek-first' : 'icon-double-angle-left bigger-140',
		'ui-icon-seek-prev' : 'icon-angle-left bigger-140',
		'ui-icon-seek-next' : 'icon-angle-right bigger-140',
		'ui-icon-seek-end' : 'icon-double-angle-right bigger-140'
	};
	$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
		var icon = $(this);
		var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
		
		if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
	})
}

function enableTooltips(table) {
	$('.navtable .ui-pg-button').tooltip({container:'body'});
	$(table).find('.ui-pg-div').tooltip({container:'body'});
}
function addStuScore(){
	var classInfo = $("#form-field-1 option:selected").val();
	var subInfo = $("#form-field-2 option:selected").val();
	var  ksDate= $.trim($("#id-date-picker-1").val());
	var nrInfo = $.trim($("#form-field-icon-1").val());
	if("" == classInfo){
		alert("请选择班级");
		return;
	}
	if("" == subInfo){
		alert("请选择考试科目");
		return;
	}
	if("" == nrInfo){
		alert("请填写考试内容");
		return;
	}
	if("" == ksDate){
		alert("请填写考试时间");
		return;
	}
	if(classInfo != "" && subInfo != "" && nrInfo != ""){
		var newKaoshinr = encodeURI(encodeURI(nrInfo));
		$.ajax({
			type: "POST",
			url: 'scoreManage.htm?regToNR_&classId='+ classInfo +'&subId=' + subInfo + '&nr=' + newKaoshinr + '&ksDate=' + ksDate,
			dataType: "json",
			cache: false,
			success: function(msg){
					if(msg != null && msg.length != 0){
						if(msg[0].flgId == '1'){
							if(confirm(msg[0].flgValue) == true){
								$("#addClass-id").val(classInfo);
								$("#addSub-id").val(subInfo);
								$("#addKSNR-id").val(newKaoshinr);
								$("#addKSDate-id").val(ksDate);
								var query_url = 'scoreManage.htm?queryOneClassStudentInfo_&classId='+ $("#addClass-id").val() + '&subId=' + $("#addSub-id").val() + '&ksnr=' + $("#addKSNR-id").val() + '&ksDate=' + $("#addKSDate-id").val() + '&status=1';
								jQuery('#grid-table-add').jqGrid('setGridParam',{url: query_url}).trigger("reloadGrid");
							}
						}else if(msg[0].flgId == '2'){
							$("#addClass-id").val(classInfo);
							$("#addSub-id").val(subInfo);
							$("#addKSNR-id").val(newKaoshinr);
							$("#addKSDate-id").val(ksDate);
							var query_url = 'scoreManage.htm?queryOneClassStudentInfo_&classId='+ $("#addClass-id").val() + '&subId=' + $("#addSub-id").val() + '&ksnr=' + $("#addKSNR-id").val() + '&ksDate=' + $("#addKSDate-id").val() + '&status=2';
							jQuery('#grid-table-add').jqGrid('setGridParam',{url: query_url}).trigger("reloadGrid");
						}else if(msg[0].flgId == '3'){
							alert(msg[0].flgValue);
							return;
						}
					}else{
						alert("系统异常,请稍后重试");
						return;
					}
		    }
		});
	}
}
function closeAddStuScore(){
	$("#studentByClassMess").css('display','block');
	$("#addStuChengJi").css('display','none');
}

</script>

