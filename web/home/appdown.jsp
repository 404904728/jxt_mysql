<%--
  Created by IntelliJ IDEA.
  User: cqmonster
  Date: 14-9-5
  Time: 上午11:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>智慧家校通-登录</title>
    <%--<link href="http://fonts.googleapis.com/css?family=Lato:100,300,400,700" media="all" rel="stylesheet"--%>
    <%--type="text/css"/>--%>
    <link href="./res/7en/stylesheets/bootstrap.min.css" media="all" rel="stylesheet" type="text/css"/>
    <link href="./res/7en/stylesheets/font-awesome.css" media="all" rel="stylesheet" type="text/css"/>
    <link href="./res/7en/stylesheets/se7en-font.css" media="all" rel="stylesheet" type="text/css"/>
    <link href="./res/7en/stylesheets/style.css" media="all" rel="stylesheet" type="text/css"/>
    <script src="./res/ace/assets/js/jquery-1.10.2.min.js"></script>
    <script src="./res/ace/assets/js/jquery-ui-1.10.3.full.min.js"></script>
    <script src="./res/7en/javascripts/bootstrap.min.js" type="text/javascript"></script>
    <script src="./res/7en/javascripts/raphael.min.js" type="text/javascript"></script>
    <script src="./res/7en/javascripts/jquery.mousewheel.js" type="text/javascript"></script>
    <script src="./res/7en/javascripts/jquery.bootstrap.wizard.js" type="text/javascript"></script>
    <script src="./res/7en/javascripts/jquery.inputmask.min.js" type="text/javascript"></script>
    <script src="./res/7en/javascripts/jquery.validate.js" type="text/javascript"></script>
    <script src="./res/7en/javascripts/main.js" type="text/javascript"></script>
    <script src="./res/7en/javascripts/respond.js" type="text/javascript"></script>
    <script src="./res/script_/hmq/hmq.js" type="text/javascript"></script>
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
    <script type="text/javascript">
        function logonFn() {
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
            $("form").formAjax(function(data) {
                if (data.type == 0) {
                    window.open("home.htm", "_self");
                } else if (data.type == 1) {//多名学生情况

                } else {
                    alert(data.msg);
                }
            })
        }
    </script>
    <style type="text/css">
    	#down img{
    		margin: 0 auto;
    	}
    </style>
</head>
<body class="login1">
<!-- Login Screen -->
<div class="login-wrapper">
    <div class="login-container" style="">
        <a href="#">
           	扫描二维码下载手机APP
        </a>
        <table id="down" style="margin: 0 auto">
        	<tr>
        		<th>android家长端</th>
        		<th>ios家长端</th>
        	</tr>
        	<tr>
        		<th><img src="./home/android_jz.png" width="120px" height="100px" alt="" /></th>
        		<th><img src="./home/ios_jz.png" width="120px" height="100px" alt="" /></th>
        	</tr>
        	<tr>
        		<th>android老师端</th>
        		<th>ios老师端</th>
        	</tr>
        	<tr>
        		<td><img src="./home/android_js.png" width="120px" height="100px" alt="" /></td>
        		<th><img src="./home/ios_js.png" width="120px" height="100px" alt="" /></th>
        	</tr>
        </table>
        <p class="signup">
           	点击<a href="home.htm">这里</a>返回登录
        </p>
    </div>
</div>
</body>
</html>