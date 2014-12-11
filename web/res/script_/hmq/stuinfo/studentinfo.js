$(document).ready(function(){
	
		$(".select2").css('width','180px').select2({allowClear:true})
		.on('change', function(){
			$(this).closest('form').validate().element($(this));
		}); 
	
	var beginUrl = 'stuinfo.htm?query_1&status=1';
	if('1' == userType_js){
		/*$("#classMessage").css('display','none');
		var stu_to_calss_1 = queryStuInfo_class('stuinfo.htm?StuClassinfo_');
		$("#home_message").html('<i class="icon-hand-right"/> ' + stu_to_calss_1);
		$("#home_message").css('display','block');*/
		
		$("#studentByClassMess").css('display','none');
		$("#readStuMeaasge").css('display','block');
		$.hmqRefreshPage("readStuMeaasge","stuinfo.htm?queryOneStuInfos_&stuId=" + idValue);
	}else if('2' == userType_js || '0' == userType_js){
		$("#classMessage").css('display','block');
		$("#home_message").css('display','none');
		querywarnkinds('form-field-select-1','stuinfo.htm?classinfo_');
		beginUrl = 'stuinfo.htm?query_1&status=2';
	}
	
	var idValue = "";
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
	jQuery(grid_selector).jqGrid({
		url:beginUrl,
		datatype: "json",
//		autoencode:true,
//		loadonce:false,
		//     {"A卷成绩":95.0,"B卷成绩":38.0,"年排":60.0,"年排涨幅":196.0"}
//		data: grid_data,
//		datatype: "local",
		height: 400,
		colNames:['操作', '学籍号', '姓名', '性别','家长姓名','生日', '职务', '身份证号','联系方式','','相册'],
		colModel:[
			{name:'myac',index:'', width:80, fixed:true, sortable:false, resize:false,hidden:true,
				formatter:'actions',
				formatoptions:{
					keys:true,
					delbutton:false,
					delOptions:{recreateForm: true, beforeShowForm:beforeDeleteCallback},
					//editformbutton:true, editOptions:{recreateForm: true, beforeShowForm:beforeEditCallback}
				}
			},
			{name:'studentCode',index:'studentCode',hidden:true,align:'center', width:150,editable: true,editoptions:{size:"20",maxlength:"30"},sortable:false},
			{name:'name',index:'name', width:100,editable: true,editoptions:{size:"20",maxlength:"30"}},
			{name:'sex',index:'sex', width:50,sortable:false,editable: true,edittype:"select",editoptions:{value:"0:保密;1:男;2:女"},
				formatter:function(cellvalue, options, rowObject){
					var temp = "男";
					if(cellvalue == 1){
						temp = "男";
					} else if(cellvalue == 2){
						temp = "女";
					} else if(cellvalue == 0){
						temp = "保密";
					}
					return temp;
				}
			},
			{name:'parentName',index:'parentName',sortable:false, width:150,editable: true,editoptions:{size:"20",maxlength:"30"}},
			{name:'birthday',index:'birthday',sortable:false,width:90, editable:true, sorttype:"date",unformat: pickDate},
			{name:'dutyPosition',index:'dutyPosition',hidden:true,sortable:false, width:150,editable: true,editoptions:{size:"20",maxlength:"30"}},
			{name:'idCardNo',index:'idCardNo',sortable:false, width:150,editable: true,editoptions:{size:"20",maxlength:"30"}},
			{name:'no',index:'no', width:60,sortable:false, sorttype:"int", editable: true},
			{name:'id',index:'id', width:60,sortable:false, sorttype:"int", editable: true,hidden:true,},
			{name:'xc',hidden:true, width:60, sorttype:"int",sortable:false, editable: false,formatter:checkPicture}
		], 
		viewrecords : true,
		rowNum:10,
		rowList:[10,20,30],
		pager : pager_selector,
		altRows: true,
		loadComplete : function() {
			var table = this;
			setTimeout(function(){
				updatePagerIcons(table);
				enableTooltips(table);
			}, 0);
		},
		editurl: "stuinfo.htm?oneStu_",
		onSelectRow: function(id) {
			idValue = id;
		},
		autowidth: true

	});
	
		jQuery(grid_selector).jqGrid('navGrid',pager_selector,{});//navButtons
		if(userType_js != 1){
			jQuery(grid_selector).jqGrid('navButtonAdd',pager_selector,
				{ 	//navbar options
					caption:"",
					buttonicon:"ui-icon icon-pencil blue",
					position:"first",
					title:"信息修改",
					onClickButton:function(){
						if(null == idValue || "" == idValue){
							alert("请选择修改的行!");
						}else{
							
							
							$.dialogACE({
								title:'信息修改',
								url:'stuinfo.htm?queryStuToUpdate_&stuId=' + idValue,
//								div:'studentImage',
								width:700,
//								height:400,
								callBack:function(id){
									var stuName = $("#name").val();
									if("" == stuName){
										alert("姓名不能为空");
										return;
									}
									var no = $("#no").val();
									if("" == no){
										alert("联系方式不能为空");
										return;
									}
									
									var parentName = $("#parentName").val();
									if("" == parentName){
										bootbox.alert("家长姓名不能为空");
										return;
									}
									var sex = $("#sex option:selected").val();
									if("" != stuName){
										$.ajax({
											type: "POST",
											dataType: "json",
											cache: false,
											url: 'stuinfo.htm?oneStu_&id=' + $("#id").val() + '&name=' + encodeURI(encodeURI(stuName)) + '&parentName=' + encodeURI(encodeURI(parentName)) + '&sex=' + sex + '&no=' + no,
											dataType: "json",
											cache: false,
											success: function(data){
												if(data != null && data.length != 0){
													alert(data.msg);
													jQuery('#grid-table').jqGrid('setGridParam').trigger("reloadGrid");
												}
										    }
										});
										$(id).dialog("destroy");
									}
//									$(id).dialog("destroy");
								}
							});
							
							
							
//							
//							$.dialogACED("stuinfo.htm?queryStuToUpdate_&", function(id){
//								$("#subFormToUpdate").submit();
//								$(id).dialog("destroy");
//							},"信息修改","stuId=" + idValue);
//							
							
							
						}
					}
				}
			);
		}
		jQuery(grid_selector).jqGrid('navButtonAdd',pager_selector,
				{ 	//navbar options
					caption:"",
					buttonicon:"ui-icon icon-zoom-in grey",
					position:"first",
					title:"信息查看",
					onClickButton:function(){
						if(null == idValue || "" == idValue){
							alert("请选择查看详细的学生!");
						}else{
							if('1' == userType_js){
								$("#studentByClassMess").css('display','none');
								$("#readStuMeaasge").css('display','block');
								$.hmqRefreshPage("readStuMeaasge","stuinfo.htm?queryOneStuInfos_&stuId=" + idValue);
							}else{
								$("#studentByClassMess").css('display','none');
								$("#readStuMeaasge").css('display','block');
								$.hmqRefreshPage("readStuMeaasge","stuinfo.htm?queryOneStuInfostotea_&stuId=" + idValue);
							}
						}
					}
				}
			);
	
	
//		
	
	
	
});



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


