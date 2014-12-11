<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh">
	<head>
		<meta charset="utf-8" />
		<title>智慧家校通</title>
		<meta name="keywords" content="智慧家校通" />
		<meta name="description" content="学校-老师-家长三位一体" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<!-- 基础样式 -->
		<link href="./res/ace/assets/css/bootstrap.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="./res/ace/assets/css/font-awesome.min.css" />
		<!--[if IE 7]>
		  <link rel="stylesheet" href="./res/ace/assets/css/font-awesome-ie7.min.css" />
		<![endif]-->
		
		<link rel="stylesheet" href="./res/ace/assets/css/jquery-ui-1.10.3.full.min.css" />
		
		<!-- page specific plugin styles -->
		<link rel="stylesheet" href="./res/ace/assets/css/datepicker.css" />
		<link rel="stylesheet" href="./res/ace/assets/css/ui.jqgrid.css" />
		
		<!-- ace styles -->
		<link rel="stylesheet" href="./res/ace/assets/css/colorbox.css" />
		<link rel="stylesheet" href="./res/ace/assets/css/ace.min.css" />
		<link rel="stylesheet" href="./res/ace/assets/css/ace-rtl.min.css" />
		<link rel="stylesheet" href="./res/ace/assets/css/ace-skins.min.css" />


		<!-- select -->
		<link rel="stylesheet" href="./res/ace/assets/css/select2.css" />
		<link rel="stylesheet" href="./res/ace/assets/css/chosen.css" />
		
		<!-- fonts 
		<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400,300" />
		-->

		<!--[if lte IE 8]>
		  <link rel="stylesheet" href="assets/css/ace-ie.min.css" />
		<![endif]-->

		<!-- inline styles related to this page -->

		<!-- ace settings handler -->
		<script src="./res/ace/assets/js/ace-extra.min.js"></script>
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
		<script src="assets/js/html5shiv.js"></script>
		<script src="assets/js/respond.min.js"></script>
		<![endif]-->
		
		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script src="./res/ace/assets/js/jquery-2.0.3.min.js"></script>
		<!-- <![endif]-->

		<!--[if IE]>
			<script src="./res/ace/assets/js/jquery-1.10.2.min.js"></script>
		<![endif]-->

		<script src="./res/ace/assets/js/bootstrap.min.js"></script>
		<script src="./res/ace/assets/js/typeahead-bs2.min.js"></script>
		<!-- page specific plugin scripts -->

		<!--[if lte IE 8]>
		  <script src="assets/js/excanvas.min.js"></script>
		<![endif]-->
		<script src="./res/ace/assets/js/jquery-ui-1.10.3.custom.min.js"></script>
		<script src="./res/ace/assets/js/jquery-ui-1.10.3.full.min.js"></script>
		<script src="./res/ace/assets/js/jquery.ui.touch-punch.min.js"></script>
		<script src="./res/ace/assets/js/jquery.slimscroll.min.js"></script>
		<script src="./res/ace/assets/js/jquery.easy-pie-chart.min.js"></script>
		<script src="./res/ace/assets/js/jquery.sparkline.min.js"></script>
		<script src="./res/ace/assets/js/flot/jquery.flot.min.js"></script>
		<script src="./res/ace/assets/js/flot/jquery.flot.pie.min.js"></script>
		<script src="./res/ace/assets/js/flot/jquery.flot.resize.min.js"></script>
		
		<!-- 验证 -->
		<script src="./res/ace/assets/js/jquery.validate.min.js"></script>
		<link rel="stylesheet" href="./res/css/validate/validate.css" type="text/css" />
		
		<!-- 表格-->
		<script src="./res/ace/assets/js/date-time/bootstrap-datepicker.min.js"></script>
		<script src="./res/ace/assets/js/jqGrid/jquery.jqGrid.min.js"></script>
		<script src="./res/ace/assets/js/jqGrid/i18n/grid.locale-ch.js"></script>
		<script src="./res/ace/assets/js/jqGrid/jqGridUtil.js"></script>
		
		<!-- 弹出框-->
		<script src="./res/ace/assets/js/jquery.gritter.min.js"></script>
		<link rel="stylesheet" href="./res/ace/assets/css/jquery.gritter.css" />
		
		<script src="./res/ace/assets/js/bootbox.min.js"></script>

		<!-- ace scripts -->
		<script src="./res/ace/assets/js/ace-elements.min.js"></script>
		<script src="./res/ace/assets/js/ace.min.js"></script>
		
		<!-- hmq script -->
		<script src="./res/script_/hmq/hmq.js"></script>
		<script src="./res/ace/aceUtil.js"></script>
		
		
		<script type="text/javascript">
		$(function(){
			
		})
		function reloadNotice(url){
			$.hmqHomePage(url);
		}
		
		function changeHeadImg(){
			$.dialogACE({
				url:"uploader.htm?show_",
				title:"修改头像",
				frame:true,width:500,height:300,
				frameId:'headImg',
				button:{
					text: "测试头像",
				    "class" : "btn btn-pink btn-xs",
					click: function() {
						var attachId=$("#headImg")[0].contentWindow.attachId;
						if(attachId==null){
							alert("请先上传照片");
							return;
						}
						$("#avatar").attr("src","download/" + attachId + ".png");
					} 
				},
				callBack:function(dialog){
					var attachId=$("#headImg")[0].contentWindow.attachId;
					if(attachId==null){
						alert("请先上传照片");
						return;
					}
					$.hmqAJAX("user/changeImg.htm",function(data){
						alert(data.msg);
						window.location.reload()
						$(dialog).dialog("destroy")
					},{"attachId":attachId})
				}
			})	
		}
