<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="./res/ace/assets/js/select2.min.js"></script>
<script type="text/javascript">
var bjzr = 1;
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
						//var usertype = $("#importexcel").contents().find("input[name='usertype']:checked").val();
						//if(!usertype){
							//if(bjzr){
							//	usertype = bjzr;
							//}else if(njzr){
							//	usertype = njzr;
							//}
						//}
						var attachId=$("#importexcel")[0].contentWindow.attachId;
						if(attachId==null){
							alert("请先上传数据文件");
							return;
						}
						$("div[aria-describedby='myDialogAce']").append('<div class="message-loading-overlay"><i class="icon-spin icon-spinner orange2 bigger-160"></i></div>');
						//setTimeout(function(){
							//$("div[aria-describedby='myDialogAce'").find('.message-loading-overlay').remove();
							//$(dialog).dialog("destroy");
							//alert("上传成功！");
						//	},3000)
						 
					    $.ajax({
					        url: "./scoreManage.htm?importScoreXls&attachID="+attachId+"&scoreType="+scoretype,
					        cache: false,
					        dataType: "json",
					        type: "POST",
					        timeout: 600000,
					        contentType: "application/x-www-form-urlencoded;charset=utf-8",
					        success: function(data) {
						        if(scoretype == 1 || scoretype == '1'){
										/* if((data.type = 0 || data.type == '0') && data.msgId){
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
										}else{ */
											colseDialog(data,dialog);
											jQuery(grid_selector).jqGrid('setGridParam').trigger("reloadGrid");
										/* } */
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
	        	 $("div[aria-describedby='myDialogAce']").find('.message-loading-overlay').remove();
		         alert(data.msg);
           		 $(dialog).dialog("destroy")
			}
			
			var grid_selector = "#igrid-table";
			var pager_selector = "#igrid-pager";
			var colname = ['编号','考试标题','附件名称','文件大小','总人数','参考人数','导入人','导入人ID','导入日期','发布状态'];
			$(grid_selector).jqGrid({
				url:'./scoreManage.htm?queryimportappendix',
				datatype: "json",
				height: 360,
				colNames:colname,
				colModel:[
					{name:'id',index:'id', hidden:true, sorttype:"Long",sortable:false, editable: false},
					{name:'ksTitle',index:'ksTitle', hidden:false,width:300, sortable:false, editable: false},
					{name:'appTtitle',index:'appTtitle',editable: false,width:350,formatter:function(cell,options,rowdata){
						return '<a href="download/'+ rowdata.id +'.xls" target="_self">'+cell+'</a>';
						}},
					{name:'appSize',index:'appSize', hidden:false,width:100, sortable:false, editable: false},
					{name:'ksTotal',index:'ksTotal', hidden:false,width:80, sortable:false, editable: false},
					{name:'sjCount',index:'sjCount', hidden:false,width:80, sortable:false, editable: false},
					{name:'impoTea',index:'impoTea', hidden:false,width:100, sortable:false, editable: false},
					{name:'impoTeaId',index:'impoTeaId', hidden:true, sorttype:"Long",sortable:false, editable: false},
					{name:'impoDate',index:'impoDate', hidden:false,width:100, sortable:false, editable: false},
					{name:'pubStatus',index:'pubStatus', hidden:false,width:100, sortable:false, editable: false}
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
				autowidth: true,
				onSelectRow:function(rowid){
					$("#title_hidden").val($("#igrid-table").getRowData(rowid).ksTitle);
				}

			});
			jQuery(grid_selector).jqGrid('navGrid',pager_selector,{});
			
			 $('body').resize(function(){   
		    	   $("#igrid-table").setGridWidth($("#studentscoregrid").width() - 8,true);
	   		 });
			//发布成绩函数
			 $("#publish_button").click(function(){
				var tit = $("#title_hidden").val();
				if(null == tit || "" == tit){
					bootbox.alert("请单击选择要发布的成绩");
					return;
				}
				if(confirm("是否确认发布?") == true){
					$("#studentByClassMess").append('<div class="message-loading-overlay"><i class="icon-spin icon-spinner orange2 bigger-160"></i></div>');
					$.ajax({
						type: "POST",
						url: 'scoreManage.htm?publishScoreByImportter&title=' + encodeURIComponent(tit),
						dataType: "json",
						cache: false,
						success: function(root){
							if(root == null){
								bootbox.alert("系统异常,请稍后在试!");
								$("#studentByClassMess").find('.message-loading-overlay').remove();
							}
							if(root.type == 0 || root.type == '0'){
								bootbox.alert("数据异常!");
								$("#studentByClassMess").find('.message-loading-overlay').remove();
							}
							if(root.type == 1 || root.type == '1'){
								bootbox.alert("班级信息异常,请联系管理员");
								$("#studentByClassMess").find('.message-loading-overlay').remove();
							}
							if(root.type == 2 || root.type == '2'){
								bootbox.alert("发送成功");
								jQuery(grid_selector).jqGrid('setGridParam').trigger("reloadGrid");
								$("#studentByClassMess").find('.message-loading-overlay').remove();
							}
							if(root.type == 100 || root.type == '100'){
								jQuery(grid_selector).jqGrid('setGridParam').trigger("reloadGrid");
								bootbox.alert(root.msg);
								$("#studentByClassMess").find('.message-loading-overlay').remove();
							}
							
						}
					});
				}
			 });
		</script>
		<div id="studentByClassMess">
			<div class="alert alert-block alert-success">
				<p>
								<c:if test="${userType == '2'}">
									<div class="btn-toolbar" style="height: 30px">
									<c:if test="${!empty drqx}">
										<div class="btn-group">
											<button class="btn btn-info btn-sm dropdown-toggle" onclick="importexcel()" type="button">导入成绩
												<i class=" icon-cloud-upload icon-on-right bigger-120"></i>
											</button>
										</div>
										<div class="btn-group">
											<button id="publish_button" class="btn btn-sm btn-success" type="button">发布成绩
												<i class="icon-globe bigger-110"></i>
											</button>
										</div>
									</c:if>
								</div>
								</c:if>
				</p>
			</div>
			<div id="studentscoregrid" class="widget-box">
				<div class="widget-header widget-header-flat  header-color-green2">
					<h4 class="smaller">导入成绩详情</h4>
				</div>
				<div class="widget-body" style="height: 460px">
					<input type="hidden" id="title_hidden" value="">
					<table id="igrid-table"></table>
					<div id="igrid-pager"></div>
				</div>
			</div>
	</div>