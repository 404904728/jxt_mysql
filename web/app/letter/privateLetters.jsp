<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
	/** 向下滚动一次加载10条记录 */
	var start = 0;
	var totalcount = 0;
	var historyCount = 0;
	var plid;
	var inboxflag=false;
	var sendboxflag=false;
	var grid_selector = "#grid-table_s";
	var pager_selector = "#grid-pager_s";
	var gridurl = './pLetter.htm?obtainInboxInfos';
	$(grid_selector).jqGrid({
		url:gridurl,
		datatype: "json",
		mtype : "post",
		height: 426,
		colNames:['编号','姓名','组织机构', '内容','时间'],
		colModel:[
			{name:'id',index:'id', width:60,hidden:true,sorttype:"int",sortable:false, editable: false},
			{name:'senderName',index:'senderName', width:70,editable: false},
			{name:'senderOrgName',index:'senderOrgName', width:70,sortable:false, editable: false},
			{name:'content',index:'content', width:130, sortable:false,editable: false},
			{name:'sendTime',index:'sendTime', width:70, sortable:false,editable: false} 
		], 

		viewrecords : true,
		rowNum:10,
		rowList:[10,20,30],
		pager : pager_selector,
		altRows: true,
		multiselect: false,
        multiboxonly: false,
		loadComplete : function() {
			setTimeout(function(){
				updatePagerIcons(this);
			}, 0);
		},

		autowidth: true,
		ondblClickRow:ondbclick
	});
	 
		function onclicksearch(){
			//var url = jQuery(grid_selector).jqGrid('getGridParam','url');
			var url = gridurl;
			var sv = $("#searchKey").val();
			if(sv){
				url = url+"&searchKey="+encodeURIComponent(sv);
			}
				jQuery(grid_selector).jqGrid('setGridParam',{url: url}).trigger("reloadGrid");
			
		}

		function enterEventIntc(){
			var event=arguments.callee.caller.arguments[0]||window.event;
			if (event.keyCode == 13) 
		    {      
				onclicksearch();
		    }
		}

		   $(function(){
		       $('body').resize(function(){   
			       if("tab-pane active" ==  $("#home4").attr("class")){
			    	   $(grid_selector).setGridWidth($("#gridbox").width() - 8,true);
			       }else if("tab-pane active" ==  $("#profile4").attr("class")){
			    	   $("#grid-table_f").setGridWidth($("#gridbox_f").width() - 8,true);
			       }
		    });
		   });
		
		$(".dialogs").scroll(function(){
			var top =  $(this)[0].scrollTop;  
			//var height = $(this)[0].scrollHeight;
			//var divh = $(".dialogs").height();
			//var sendformh = $("#sendform").height();
			if(top == 0 && historyCount > 0){
				beforedetail(plid,'detailinfo');
			}
			});
		function ondbclick(id){
			$("#alert-info_s").addClass('hide');
			$("#detailinfo").removeClass('hide');
			$(".form-actions").removeClass('hide');
			$("#detailinfo .dialogs").html("");
			start = 0;
			refreshdetail(id,'detailinfo');
		}

		function onclickInbox(){
			if(inboxflag){
				return;
			}else{
				inboxflag= true;
				$.hmqRefreshPage('profile4','./app/letter/outbox.jsp');
			}
		}
		
		function onclicksendbox(){
			if(sendboxflag){
				return;
			}else{
				sendboxflag= true;
				$.hmqRefreshPage('dropdown14','./pLetter.htm?sendLetter');
			}
		}
</script>

								<div id="privateletters" class="row">
									<div class="col-sm-12">
									<div class="tabbable">
											<ul class="nav nav-tabs padding-12 tab-color-blue background-blue" id="myTab4">
											   		<li class="active">
															<a data-toggle="tab" href="#home4">私信信息</a>
														</li>
												<!--<li>
													<a data-toggle="tab" href="#profile4" onclick="onclickInbox()">发件箱</a>
												</li>
												--><li>
													<a data-toggle="tab" href="#dropdown14" onclick="onclicksendbox()">发私信</a>
												</li>
											</ul>

											<div class="tab-content">
											    <div id="home4" class="tab-pane in active">
													<%@include file="./inbox.jsp"%>
												</div><!-- 收件tab ENDS -->
												<div id="profile4" class="tab-pane">
												</div>
												<div id="dropdown14" class="tab-pane">
												</div>
											</div>
										</div>
									</div>
								</div><!-- PAGE CONTENT ENDS -->