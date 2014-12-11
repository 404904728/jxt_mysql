/*$(document).ready(function(){
	var  a = ['标题', '学号','姓名','内容','总成绩'];
	var  b = [{name:'title',index:'title',  align:'left', width:150,editable: false,sortable:false},
	     {name:'no',index:'no',align:'left', width:150,editable: false,sortable:false},
	     {name:'name',index:'name',align:'left', width:150,editable: false},
	     {name:'scoreA',index:'scoreA',align:'left', width:150,editable: false,sortable:false},
	     {name:'totalScore',index:'totalScore',align:'left', width:150,editable: false,sortable:false}
		];
//	var hsFlg = true;
	//家长登录处理
	if('1' == userType_js){
		a = ['标题', '学号','姓名','内容','总成绩','班排','班排涨幅','年排','年排涨幅'];
		b = [{name:'title',index:'title',  align:'left', width:220,editable: false,sortable:false},
		     {name:'no',index:'no',align:'left', width:100,editable: false,sortable:false},
		     {name:'name',index:'name',align:'left', width:60,editable: false,sortable:false},
		     {name:'scoreA',index:'scoreA',align:'left', width:350,editable: false,sortable:false},
		     {name:'totalScore',index:'totalScore',align:'left', width:50,editable: false},
		     {name:'classOrder',index:'classOrder',align:'left', width:150,editable: false,sortable:false},
		     {name:'classAsc',index:'classAsc',align:'left', width:150,editable: false,sortable:false},
		     {name:'drandOrder',index:'drandOrder',align:'left', width:150,editable: false,sortable:false},
		     {name:'drandAsc',index:'drandAsc',align:'left', width:150,editable: false,sortable:false}
			];
		var stu_to_calss_1 = queryStuInfo_class('scoreManage.htm?StuClassinfo_');
		$("#home_stu_message").html('<i class="icon-hand-right"/> ' + stu_to_calss_1);
	//教师登录处理
	}else if('2' == userType_js){
		hsFlg = false;
		querywarnkinds('form-field-select-1','scoreManage.htm?classinfo_');
	}
	
	var idValue = "";
	var beginUrl = '';
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
	jQuery(grid_selector).jqGrid({
		url:beginUrl,
		datatype: "json",
		height: 400,
		colNames:a,
		colModel:b,
		viewrecords : true,
		rowNum:10,
		rowList:[10,20,30],
		pager : pager_selector,
		altRows: true,
		editurl: "stuinfo.htm?oneStu_",
		onSelectRow: function(id) {
			idValue = id;
		},
		loadComplete : function() {
			var table = this;
			setTimeout(function(){
				updatePagerIcons(table);
				enableTooltips(table);
			}, 0);
		},
		autowidth: true

	});
		jQuery(grid_selector).jqGrid('navGrid',pager_selector,{});//navButtons
	
});*/




function inithqdd(a,b){
	var idValue = "";
	var beginUrl = '';
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
	jQuery(grid_selector).jqGrid({
		url:beginUrl,
		datatype: "json",
		height: 600,
		colNames:a,
		colModel:b,
		viewrecords : true,
		rowNum:20,
		rowList:[20,30,40],
		pager : pager_selector,
		altRows: true,
		editurl: "stuinfo.htm?oneStu_",
		onSelectRow: function(id) {
			idValue = id;
		},
		loadComplete : function() {
			var table = this;
			setTimeout(function(){
				updatePagerIcons(table);
				enableTooltips(table);
			}, 0);
		},
		autowidth: true

	});
	
		jQuery(grid_selector).jqGrid('navGrid',pager_selector,{});//navButtons
		
	
}


/*function style_edit_form(form) {
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
}*/


//var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');


/**
 * 通用方法 加载下拉框
 */
