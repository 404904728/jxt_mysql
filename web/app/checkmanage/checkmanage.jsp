<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="./res/ace/assets/js/select2.min.js"></script>	
		<script type="text/javascript">
		var g_classid;
			 $('body').resize(function(){   
		    	   $("#grid-table").setGridWidth($("#checkmanagegrid").width() - 8,true);
	   		 });
			var type = "${type}";
				$(".select2").css('width','180px').select2({allowClear:true})
				.on('change', function(){
					$(this).closest('form').validate().element($(this));
				}); 

				
				var colnam = ['编号','标题','考勤教师','实到人数', '未到人数', '总数','出勤率','录入时间'];
				var colmode = [
							    {name:'id',index:'id',sortable:false, sorttype:"int", editable: false, hidden:true},
								{name:'title',index:'title', width:150,editable: false,editoptions:{size:"20",maxlength:"30"},sortable:false},
								{name:'tea.name',index:'tea.name', width:100,sortable:false,editoptions:{size:"20",maxlength:"30"}},
								{name:'hadCount',index:'hadCount',sortable:false,width:80, editable:false, sorttype:"date"},
								{name:'unCount',index:'unCount',sortable:false, width:80,editable: true},
								{name:'total',index:'total',sortable:false, width:80,editable: false,formatter:function(cellvalue, options, rowObject){
									return rowObject.hadCount+rowObject.unCount;
								}},
								{name:'attendance', index:'attendance', width:60, sorttype:"int",sortable:false, editable: false,formatter:function(cellvalue, options, rowObject){
									return cellvalue +'%';
								}},
								{name:'checkDate',index:'checkDate', width:150,sortable:false,editable: false}
							];
				var grid_selector = "#grid-table";
				var pager_selector = "#grid-pager";
				var url = './checkManage.htm?findCheckManageInfos';
				if(type == '1'){
					url = './checkManage.htm?queryStuCheck';
					colnam = ['编号','标题','录入时间','出勤情况'];
					var colmode = [
								    {name:'id',index:'id',sortable:false, sorttype:"int", editable: false, hidden:true},
									{name:'title',index:'title', width:150,editable: false,editoptions:{size:"20",maxlength:"30"},sortable:false},
									{name:'checkDate',index:'checkDate', width:150,sortable:false,editable: false},
									{name:'qk',index:'qk', width:150,sortable:false,editable: false}
								];
					
				}
				jQuery(grid_selector).jqGrid({
					url:url,
					datatype: "json",
					height: 400,
					colNames:colnam,
					colModel:colmode, 
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
					onSelectRow: function(id) {
					},
					ondblClickRow: function(id){
						if(type == '2'){
							$("#chckmain").css('display','none');
							$("#addCheckJsp").css('display','block');
							$.hmqRefreshPage("addCheckJsp","checkManage.htm?queryCheckByClass_&checkId=" + id);
						}else{
							return;
						}
					},
					autowidth: true

				});
				jQuery(grid_selector).jqGrid('navGrid',pager_selector,{});

				if(type == '2'){
					jQuery(grid_selector).jqGrid('navButtonAdd',pager_selector,
							{ 	//navbar options
								caption:"",
								buttonicon:"ui-icon icon-zoom-in grey",
								position:"first",
								title:"新增考勤",
								onClickButton:function(){
									var classId = $("#c_select option:selected").val();
									if(classId == ""){
										bootbox.alert("请选择班级后在新增考勤");
										return;
									}
									$("#chckmain").css('display','none');
									$("#addCheckJsp").css('display','block');
									$.hmqRefreshPage("addCheckJsp","checkManage.htm?initAddCheck_&classId=" + classId);
								}
							}
						);
					jQuery(grid_selector).jqGrid('navButtonAdd',pager_selector,
							{ 	//navbar options
								caption:"",
								buttonicon:"ui-icon icon-bar-chart",
								position:"second",
								title:"统计",
								onClickButton:function(){
								if(g_classid){
									$("#chckmain").css('display','none');
									$("#chckanaly").css('display','block');
									$.hmqRefreshPage("chckanaly","./checkManage.htm?checkanaly");
								}else{
									bootbox.alert("请先选择班级！");
									return;
								}
							  }
							}
						);
				}

		function queryCheckByClass(v){
			var newUrl = "";
			 if(v){
				 // url += "&classId="+v
				  g_classid = v;
				  newUrl = url+"&classId="+v;
			  }else{
			    g_classid = null;
			    newUrl = url;
			  }
			  jQuery(grid_selector).jqGrid('setGridParam',{url: newUrl}).trigger("reloadGrid");
		}
		</script>
		<div id="chckmain">
			<div class="alert alert-info" style="height: 50px;">
				<form class="form-horizontal" role="form" onsubmit="return false;">
					<div class="form-group" id="classMessage">
							<div class="col-sm-9">
								<span> 班级:&nbsp;&nbsp;
								<c:if test="${type == '2'}">
									<select id="c_select" class="select2"
										data-placeholder="选择..." onchange="queryCheckByClass(this.value)">
										<option value="">&nbsp;</option>
										<c:forEach items="${banjis}" var="cls">
											<option value="${cls.org.id}">${cls.org.name}</option>
										</c:forEach>
									</select>
								</span>
								</c:if>
								<c:if test="${type == '1'}">
									<span>${banjiName}</span>
								</c:if>
								
							</div>
						</div>
				</form>
			</div>
			
			<div id="checkmanagegrid" class="widget-box">
				<div class="widget-header widget-header-small  header-color-green">
					<h4 class="smaller">考勤详情</h4>
				</div>
				<div class="widget-body" style="height: 498px">
					<table id="grid-table"></table>
					<div id="grid-pager"></div>
				</div>
			</div>
		</div>	
		<div id="addCheckJsp" style="display: none;"></div>
		<div id="chckanaly" style="display:none"></div>
