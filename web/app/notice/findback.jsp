<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link  rel="stylesheet" href="./res/script_/ListNav/jquery.listnav-2.1.css"></link>
<script src="./res/script_/ListNav/jquery.listnav-2.1.js?tsp=_"></script>
<script type="text/javascript">
if(!selectstudentIds){
	var selectstudentIds=new Array();
	var selectstudentNames=new Array();
}else{
	for(var i=0;i<selectstudentIds.length;i++){
		$("#stuId"+selectstudentIds[i]).attr("style","background-color:rgb(179, 219, 255)");
	}
}
jQuery(function($){
	$(".ln--").each(function(){
		//.removeClass("ln--");
	})
	$("#demoFour").listnav({
		showCounts:false,
		onClick:function(letter){
		}
	});
})
function choose(ID){
	if($(ID).attr("style")){
		//console.log("选中");
		$(ID).attr("style","")
		$.removeList(selectstudentIds,$(ID).attr("stuId"));
		$.removeList(selectstudentNames,$(ID).html());
	}else{
		//console.log("没选中")
		$(ID).attr("style","background-color:rgb(179, 219, 255)");
		selectstudentIds.push($(ID).attr("stuId"))
		selectstudentNames.push($(ID).html())
	}
	//console.log(selectstudentIds);
}
</script>
<div class="listWrapper">
	<div id="demoFour-nav"></div>
	<ul id="demoFour" style="list-style-type:none;margin: 0px">
		<c:forEach items="${students}" var="stuInfo">
			<li class="${stuInfo.ln}"><a id="stuId${stuInfo.id}" stuId="${stuInfo.id}" href="#" onclick="choose(this)">${stuInfo.name}</a></li>
		</c:forEach>
	</ul>
</div>