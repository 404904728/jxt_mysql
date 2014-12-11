<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!-- message-inline-open  <%@include file="noticedetail.jsp" %>-->
<h4 class="blue">
	
</h4>
<c:forEach items="${sendlist.list}" var="send">
	<div class="message-item">
		 <i class="message-star icon-star-empty light-grey"></i>
		 <span class="sender">
		 	<c:if test="${send.genre==0}">作业通知</c:if>
		 	<c:if test="${send.genre==1}">班级通知</c:if>
		 	<c:if test="${send.genre==2}">教务处通知</c:if>
		 	<c:if test="${send.genre==3}">学生处通知</c:if>
		 	<c:if test="${send.genre==4}">年级组通知</c:if>
            <c:if test="${send.genre==5}">招生办通知</c:if>
            <c:if test="${send.genre==6}">行政办通知</c:if>
            <c:if test="${send.genre==7}">研修室通知</c:if>
		 	<!-- 0作业通知 1：班级通知 2：教务处通知 3：学生处通知 4校内通告 -->
		</span>
		<span class="sender" title="${send.teacherInfo.name}"></span>
		<span class="time" style="width: 150px">${send.date}</span>
		<!-- 	 <span class="attachment">  -->
		<!-- 	 	<i class="icon-paper-clip"></i> -->
		<!-- 	 </span>  附件-->
		 <span class="summary" onclick="showNoticeMessage(this,${send.id})"><span class="message-flags"></span>
         <span class="text" style="width: 300px">标题：${send.title} </span>
             <c:if test="${send.shortMsg==true}">
                 <div class="action-buttons inline" title="该通知推送了短信">
                    <a href="#">
                        <i class="icon-envelope-alt bigger-125 red"></i>
                    </a>
                </div>
             </c:if>
		</span>
	</div>
</c:forEach>
<div class="message-footer clearfix">
	<div class="pull-left"> 共${sendlist.totalCount}条记录 </div>
	<div class="pull-right">
		<div class="inline middle">当前第${sendlist.pageNo}页，共${sendlist.pageCount}页</div>
		&nbsp; &nbsp;
		<ul class="pagination middle">
			<c:if test="${sendlist.pageNo==1}">
				<li class="disabled">
					<span><i class="icon-step-backward middle"></i></span>
				</li>
				<li class="disabled">
					<span><i class="icon-caret-left bigger-140 middle"></i></span>
				</li>
			</c:if>
			<c:if test="${sendlist.pageNo!=1}">
				<li>
					<a href="javascript:$.hmqRefreshPage('sent','notice/noticeinfo.htm?page=1&type=${noticeType}&draft=false')">
						<i class="icon-step-backward middle"></i>
					</a>
				</li>
				<li>
					<a href="javascript:$.hmqRefreshPage('sent','notice/noticeinfo.htm?page=${sendlist.pageNo-1}&type=${noticeType}&draft=false')">
						<i class="icon-caret-left bigger-140 middle"></i>
					</a>
				</li>
			</c:if>
			<li>
				<span>
					<input id="sendPageNo" value="${sendlist.pageNo}" maxlength="3" type="text" onkeyup="enterEvent()"/>
				</span>
			</li>
			<c:if test="${sendlist.pageNo==sendlist.pageCount}">
				<li class="disabled">
					<span><i class="icon-caret-right bigger-140 middle"></i></span>
				</li>
				<li class="disabled">
					<span><i class="icon-step-forward middle"></i></span>
				</li>
			</c:if>
			<c:if test="${sendlist.pageNo<sendlist.pageCount}">
				<li>
					<a href="javascript:$.hmqRefreshPage('sent','notice/noticeinfo.htm?page=${sendlist.pageNo+1}&type=${noticeType}&draft=false')">
						<i class="icon-caret-right bigger-140 middle"></i>
					</a>
				</li>
				<li>
					<a href="javascript:$.hmqRefreshPage('sent','notice/noticeinfo.htm?page=${sendlist.pageCount}&type=${noticeType}&draft=false')">
						<i class="icon-step-forward middle"></i>
					</a>
				</li>
			</c:if>
		</ul>
	</div>
</div>
<script type="text/javascript">
	function enterEvent(){
		var event=arguments.callee.caller.arguments[0]||window.event;
		if (event.keyCode == 13) {
			if($("#sendPageNo").val()<1||$("#sendPageNo").val()>${sendlist.pageCount}){
				bootbox.alert("请您输入正确的页数！");
				return;
			}
			$.hmqRefreshPage('sent','notice/noticeinfo.htm?page='+$("#sendPageNo").val()+'&type='+${noticeType}+'&draft=false')
	    }
	}
</script>