<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="viewport"
	content="width=100%, initial-scale=1.0, user-scalable=no">
<meta content="telephone=no" name="format-detection">
<title>腾讯新闻</title>
<link href="./app/grab/grab.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="borderLogo">
		<c:if test="${map.grab.type==0}">
			<div class="logoImg" style="background-image: url('./app/grab/news.png');"></div>
		</c:if>
		<c:if test="${map.grab.type==1}">
			<div class="logoImg" style="background-image: url('./app/grab/introduction.png');"></div>
		</c:if>
	</div>
	<div id="content" class="main fontSize2">
		<p class="title" align="left">
			${map.grab.title}
		</p>
		<span class="src">
			${map.grab.date}
		</span>
		<p class="text">
			${map.content}
		</p>
	</div>
</body>