</script>
		
	</head>
	<body>
		
		<!-- *********头部开始********* -->
		<%@include file="./main/heard.jsp"%>
		<!-- *********头部结束********* -->
		
		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>

			<div class="main-container-inner">
				<a class="menu-toggler" id="menu-toggler" href="#">
					<span class="menu-text"></span>
				</a>
				<!-- *********菜单开始********* -->
				<%@include file="./main/menu.jsp"%>
				<!-- *********菜单结束********* -->


				
				<div class="main-content">
					<div class="breadcrumbs" id="breadcrumbs">
						<script type="text/javascript">
							try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
						</script>
						<ul class="breadcrumb">
							<li>
								<i class="icon-home home-icon"></i>
								<a href="#">首页</a>
							</li>
							<li id="g_menu" class="active">个人信息</li>
						</ul><!-- .breadcrumb -->
						<div class="nav-search" id="nav-search">
<!-- 							<form class="form-search"> -->
<!-- 								<span class="input-icon"> -->
<!-- 									<input type="text" placeholder="关键字 ..." class="nav-search-input" id="nav-search-input" autocomplete="off" /> -->
<!-- 									<i class="icon-search nav-search-icon"></i> -->
<!-- 								</span> -->
<!-- 							</form> -->
						</div><!-- #nav-搜索框 -->
					</div>
					<!-- *********内容部分开始********************************************************************************************************************* -->
					<div id="homePageContent" class="page-content">	</div>
					<script>
						if(${sessionModal.userType=="1"}){
							$.hmqHomePage("menuPage.htm?studentSelfPage");
						}else{
							$.hmqHomePage("menuPage.htm?teacherSelfPage");
						}
						
					</script>
					<!-- *********内容部分 结束********************************************************************************************************************* -->
			  </div><!-- /.main-content -->

