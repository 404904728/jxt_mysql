<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="hmq" uri="/WEB-INF/hmq-tags.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="./res/js/hmq/commonEasy.js"></script>
<link rel="stylesheet"  type='text/css' href="./res/css/win7/win7.css"></link>
<link rel="stylesheet"  type='text/css' href="./res/css/win7/ligerui-dialog.css"></link>
<link rel="stylesheet"  type='text/css' href="./res/css/win7/ligerui-common.css"></link>
<script type="text/javascript">
function windowShow(winId){
	$('#'+winId).window('open');
}
</script>
<title></title>
</head>
<body  style="overflow:hidden;background: url(./res/img/home/win7.jpg) no-repeat center center;">
	<div id="winlinks">
		<ul></ul>
	</div>
	<div class="l-taskbar" style="display: block; bottom: 0px">
		<div id="taskBar" class="l-taskbar-tasks" style="text-align:left;">
			
		</div>
		<div class="l-taskbar-task" onclick="$.logout();" style="float: right;">
			<div class="l-taskbar-task-icon">
				<img src="./res/img/home/png-1484.png"/>
			</div>
			<div class="l-taskbar-task-content">退出</div>
		</div>
		<!--<div class="l-clear"></div>-->
	</div>
<script>
var LINKWIDTH = 90, LINKHEIGHT = 90, TASKBARHEIGHT = 43;
var winlinksul = $("#winlinks ul");
var links = [
	    <hmq:authoried permissionId="1">
	    {icon : './res/img/home/png-0098.png',id:'orgWinHome',title : '部门用户',url : 'user.do?showPage'},
	    {icon : './res/img/home/png-0099.png',id:'roleWinHome',title : '角色管理',url : 'role.do?page'},
        {icon : './res/img/home/png-0099.png',id:'menuWinHome',title : '菜单管理',url : './home/left.jsp'},
	    </hmq:authoried>
	    {}//有个空是为了避免IE下多逗号出错
	 ];
function onResize() {
	var linksHeight = $(window).height() - TASKBARHEIGHT;
	var winlinks = $("#winlinks");
	winlinks.height(linksHeight);
	var colMaxNumber = parseInt(linksHeight / LINKHEIGHT);//一列最多显示几个快捷方式
	for ( var i = 0, l = links.length-1; i < l; i++) {
		var link = links[i];
		var jlink = $("li[linkindex=" + i + "]", winlinks);
		var top = (i % colMaxNumber) * LINKHEIGHT, left = parseInt(i/ colMaxNumber)* LINKWIDTH;
		if (isNaN(top) || isNaN(left))
			continue;
		jlink.css( {
			top : top,
			left : left
		});
	}
}
function linksInit() {
	for ( var i = 0, l = links.length-1; i < l; i++) {
		var link = links[i];
		var jlink;
		var jlink = $("<li></li>");
		jlink.attr("linkindex", i);
		jlink.append("<img src='" + link.icon+ "' />");
		jlink.append("<span>" + link.title + "</span>");
		jlink.append("<div class='bg'></div>");
		jlink.hover(function() {
			$(this).addClass("l-over");
		}, function() {
			$(this).removeClass("l-over");
		}).click(function() {
			var linkindex = $(this).attr("linkindex");
			var link = links[linkindex];
			f_open(link.url, link.title, link.icon,link.id);
			
		});
		jlink.appendTo(winlinksul);
	}
}
function f_open(url, title, icon,id) {
	if($("#"+id).length==0){
		$("body").append("<div id='"+id+"'></div>");
		$("#"+id).window({
			 width:$("body").width()*0.9,
			 height:$("body").height()*0.9,
			 href:url,title:title,
			 onClose:function(){
				console.log(2);
				$("#bar"+id).remove();
			 },
			 onDestroy:function(){
				 
			}
		});
	}else{
		$('#'+id).window('open');
	}
	if($("#bar"+id).length==0){
		$("#taskBar").append("<div id='bar"+id+"' class='l-taskbar-task' onclick=windowShow('"+id+"')><div class='l-taskbar-task-icon'><img src='"+icon+"'/></div><div class='l-taskbar-task-content' >"+title+"</div></div>");
	}
  }
$(window).resize(onResize);
linksInit();
onResize();
</script>
<div id="formWin"></div>
</body>
</html>
