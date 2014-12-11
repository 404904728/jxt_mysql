<%--
  Created by IntelliJ IDEA.
  User: cqmonster
  Date: 14-9-5
  Time: 上午11:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
<title>智慧家校通-登录</title>
<%--<link href="http://fonts.googleapis.com/css?family=Lato:100,300,400,700" media="all" rel="stylesheet"--%>
<%--type="text/css"/>--%>
<link href="./res/7en/stylesheets/bootstrap.min.css" media="all"
	rel="stylesheet" type="text/css" />
<link href="./res/7en/stylesheets/font-awesome.css" media="all"
	rel="stylesheet" type="text/css" />
<link href="./res/7en/stylesheets/se7en-font.css" media="all"
	rel="stylesheet" type="text/css" />
<link href="./res/7en/stylesheets/style.css" media="all"
	rel="stylesheet" type="text/css" />
<script src="./res/ace/assets/js/jquery-1.10.2.min.js"></script>
<script src="./res/ace/assets/js/jquery-ui-1.10.3.full.min.js"></script>
<script src="./res/7en/javascripts/bootstrap.min.js"
	type="text/javascript"></script>
<script src="./res/7en/javascripts/raphael.min.js"
	type="text/javascript"></script>
<script src="./res/7en/javascripts/jquery.mousewheel.js"
	type="text/javascript"></script>
<script src="./res/7en/javascripts/jquery.bootstrap.wizard.js"
	type="text/javascript"></script>
<script src="./res/7en/javascripts/jquery.inputmask.min.js"
	type="text/javascript"></script>
<script src="./res/7en/javascripts/jquery.validate.js"
	type="text/javascript"></script>
<script src="./res/7en/javascripts/main.js" type="text/javascript"></script>
<script src="./res/7en/javascripts/respond.js" type="text/javascript"></script>
<script src="./res/script_/hmq/hmq.js" type="text/javascript"></script>
<meta
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"
	name="viewport">
<script type="text/javascript">
	function logonFn(type,uid) {
		var l_no = $("input[name='no']").val();
		if (!l_no || l_no == 'undefined') {
			alert('请输入用户名！');
			return;
		}
		var l_pwd = $("input[name='pwd']").val();
		if (!l_pwd || l_pwd == 'undefined') {
			alert('请输入密码！');
			return;
		}
		$("#userType").val(type);
		if(!$.isEmpty(uid)){
			$("#uId").val(uid);
		}
		$("form").formAjax(
				function(data) {
					console.log(data);
					if (data.type == 0) {
						window.open("home.htm", "_self");
					} else if (data.type == 1) {//多名学生情况
						var stu = data.msgId.split(",");
						$("#dialog").html("");
						for (var i = 0; i < stu.length; i++) {
							$("#dialog").append('<a class="btn btn-primary" href="#" onclick="logonFn(1,'+ stu[i].split(":")[0]+ ')"><i class="icon-user"></i>'+ stu[i].split(":")[1]+ '</a>');
						}
						$("#studentModal").click();
					} else {
						alert(data.msg);
					}
				})
	}
	function ss(){
		$("#myModal").toggle();
	}
</script>
</head>
<body class="login1">
	<!-- Login Screen -->
	<div class="login-wrapper">
		<div class="login-container">
			<a href="#"> 成都七中实验智慧家校通登录 </a>

			<form action="home.htm?logonMethod" method="post">
				<input id="uId" type="hidden" name="uId"/>
				<input type="hidden" name="mobile" value="true"> <input
					id="userType" type="hidden" name="userType" value="2">
				<div class="form-group">
					<input class="form-control" name="no" placeholder="用户名" type="text">
				</div>
				<div class="form-group">
					<input class="form-control" name="pwd" placeholder="密码"
						type="password">
				</div>
				<div class="form-options clearfix">
					<a class="pull-right" href="#">忘记密码?</a>

					<div class="text-left">
						<label class="checkbox"><input type="checkbox"><span>记住我</span></label>
					</div>
				</div>
			</form>
			<div class="social-login clearfix">
				<a class="btn btn-primary pull-right twitter" href="javascript:logonFn(2)" id="teacherLogon">
					<i class="icon-user"></i>教师端登录
				</a>
			    <a class="btn btn-primary pull-left facebook"   href="javascript:logonFn(1)" id="studentLogon">
			    	<i class="icon-comments"></i>家长端登录
			    </a>
			     <a class="btn btn-primary  facebook" style="display: none"  data-toggle="modal" href="#myModal" id="studentModal">
			    	<i class="icon-comments"></i>家长端登录
			    </a>
			</div>
			<p class="signup">
				手机APP请点击<a href="home.htm?qrcod">这里</a>扫描二维码下载
			</p>
		</div>
		<div class="modal fade" id="myModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button aria-hidden="true" class="close" data-dismiss="modal"
							type="button">&times;</button>
						<h4 class="modal-title">提示</h4>
					</div>
					<div class="modal-body">
						<h1>请选择要查看的学生</h1>
						<p id="dialog">
							<a class="btn btn-primary  twitter" href="javascript:logonFn(2)"><i class="icon-user"></i>教师端登录</a> 
						</p>
					</div>
					<div class="modal-footer">
						<button class="btn btn-primary" type="button">确认</button>
						<button class="btn btn-default-outline" data-dismiss="modal"
							type="button">取消</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>