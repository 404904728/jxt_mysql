<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
	var studentId = '<%=request.getParameter("userName") %>';
	
</script>

<!-- page specific plugin scripts -->
<script src="res/ace/assets/js/jquery.colorbox-min.js"></script>
<script src="res/script_/hmq/stuinfo/imageread.js"></script>
<!-- ace scripts -->
<div class="alert alert-success" style="margin-bottom: 5px">
	<i class="icon-reply green"></i>
	<button class="btn btn-link" onclick="rollBackStuInfo()">返回学生信息</button>
</div>
<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->

		<div class="row-fluid">
			<input type="hidden" id="stuid_value" value="${stuId}">
			<ul id="image_query_to_read" class="ace-thumbnails">
				<c:choose>
					<c:when test="${empty modeList}">
						<li>
							<img alt="150x150" width="150" height="150" src="res/img/stuinfo/noimage.png" />
						</li>
					</c:when>
					<c:otherwise>
						<c:forEach items="${modeList}" var="img">
							<li><a href="${img.htmlUrl}" data-rel="colorbox"> 
									<img alt="150x150" width="150" height="150" src="${img.htmlUrl}" />
									<div class="text">
										<div class="inner">${img.fileName}</div>
									</div> <!--<div class="tags">
													<span class="label-holder">
														<span class="label label-warning arrowed-in" >${img.htmlUrl }</span>
													</span>
												</div>
											--></a> <c:if test="${userType !='1'}">
									<div class="tools tools-bottom">
										<a href="javascript:void(0)" onclick="deleteImage(${img.id})">
											<i class="icon-remove red"></i>
										</a>
									</div>
								</c:if>
							</li>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
		<!-- PAGE CONTENT ENDS -->
	</div>
	<!-- /.col -->
</div>
<!-- /.row -->
