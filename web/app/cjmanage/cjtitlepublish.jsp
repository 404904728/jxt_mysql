<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="studentByClassMess">
			<div class="alert alert-block alert-success">
				<p>
					<div class="btn-group">
						<button id="bootbox-confirm" class="btn btn-sm btn-success" type="button">发布成绩
							<i class="icon-globe bigger-110"></i>
						</button>
					</div>
					<div class="btn-group">
						<button class="btn btn-purple btn-sm" onclick="onclickQuery()" type="button">成绩查询
							<i class="icon-search icon-on-right bigger-120"></i>
						</button>
					</div>
					<c:if test="${!empty bjzr}">
						<c:if test="${!empty drqx}">
							<div class="btn-group">
								<button class="btn btn-info btn-sm dropdown-toggle" onclick="importexcel()" type="button">导入成绩
									<i class=" icon-cloud-upload icon-on-right bigger-120"></i>
								</button>
							</div>
					    </c:if>
					</c:if>
				</p>
			</div>
			
	<div id="scoretitlegrid" class="widget-box">
		<div class="widget-header widget-header-flat  header-color-green2">
			<h4 class="smaller">考试标题</h4>
		</div>
		<div class="widget-body" style="height: 460px">
			<input type="hidden" id="title">
			<input type="hidden" id="status">
			<input type="hidden" id="code">
			<table id="sctitle_grid"></table>
			<div id="sctitle_pager"></div>
		</div>
	</div>
</div>
<div id="addCheckJsp"></div>
<script type="text/javascript">
			var bjzr =  "${bjzr}";
			var njzr = 1;
			var grid_selector = "#sctitle_grid";
			var pager_selector = "#sctitle_pager";
			var colname = ['id','code','考试标题','状态','短信状态'];
			$(grid_selector).jqGrid({
				url:'./scoreManage.htm?findAlltitleByClassId&classId=' + bjzr,
				datatype: "json",
				height: 360,
				colNames:colname,
				colModel:[
					{name:'id',index:'id', hidden:true,width:300, sortable:false, editable: false},
					{name:'classCode',index:'classCode', hidden:true,width:300, sortable:false, editable: false},
					{name:'title',index:'title', hidden:false,width:300, sortable:false, editable: false,formatter:function(cell,options,rowdata){
						if(null != rowdata &&　rowdata.classOrder){
							return '<a href="download/'+ rowdata.classOrder +'.'+rowdata.classAsc+'" target="_self">'+cell+'</a>';
						}else{
							return cell;
						}
					}},
					{name:'publishStatus',index:'publishStatus', hidden:false,width:80, sortable:false, editable: false,formatter:function(cell,options,rowdata){
						if(cell == 1){
							return '已发布';
						}else{
							return '未发布';
						}
					}},
					{name:'xc',hidden:false, width:60, sorttype:"int",sortable:false, editable: false,formatter:checkPicture}
				], 
				viewrecords : true,
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				pager : pager_selector,
				altRows : true,
				loadComplete : function() {
					setTimeout(function() {
						updatePagerIcons(this);
					}, 0);
				},
				autowidth: true,
				onSelectRow:function(rowid){
					var title = $("#sctitle_grid").getRowData(rowid).title;
					var status = $("#sctitle_grid").getRowData(rowid).publishStatus;
					var code = $("#sctitle_grid").getRowData(rowid).classCode;
					try{
						$("#title").val($(title).text());
					}catch(err){
						$("#title").val(title);
					}
					$("#status").val(status);
					$("#code").val(code);
				},
				ondblClickRow: function(rowid){
					var title = $("#sctitle_grid").getRowData(rowid).title;
					
					try{
						title =$(title).text();
					}catch(err){
						title = title;
					}
					var status = $("#sctitle_grid").getRowData(rowid).publishStatus;
					var code = $("#sctitle_grid").getRowData(rowid).classCode;
					$("#studentByClassMess").css('display','none');
					$("#addCheckJsp").css('display','block');
					$.hmqRefreshPage("addCheckJsp","./scoreManage.htm?queryScoreByTitle&title=" + encodeURIComponent(title)+"&code=" + code + "&bjzr=" + bjzr);
				}
			});
			
			//导入成绩的方法
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
			
			/**
			 * 
			 * @param cellvalue
			 * @param options
			 * @param rowObject
			 * @returns {String}
			 * 加载查看短信状态
			 */
			function checkPicture(cellvalue, options, rowObject){
				return "<a href='javascript:void(0)' onclick='querymessageinfo(\""+ rowObject.title +"\",\""+ bjzr +"\")'>短信查看</a>";
			}
			//打开短信查询页面 
				function querymessageinfo(title,bjzr){
					$.dialogACE({
	                    url:"scoreManage.htm?showdetail",parameters:{"id":bjzr,"title":title},
	                    title:"短信发送详细",width:800,height:400,
	                    callBack:function(dialog) {
	                        $(dialog).dialog("destroy");
	                    }
	                })
				}
			
			jQuery(grid_selector).jqGrid('navGrid',pager_selector,{});
			
			 $('body').resize(function(){   
		    	   $("#sctitle_grid").setGridWidth($("#scoretitlegrid").width() - 8,true);
	   		 });
			 //发布成绩函数
			 $("#bootbox-confirm").click(function(){
				var tit = $("#title").val();
				var stats = $("#status").val();
				var cod = $("#code").val();
				if(null == tit || "" == tit || null == stats || "" == stats || null == cod || "" == cod){
					bootbox.alert("请选择发布成绩行");
					return;
				}
				if("已发布" == stats){
					bootbox.alert("此成绩已经发布,请重新选择");
					return;
				}
				if("未发布" == stats){
					if(confirm("是否确认发布?") == true){
						$("#studentByClassMess").append('<div class="message-loading-overlay"><i class="icon-spin icon-spinner orange2 bigger-160"></i></div>');
						$.ajax({
							type: "POST",
							url: 'scoreManage.htm?updateAnnounceStatus_&id=' + bjzr + '&title=' + encodeURIComponent(tit),
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
				}
			 });
			 /**
				 * 点击后跳转到新的查询页面
				 */
				function onclickQuery(){
					$.hmqHomePage("menuPage.htm?queryCJByclass&zrflg=2");
				}
			 
</script>		
