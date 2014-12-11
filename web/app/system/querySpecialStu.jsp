<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script src="./res/ace/assets/js/select2.min.js"></script>	
<script type="text/javascript">
	var gridurl = './systemPage.htm?querySpecialStudent';
    var grid_selector = "#grid-table-tc";
	var pager_selector = "#grid-query-pager";
	$(function(){
		
		$(".select2").css('width','180px').select2({allowClear:true})
		.on('change', function(){
			$(this).closest('form').validate().element($(this));
		}); 
		
		$('body').resize(function(){   
		    $("#grid-table-tc").setGridWidth($("#queryStudentInfo").width() - 8,true);
	    });
		$(grid_selector).jqGrid({
			//direction: "rtl",
			url:gridurl,
			datatype: "json",
			mtype : "post",
			height: 255,
			colNames:['编号','学生姓名','所在班级', '性别','电话号码', '地址','删除原因'],
			colModel:[
				{name:'id',index:'id', width:60,hidden:true, sorttype:"int",sortable:false, editable: false},
				{name:'name',index:'name', width:70,sortable:false,editable: false},
				{name:'orgName',index:'orgName', width:70,sortable:false, editable: false},
				{name:'sex',index:'sex', width:50,sortable:false,editable: false,edittype:"select",editoptions:{value:"0:保密;1:男;2:女"},
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
				{name:'tel',index:'tel', width:70, sortable:false,editable: false},
				{name:'address',index:'address', width:60, sortable:false,editable: false},
				{name:'type',index:'type', width:50,sortable:false,editable: false,edittype:"select",editoptions:{value:"0:保密;1:男;2:女"},
					formatter:function(cellvalue, options, rowObject){
						var temp = "";
						if(cellvalue == 1){
							temp = "转学";
						} else if(cellvalue == 2){
							temp = "退学";
						} else if(cellvalue == 3){
							temp = "休学";
						} else if(cellvalue == 4){
							temp = "辞退";
						} else if(cellvalue == 5){
							temp = "其他";
						}
						return temp;
					}
				}
			], 

			viewrecords : true,
			rowNum:10,
			rowList:[10,20,30],
			pager : pager_selector,
			altRows: true,
			//toppager: true,
			
			multiselect: false,
			//multikey: "ctrlKey",
	        multiboxonly: false,
			loadComplete : function() {
				setTimeout(function(){
					updatePagerIcons(this);
				}, 0);
			},

			autowidth: true

		});

		
		});



	function onclicksearch(){
		//var url = jQuery(grid_selector).jqGrid('getGridParam','url');
		var stuname = $.trim($("#stuName").val());
		var stureason = $("#stuReason option:selected").val();
		jQuery(grid_selector).jqGrid('setGridParam', {
               postData:{'stuName':stuname,'reason':stureason} //发送数据
           }).trigger("reloadGrid"); //重新载入
	}

</script>
								<!-- PAGE CONTENT BEGINS -->
								<div id="queryStudentInfo">

										<div class="row">
											<div class="col-xs-12">
												<div class="widget-box">
												<div class="widget-header widget-header-small">
												<h5 class="lighter">学生信息查询</h5>
												</div>
												<div class="widget-body">
												<div class="widget-main">
												<form class="form-search" onsubmit="return false;">
												<div class="row">
												<div class="col-xs-12 col-sm-12">
												<div class="input-group">
															姓名：<span class="input-icon">
															<input id="stuName" type="text">
															<i class="icon-leaf blue"></i>
															</span>
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															
															删除类别：
															<span> <select id="stuReason" class="select2
																 onchange="queryKaoShiSub(this)">
																<option value="0">--请选择--</option>
																<option value="1">转学</option>
																<option value="2">退学</option>
																<option value="3">休学</option>
																<option value="4">辞退</option>
																<option value="5">其他</option>
															</select>
															</span>
														<span
													class="input-group-btn">
														<button type="button" class="btn btn-purple btn-sm" onclick="onclicksearch()">查询 <i
													class="icon-search icon-on-right bigger-100"></i></button>
														</span>
												</div>
												</div>
												</div>
												</form>
												</div>
												</div>
												</div>
												<div class="widget-box">
													<div class="widget-header widget-header-flat  header-color-green2">
														<h4 id="gridtitle" class="smaller">学生详情</h4>
													</div>

													<div class="widget-body" >
														<table id="grid-table-tc"></table>
														<div id="grid-query-pager"></div>
														
													</div>
												</div>
											</div>

									</div>
								</div><!-- PAGE CONTENT ENDS -->
