<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" href="./res/script_/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="./res/script_/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="./res/script_/zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="./res/script_/zTree/js/jquery.ztree.exedit-3.5.js"></script>
<style type="text/css">
.ztree li span.demoIcon{padding:0 2px 0 10px;}
.ztree li span.button.icon{margin:0; background: url(./res/script_/zTree/css/zTreeStyle/img/diy/del.png) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
</style>
<div class="row">
	<div class="col-xs-12">
		<div class="clearfix">
			<div class="pull-left alert alert-success no-margin">
				<button type="button" class="close" data-dismiss="alert">
					<i class="icon-remove"></i>
				</button>
				<i class="icon-umbrella bigger-120 blue"></i>
				点击下面的树可以查看班级的信息,学校教职工的信息...
			</div>
		</div>
	</div>
		
	<div class="col-xs-12" style="height:10px"></div>
	<div class="col-xs-12">
		<div class="row">
			<div class="col-sm-3">
				<div class="widget-box">
					<div class="widget-header header-color-blue2">
						<h4 class="lighter smaller">学校组织</h4>
					</div>
					<div class="widget-body">
						<div class="widget-main padding-8" style="height: 500px">
							<!-- <div id="tree1" class="tree"></div> -->
							<ul id="menuTree" class="ztree" style="height:480px;overflow: auto;"></ul>
						</div>
					</div>
				</div>
			</div>
			<div class="col-sm-9">
				<div class="widget-box">
					<div class="widget-header widget-header-small">
						<h5 class="lighter">查询搜索</h5>
					</div>
					<div class="widget-body"><div class="widget-main">
					<form class="form-search" onsubmit="return false;">
						<div class="row">
							<div class="col-xs-12 col-sm-8">
								<div class="input-group">
									<input type="text" id="teacherInput" onkeydown="teacherQueryKey()" class="form-control search-query" placeholder="请输入关键字查询" />
										<span class="input-group-btn">
											<button type="button" id="teacherQuery" class="btn btn-purple btn-sm">
												查询
												<i class="icon-search icon-on-right bigger-110"></i>
											</button>
										</span>
										&nbsp;
										<span class="input-group-btn">
											<button type="button" id="teacherOrgAdd" class="btn btn-purple btn-sm">
												添加组织
												<i class="icon-plus-sign purple icon-on-right bigger-110"></i>
											</button>
										</span>
										&nbsp;
										<span class="input-group-btn">
											<button type="button" id="teacherOrgupdate" class="btn btn-purple btn-sm">
												修改组织
												<i class="icon-plus-sign purple icon-on-right bigger-110"></i>
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
						<div class="widget-toolbar no-border">
						</div>
					</div>
					<div class="widget-body" style="height:405px">
						<div id="teacherInfoMsg" class="widget-main padding-8">
							<table id="grid-table"></table>
							<div id="grid-pager"></div>
						</div>
						<div id="teacherInfo"  style="display:none;" class="widget-main padding-8">
							
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
var treeObj,delObj;
jQuery(function($){
	$.fn.zTree.init($("#menuTree"),{
		async: {
			enable: true,
			//url:"org.htm?findTeacher",
			url:"role.htm?findTeacherRole",
			autoParam:["id"],
		},
		callback: {
			onMouseUp: onMouseUp
		}
	});
	treeObj = $.fn.zTree.getZTreeObj("menuTree");
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
	jQuery(grid_selector).jqGrid({
		url:'user.htm?findTeacher',datatype: "json",
		mtype:"post",height: 300,
		colModel:[
			{name:'id',label:"ID",index:'id',hidden:true, width:60},
			{name:'name',label:"姓名",index:'name', width:120,formatter:function(cellvalue, options, rowObject){
				return "<a herf='#' onclick=showTeacher("+rowObject.id+")>"+cellvalue+"</a>"
			}},
			{name:'tel',label:"电话号码",index:'tel',width:170,sorttype:"date"},
			{name:'gender',index:'gender',label:"性别", width:80,formatter:function(cellvalue, options, rowObject){
					if(cellvalue==1)return "男";
					else if(cellvalue==0)return "女";
					else return "保密";
			}},
			{name:'zuzhi',index:'zuzhi',label:"组织", width:150},
			{name:'org',index:'org',label:"部门", width:150} 
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
			title:"添加老师",
			onClickButton:function(){
				var selectNodes = treeObj.getSelectedNodes();//获取选中的节点
				if(selectNodes.length==0||selectNodes[0].id.split(":")[0]!='role'){
					bootbox.alert("请先选择组织");
					return;
				}
				var roleids=new Array();
				var rolenames=new Array();
				for(var i=0;i<selectNodes.length;i++){
					roleids.push(selectNodes[i].id.split(":")[1]);
					rolenames.push(selectNodes[i].name);
				}
				$.dialogACE({
					url:"teacherInfo/addpageform.htm?ids="+roleids+"&names="+encodeURI(rolenames),
					title:"添加老师",
					width:600,
					height:400,
					callBack:function(dialog){
						$("#teacherInfo_form_add").submit();
					}
				})
			}
		}
	)
	jQuery(grid_selector).jqGrid('navButtonAdd',pager_selector,{ 
			caption:"",
			buttonicon:"icon-trash red",
			title:"删除教师",
			onClickButton:function(){
				var selectId=$(grid_selector).jqGrid('getGridParam','selrow');
				if($.isEmpty(selectId)){
					bootbox.confirm("你需要先选择删除的行?", function(result){});
					return;
				}
				deleteTeacherInfo(selectId);
			}
		}
	)
	$("#teacherQuery").click(function(){//查询
		jQuery(grid_selector).jqGrid('setGridParam',{
	        postData:{'searchKey':$("#teacherInput").val()}, //发送数据  
		}).trigger("reloadGrid"); //重新载入
		$("#teacherInfoMsg").show();
		$("#teacherInfo").hide();
	});
	$("#teacherOrgAdd").click(function(){//添加组织
		//$.hmqRefreshPage("addClass","systemPage.htm?orgform&oId="+selectNodes[0].id.split(":")[1]);
		$("#teacherInfoMsg").hide();
		$("#teacherInfo").show();
		$.hmqRefreshPage("teacherInfo","systemPage.htm?orgform&oId=2");
	});
	$("#teacherOrgupdate").click(function(){
		var treeObj = $.fn.zTree.getZTreeObj("menuTree");
		var selectNodes = treeObj.getSelectedNodes();//获取选中的节点
		if(selectNodes.length==0||selectNodes[0].id.split(":")[0]!='role'){
			bootbox.alert("请先在左边的结构树选择要修改的组织");
			return;
		}
		$("#teacherInfoMsg").hide();
		$("#teacherInfo").show();
		$.hmqRefreshPage("teacherInfo","systemPage.htm?orgformupdate&id="+selectNodes[0].id.split(":")[1]);
	})
});
function teacherQueryKey(){
	var event = arguments.callee.caller.arguments[0] || window.event;
	if (event.keyCode == 13) {
		jQuery("#grid-table").jqGrid('setGridParam',{
	        postData:{'searchKey':$("#teacherInput").val()} //发送数据
		}).trigger("reloadGrid"); //重新载入
		$("#teacherInfoMsg").show();
		$("#teacherInfo").hide();
	}
}
function  deleteTeacherInfo(selectId,treeNodeTId){
	bootbox.confirm("您确定删除该老师信息吗?", function(result) {
		if(result) {
			$.hmqAJAX("teacherInfo.htm?deleteById",function(data){
				alert(data.msg);
				if(delObj&&data.type==0){
					delObj.remove();
					delObj==null;
				}
				jQuery("#grid-table").jqGrid('setGridParam').trigger("reloadGrid");
			},{"id":selectId});
		}
	});
}
function deleteOrgInfo(orgId){
	bootbox.confirm("您确定删除该组织信息吗? 若该组织下有人员，将不能删除", function(result) {
		if(result) {
			$.hmqAJAX("org/del.htm",function(data){
				alert(data.msg);
				if(delObj&&data.type==0){
					delObj.remove();
					delObj==null;
				}
			},{"id":orgId});
		}
	});
}
function backInfoMsg(){
	if($("#teacherInfoMsg").is(":hidden")){
		$("#teacherInfoMsg").show();
		$("#teacherInfo").hide();
	}else{
		$("#teacherInfoMsg").hide();
		$("#teacherInfo").show();
	}
}
function showTeacher(id){
	$("#teacherInfoMsg").hide();
	$("#teacherInfo").show();
	$.hmqRefreshPage("teacherInfo","systemPage.htm?teacherInfoPage&tId="+id);
}
function onMouseUp(event, treeId, treeNode) {
	if($.isEmpty(treeNode))return;
	var typeId=treeNode.id.split(":");
	if(typeId[0]=="teacherInfo"){//老师
		$.hmqRefreshPage("teacherInfo","systemPage.htm?teacherInfoPage&tId="+typeId[1]);
		$("#teacherInfoMsg").hide();
		$("#teacherInfo").show();
	}
}
</script>