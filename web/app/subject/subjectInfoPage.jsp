<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="row">
	<div class="col-xs-12" style="height:10px"></div>
	<div class="col-xs-12">
		<div class="row">
			<div class="col-sm-12">
				<div class="widget-box">
					<div class="widget-header widget-header-small">
						<h5 class="lighter">查询搜索</h5>
					</div>
					<div class="widget-body"><div class="widget-main">
					<form class="form-search" onsubmit="return false;">
						<div class="row">
							<div class="col-xs-12 col-sm-8">
								<div class="input-group">
									<input type="text" id="roleInput" onkeydown="subjectQuery()" class="form-control search-query" placeholder="请输入关键字查询" />
										<span class="input-group-btn">
											<button type="button" id="roleQuery" class="btn btn-purple btn-sm">
												查询
												<i class="icon-search icon-on-right bigger-110"></i>
											</button>
										</span>
								</div>
							</div>
						</div>
					</form>
					</div>
					</div>
				</div>
				<div class="widget-box">
					<div class="widget-header header-color-blue">
						<h5 class="bigger lighter"><i class="icon-table"></i>信息</h5>
					</div>
					<div class="widget-body" style="height:410px">
						<div class="widget-main padding-8">
							<table id="grid-table-subject"></table>
							<div id="grid-pager-subject"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			var $assets = "assets";//this will be used in fuelux.tree-sampledata.js
		</script>

		<!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div>
<script type="text/javascript">
jQuery(function($){
	var grid_selector = "#grid-table-subject";
	var pager_selector = "#grid-pager-subject";
	jQuery(grid_selector).jqGrid({
		url:'subject.htm?findData',datatype: "json",
		mtype:"post",height: 300,
		colModel:[
			{name:'name',label:"科目名称",index:'name', width:100},
			{name:'desc',index:'desc',label:"描述", width:150},
			{name:'myac',index:'',label:"操作", width:150,fixed:true,sortable:false, resize:false,
				formatter:function(cellvalue,options,rowObject){
					var html='<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">';
					html+='<a class="green" href="javascript:editSubjectInfo('+rowObject.id+')" title="编辑">';
					html+='<i class="icon-pencil bigger-130"></i></a>';
					html+='<a class="red" href="javascript:delSubjectInfo('+rowObject.id+')" title="删除">';
					html+='<i class="icon-trash bigger-130"></i></a>';
					html+='</div>'
					return html;
				}
				
			}
		], 
		viewrecords : true,rowNum:10,rowList:[10,20,30],
		pager : pager_selector,altRows: true,//toppager: true,
		multiselect: false,multiboxonly: true,autowidth: true,//multikey: "ctrlKey", 
		loadComplete : function() {
			var table = this;
			setTimeout(function(){
				updatePagerIcons(table);
				enableTooltips(table);
			}, 0);
		}
	});
	jQuery(grid_selector).jqGrid('navGrid',pager_selector,{});//navButtons
	jQuery(grid_selector).jqGrid('navButtonAdd',pager_selector,
		{ 	//navbar options
			caption:"",
			buttonicon:"icon-plus-sign purple",
			position:"first",
			title:"添加科目",
			onClickButton:function(){
				$.dialogACE({
					url:"subject/pageform.htm",
					title:"添加科目",
					width:600,
					height:400,
					callBack:function(dialog){
						$("#subjectform").submit();
					}
				})
			}
		}
	)
	$("#roleQuery").click(function(){//查询
		jQuery("#grid-table-subject").jqGrid('setGridParam',{
	        postData:{'searchKey':$("#roleInput").val()}, //发送数据  
		}).trigger("reloadGrid"); //重新载入
	})
});
function subjectQuery(){
	var event = arguments.callee.caller.arguments[0] || window.event;
	if (event.keyCode == 13) {
		jQuery("#grid-table-subject").jqGrid('setGridParam',{
			postData:{'searchKey':$("#roleInput").val()}, //发送数据  
		}).trigger("reloadGrid"); //重新载入
	}
}
function editSubjectInfo(id){
	$.dialogACE({
		url:"subject/pageform.htm",
		parameters:{"id":id},
		title:"编辑科目",
		width:600,
		height:400,
		callBack:function(dialog){
			$("#subjectform").submit();
		}
	})
}
function delSubjectInfo(id){
	bootbox.confirm("您确定删除该科目信息吗?", function(result) {
		if(result) {
			$.hmqAJAX("subject/del.htm",function(data){
				alert(data.msg);
				jQuery("#grid-table-subject").jqGrid('setGridParam').trigger("reloadGrid");
			},{"id":id});
		}
	});
}
</script>