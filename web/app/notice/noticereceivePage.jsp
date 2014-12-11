<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="./app/notice/notice.js"/>
<div class="tab-content no-border padding-24">
<h4 class="blue">
	<i class="green icon-inbox bigger-110"></i>
	您共有${receivelist.totalCount}条信息
</h4>
<c:forEach items="${receivelist.list}" var="receive">
	<c:if test="${receive.look==true}"><!-- 是否看过 -->
		<div class="message-item">
			 <i class="message-star icon-star-empty light-grey"></i>
			 <span class="sender" title="作业通知">
			 	<c:if test="${receive.notice.genre==0}">作业通知</c:if>
			 	<c:if test="${receive.notice.genre==1}">班级通知</c:if>
			 	<c:if test="${receive.notice.genre==2}">教务处通知</c:if>
			 	<c:if test="${receive.notice.genre==3}">学生处通知</c:if>
			 	<c:if test="${receive.notice.genre==4}">校内通告</c:if>
			 	<!-- 0作业通知 1：班级通知 2：教务处通知 3：学生处通知 4校内通告 -->
			 </span>
			 <span class="sender" title="${receive.notice.teacherInfo.name}">${receive.notice.teacherInfo.name}</span>
			 <span class="time" style="width:150px">${receive.notice.date}</span>
			 <span class="summary" onclick="showNoticeMessageLook(this,${receive.notice.id})">
			 	 <span class="message-flags"> 
			 	 	<i class="icon-reply light-grey"></i>
			 	 </span>
				<span class="text" style="width: 300px">${receive.notice.title}</span>
			 </span>
		</div>
	</c:if>
	<c:if test="${receive.look==false}">
	<div class="message-item">
		<i id="star${receive.notice.id}" class="message-star icon-star orange2"></i>
		 <span class="sender" title="作业通知">
			 	<c:if test="${receive.notice.genre==0}">作业通知</c:if>
			 	<c:if test="${receive.notice.genre==1}">班级通知</c:if>
			 	<c:if test="${receive.notice.genre==2}">教务处通知</c:if>
			 	<c:if test="${receive.notice.genre==3}">学生处通知</c:if>
			 	<c:if test="${receive.notice.genre==4}">校内通告</c:if>
			 	<!-- 0作业通知 1：班级通知 2：教务处通知 3：学生处通知 4校内通告 -->
		</span>
		<span class="sender" title="${receive.notice.teacherInfo.name}">${receive.notice.teacherInfo.name}</span>
		<span class="time" style="width: 150px">${receive.notice.date}</span> 
		<span id="${receive.notice.id}" class="summary" onclick="showNoticeMessageLook(this,${receive.notice.id})">
			<span class="badge badge-pink mail-tag"></span>
			<span class="text" style="width: 300px">${receive.notice.title} </span>
		</span>
	</div>
	</c:if>
</c:forEach>
<div class="message-footer clearfix" style="height: 48px">
	<div class="pull-left"> 共${receivelist.totalCount}条记录 </div>
	<div class="pull-right">
		<div class="inline middle">当前第${receivelist.pageNo}页，共${receivelist.pageCount}页</div>
		&nbsp; &nbsp;
		<ul class="pagination middle">
			<c:if test="${receivelist.pageNo==1}">
				<li class="disabled">
					<span><i class="icon-step-backward middle"></i></span>
				</li>
				<li class="disabled">
					<span><i class="icon-caret-left bigger-140 middle"></i></span>
				</li>
			</c:if>
			<c:if test="${receivelist.pageNo!=1}">
				<li>
					<a href="javascript:$.hmqHomePage('menuPage.htm?noticereceivePage&page=1')">
						<i class="icon-step-backward middle"></i>
					</a>
				</li>
				<li>
					<a href="javascript:$.hmqHomePage('menuPage.htm?noticereceivePage&page=${receivelist.pageNo-1}')">
						<i class="icon-caret-left bigger-140 middle"></i>
					</a>
				</li>
			</c:if>
			<li>
				<span>
					<input id="sendPageNo" value="${receivelist.pageNo}" maxlength="3" type="text" onkeyup="enterEvent()"/>
				</span>
			</li>
			<c:if test="${receivelist.pageNo==receivelist.pageCount}">
				<li class="disabled">
					<span><i class="icon-caret-right bigger-140 middle"></i></span>
				</li>
				<li class="disabled">
					<span><i class="icon-step-forward middle"></i></span>
				</li>
			</c:if>
			<c:if test="${receivelist.pageNo<receivelist.pageCount}">
				<li>
					<a href="javascript:$.hmqHomePage('menuPage.htm?noticereceivePage&page=${receivelist.pageNo+1}')">
						<i class="icon-caret-right bigger-140 middle"></i>
					</a>
				</li>
				<li>
					<a href="javascript:$.hmqHomePage('menuPage.htm?noticereceivePage&page=${receivelist.pageCount}')">
						<i class="icon-step-forward middle"></i>
					</a>
				</li>
			</c:if>
		</ul>
	</div>
</div>
</div>
<script type="text/javascript">
	function enterEvent(){
		var event=arguments.callee.caller.arguments[0]||window.event;
		if (event.keyCode == 13) {
			if($("#sendPageNo").val()<1||$("#sendPageNo").val()>${receivelist.pageCount}){
				bootbox.alert("请您输入正确的页数！");
				return;
			}
			$.hmqHomePage('menuPage.htm?noticereceivePage&page='+$("#sendPageNo").val())
	    }
	}
</script>
<!-- <div class="message-item message-unread"> -->
<!-- 	<i class="message-star icon-star orange2"></i> -->
<!-- 	<span class="sender" title="Alex John Red Smith">班主任</span> -->
<!-- 	<span class="time">1:33 pm</span>  -->
<!-- 	<span class="summary" onclick="showNoticeMessage(this)"> -->
<!-- 		<span class="badge badge-pink mail-tag"></span> -->
<!-- 		<span class="text"> 放假通知 </span> -->
<!-- 	</span> -->
<!-- </div> -->
<!-- <div class="message-item message-unread"> -->
<!-- 	 <i class="message-star icon-star-empty light-grey"></i> -->
<!-- 	 <span class="sender" title="John Doe">王大山老师</span> -->
<!-- 	 <span class="time">7:15 pm</span> -->
<!-- 	 <span class="attachment">  -->
<!-- 	 	<i class="icon-paper-clip"></i> -->
<!-- 	 </span>  附件-->
<!-- 	 <span class="summary" onclick="showNoticeMessage(this)"> -->
<!-- 	 	<span class="badge badge-pink mail-tag"></span> -->
<!-- 		<span class="text"> 今天的家庭作业 </span> -->
<!-- 	 </span> -->
<!-- </div> -->
<!-- <div class="message-item"> -->
<!-- 	 <i class="message-star icon-star-empty light-grey"></i> -->
<!-- 	 <span class="sender" title="John Doe">汪小二老师</span> -->
<!-- 	 <span class="time">7:15 pm</span> -->
<!-- 	 <span class="attachment">  -->
<!-- 	 	<i class="icon-paper-clip"></i> -->
<!-- 	 </span>  附件-->
<!-- 	 <span class="summary"> -->
<!-- 	 	 <span class="message-flags">  -->
<!-- 	 	 	<i class="icon-reply light-grey"></i> -->
<!-- 	 	 </span> -->
<!-- 		<span class="text"> 今天的家庭作业 </span> -->
<!-- 	 </span> -->
<!-- </div> -->