<!-- 				<div class="ace-settings-container" id="ace-settings-container"> -->
<!-- 					<div class="btn btn-app btn-xs btn-warning ace-settings-btn" id="ace-settings-btn"> -->
<!-- 						<i class="icon-cog bigger-150"></i> -->
<!-- 					</div> -->
<!-- 					<div class="ace-settings-box" id="ace-settings-box"> -->
<!-- 						<div> -->
<!-- 							<div class="pull-left"> -->
<!-- 								<select id="skin-colorpicker" class="hide"> -->
<!-- 									<option data-skin="default" value="#438EB9">#438EB9</option> -->
<!-- 									<option data-skin="skin-1" value="#222A2D">#222A2D</option> -->
<!-- 									<option data-skin="skin-2" value="#C6487E">#C6487E</option> -->
<!-- 									<option data-skin="skin-3" value="#D0D0D0">#D0D0D0</option> -->
<!-- 								</select> -->
<!-- 							</div> -->
<!-- 							<span>&nbsp; 选择皮肤</span> -->
<!-- 						</div> -->
<!-- 						<div> -->
<!-- 							<input type="checkbox" class="ace ace-checkbox-2" id="ace-settings-navbar" /> -->
<!-- 							<label class="lbl" for="ace-settings-navbar"> 固定导航条</label> -->
<!-- 						</div> -->
<!-- 						<div> -->
<!-- 							<input type="checkbox" class="ace ace-checkbox-2" id="ace-settings-sidebar" /> -->
<!-- 							<label class="lbl" for="ace-settings-sidebar"> 固定滑动条</label> -->
<!-- 						</div> -->
<!-- 						<div> -->
<!-- 							<input type="checkbox" class="ace ace-checkbox-2" id="ace-settings-breadcrumbs" /> -->
<!-- 							<label class="lbl" for="ace-settings-breadcrumbs">固定面包屑</label> -->
<!-- 						</div> -->
<!-- 						<div> -->
<!-- 							<input type="checkbox" class="ace ace-checkbox-2" id="ace-settings-rtl" /> -->
<!-- 							<label class="lbl" for="ace-settings-rtl">切换到左边</label> -->
<!-- 						</div> -->
<!-- 						<div> -->
<!-- 							<input type="checkbox" class="ace ace-checkbox-2" id="ace-settings-add-container" /> -->
<!-- 							<label class="lbl" for="ace-settings-add-container">切换窄屏<b></b></label> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div>/#ace-settings-container -->
			</div><!-- /.main-container-inner -->
			
			<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="icon-double-angle-up icon-only bigger-110"></i>
			</a>
			
			<div id="modal_form_updatePwd" class="modal" tabindex="-1">
									<div class="modal-dialog">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal">&times;</button>
												<h4 class="blue bigger">请输入原密码后修改密码</h4>
											</div>

											<div class="modal-body overflow-visible">
												<div class="row">
														<div class="col-xs-12">
											<form class="form-horizontal">
												<div class="form-group">
													<label class="col-sm-4 control-label no-padding-right" for="form-field-1"> 原密码:</label>
			
													<div class="col-sm-8">
														<input type="password" id="oldpwd" class="col-xs-10 col-sm-5" />
													</div>
												</div>
			
												<div class="space-4"></div>
			
												<div class="form-group">
													<label class="col-sm-4 control-label no-padding-right" for="form-field-2">新密码:</label>
			
													<div class="col-sm-8">
														<input type="password" id="newpwd1"  class="col-xs-10 col-sm-5" />
													</div>
												</div>
												<div class="space-4"></div>
												<div class="form-group">
													<label class="col-sm-4 control-label no-padding-right" for="form-input-readonly">确认新密码:</label>
													<div class="col-sm-8">
														<input type="password" id="newpwd2" class="col-xs-10 col-sm-5" />
													</div>
												</div>
											</form>
												
												</div>
											</div>

											<div class="modal-footer">
												<button id="modifypwdcancel" type="reset" class="btn btn-sm" data-dismiss="modal">
													<i class="icon-remove"></i>
													取消
												</button>

												<button onclick="modifyPwd()" type="button" class="btn btn-sm btn-primary" >
													<i class="icon-ok"></i>
													保存
												</button>
											</div>
										</div>
									</div>
								</div>
			</div>
		</div><!-- /.main-container -->
</body>
</html>