/*function querywarnkinds(unid, url){
	$.ajax({
		type: "POST",
		url: url,
		dataType: "json",
		cache: false,
		success: function(msg){
//			console.log(msg);
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
*//**
 * 如果角色是家长，通过此方法查询学生所在班级
 *//*
function queryStuInfo_class(url){
	var banji = "";
	$.ajax({
		type: "POST",
		url: url,
		dataType: "json",
//		cache: false,
		data:'formInfo', 
		async:false, 
		success: function(msg){
			if(msg != null && msg.length != 0){
				if(msg.flg == '1'){
					var strs= new Array();
					strs = msg.value.split(",");
					banji = "班级： " + strs[0];
					$("#classId_toStu").val(strs[1]);
				}else if(msg.flg == '2'){
					banji = '班级： 暂无班级信息';
				}
			}else{
				return;
			}
	    }
	});
	return banji;
}*/
/**
 * 选择班级下拉框后通过此方法查询考试科目
 *//*
function queryKaoShiSub(_this){
	var classValue = $(_this).val();
//	console.log(classValue);
	$("#classId_toStu").val(classValue);
	if("" != classValue && null != classValue){
		querywarnkinds('form-field-2','scoreManage.htm?querySub_&status=2&classId=' + classValue);
	}else if("" == classValue || null == classValue){
		$("#form-field-2").empty();
		var cityoption = "<option value=''>--请选择--</option>";
		$("#form-field-2").append(cityoption);
	}
}
*/

///**
// * 
// * @param cellvalue
// * @param options
// * @param rowObject
// * @returns {String}
// * 学生成绩加载时为成绩修改加载按钮事件
// */
//function checkPicture(cellvalue, options, rowObject){
//	
//	return "<a href='javascript:void(0)' title='编辑' class='btn btn-warning btn-xs' " +
//			"onclick='finalTest(\""+ rowObject.id +"\")'><i class='icon-wrench bigger-110 icon-only'></i></a>";
//	
//}
///**
// * 跳转到相册页面
// * @param stuId
// */
//function finalTest(stuId){
//	 $.dialogACE("成绩修改","scoreManage.htm?queryStuToUpdate_&cjManageId=" + stuId,function(id){
//		$("#subFormToUpdate").submit();
//		$(id).dialog( "destroy" );
//	}); 
//}
/**
 * 查询按钮事件
 *//*
function  onclicksearch(){
		//获取班级ID 从隐藏域中
		var classId = $("#classId_toStu").val();
		if('2' == userType_js){
		//考试科目
		var kaoshiSub = $("#form-field-2 option:selected").val();
		//考试内容
		var kaoshinr = $.trim($("#form-field-icon-1").val());
		
		if("" == classId || null == classId){
			alert("请选择班级!");
			return;
		}
		if("" == kaoshiSub || null == kaoshiSub){
			alert("请选择考试科目!");
			return;
		}
		if("" == kaoshinr || null == kaoshinr){
			alert("请选择考试内容!");
			return;
		}
		
		if("" != classId && "" != kaoshiSub && "" != kaoshinr){
			var newKaoshinr = encodeURI(encodeURI(kaoshinr));
			var query_url1 = 'scoreManage.htm?queryClass_1&classId='+ classId +'&kaoshiSub=' + kaoshiSub + '&kaoshinr=' + newKaoshinr + '&status=2';
			jQuery('#grid-table').jqGrid('setGridParam',{url: query_url1}).trigger("reloadGrid");
		}
	}else if('1' == userType_js){
		var query_url = 'scoreManage.htm?queryClass_1&status=1&classId=' + classId;
		jQuery('#grid-table').jqGrid('setGridParam',{url: query_url}).trigger("reloadGrid");
	}
}*/
///**
// * 点击后跳转到成绩新增页面
// */
//function onclickAdd(){
//	$("#studentByClassMess").css('display','none');
//	$("#addStuChengJi").css('display','block');
//	$.hmqRefreshPage("addStuChengJi","scoreManage.htm?addStuScore_&classId=" + $("#classId_toStu").val());
//}



//-------------------------------------------------------------------------------------------------------




/**
 * 点击后跳转到新的查询页面
 */
/*function onclickQuery(){
	$.hmqHomePage("menuPage.htm?queryCJByclass&zrflg=2");
}*/

