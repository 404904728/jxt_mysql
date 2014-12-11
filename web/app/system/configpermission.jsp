<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="./res/script_/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="./res/script_/zTree/js/jquery.ztree.all-3.5.min.js"></script>
<div class="row">
	<div class="col-xs-12">
		<div class="clearfix">
			<div class="pull-left alert alert-success no-margin">
				<button type="button" class="close" data-dismiss="alert">
					<i class="icon-remove"></i>
				</button>
				<i class="icon-umbrella bigger-120 blue"></i>
				点击下面的树可以选择权限，关联到该角色
			</div>
		</div>
	</div>
	<div class="col-xs-12" style="height:10px"></div>
	<div class="col-xs-12">
		<div class="row">
			<div class="col-sm-12">
				<div class="widget-box">
					<div class="widget-header header-color-blue2">
						<h4 class="lighter smaller">权限</h4>
					</div>
					<div class="widget-body">
						<div class="widget-main padding-8" style="height:350px;overflow: auto;">
							<!-- <div id="tree1" class="tree"></div> -->
							<ul id="configpermissiontree" class="ztree"></ul>
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
	$.fn.zTree.init($("#configpermissiontree"),{
		check: {
			enable: true
		},
		async: {
			enable: true,
			url:"permission.htm?findPermission&rId="+${rId},
			autoParam:["id"]
		}
	});
});
</script>