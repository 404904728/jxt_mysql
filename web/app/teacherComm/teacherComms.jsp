<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script src="./res/ace/assets/js/fuelux/data/fuelux.tree-sampledata.js"></script>
<script src="./res/ace/assets/js/fuelux/fuelux.tree.min.js"></script>
<script type="text/javascript">
	//String.prototype.evalJSON = function(){ return eval('(' + this + ')'); }
	var gridurl = './TeacherComm.htm?findTeacherGridDataForAce';
    var grid_selector = "#grid-table-tc";
	var pager_selector = "#grid-pager";
	$(function(){
	    $.ajax({
	        url: "./TeacherComm.htm?findOrgTreeDataForAce",
	        cache: false,
	        dataType: "json",
	        type: "POST",
	        contentType: "application/x-www-form-urlencoded;charset=utf-8",
	        success: function(data) {
	      	  $('#orgtreetc').ace_tree({
	      		dataSource: new DataSourceTree({data: data}),//str.evalJSON()
	      		multiSelect:false,
	      		loadingHTML:'<div class="tree-loading"><i class="icon-refresh icon-spin blue"></i></div>',
	      		'open-icon' : 'icon-minus',
	      		'close-icon' : 'icon-plus',
	      		'selectable' : true,
	      		'selected-icon' : 'icon-ok',
	      		'unselected-icon' : 'icon-remove'
	      	});
	       }
	    });

		$(grid_selector).jqGrid({
			//direction: "rtl",
			url:gridurl,
			datatype: "json",
			mtype : "post",
			height: 426,
			colNames:['编号','教师姓名','所授科目', '电话号码', '邮箱地址'],
			colModel:[
				{name:'id',index:'id', width:60, sorttype:"int",hidden:true, sortable:false, editable: false},
				{name:'name',index:'name', width:70,editable: false},
				{name:'suject',index:'suject', width:70,sortable:false, editable: false},
				{name:'tel',index:'tel', width:70, sortable:false,editable: false},
				{name:'email',index:'email', width:60, sortable:false,editable: false} 
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

		$('#orgtreetc').on('selected', function (evt, data) {
			 var url = gridurl;
			 var orgid =  data.info[0].id;
			  if(orgid){
				  url = url+"&orgId="+orgid;
			  }
			  if(data.info[0].phone){
				  $("#gridtitle").text(data.info[0].name+"("+data.info[0].phone+")");
			  }else{
				  $("#gridtitle").text(data.info[0].name);
			  }
			  jQuery(grid_selector).jqGrid('setGridParam',{url: url,page:0}).trigger("reloadGrid");
				 
			 // if('o' == data.info[0].itemtype){

			  //}else if('r' == data.info[0].itemtype){
				//  jQuery(grid_selector).jqGrid('setGridParam',{url: './TeacherComm.htm?findTeacherGridDataByRole&roleId='+data.info[0].roleid}).trigger("reloadGrid");
			 // }

			  
		});

		$('#treedivmain').slimScroll({
			height : '600px'
		});
		
		});

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


		function onclicksearch(){
			//var url = jQuery(grid_selector).jqGrid('getGridParam','url');
			var url = gridurl;
			var sv = $("#searchKey").val();
			if(sv){
				url = url+"&searchKey="+encodeURIComponent(sv);
			}
				jQuery(grid_selector).jqGrid('setGridParam',{url: url,page:0}).trigger("reloadGrid");
			
		}

		function enterEventIntc(){
			var event=arguments.callee.caller.arguments[0]||window.event;
			if (event.keyCode == 13) 
		    {      
				onclicksearch();
		    }
		}
</script>
								<!-- PAGE CONTENT BEGINS -->
								<div class="row">
									<div class="col-sm-4">
										<div class="widget-box">
											<div class="widget-header widget-header-flat header-color-blue2">
												<h4 class="smaller">
													<i class="icon-folder-open-alt smaller-80"></i>
													机构组织列表
												</h4>
											</div>

											<div class="widget-body" style="height: 620px">
												<div id="treedivmain" class="widget-main">
													<div id="orgtreetc" class="tree"></div>
												</div>
											</div>
										</div>
									</div>

									<div class="col-sm-8">
										<div class="row">
											<div class="col-xs-12">
												<div class="widget-box">
												<div class="widget-header widget-header-small">
												<h5 class="lighter">教师姓名查询</h5>
												</div>
												<div class="widget-body">
												<div class="widget-main">
												<form class="form-search" onsubmit="return false;">
												<div class="row">
												<div class="col-xs-12 col-sm-8">
												<div class="input-group">
												<input type="text" id="searchKey"
													class="form-control search-query" placeholder="Search ..." autocomplete="off" onkeydown="enterEventIntc()"/> <span
													class="input-group-btn">
														<button type="button" class="btn btn-purple btn-sm" onclick="onclicksearch()">查询 <i
													class="icon-search icon-on-right bigger-110"></i></button>
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
														<h4 id="gridtitle" class="smaller">通讯详情</h4>
													</div>

													<div class="widget-body" style="height: 525px">
														<table id="grid-table-tc"></table>
														<div id="grid-pager"></div>
														
													</div>
												</div>
											</div>
										</div>

									</div>
								</div><!-- PAGE CONTENT ENDS -->
