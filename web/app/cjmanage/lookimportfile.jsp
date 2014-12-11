<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
	var grid_selector = "#igrid-table";
	var pager_selector = "#igrid-pager";
	$(grid_selector).jqGrid({
		url:'./scoreManage.htm?lookimportfile',
		datatype: "json",
		mtype : "post",
		height: 360,
		colNames:['编号','文件名','后缀', '大小', '导入日期'],
		colModel:[
			{name:'id',index:'id', width:60,hidden:true, sorttype:"int",sortable:false, editable: false},
			{name:'fileName',index:'fileName', width:470,editable: false,formatter:function(cell,options,rowdata){
				return '<a href="'+rowdata.htmlUrl+'" target="_self">'+cell+'</a>';
				}},
			{name:'suffix',index:'suffix', width:80,sortable:false, editable: false},
			{name:'size',index:'size', width:100, sortable:false,editable: false},
			{name:'date',index:'date', width:120, sortable:false,editable: false} 
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
		}
	});
</script>
								<!-- PAGE CONTENT BEGINS -->
								<div class="row">
									<div class="col-sm-12">
												<div class="widget-box">
													<div class="widget-header widget-header-flat  header-color-green2">
														<h4 class="smaller">详情</h4>
													</div>
													<div class="widget-body" style="height: 460px">
														<table id="igrid-table"></table>
														<div id="igrid-pager"></div>
													</div>
												</div>
									</div>
								</div><!-- PAGE CONTENT ENDS -->