//var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');


/**
 * 如果是教师角色，通过此方法加载班级下拉框
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
				var cityoption = "<option value='' selected='selected'></option>";
				for(var len = 0; len < msg.length; len++){
						var typeId = msg[len].id;
						var typeName = msg[len].name;
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
/**
 * 如果角色是家长，通过此方法查询学生所在班级
 */
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
					banji = "班级： " + msg.value;
				}else if(msg.flg == '2'){
					banji = '班级： 暂无班级信息';
				}
			}else{
				return;
			}
	    }
	});
	return banji;
}
/**
 * 教师根据选择班级查看出班级中所有学生信息
 */
function queryStuByClass(){
	var classId = $("#form-field-select-1 option:selected").val();
	if('' != classId && null != classId){
		var query_url = 'stuinfo.htm?query_1&status=2&classid=' + classId;
		jQuery('#grid-table').jqGrid('setGridParam',{url: query_url}).trigger("reloadGrid");
	}else if(typeof(classId) == "undefined"){
		return;
	}else{
		var query_url = 'stuinfo.htm?query_1&status=2';
		jQuery('#grid-table').jqGrid('setGridParam',{url: query_url}).trigger("reloadGrid");
	}
}


/**
 * 
 * @param cellvalue
 * @param options
 * @param rowObject
 * @returns {String}
 * 学生信息加载时在表格里添加查看相册超链接
 */
function checkPicture(cellvalue, options, rowObject){
	
	return "<a href='javascript:void(0)' onclick='finalTest(\""+ rowObject.id +"\")'>查看相册</a>";
	
}
/**
 * 跳转到相册页面
 * @param stuId
 */
function finalTest(stuId){
	$("#studentByClassMess").css('display','none');
	$("#studentImage").css('display','block');
	$.hmqRefreshPage("studentImage","stuinfo.htm?queryImage_&stuId=" + stuId);
}

