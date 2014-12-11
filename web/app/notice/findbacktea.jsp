<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link  rel="stylesheet" href="./res/script_/ListNav/jquery.listnav-2.1.css"></link>
<script src="./res/script_/ListNav/jquery.listnav-2.1.js?tsp=_"></script>
<script type="text/javascript">
if(!selectteacherIds){
	var selectteacherIds=new Array();
	var selectteacherNames=new Array();
}else{
	for(var i=0;i<selectteacherIds.length;i++){
		$("#teaId"+selectteacherIds[i]).attr("style","background-color:rgb(179, 219, 255)");
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
		$.removeList(selectteacherIds,$(ID).attr("teaId"));
		$.removeList(selectteacherNames,$(ID).html());
	}else{
		//console.log("没选中")
		$(ID).attr("style","background-color:rgb(179, 219, 255)");
		selectteacherIds.push($(ID).attr("teaId"))
		selectteacherNames.push($(ID).html())
	}
	//console.log(selectteacherIds);
}
</script>
<div class="listWrapper">
	<div id="demoFour-nav"></div>
	<ul id="demoFour" style="list-style-type:none;margin: 0px">
		<c:forEach items="${teachers}" var="teaInfo">
			<li class="${teaInfo[0]}"><a id="teaId${teaInfo[0]}" teaId="${teaInfo[0]}" href="#" onclick="choose(this)">${teaInfo[1]}</a></li>
		</c:forEach>
	</ul>
</div>