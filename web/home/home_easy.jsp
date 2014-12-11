<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
     <head>
     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
     <title>智慧家校通</title>
     <script type="text/javascript" src="./res/script_/hmq/commonEasy.js"></script>
     <link rel="stylesheet" href="./res/css/home/style_home.css" type="text/css" />
     <script type="text/javascript">
     $(function(){
         $("#homeWin").window({
     		 width:$(self).width()*0.8,height:$(self).height()*0.8,closed:true,modal:true,minimizable:false
    	 });	
 		 /**一级菜单控制*/
         $(".sf-menu li").click(function(){
         	$(".current").attr("class","");
         	$(this).attr("class","current");
        		$("#homeLayout").layout("panel", "center").panel("refresh",$(this).attr("url")+"&mId="+$(this).attr("mId"));
         })
     });
     function refSubMenu(layoutId){/**二级菜单控制*/
     	$(".submenu li").click(function(){
			$(".selected").attr("class","");
			$(this).find("a").attr("class","selected");
			$("#"+layoutId).layout("panel", "center").panel("refresh",$(this).attr("url"));
		})
     }
     </script>
    </head>
    <body>
    	<div class="easyui-panel" data-options="fit:true" style="border-color: #9a9899">
    		  <div id="homeLayout" class="easyui-layout" data-options="fit:true" style="border:1px #9a9899 solid;border-color: #9a9899;">
		            <div data-options="region:'north',border:true" style="height:115px;border-color: #9a9899;">
						<div id="top"><!-- 菜单 -->
							<div class="wrapper">
								<div id="title">
									<span>智慧</span>家校通</div>
								<!-- Top navigation -->
								<div id="topnav">
									<a href="#"><img class="avatar" SRC="./res/lanheimoban/img/user_32.png" alt="" /></a>
									您好！ <b>${sessionModal.name}</b>
									<span>|</span> <a href="#">设置</a>
									<span>|</span> <a href="javascript:$.logout()">退出</a><br/><br/>
									<small>您有<a href="#" class="high"><b>1</b> 条新消息</a></small>
								</div>
								<ul class="sf-menu">
									<c:forEach var="menu" items="${menus}">
										<li mId="${menu.id}" url="${menu.attributes}"><a href="#">${menu.text}</a></li>
									</c:forEach>
								</ul>
							    </div>
						</div>
	            </div>
	            <div data-options="region:'south',border:true" style="height:30px;text-align:center;">
	                Copyright © 2013-1-25开始
	            </div>
	            <div data-options="region:'center',border:true"></div>
	        </div>
    	</div>
    	<div id="homeWin" style="padding:5px"></div>
    </body>
</html>