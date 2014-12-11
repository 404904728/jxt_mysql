<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="message-header clearfix">
										<div class="pull-left">
											<span class="blue bigger-125"> 详细信息 </span>(${leaveinfo.approveStatus})
											<input type="hidden" id="u_status" value="${u_status}"></input>
											<input type="hidden" id="infoid" value="${leaveinfo.id}"></input>
											<input type="hidden" id="self_status" value="${leaveinfo.status}"></input>
											<div class="space-4"></div>
											<div class="hr"></div>
											&nbsp;
											<img class="middle" alt="${leaveinfo.name}" src="${leaveinfo.headPic}" width="35" />
											
											&nbsp;
											<a href="#" class="sender">${leaveinfo.name}</a>
											
											&nbsp;
											<div class="space-4"></div>
											
											&nbsp;
											<i class=" icon-user orange middle"></i>
											<a href="#" class="sender">请假人：</a><span class="time">${leaveinfo.leaveUserName}</span>

											&nbsp;
											<div class="space-4"></div>
											
											&nbsp;
											<i class=" icon-leaf green middle"></i>
											<a href="#" class="sender">类型：</a><span class="time">${leaveinfo.leaveType}</span>
										&nbsp;
											<div class="space-4"></div>
									   &nbsp; <i class="icon-time bigger-110 orange middle"></i>
									   <a href="#" class="sender">时间：</a>								  
												<span class="time">${leaveinfo.startDate} - ${leaveinfo.endDate}</span>
										</div>

									</div>

									<div class="hr hr-double"></div>

									<div class="message-body">
										${leaveinfo.leaveReason}
									</div>

									<div class="hr hr-double"></div>

									<div class="message-attachment clearfix">

										<div class="attachment-images pull-right">
											<div class="vspace-sm-4"></div>
											<div>
												申请日期：${leaveinfo.writeDate}
											</div>
										</div>
									</div>
									<c:if test="${isClassLeader == 'isClassLeader'}">
										<c:if test="${leaveinfo.sendusertype == 9}">
											<div class="messagebar-item-right">
												<input type="hidden" name="leaveUserTel" value="${leaveinfo.leaveUserTel}"></input>
												<span class="inline btn-send-message">
													<button class="btn btn-sm btn-success no-border" style="margin-right: 5px" onclick="informJZ()">
														<i class="icon-ok"></i>
														<span class="bigger-120">通知家长</span>
													</button>
												</span>
											</div>
									 	</c:if>
									<c:if test="${leaveinfo.sendusertype == 1}">
										<c:if test="${leaveinfo.approveStatus ne '已确认'}">
											<div class="messagebar-item-right">
												<span class="inline btn-send-message">
													<button class="btn btn-sm btn-success no-border" style="margin-right: 5px" onclick="onclickOk('已确认')">
														<i class="icon-ok"></i>
														<span class="bigger-120">同意</span>
													</button>
												</span>
												<span class="inline btn-send-message">
													<button  class="btn btn-sm btn-warning no-border" style="margin-right: 5px" onclick="onclickOk('待确认')">
														<span class="bigger-120">待定</span>
														<i class="icon-question icon-on-right"></i>
													</button>
												</span>
												<span class="inline btn-send-message">
													<button class="btn btn-sm btn-primary no-border" style="margin-right: 5px" onclick="onclickOk('拒绝')">
														<span class="bigger-120">拒绝</span>
														<i class="icon-undo icon-on-right"></i>
													</button>
												</span>
											</div>
										</c:if>
									</c:if>	
						  </c:if>
				<script type="text/javascript">
				function onclickOk(status){
					if(status){
					    $.ajax({
					        url: "./leaveinfo.htm?updateLeaveInfo&id="+$("#infoid").val()+"&status="+encodeURIComponent(status),
					        cache: false,
					        dataType: "json",
					        type: "POST",
					        contentType: "application/x-www-form-urlencoded;charset=utf-8",
					        success: function(data) {
					        	bootbox.alert(data.msg,function(){
									Inbox.show_list();
									$('#inbox-tabs a[data-target="inbox"]').tab('show'); 
									obtainLeaveInfo(0,pagesize,null,'0',g_classid);
						        });
					       }
					    });
					}
				}

				function informJZ(){
					$.post("./leaveinfo.htm?informJZ", { tel: $("input[name = 'leaveUserTel']").val(),
						 content: $(".message-body").text()},
							   function(data){
							 	bootbox.alert(data.msg);
							   },'JSON');

				}
				</script>