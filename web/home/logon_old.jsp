<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>智慧家校通</title>

<!--焦点图javascript-->
<script type="text/javascript" src="./res/script_/hmq/common.js"></script>
<link rel="stylesheet" href="./res/css/form/style.css" type="text/css">
<script type="text/javascript">
function logonFn(){
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

	var verifycode = $("input[name='verifycode']").val();
	if(!verifycode || verifycode == 'undefined'){
		alert('请输入验证码！');
		return;
	}
	
	$("#logonForm").formAjax(function(data){
		if(data.type==0){
			$.hmqLogon();
		}else{
			alert(data.msg);
		}
	})
}

function clickValideCode(img){
	img.src = img.src+'?t='+Math.random(); 
}

function enterEvent(){
	var event=arguments.callee.caller.arguments[0]||window.event;
	if (event.keyCode == 13) 
    {      
		 logonFn();
    }
}
</script>
</head>
<body style="BACKGROUND: url(./res/img/logon/lgoin_body.jpg) #d6dee0 no-repeat center top">
	<div id="login">
		<div id="login_title"></div>
		<div id="login_body">
			<form id="logonForm" action="home.htm?logonMethod" method="post">
				<TABLE>
					<TBODY>
						<TR>
							<TD width="60" align="right">用户名：</TD>
							<TD colSpan="2"><INPUT class="form_text" type="text"
								name="no"></TD>
						</TR>
						<TR>
							<TD align="right">密&nbsp&nbsp码：</TD>
							<TD colSpan="2"><INPUT class="form_text" type="password"
								name="pwd"></TD>
						</TR>
						<tr>
							<td align="right">角&nbsp&nbsp色：</td>
							<td><select class="form_select" name="userType">
									<option value="1">家长</option>
									<option value="2">教师</option>
									<option value="0">管理员</option>
							</select></td>
						</tr>
						<TR>
							<TD align="right">验证码：</TD>
							<TD><INPUT class="form_text" maxLength="4" size="8"	type="text" name="verifycode" onkeydown="enterEvent()"/></TD>
							<TD width="170"><IMG id="logonimg"
								style="PADDING-BOTTOM: 5px; PADDING-LEFT: 5px; PADDING-RIGHT: 5px; BACKGROUND: #eeeeee; CURSOR: pointer; PADDING-TOP: 5px"
								title="点击更换 " 	onclick="clickValideCode(this);"
								alt=点击更换 src="./authImg.jpg"></TD>
						</TR>
						<TR align="center">
							<TD colSpan="3"><INPUT id="login_button" onclick="logonFn()" type="button" name="submitbutton"/></TD>
						</TR>
					</TBODY>
				</TABLE>
			</form>
		</div>
		<DIV style="TEXT-ALIGN: center" id=login_bottom>
			<P>
				版权所有 2014-2024 <A href="#" target=_blank>蓝图信产</A> 保留所有权利
			</P>
			<P>Version:Business1_0_20140531</P>
		</DIV>
	</div>
</body>
</html>