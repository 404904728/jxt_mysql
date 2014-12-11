<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="./res/ace/assets/js/select2.min.js"></script>
<script type="text/javascript">
	var $path_base = "/";//this will be used in gritter alerts containing images
	/** 登录用户类型 1 家长 2 */
	var userType_js = "${userType}";
	var jzclassid = "${jzclassid}";
	$(document).ready(function(){
		$(".select2").css('width','180px').select2({allowClear:true})
		.on('change', function(){
			$(this).closest('form').validate().element($(this));
		}); 

		var  a = ['id','标题', '学号','姓名','内容','总成绩','班排','班排涨幅','年排','年排涨幅'];
		var  b = [	 {name:'id',index:'id',align:'left',hidden:true, width:120,editable: false,sortable:false},
		          	 {name:'title',index:'title',  align:'left', width:300,editable: false,sortable:false},
				     {name:'no',index:'no',align:'left', width:120,editable: false,sortable:false},
				     {name:'name',index:'name',align:'left', width:80,editable: false,sortable:false},
				     {name:'scoreA',index:'scoreA',align:'left', width:400,editable: false,sortable:false},
				     {name:'totalScore',index:'totalScore',align:'left', width:80,editable: false},
				     {name:'classOrder',index:'classOrder',align:'left', width:80,editable: false,sortable:false},
				     {name:'classAsc',index:'classAsc',align:'left', width:80,editable: false,sortable:false},
				     {name:'drandOrder',index:'drandOrder',align:'left', width:80,editable: false,sortable:false},
				     {name:'drandAsc',index:'drandAsc',align:'left', width:80,editable: false,sortable:false}
					];
		
		var idValue = "";
		var beginUrl = '';

		if('1' == userType_js){
			if(jzclassid){
				beginUrl = 'scoreManage.htm?queryClass_1&status=1&classId=' + jzclassid;
			}
		}else if('2' == userType_js){	//教师登录处理
			hsFlg = false;
			querywarnkinds('form-field-select-1','scoreManage.htm?classinfo_');
		}
		
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
			onSelectRow: function(id) {
				idValue = id;
			},
			autowidth: true,
			loadComplete : function() {
				var table = this;
				setTimeout(function(){
					updatePagerIcons(table);
					enableTooltips(table);
				}, 0);
			}

		});
			jQuery(grid_selector).jqGrid('navGrid',pager_selector,{});//navButtons
	});
	
    $('body').resize(function(){   
	    $("#grid-table").setGridWidth($("#studentscoregrid").width() - 8,true);
    });
	
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
	/**
	 * 如果角色是家长，通过此方法查询学生所在班级
	 */
	function queryStuInfo_class(url){
		var banji = "";
		$.ajax({
			type: "POST",
			url: url,
			dataType: "json",
//			cache: false,
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
	}
	
	/**
	 * 点击后跳转到新的查询页面
	 */
	function onclickQuery(){

			$.dialogACE({
				title:'查询条件',
//				url:'./app/cjmanage/cjquerycondtion.jsp',
				div:'studentImage',
				width:500,
				height:165,
				callBack:function(id){
					onclicksearch();
					$(id).dialog("destroy");
				}
			});
		
	}
	
	/**
	 * 查询按钮事件
	 */
	function  onclicksearch(){
			//获取班级ID 从隐藏域中
			var classId = $("#classId_toStu").val();
			
			if('2' == userType_js){
			//考试科目
			var kaoshiSub = $("#form-field-2 option:selected").val();
			//考试内容
			/* var kaoshinr = $.trim($("#form-field-icon-1").val()); */
			
			if("" == classId || null == classId){
				alert("请选择班级!");
				return;
			}
			if("" == kaoshiSub || null == kaoshiSub){
				alert("请选择对应学生!");
				return;
			}
			/* if("" == kaoshinr || null == kaoshinr){
				alert("请选择考试内容!");
				return;
			} */
			
			if("" != classId && "" != kaoshiSub){
				//var query_url1 = 'scoreManage.htm?queryClass_1&classId='+ classId +'&kaoshiSub=' + kaoshiSub + '&kaoshinr=' + newKaoshinr + '&status=2';
				var query_url1 = 'scoreManage.htm?queryClass_2&classId='+ classId + '&name=' + encodeURIComponent(kaoshiSub);
				
				jQuery('#grid-table').jqGrid('setGridParam',{url: query_url1}).trigger("reloadGrid");
			}
		}else if('1' == userType_js){
			var query_url = 'scoreManage.htm?queryClass_1&status=1&classId=' + classId;
			jQuery('#grid-table').jqGrid('setGridParam',{url: query_url}).trigger("reloadGrid");
		}
	}
	
	/**
	 * 选择班级下拉框后通过此方法查询班级学生姓名
	 */
	function queryKaoShiSub(_this){
		var classValue = $(_this).val();
//		console.log(classValue);
		$("#classId_toStu").val(classValue);
		if("" != classValue && null != classValue){
			querywarnkinds('form-field-2','scoreManage.htm?querySub_&status=2&classId=' + classValue);
		}else if("" == classValue || null == classValue){
			$("#form-field-2").empty();
			var cityoption = "<option value=''>--请选择--</option>";
			$("#form-field-2").append(cityoption);
		}
	}
	
function callbackchmanage(){
	$.hmqHomePage("menuPage.htm?cjManageInfoPage");
}

function gotoChart(){
	if(jzclassid){
		$("#lookscorechart").show();
		$("#studentByClassMess").hide();
		setTimeout(function(){
		    $.ajax({
		        url: "./scoreManage.htm?analyCjByStudentNameAndOrg",
		        cache: false,
		        dataType: "json",
		        type: "POST",
		        contentType: "application/x-www-form-urlencoded;charset=utf-8",
		        success: function(data) {
					$('#container').highcharts({
					        title: {
					            text: '近期月考成长曲线',
					            x: -20
					        },
					        xAxis: {
					            categories: data.x
					        },
					        tooltip: {
					            formatter: function(event) {
					                    return '<b>'+this.series.name+'</b><br/>'+
					                       this.point.name +'<br/>'+
					                       '排名：'+this.y;
					            }
					        },
					        yAxis: {
					            min:1,
					            reversed:true,
					            title: {
					                text: '年级排名'
					            }
					        },
					        series: data.y
					    });
		       }
		    });
		},500);
	}
}

</script>	
		<div id="studentByClassMess">
			<div class="alert alert-block alert-success">
				<input type="hidden" id="classId_toStu" value="">
				<p>
								<c:if test="${userType == '1'}">
									<div class="btn-toolbar" style="height: 30px">
										<div class="btn-group">
											<button class="btn btn-info btn-sm dropdown-toggle" onclick="gotoChart()" type="button">成绩图表
												<i class="icon-credit-card icon-on-right bigger-110"></i>
											</button>
										</div>
									</div>
								</c:if>
								
								<c:if test="${userType == '2'}">
									<div class="btn-toolbar" style="height: 30px">
										<div class="btn-group">
											<button class="btn btn-info btn-sm dropdown-toggle" onclick="onclickQuery()" type="button">详细成绩查询
												<i class="icon-search icon-on-right bigger-120"></i>
											</button>
										</div>
										<c:if test="${userType == '2' || userType == 2}">
										<!-- 0是指任课教师  不应该有返回按钮 -->
											<c:if test="${zrflg != '0' || zrflg != 0}">
												<div class="btn-group">
													<button class="btn btn-sm" onclick="callbackchmanage()" type="button">返回
														<i class="icon-reply icon-on-right bigger-110"></i>
													</button>
												</div>
											</c:if>
										</c:if>
									</div>
								</c:if>
				</p>
			</div>
			
			<div id="studentscoregrid" class="widget-box">
				<div class="widget-header widget-header-flat header-color-blue">
					<h4 class="smaller">成绩详情</h4>
				</div>
				<div class="widget-body" style="height: 498px">
					<table id="grid-table"></table>
					<div id="grid-pager"></div>
				</div>
			</div>
	
			</div>
			<div id="studentImage" class="hide">
				<div class="row">
					<div class="col-xs-12">
						<form class="form-horizontal">
							<c:if test="${userType == '2'}">
								<div class="form-group">
									<label class="col-sm-3 control-label no-padding-right">班级:</label>
									<div class="col-sm-9">
										<span> <select id="form-field-select-1" class="select2
											data-placeholder="选择..." onchange="queryKaoShiSub(this)">
										</select>
										</span>
									</div>
								</div>
							</c:if>
							<div class="hr hr-18 dotted"></div>
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right">学生姓名:</label>
								<div class="col-sm-9">
									<span> 
										<select id="form-field-2" class="select2">
											<option value="">--请选择--</option>
										</select>
									</span>
								</div>
							</div>	
			
							<div class="hr hr-18 dotted"></div>
						</form>
					</div>
				</div>

</div>
			<div id="lookscorechart" style="display:none;" class="widget-box">
					<div class="alert alert-block alert-success">
						<div class="btn-toolbar" style="height: 30px">
						<!-- <h4 class="blue">
							<i class="orange icon-credit-card bigger-110"></i>
							学生成绩统计
						</h4> -->
							<div class="btn-group">
								<button class="btn btn-sm" onclick="callbackchmanage()" type="button">返回
									<i class="icon-reply icon-on-right bigger-110"></i>
								</button>
							</div>
						</div>
					</div>
					<div class="widget-header widget-header-flat header-color-blue">
						<h4 class="smaller">学生成绩统计</h4>
					</div>
					<div class="widget-body" style="height: 498px">
						<div class="space-8"></div>
						<div id="container"></div>
					</div>
			</div>
