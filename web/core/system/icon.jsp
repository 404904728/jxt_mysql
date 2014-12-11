<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table class="" title="图标" style="width:700px;height:600px">
		<thead>
			<tr>
				<th>图标</th>
				<th>名字</th>
				<th>路径</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${iconList}" var="icon">
				<tr>  
            		<td><img src="./res/material/icon/${icon}"/></td>
            		<td>${icon}</td>
            		<td>./res/material/icon/${icon}</td>  
        		</tr>
			</c:forEach>
		</tbody>
	</table>
