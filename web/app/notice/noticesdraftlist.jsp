<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!-- message-inline-open  <%@include file="noticedetail.jsp" %>-->
<h4 class="blue">
</h4>
<c:forEach items="${draftlist.list}" var="draft">
	<div class="message-item" id="draft${draft.id}">
		 <span class="sender">
		 	<c:if test="${draft.genre==0}">作业通知</c:if>
		 	<c:if test="${draft.genre==1}">班级通知</c:if>
		 	<c:if test="${draft.genre==2}">教务处通知</c:if>
		 	<c:if test="${draft.genre==3}">学生处通知</c:if>
		 	<c:if test="${draft.genre==4}">校内通告</c:if>
		 	<!-- 0作业通知 1：班级通知 2：教务处通知 3：学生处通知 4校内通告 -->
		</span>
		<span class="sender" title="John Doe">${draft.teacherInfo.name}</span>
		<span class="time" style="width: 150px">${draft.date}</span>
		<!-- 	 <span class="attachment">  -->
		<!-- 	 	<i class="icon-paper-clip"></i> -->
		<!-- 	 </span>  附件-->
		<span class="summary" onclick="draftNoticeMessage(this,${draft.id})"> <span class="message-flags"> <i
				class="icon-reply light-grey"></i>
		</span> <span class="text" style="width: 300px">${draft.title} </span>
		</span>
	</div>
</c:forEach>
<div class="message-footer clearfix">
	<div class="pull-left"> 共${draftlist.totalCount}条记录 </div>
	<div class="pull-right">
		<div class="inline middle">当前第${draftlist.pageNo}页，共${draftlist.pageCount}页</div>
		&nbsp; &nbsp;
		<ul class="pagination middle">
			<c:if test="${draftlist.pageNo==1}">
				<li class="disabled">
					<span><i class="icon-step-backward middle"></i></span>
				</li>
				<li class="disabled">
					<span><i class="icon-caret-left bigger-140 middle"></i></span>
				</li>
			</c:if>
			<c:if test="${draftlist.pageNo!=1}">
				<li>
					<a href="javascript:$.hmqRefreshPage('draft','notice/noticeinfo.htm?page=1&type=${noticeType}&draft=true')">
						<i class="icon-step-backward middle"></i>
					</a>
				</li>
				<li>
					<a href="javascript:$.hmqRefreshPage('draft','notice/noticeinfo.htm?page=${draftlist.pageNo-1}&type=${noticeType}&draft=true')">
						<i class="icon-caret-left bigger-140 middle"></i>
					</a>
				</li>
			</c:if>
			<li>
				<span>
					<input id="draftPageNo" value="${draftlist.pageNo}" maxlength="3" type="text" onkeyup="enterEventdraft()"/>
				</span>
			</li>
			<c:if test="${draftlist.pageNo==draftlist.pageCount}">
				<li class="disabled">
					<span><i class="icon-caret-right bigger-140 middle"></i></span>
				</li>
				<li class="disabled">
					<span><i class="icon-step-forward middle"></i></span>
				</li>
			</c:if>
			<c:if test="${draftlist.pageNo<draftlist.pageCount}">
				<li>
					<a href="javascript:$.hmqRefreshPage('draft','notice/noticeinfo.htm?page=${draftlist.pageNo+1}&type=${noticeType}&draft=true')">
						<i class="icon-caret-right bigger-140 middle"></i>
					</a>
				</li>
				<li>
					<a href="javascript:$.hmqRefreshPage('draft','notice/noticeinfo.htm?page=${draftlist.pageCount}&type=${noticeType}&draft=true')">
						<i class="icon-step-forward middle"></i>
					</a>
				</li>
			</c:if>
		</ul>
	</div>
</div>
<script type="text/javascript">
	function enterEventdraft(){
		var event=arguments.callee.caller.arguments[0]||window.event;
		if (event.keyCode == 13) {
			if($("#draftPageNo").val()<1||$("#draftPageNo").val()>${draftlist.pageCount}){
				bootbox.alert("请您输入正确的页数！");
				return;
			}
			$.hmqRefreshPage('draft','notice/noticeinfo.htm?page='+$("#draftPageNo").val()+'&type='+${noticeType}+'&draft=true')
	    }
	}
</script>