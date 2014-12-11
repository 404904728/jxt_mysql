<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
		<div class="alert alert-success" style="margin-bottom: 5px">
			<i class="icon-reply green"></i>
			<button class="btn btn-link" onclick="rollBackCheck()">返回考勤信息</button>
		</div>
		<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
		<div class="widget-box">
							<div class="widget-header">
								<h4 class="smaller">
									<i class="icon-comments smaller-80"></i>
									${checkCount.title}
								</h4>
									${checkCount.stitle}
							</div>
							<div class="widget-body" style="height: 0px;">
								<div class="widget-main">
									
				<c:if test="${checkCount.flg == 0 || checkCount.flg == '0'}">
					<c:forEach items="${checkCount.content}" var="con" varStatus="status">
						<div class="pricing-span" id="testId">
							<div class="widget-box pricing-box-small">
								<c:choose>
									<c:when test="${con.flgId == 1 || con.flgId == '1'}">
										<c:if test="${status.index%2 == 0}">
											<div class="widget-header header-color-blue5">
										</c:if>
										<c:if test="${status.index%2 != 0}">
											<div class="widget-header header-color-blue4">
										</c:if>
									</c:when>
									<c:otherwise>
										<div class="widget-header header-color-orange">
									</c:otherwise>
								</c:choose>
										<!--<h5 class="bigger lighter">${con.flgValue}</h5>-->
							</div>
							<div class="widget-body">
								<div class="widget-main no-padding">
									<ul class="list-unstyled list-striped pricing-table">
										<li><dt>${con.flgValue}<dt></li>
									</ul>
								</div>
								<a href="javascript:void(0)" style="cursor:default" class="btn btn-block btn-sm btn-blue">
									<c:choose>
										<c:when test="${con.flgId == 1 || con.flgId == '1'}">
											<span>已到&nbsp;</span>
										</c:when>
										<c:otherwise>
											<span>未到&nbsp;</span>
										</c:otherwise>
									</c:choose>
								</a>
							</div>
						   </div>
						 </div>
					</c:forEach>
				</c:if>
							<c:if test="${checkCount.flg == 3 || checkCount.flg == '3'}">
								<div class="pricing-span" id="testId">
									<div class="widget-box pricing-box-small">
											<div class="widget-body">
												<div class="widget-main no-padding">
													<ul class="list-unstyled list-striped pricing-table">
														<li><dt>暂无数据<dt></li>
													</ul>
												</div>
											</div>
										</div>
									</div>
							</c:if>
				
											
								</div>
							</div>
		</div>
		
				</div>
			</div>
</div>
<script type="text/javascript">
	/* $(document).ready(function(){
		$("#mainDiv").empty();
		var htmlVal = '';
		for(var i = 0; i < 46; i++){
			htmlVal += '<div class="pricing-span" id="testId'+ i +'">' + 
			'<div class="widget-box pricing-box-small">';
			//if(i%5 == 1){
			//	htmlVal += '<div class="widget-header header-color-red3"><h5 class="bigger lighter">'+ (i+1) +'</h5></div>';
			//}else if(i%5 == 2){
			//	htmlVal += '<div class="widget-header header-color-orange"><h5 class="bigger lighter">'+ (i+1) +'</h5></div>';
			//}else if(i%5 == 3){
			//	htmlVal += '<div class="widget-header header-color-blue"><h5 class="bigger lighter">'+ (i+1) +'</h5></div>';
			//}else if(i%5 == 4){
			//	htmlVal += '<div class="widget-header header-color-green"><h5 class="bigger lighter">'+ (i+1) +'</h5></div>';
			//}else if(i%5 == 0){
				
				htmlVal += '<div class="widget-header header-color-green"><h5 class="bigger lighter">'+ (i+1) +'</h5></div>';
			//}
			htmlVal += '<div class="widget-body">' + 
			'<div class="widget-main no-padding">' +
			'<ul class="list-unstyled list-striped pricing-table">' + 
			'<li><dt>王小二<dt></li></ul>' + 
			''+
			'</div>'+
			'<a href="javascript:void(0)" onclick="clickweidao(this)" class="btn btn-block btn-sm btn-blue"><span>未到&nbsp;</span></a></div></div></div>';
		}
		$("#mainDiv").append(htmlVal);
	}); */

	
	function rollBackCheck(){
		$("#addCheckJsp").css('display','none');
		$("#chckmain").css('display','block');
	}
	function clickweidao(v){
		//console.info($(v).parent().parent().attr("class"));
	}
	function removeElement(_this,id){
		$("#" + id).fadeOut("slow");
	}
	function addElement(_this,id){
		$("#" + id).fadeOut("slow");
	}

</script>