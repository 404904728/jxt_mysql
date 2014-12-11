<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="h" uri="/hmq-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="./app/notice/notice.js"/>
<script type="text/javascript">
    function panduan(){
        $('#frameWrite').attr('src','notice/noticeform.htm?noticeType=${noticeType}')
    }
</script>
<div class="page-header">
	<h1>
		<c:if test="${noticeType==0}">作业通知</c:if>
	 	<c:if test="${noticeType==1}">班级通知</c:if>
	 	<c:if test="${noticeType==2}">教务处通知</c:if>
	 	<c:if test="${noticeType==3}">学生处通知</c:if>
	 	<c:if test="${noticeType==4}">年级组通知</c:if>
	 	<c:if test="${noticeType==5}">招生办通知</c:if>
	 	<c:if test="${noticeType==6}">行政办通知</c:if>
	 	<c:if test="${noticeType==7}">研修室通知</c:if>
		<small>
			<i class="icon-double-angle-right"></i>
			这里可以看到你已发送的通知
			<c:if test="${noticeType==0}">作业通知</c:if>
		 	<c:if test="${noticeType==1}">班级通知</c:if>
		 	<c:if test="${noticeType==2}">教务处通知</c:if>
		 	<c:if test="${noticeType==3}">学生处通知</c:if>
		 	<c:if test="${noticeType==4}">年级组通知</c:if>
		 	<c:if test="${noticeType==5}">招生办通知</c:if>
	 		<c:if test="${noticeType==6}">行政办通知</c:if>
	 		<c:if test="${noticeType==7}">研修室通知</c:if>
		</small>
	</h1>
</div><!-- /.page-header -->
<div id="notice-container" class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="row">
			<div class="col-xs-12">
				<div class="tabbable">
					<ul id="inbox-tabs" class="inbox-tabs nav nav-tabs padding-16  tab-space-1"><!-- tab-size-bigger -->
						   <li class="in active">
							<a data-toggle="tab" href="#sent">
								<i class="orange icon-location-arrow bigger-130 "></i>
								<span class="bigger-110">发件箱</span>
							</a>
							</li>
<!-- 								<li> -->
<!-- 									<a data-toggle="tab" href="#draft"> -->
<!-- 										<i class="green icon-pencil bigger-130"></i> -->
<!-- 										<span class="bigger-110">草稿箱</span> -->
<!-- 									</a> -->
<!-- 								</li> -->
							<li class="li-new-mail pull-right">
								<a data-toggle="tab" href="#write" onclick="panduan()" class="btn-new-mail">
									<span class="btn bt1n-small btn-purple no-border">
										<i class=" icon-envelope bigger-130"></i>
										<span class="bigger-110">写通知</span>
									</span>
								</a>
							</li>
						<!-- ./li-new-mail -->
					</ul>
					<div class="tab-content no-border padding-24">
						<div id="sent" class="tab-pane fade in active">
							<!-- 发件箱div -->
								 <%@include file="noticesendlist.jsp"%>
						</div>
						<%--<div id="draft" class="tab-pane fade">--%>
							  <%--<!-- 草稿箱div-->--%>
							    <%--<%@include file="noticesdraftlist.jsp"%>--%>
						<%--</div>--%>
						<div id="write" class="tab-pane fade">
							<iframe id="frameWrite" width="100%" height="500px" style="border:0px;index-z:-999px" >
							</iframe>
						</div>
					</div>
				</div><!-- /.tabbable -->
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.col -->
</div><!-- /.row -->