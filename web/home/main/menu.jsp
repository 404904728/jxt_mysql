<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<script type="text/javascript">
function liClass(ID,child){
	//boolean b=$(ID).attr("class").indexOf("open");
	$(".oneMenu").each(function(){
		$(this).removeClass("active");
	});
	if(child){
		
	}else{
		$(ID).addClass("active");
	}
}
function oneMenuUrl(url,name){
	$("#g_menu").text(name);
	if($.isEmpty(url))return;
	$.hmqHomePage(url);
}
function twoMenuUrl(url){
	$.hmqHomePage(url);
}
//${menu.url}
</script>
<div class="sidebar" id="sidebar">
		<script type="text/javascript">
			try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
		</script>
		<!--<div class="sidebar-shortcuts" id="sidebar-shortcuts">
			<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
				<button name="default" class="btn btn-success"><i class="icon-leaf"></i></button>
				<button name="skin-1" class="btn btn-info"><i class="icon-leaf"></i></button>
				<button name="skin-2"class="btn btn-warning"><i class="icon-leaf"></i></button>
				<button name="skin-3" class="btn btn-danger"><i class="icon-leaf"></i></button>
			</div>
			<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
				<span class="btn btn-success"></span><span class="btn btn-info"></span>
				<span class="btn btn-warning"></span><span class="btn btn-danger"></span>
			</div>
		</div> #sidebar-shortcuts -->
		<ul class="nav nav-list">
			<c:forEach items="${menus}" var="menu">
				<li onclick="liClass(this,${menu.child!=null})" class="oneMenu">
					<a href="#" onclick="oneMenuUrl('${menu.url}','${menu.name}')" class="dropdown-toggle">
						<i class="${menu.icon}"></i>
						<c:if test="${menu.child!=null}">
							<span class="menu-text">
								${menu.name}<span class="badge badge-primary ">${menu.childsize}</span>
							</span>
							<b class="arrow icon-angle-down"></b>
						</c:if>
						<c:if test="${menu.child==null}">
							<span class="menu-text">
								${menu.name}
							</span>
						</c:if>
					</a>
					<c:if test="${menu.child!=null}">
						<ul class="submenu">
							<c:forEach items="${menu.child}" var="menuSon">
								<li><a href="#" onclick="twoMenuUrl('${menuSon.url}')"><i class="icon-double-angle-right"></i>${menuSon.name}</a></li>
							</c:forEach>
						</ul>
					</c:if>
				</li>
			</c:forEach>
		</ul>
		<div class="sidebar-collapse" id="sidebar-collapse">
			<i class="icon-double-angle-left" data-icon1="icon-double-angle-left" data-icon2="icon-double-angle-right"></i>
		</div>
		<script type="text/javascript">
			try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
		</script>
	</div>
</body>
</html>