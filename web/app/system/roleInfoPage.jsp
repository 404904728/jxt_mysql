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
									<input type="text" id="roleInput"  onkeydown="roleQueryKey()" class="form-control search-query" placeholder="请输入关键字查询" />
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
							<table id="grid-table"></table>
							<div id="grid-pager"></div>
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
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
	jQuery(grid_selector).jqGrid({
		url:'role.htm?findData',datatype: "json",
		mtype:"post",height: 300,
		colModel:[
			{name:'name',label:"名称",index:'name', width:100},
			{name:'use',label:"是否可用",index:'use',width:80,formatter:function(cellvalue, options, rowObject){
					if(cellvalue)return "可用";
					else return "不可用";
			}},
			{name:'type',index:'type',label:"角色类型", width:100,
				formatter:function(cellvalue,options,rowObject){
					return cellvalue==0?'普通角色':'教师角色';
				}
			},
			{name:'desc',index:'desc',label:"描述", width:150},
			{name:'myac',index:'',label:"操作", width:150,fixed:true,sortable:false, resize:false,
				formatter:function(cellvalue,options,rowObject){
					var html='<div class="visible-md visible-lg hidden-sm hidden-xs action-buttons">';
					html+='<a class="green" href="javascript:editRoleInfo('+rowObject.id+')" title="编辑">';
					html+='<i class="icon-pencil bigger-130"></i></a>';
					html+='<a class="red" href="javascript:configRoleInfo('+rowObject.id+')" title="配置人员">';
					html+='<i class="icon-cog bigger-130"></i></a>';
					html+='<a class="red" href="javascript:grantPerInfo('+rowObject.id+')" title="配置权限">';
					html+='<i class="icon-lock bigger-130"></i></a>';
					html+='<a class="red" href="javascript:delRoleInfo('+rowObject.id+')" title="删除">';
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
			title:"添加角色",
			onClickButton:function(){
				$.dialogACE({
					url:"role.htm?pageform",
					title:"添加角色",
					width:600,
					height:400,
					callBack:function(dialog){
						$("#roleform").submit();
					}
				})
			}
		}
	)
	$("#roleQuery").click(function(){//查询
		jQuery(grid_selector).jqGrid('setGridParam',{
	        postData:{'searchKey':$("#roleInput").val()} //发送数据  
		}).trigger("reloadGrid"); //重新载入
	})
});
function roleQueryKey(){
	var event = arguments.callee.caller.arguments[0] || window.event;
	if (event.keyCode == 13) {
		jQuery("#grid-table").jqGrid('setGridParam',{
	        postData:{'searchKey':$("#roleInput").val()} //发送数据  
		}).trigger("reloadGrid"); //重新载入
	}
}
function editRoleInfo(id){
	$.dialogACE({
		url:"role.htm?pageform",
		parameters:{"id":id},
		title:"编辑角色",
		width:600,
		height:400,
		callBack:function(dialog){
			$("#roleform").submit();
		}
	})
}
function delRoleInfo(id){
	bootbox.confirm("您确定删除该角色信息吗?", function(result) {
		if(result) {
			$.hmqAJAX("role.htm?del",function(data){
				alert(data.msg);
				jQuery("#grid-table").jqGrid('setGridParam').trigger("reloadGrid");
			},{"id":id});
		}
	});
}
var arrayObj = new Array();
//角色配置给人员
function configRoleInfo(id){//角色配置人员
	$.dialogACE({
		url:"role.htm?configrole",parameters:{"id":id},
		title:"配置人员",width:600,height:400,
		callBack:function(dialog){
			var treeObj = $.fn.zTree.getZTreeObj("configroleteachertree");
			var selectNodes = treeObj.getCheckedNodes(true);//获取选中的节点
			var teacherIds="";
			for(var i=0;i<selectNodes.length;i++){
				var typeId=selectNodes[i].id.split(":");
				if(typeId[0]=="teacherInfo"){//老师
					teacherIds+=typeId[1]+",";
				}
			}
			$.hmqAJAX("role/roleuserbinding.htm",function(data){
				alert(data.msg);
				arrayObj=[];
				$(dialog).dialog("destroy"); 
			},{"teacherIds":arrayObj.join(","),"roleId":id});
		}
	})
}
//权限分配到角色
function grantPerInfo(id){
	$.dialogACE({
		url:"role.htm?configpermission",parameters:{"id":id},
		title:"配置人员",width:600,height:400,
		callBack:function(dialog){
			var treeObj = $.fn.zTree.getZTreeObj("configpermissiontree");
			var selectNodes = treeObj.getCheckedNodes(true);//获取选中的节点
			var permissionIds="";
			for(var i=0;i<selectNodes.length;i++){
				permissionIds+=selectNodes[i].id+",";
			}
			$.hmqAJAX("permission.htm?roleGrantPermission",function(data){
				alert(data.msg);
				$(dialog).dialog("destroy"); 
			},{"ids":permissionIds,"id":id});
		}
	})
}
</script>