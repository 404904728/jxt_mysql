<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>智慧家校通</title>
<style type="text/css">
/* 页面样式 */
body,ul,li,h1,h2,h3,blockquote,p{font-size:14px;margin:0; padding:0;list-style:none;color:#333; font-family:'微软雅黑',Arial, Helvetica, sans-serif;}
a img{border:none;}
a{text-decoration:none;color:#333;}
body{background:url()}
.logo{font-size:38px;margin:0 0 50px 50px;padding-right:300px;}.logo span{font-size:14px;margin-left:10px;}
h2{margin-left:150px;font-size:24px;margin-bottom:30px;}
.code{width:800px;margin:0 auto;border:1px dotted #333;background:#f1f1f1;padding:0 0 10px 50px;font-weight:bold;margin:50px auto;}
.code p,#parameters p{text-indent:48px;line-height:30px;}.code p.textIn,#parameters p.textIn{text-indent:24px;}.code p.notextIn,#parameters p.notextIn{text-indent:0;}
.code h3{font-size:16px;margin:10px 0;}
#parameters{width:100%;border-top:1px solid #999;background:#fff;}
#parameters p{width:800px;margin:0 auto;font-weight:bold;}#parameters p span{margin-left:30px;color:#9d6f15;}
#parameters h2{font-size:30px;margin-top:20px;font-family:'宋体';font-weight:bold;}#btn{float:right;font-size:20px;margin-top:26px;font-weight:normal;outline:none;}
</style>
<!--jdtjavascript-->
<link type="text/css" rel="stylesheet" href="./res/jdt/css/style.css" />
<link href="./home/logon/css/login.css" rel="stylesheet" rev="stylesheet" type="text/css" media="all" />
<script type="text/javascript" src="./res/jdt/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="./res/jdt/js/easing.js"></script>
<script type="text/javascript" src="./res/jdt/js/MogFocus.js"></script>
<script type="text/javascript" src="./res/script_/hmq/hmq.js"></script>
<script type="text/javascript" src="./res/logonJS/jquery.fbmodel.js"></script>
<link rel="stylesheet" type="text/css" href="./res/logonJS/fbmodal.css" />
<script type="text/javascript">
$(function(){
	$(".prev").remove();
	$(".next").remove();
	//demo2
	$("#focus").mogFocus({
		loadAnimation : false,
		time : 3000,
		scrollWidth : 850,
		animationWay : 'randomImg',
		btnStyle : 'hidden',
		randeasing : 'easeOutQuad'
	});
//    if($.booleanIE()<10){
//        alert("您使用的IE浏览器低于10版本，系统将隐藏登录框，请使用IE10及以上版本或google、firefox浏览器");
//        $("#o-box-down").hide();
//    }
})
function logonFn(id){
	if(!$.isEmpty(id)){
		$("#uId").val(id);
	}
	var l_no = $("input[name='no']").val();
	if(!l_no || l_no == 'undefined'){
		alert('请输入用户名！');
		return;
	}
	var l_pwd = $("input[name='pwd']").val();
	if(!l_pwd || l_pwd == 'undefined'){
		alert('请输入密码！');
		return;
	}

	
	$("#logonForm").formAjax(function(data){
		if(data.type==0){
			$.hmqLogon();
		}else if(data.type==1){//多名学生情况
			var stu=data.msgId.split(",");
			$("#dialog").html("");
			for(var i=0;i<stu.length;i++){
				$("#dialog").append('<a href="javascript:logonFn('+stu[i].split(":")[0]+')" id="close">'+stu[i].split(":")[1]+'</a>&nbsp;&nbsp;&nbsp;');
			}
			$("#dialog").fbmodal({},function(callback){});
		}else{
			alert(data.msg);
		}
	})
}
function clickvalidecode(img){
	img.src = img.src+'?t='+Math.random(); 
}
function enterEvent(){
	var event=arguments.callee.caller.arguments[0]||window.event;
	if (event.keyCode == 13) 
    {      
		 logonFn();
    }
}
function cutover(){
	var usertype=$("#userType").val();
	if(usertype==1){//当前是家长
		$(".error-box").html("当前是老师角色登录");
		$("#userType").val(2)
	}else{//当前是老师
		$(".error-box").html("当前是家长角色登录");
		$("#userType").val(1)
	}
}
</script>
</head>
<body style="background-color: #d7e8fd">
<div id="dialog" style="display: none;">
	
</div>
<div class="header" style="background-image: url('./home/logon/images/top.png'); ">
</div>
<div class="banner" style="background-image: url('./res/jdt/jxt/15.png')">
	<table style="margin:15px auto;">
		<tr>
			<th>
				<div id="focus">
			        <div class="focusWarp">
			            <ul class="imgList">
			                <li><a href="#"><img src="./res/jdt/jxt/one.png" width="800" height="480" /></a></li>
			                <li><a href="#"><img src="./res/jdt/jxt/two.png" width="800" height="480" /></a></li>
			                <li><a href="#"><img src="./res/jdt/jxt/three.png" width="800" height="480" /></a></li>
			                <li><a href="#"><img src="./res/jdt/jxt/four.png" width="800" height="480" /></a></li>
			            </ul>
			            <ul class="imgList_two">
			                <li><img src="./res/jdt/jxt/one_text.png" width="800" height="480" /></li>
			                <li><img src="./res/jdt/jxt/two_text.png" width="800" height="480" /></li>
			                <li><img src="./res/jdt/jxt/three_text.png" width="800" height="480" /></li>
			                <li><img src="./res/jdt/jxt/four_text.png" width="800" height="480" /></li>
			            </ul>
			        </div>
			   </div>
			</th>
			<td>
				<div class="login-aside">
				  <div id="o-box-up"></div>
				  <div id="o-box-down"  style="table-layout:fixed;">
				   <div class="error-box">当前是教师角色登录</div>
					  <form id="logonForm" action="home.htm?logonMethod"  method="post">
					  	   <input id="uId" type="hidden" name="uId"/>
						   <table id="logonFormTable">
						   		<tr height="20px"></tr>
						   		<tr>
						   		<tr>
						   		  <th width="20%"><label id="logonId" for="logonId" class="form-label">账号：</label></th>
						   		  <td width="80%"><input type="text" value="" maxlength="80" id="no"  name="no" class="i-text"  ></td> 
						   		</tr>
						   		<tr height="20px"></tr>
						   		<tr>
						   		  <th><label for="logonId" class="form-label">密码：</label></th>
						   		  <td> <input type="password" value="" maxlength="100" id="password" name="pwd" class="i-text" >    </td> 
						   		</tr>
						   		<tr height="20px"></tr>
						   		<tr>
						  		<tr>
						   		  <th> <label for="logonId" class="form-label">验证码</label></th>
						   		  <td>
						   		  	 <input type="text" value="" maxlength="100" id="yzm" class="i-text yzm" name="verifycode" onkeydown="enterEvent()" nullmsg="请输入验证码！" >  
						   		  	 <img src="./authImg.jpg" class=""  style="float: right;" onclick="clickvalidecode(this);"/>
						   		  </td> 
						   		</tr>
						   		<tr height="20px"></tr>
						   		<tr>
						   </table>
					   <input id="userType" type="hidden" name="userType" value="2"/>
					  </form>
					  <div class="fm-item">
						   <label for="logonId" class="form-label"></label>
						   <!-- 1 家长 2 老师 0管理员 -->
						   <input type="button" value="" onclick="logonFn()" tabindex="4" id="send-btn" class="btn-login"> 
						    <input type="button" value="" onclick="cutover()" tabindex="4" id="send-btn" class="btn-cutover"> 
					       <div class="ui-form-explain"></div>
					  </div>
				  </div>
				</div>
			</td>
		</tr>
	</table>
</div>
<div class="banner-shadow" style="background-color: #d7e8fd"></div>
<div class="footer">
<!--    <p>Copyright &copy; 2014-2020.<a target="_blank" href="">重庆蓝图信息产业股份有限公司</a></p> -->
</div>
</body>
</html>