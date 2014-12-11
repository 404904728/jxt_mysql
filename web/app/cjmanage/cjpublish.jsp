<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<script src="./res/ace/assets/js/select2.min.js"></script>
		<!-- ace scripts -->

		<script type="text/javascript">
			var bjzr =  "${bjzr}";
			var title =  "${title}";
			var njzr = 1;
			function importexcel(){
				var url = "scoreManage.htm?excelfileupload";
				if(bjzr){
					url+= "&bjzr="+bjzr;
				}
				if(njzr){
					url+= "&njzr="+njzr;
				}
				$.dialogACE({
					url:url,
					title:"导入成绩",
					frame:true,width:500,height:300,
					frameId:'importexcel',
					callBack:function(dialog){
						var scoretype = $("#importexcel").contents().find("input[name='scoretype']:checked").val();
						if(!scoretype){
							alert("请先选择考试类型");
							return;
						}
						var usertype = $("#importexcel").contents().find("input[name='usertype']:checked").val();
						if(!usertype){
							if(bjzr){
								usertype = bjzr;
							}else if(njzr){
								usertype = njzr;
							}
						}
						var attachId=$("#importexcel")[0].contentWindow.attachId;
						if(attachId==null){
							alert("请先上传数据文件");
							return;
						}
						$("div[aria-describedby='myDialogAce'").append('<div class="message-loading-overlay"><i class="icon-spin icon-spinner orange2 bigger-160"></i></div>');
						//setTimeout(function(){
							//$("div[aria-describedby='myDialogAce'").find('.message-loading-overlay').remove();
							//$(dialog).dialog("destroy");
							//alert("上传成功！");
						//	},3000)
						 
					    $.ajax({
					        url: "./scoreManage.htm?importScoreXls&attachID="+attachId+"&scoreType="+scoretype+"&userType="+usertype,
					        cache: false,
					        dataType: "json",
					        type: "POST",
					        timeout: 600000,
					        contentType: "application/x-www-form-urlencoded;charset=utf-8",
					        success: function(data) {
						        if(scoretype == 1 || scoretype == '1'){
										if((data.type = 0 || data.type == '0') && data.msgId){
										    $.ajax({
										        url: "./scoreManage.htm?paimingByweek&str="+encodeURIComponent(data.msgId),
										        cache: false,
										        dataType: "json",
										        type: "POST",
										        timeout: 600000,
										        contentType: "application/x-www-form-urlencoded;charset=utf-8",
										        success: function(data) {
										        	colseDialog(data,dialog);
										        	jQuery(grid_selector).jqGrid('setGridParam').trigger("reloadGrid");
										       }
										    });
										}else{
											colseDialog(data,dialog);
											jQuery(grid_selector).jqGrid('setGridParam').trigger("reloadGrid");
										}
						        }else{
						        	colseDialog(data,dialog);
						        	jQuery(grid_selector).jqGrid('setGridParam').trigger("reloadGrid");
						        }
					       }
					    });
					}
				})	
			}

			function colseDialog(data,dialog){
	        	 $("div[aria-describedby='myDialogAce'").find('.message-loading-overlay').remove();
		         alert(data.msg);
           		 $(dialog).dialog("destroy")
			}
			
			var grid_selector = "#igrid-table";
			var pager_selector = "#igrid-pager";
			var colname = ['编号','考试标题','班级','姓名','内容','总成绩','班排','年排','评语','状态'];
			$(grid_selector).jqGrid({
				url:'./scoreManage.htm?findAllUnPublishScoreByClassCode&classId=' + bjzr + "&title=" + encodeURIComponent(title),
				datatype: "json",
				height: 360,
				colNames:colname,
				colModel:[
					{name:'id',index:'id', hidden:true, sorttype:"Long",sortable:false, editable: false},
					{name:'title',index:'title', hidden:false,width:300, sortable:false, editable: false},
					{name:'classCode',index:'classCode',editable: false,width:100},
					{name:'name',index:'name', hidden:false,width:100, sortable:false, editable: false},
					{name:'scoreA',index:'scoreA', hidden:false,width:460, sortable:false, editable: false},
					{name:'totalScore',index:'totalScore', hidden:false,width:80, sortable:false, editable: false},
					{name:'classOrder',index:'classOrder', hidden:false, sorttype:"Long",sortable:false, editable: false},
					{name:'drandOrder',index:'drandOrder', hidden:false,width:80, sortable:false, editable: false},
					{name:'bzrComments',index:'bzrComments', hidden:false,width:150, sortable:false, editable: false},
					{name:'publishStatus',index:'publishStatus', hidden:true,width:80, sortable:false, editable: false,formatter:function(cell,options,rowdata){
						if(cell == 1){
							return '已发布';	
						}else{
							return '未发布';	
						}
					}}
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
				onSelectRow: function(id,rowdata) {
					$("#fabustatus").val(id);
				},
				autowidth: true

			});
			jQuery(grid_selector).jqGrid('navGrid',pager_selector,{});
			
			 $('body').resize(function(){   
		    	   $("#igrid-table").setGridWidth($("#studentscoregrid").width() - 8,true);
	   		 });
			 

			/**
			 * 点击后跳转到新的查询页面
			 */
			function onclickQuery(){
				$.hmqHomePage("menuPage.htm?queryCJByclass&zrflg=2");
			}


			$("#bootbox-confirm").on(ace.click_event, function() {
				if(jQuery(grid_selector).jqGrid('getGridParam','records') < 1){
					bootbox.alert("无成绩需要发布!");
					return false;
				}
				bootbox.confirm("确定要发布吗？", function(result) {
					if(result) {
						fabuScore();
					}
				});
			});
			
			function fabuScore(){
					$("#studentByClassMess").append('<div class="message-loading-overlay"><i class="icon-spin icon-spinner orange2 bigger-160"></i></div>');
					$.ajax({
						type: "POST",
						url: 'scoreManage.htm?updateAnnounceStatus_&id=' + bjzr + '&title=' + encodeURIComponent(title),
						dataType: "json",
						cache: false,
						success: function(root){
							if(root == null){
								bootbox.alert("系统异常,请稍后在试!");
								$("#studentByClassMess").find('.message-loading-overlay').remove();
							}
							if(root.type == 0 || root.type == '0'){
								if(null == root.msg || "" == root.msg){
									bootbox.alert("发布成功!");
								}else{
									bootbox.alert(root.msg);
								}
								jQuery(grid_selector).jqGrid('setGridParam').trigger("reloadGrid");
								$("#studentByClassMess").find('.message-loading-overlay').remove();
							}
							if(root.type == 3 || root.type == '3'){
								bootbox.alert("数据异常,请联系管理员");
								$("#studentByClassMess").find('.message-loading-overlay').remove();
							}
							if(root.type == 100 || root.type == '100'){
								bootbox.alert(root.msg);
								$("#studentByClassMess").find('.message-loading-overlay').remove();
							}
							if(root.type == 200 || root.type == '200'){
								bootbox.alert(root.msg);
								$("#studentByClassMess").find('.message-loading-overlay').remove();
							}
							if(root.type == 300 || root.type == '300'){
								bootbox.alert(root.msg);
								$("#studentByClassMess").find('.message-loading-overlay').remove();
							}
						}
					});
			}

	$("#bootbox-regular").on(ace.click_event, function() {
		var id = $("#fabustatus").val();
		if("" == id || null == id){
			bootbox.alert("请先选择学生后添加评语");
			return;
		}

		bootbox.prompt("请输入您的评语：", function(result) {
			if (result === null) {
			} else {
				if(result.length > 10){
					alert("输入内容长度不能大于10");
					return false;
				}
				$.post("scoreManage.htm?addCommentsForScore",
					  { scId: id, comment: result,bjzr:bjzr},
					   function(data){
					     alert(data.msg);
					     if(data.type == 0){
								jQuery(grid_selector).jqGrid('setGridParam',{url: './scoreManage.htm?findAllUnPublishScoreByClassCode&classId=${bjzr}&title='+encodeURIComponent(title),
									page:jQuery(grid_selector).jqGrid('getGridParam','page')-1 }).trigger("reloadGrid");
					     }
					   },'json');
			}
		});
	});
	function callbackchmanage(){
		$.hmqHomePage("menuPage.htm?cjManageInfoPage");
	}
			
</script>
		<div id="studentByClassMess">
			<div class="alert alert-block alert-success">
				<input type="hidden" id="fabustatus" value="">
				<p>
								<c:if test="${userType == '2'}">
									<div class="btn-toolbar" style="height: 30px">
									<!-- <div class="btn-group">
										<button class="btn btn-purple btn-sm" onclick="onclickQuery()" type="button">成绩查询
											<i class="icon-search icon-on-right bigger-120"></i>
										</button>
									</div> -->
								<c:if test="${!empty bjzr}">
									<!-- <div class="btn-group">
										<button id="bootbox-confirm" class="btn btn-sm btn-success" type="button">发布成绩
											<i class="icon-globe bigger-110"></i>
										</button>
									</div> -->
									<div class="btn-group">
										<button id="bootbox-regular" class="btn btn-sm btn-info" type="button">添加评语发布
											<i class="icon-edit bigger-110"></i>
										</button>
										<!--<button data-toggle="dropdown" class="btn btn-purple btn-sm dropdown-toggle">
														<span class="icon-caret-down icon-only"></span>
										</button>
										 <ul class="dropdown-menu dropdown-inverse">
											<li>
												<a href="#" onclick="importexcel()">导入成绩</a>
											</li>
											<li>
												<a href="#" onclick="lookimportfile()">查看历史导入文件</a>
											</li>
										</ul> -->
									</div>
									<div class="btn-group">
													&nbsp;&nbsp;&nbsp;
													<button class="btn btn-sm" onclick="callbackchmanage()" type="button">返回
														<i class="icon-reply icon-on-right bigger-110"></i>
													</button>
												</div>
										<%-- <c:if test="${!empty drqx}">
											&nbsp;&nbsp;&nbsp;&nbsp;
											<div class="btn-group">
												<button class="btn btn-info btn-sm dropdown-toggle" onclick="importexcel()" type="button">导入成绩
													<i class=" icon-cloud-upload icon-on-right bigger-120"></i>
												</button>
											</div>
									    </c:if> --%>
								</c:if>
								</div>
									<!--<button class="btn btn-purple btn-sm" onclick="onclickAdd()" type="button">成绩新增
										<i class="icon-arrow-right icon-on-right bigger-125"></i>
									</button> -->
								</c:if>
				</p>
			</div>
			
			<!-- <div id="studentscoregrid" class="widget-box">
				<div class="widget-header widget-header-flat header-color-blue">
					<h4 class="smaller">成绩详情</h4>
				</div>
				<div class="widget-body" style="height: 498px">
					<table id="grid-table"></table>
					<div id="grid-pager"></div>
				</div>
			</div> -->
			<div id="studentscoregrid" class="widget-box">
				<div class="widget-header widget-header-flat  header-color-green2">
					<h4 class="smaller">成绩详情</h4>
				</div>
				<div class="widget-body" style="height: 460px">
					<table id="igrid-table"></table>
					<div id="igrid-pager"></div>
				</div>
			</div>
	
			</div>
			<%-- <div id="studentImage" class="hide">
				<div class="row">
					<div class="col-xs-12">
						<form class="form-horizontal" role="form">
							<c:if test="${stuValue == '1'}">
								<div class="form-group">
									<label class="col-sm-3 control-label no-padding-right" for="form-field-select-1"></label>
									<div class="col-sm-9">
										<span> 
											<label for="grand-field-select-1" id="home_stu_message">三年二班</label>
										</span>
									</div>
								</div>
							</c:if>
							<c:if test="${stuValue == '2'}">
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
								<label class="col-sm-3 control-label no-padding-right">科目:</label>
								<div class="col-sm-9">
									<span> 
										<select id="form-field-2" class="select2">
											<option value="">--请选择--</option>
										</select>
									</span>
								</div>
							</div>	
			
							<div class="hr hr-18 dotted"></div>
			
							<div class="form-group">
								<label class="col-sm-3 control-label no-padding-right">内容:</label>
								<div class="col-sm-9">
									<span> <input id="form-field-icon-1" type="text">
									</span>
								</div>
							</div>
							<div class="hr hr-18 dotted"></div>
						</form>
					</div>
				</div>

</div> --%>
			<div id="addStuChengJi" style="display:none;" class="widget-main padding-8">
			</div>
			
