<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script language="javascript" type="text/javascript" src="./res/script_/date/datePicker/WdatePicker.js"></script>
<script src="./res/ace/assets/js/select2.min.js"></script>	
   <script type="text/javascript">
	/** 分页 */
	var total =  "${pageList.totalCount}";
	var pagecount = "${pageList.pageCount}";
	var pagesize =  "${pageList.pageSize}";
	if(!pagesize){
		pagesize = 10;
	}
	var pageno =  "${pageList.pageNo}";
	var g_classid = null;
	var u_status = null;
	var resubmit = false;
				//handling tabs and loading/displaying relevant messages and forms
				//not needed if using the alternative view, as described in docs
				var prevTab = 'inbox'
				$('#inbox-tabs a[data-toggle="tab"]').on('show.bs.tab', function (e) {
					var currentTab = $(e.target).data('target');
					if(currentTab == 'write') {
						Inbox.show_form();
					}
					else {
						if(prevTab == 'write')
							Inbox.show_list();
			
						//load and display the relevant messages 
					}
					prevTab = currentTab;
				})
			
			
				//back to message list
				$('.btn-back-message-list').on('click', function(e) {
					e.preventDefault();
					Inbox.show_list();
					$('#inbox-tabs a[data-target="inbox"]').tab('show'); 
					if($("#u_status").val() != 'none'){
						var sk = $("#searchkey").val();
						if(sk){
							obtainLeaveInfo(0,pagesize,encodeURIComponent(sk),'0',g_classid);
						}else{
							obtainLeaveInfo(0,pagesize,null,'0',g_classid);
						}
					}
				});
				
				//submit
				$('.btn-send-message').on('click', function(e) {
					e.preventDefault();
					if($.isEmpty($("input[name='startDate']").val())){
						bootbox.alert("请输入开始时间！");
						return;
					}
					if($.isEmpty($("input[name='endDate']").val())){
						bootbox.alert("请输入结束时间！");
						return;
					}
					if($.isEmpty($("textarea[name='leaveReason']").val())){
						bootbox.alert("请输入请假事由！");
						return;
					}
					
					if(g_classid){
						var qingjiaren = $("input[name='receiverId']").val();
						if(qingjiaren){
							$("input[name='leaveUser.id']").val(qingjiaren);
						}
					}
					if(resubmit){
						return false;
					}
					resubmit = true;
					$('#id-message-form').submit();
				});
			
				var Inbox = {}
			
				//show message list (back from writing mail or reading a message)
				Inbox.show_list = function() {
					$('.message-navbar').addClass('hide');
					$('#id-message-list-navbar').removeClass('hide');
			
					$('.message-footer').addClass('hide');
					$('.message-footer:not(.message-footer-style2)').removeClass('hide');
			
					$('.message-list').removeClass('hide').next().addClass('hide');
					//hide the message item / new message window and go back to list
				}
			
				//show write mail form
				Inbox.show_form = function() {
					if($('.message-form').is(':visible')) return;
					var message = $('.message-list');
					$('.message-container').append('<div class="message-loading-overlay"><i class="icon-spin icon-spinner orange2 bigger-160"></i></div>');
					
					setTimeout(function() {
						message.next().addClass('hide');
						
						$('.message-container').find('.message-loading-overlay').remove();
						
						$('.message-list').addClass('hide');
						$('.message-footer').addClass('hide');
						$('.message-form').removeClass('hide').insertAfter('.message-list');
						
						$('.message-navbar').addClass('hide');
						$('#id-message-new-navbar').removeClass('hide');
						//reset form??
						$('.message-form').get(0).reset();
						
					}, 300 + parseInt(Math.random() * 300));
				}



				function onclickhang(aa){
					var url = "./leaveinfo.htm?findLIById&id=" + $(aa).find("#lid").val();
					if(g_classid){
						url += "&rkClassid="+g_classid;
					}
					$.hmqRefreshPage("id-message-content",url);
					//show the loading icon
					$('.message-container').append('<div class="message-loading-overlay"><i class="icon-spin icon-spinner orange2 bigger-160"></i></div>');
					
					$('.message-inline-open').removeClass('message-inline-open').find('.message-content').remove();
			
					var message_list = $(aa).closest('.message-list');
			
					//some waiting
					setTimeout(function() {
			
						//hide everything that is after .message-list (which is either .message-content or .message-form)
						message_list.next().addClass('hide');
						$('.message-container').find('.message-loading-overlay').remove();
			
						//close and remove the inline opened message if any!
			
						//hide all navbars
						$('.message-navbar').addClass('hide');
						//now show the navbar for single message item
						$('#id-message-item-navbar').removeClass('hide');
			
						//hide all footers
						$('.message-footer').addClass('hide');
						//now show the alternative footer
						$('.message-footer-style2').removeClass('hide');
			
						
						//move .message-content next to .message-list and hide .message-list
						message_list.addClass('hide').after($('.message-content')).next().removeClass('hide');
			
						//add scrollbars to .message-body
						$('.message-content .message-body').slimScroll({
							height: 200,
							railVisible:true
						});
					}, 500 + parseInt(Math.random() * 500));
			};

			function onclicknext(){
				if(pagecount == 0){
					return;
				}
				var curval = $("#pagevalue").val();
				if(curval >= pagecount){
					return;
				}
				var nextval = parseInt(curval)+1;
				if(nextval <= pagecount){
					$("#pagevalue").val(nextval);
				}
				$("#first").removeClass("disabled");
				$("#last").removeClass("disabled");	
				if(nextval == pagecount){
					$("#next").addClass("disabled");
					$("#end").addClass("disabled");
				}
				var sk = $("#searchkey").val();
				if(sk){
					obtainLeaveInfo(nextval-1,pagesize,encodeURIComponent(sk),'0',g_classid);
				}else{
					obtainLeaveInfo(nextval-1,pagesize,null,'0',g_classid);
				}
			}
			function onclicklast(){
				if(pagecount == 0){
					return;
				}
				var curval = $("#pagevalue").val();
				if(curval > pagecount || curval == '1' || curval == 1){
					return;
				}
				var nextval = parseInt(curval)- 1;
				if(nextval >= 1){
					$("#pagevalue").val(nextval);
				}
				$("#next").removeClass("disabled");
				$("#end").removeClass("disabled");
				if(nextval == 1){
					$("#first").addClass("disabled");
					$("#last").addClass("disabled");	
				}
				var sk = $("#searchkey").val();
				if(sk){
					obtainLeaveInfo(nextval-1,pagesize,encodeURIComponent(sk),'0',g_classid);
				}else{
					obtainLeaveInfo(nextval-1,pagesize,null,'0',g_classid);
				}
			}
			function onclickend(){
				if(pagecount == 0){
					return;
				}
				var curval = $("#pagevalue").val();
				if(curval > pagecount ||curval >= pagecount){
					return;
				}
				$("#first").removeClass("disabled");
				$("#last").removeClass("disabled");	
				$("#next").addClass("disabled");
				$("#end").addClass("disabled");
				$("#pagevalue").val(pagecount);
				
				var sk = $("#searchkey").val();
				if(sk){
					obtainLeaveInfo(pagecount-1,pagesize,encodeURIComponent(sk),'0',g_classid);
				}else{
					obtainLeaveInfo(pagecount-1,pagesize,null,'0',g_classid);
				}
			}
			function onclickfirst(){
				if(pagecount == 0){
					return;
				}
				var curval = $("#pagevalue").val();
				if(curval == '1' || curval == 1){
					return;
				}
				$("#next").removeClass("disabled");
				$("#end").removeClass("disabled");
				$("#first").addClass("disabled");
				$("#last").addClass("disabled");	
				$("#pagevalue").val(1);
				var sk = $("#searchkey").val();
				if(sk){
					obtainLeaveInfo(0,pagesize,encodeURIComponent(sk),'0',g_classid);
				}else{
					obtainLeaveInfo(0,pagesize,null,'0',g_classid);
				}
			};

			function obtainLeaveInfo(pn,ps,sk,status,classid){
				var url ='./leaveinfo.htm?obtainLeaveInfo&pageNo='+pn+'&pageSize='+ps
				if(sk){
					url+= '&searchKey='+sk;
				}
				if(classid){
					url+= '&rkClassid='+classid;
				}
				var html = '';
			    $.ajax({
			        url: url,
			        cache: false,
			        dataType: "json",
			        type: "POST",
			        contentType: "application/x-www-form-urlencoded;charset=utf-8",
			        success: function(data) {
				        if(data.list){
					           /** 当从收索进来时， 初始化页面的分页参数 */
					           if(status && status == '1'){
					        	   $("div .message-footer").children("div .pull-left").text('共'+data.totalCount+'条记录 ');
					        	   $("div .message-footer").children("div .pull-right").children("div").text('每页 '+data.pageSize+'条,共('+data.pageCount+')页');
						      		$("#next").removeClass("disabled");
									$("#end").removeClass("disabled");
									$("#first").addClass("disabled");
									$("#last").addClass("disabled");	
									$("#pagevalue").val(1);
									pagecount = data.pageCount;
					           }
								/** 初始化页面的数据 */
								for(var i = 0; i < data.list.length;i++){
									var onehtml = '';
									var leaveinfo = data.list[i];
									if(!leaveinfo.status){
										onehtml += '<div class="message-item message-unread" onclick="onclickhang(this)">';
									}else{
										onehtml += '<div class="message-item" onclick="onclickhang(this)">';
									}
									onehtml += '<input type="hidden" id="lid" value="'+leaveinfo.id+'">';
									if(!leaveinfo.status){
										onehtml += '<i class="message-star icon-star orange2"></i>';
									}else{
										onehtml += '<i class="message-star icon-star icon-star-empty"></i>';
									}
									onehtml += '<span class="sender" title="'+leaveinfo.name+'">'+leaveinfo.name+'</span>';
									onehtml += '<span class="time">'+leaveinfo.leaveType+'</span>';
									onehtml += '<span class="summary"><span class="text">'+leaveinfo.leaveReason+'</span> </span></div>';
									html += onehtml;
								}
							$("#message-list").html(html);
				        }else{$("#message-list").html("暂无数据")}
			       }
			    });
			}

			function onclicksearch(){
				var event=arguments.callee.caller.arguments[0]||window.event;
				if (event.keyCode == 13) 
			    {      
					var sk = $("#searchkey").val();
					if(sk){
						obtainLeaveInfo(0,pagesize,encodeURIComponent(sk),'1',g_classid);
					}else{
						obtainLeaveInfo(0,pagesize,null,'1',g_classid);
					}
				    return;
			    }
			}

			$(".select2").css('width','210px').select2({allowClear:true})
			.on('change', function(){
				$(this).closest('form').validate().element($(this));
			}); 

			$("#id-message-form").validACE({
				"leaveReason":{required : true},
				"startDate":{required : true},
				"endDate":{required : true},
				"receiver.id":{required : true}
			},function(data){
				resubmit = false;
				bootbox.alert(data.msg,function(){
				if(data.type == 0 || data.type == '0'){
					Inbox.show_list();
					$('#inbox-tabs a[data-target="inbox"]').tab('show'); 
					obtainLeaveInfo(0,pagesize,'','1',g_classid);
					$("span.select2-chosen").text(""); 
				}});
			});

			function deleteInfo(){
				bootbox.confirm("你确定要删除吗 ?", function(result) {
				if(result == true){
					var st = $("#self_status").val();
					if(st == true || st == 'true'){
						bootbox.alert("该记录已查看,不能删除！");
						return;
					}
					$.ajax({
				        url: './leaveinfo.htm?deleteLeaveInfo&id='+$("#infoid").val(),
				        cache: false,
				        dataType: "json",
				        type: "POST",
				        contentType: "application/x-www-form-urlencoded;charset=utf-8",
				        success: function(data) {
				        	//alert(data.msg);
				        	bootbox.alert(data.msg, function() {
				        		if(data.type == 0 || data.type == '0'){
									Inbox.show_list();
									$('#inbox-tabs a[data-target="inbox"]').tab('show'); 
									obtainLeaveInfo(0,pagesize,'','1',g_classid);
								}
				        		});
				        }
				})
				}
			}); 
		}

			function onchangebanji(classid){
				if(classid){
					g_classid = classid;
					$("#classid").val(classid);
					obtainLeaveInfo(0,10,'','1',g_classid);
					 $.ajax({
					        url: './leaveinfo.htm?judgeClassBanzhuren&rkClassid='+classid,
					        cache: false,
					        dataType: "json",
					        type: "POST",
					        contentType: "application/x-www-form-urlencoded;charset=utf-8",
					        success: function(data) {
						        if(data.isClassLeader == 0 || data.isClassLeader == '0'){
						    		$('.btn-new-mail').attr("style","display:zz");
						    		var html= '<option value="">&nbsp;</option>';
									$("select[name='receiver.id'").html();
								    var o = data.data;
						    		if(o){
									  for (var prop in o){
										  if (o.hasOwnProperty(prop)) {   
											    html+= '<option value="'+prop+'">'+o[prop]+'</option>';
										 }
									  }
						    		}
						    		$("select[name='receiver.id'").html(html);
						        }else{
						        	$('.btn-new-mail').attr("style","display:none");
						        }
					       }
					    });
					
				}else{
					return;
				}
			}

			function onchangestatus(status){
				obtainLeaveInfo(0,10,encodeURIComponent($("#searchkey").val()),'1',null);
			}

			function findBack(){
				if($.isEmpty($("select[name='c_select']").val())){
					bootbox.alert("请先选择班级！");
					return;
				}
				selected_stuId = null;
				selected_stuName = null;
				$.dialogACE({
					url:"pLetter.htm?goto_studentsPage",
					title:"选择请假学生",
					callBack:function(dialog){
					if(!selected_stuId){
						alert("请选择数据！");
					}else{
						 $("input[name='leaveUser.id']").val(selected_stuId); 
						 $("input[name='receicerName']").val(selected_stuName); 
						 $(dialog).dialog("destroy"); 
					}
					},
					width:800,
					height:640});
			}
</script>
<div class="row">
							<div class="col-xs-12">
								<!-- PAGE CONTENT BEGINS -->
								<div class="row">
									<div class="col-xs-12">
										<div class="tabbable">
											<ul id="inbox-tabs" class="inbox-tabs nav nav-tabs padding-16 ">
												<li class="li-new-mail pull-right">
													<a data-toggle="tab" href="#write" data-target="write" style="display: ${usertype == '1' ? '': 'none'}" class="btn-new-mail">
														<span class="btn btn-purple bigger-110">
														    <div style="height: 4px"></div>
															<i class=" icon-envelope bigger-110"></i>
															<span class="bigger-110">请假申请</span>
															<div style="height: 4px"></div>
														</span>
													</a>
												</li><!-- ./li-new-mail -->
												<li class="active">
													<a data-toggle="tab" href="#inbox" data-target="inbox">
														<i class="blue icon-inbox bigger-130"></i>
														<span class="bigger-110">请假信息</span>
													</a>
												</li>
											</ul>

											<div class="tab-content  no-padding">
												<div class="tab-pane in active">
													<div class="message-container">
														<div id="id-message-list-navbar" class="message-navbar align-center clearfix">
															<div class="message-bar">
															<div>
															<c:if test="${usertype == '1'}"><!--	
																  <div class="messagebar-item-left">
																		<dt>姓名：</dt>
																   </div>
																<div class="nav-search minimized ">
																	<form id="form-search" onsubmit="return false">
																		<span class="input-icon">
																			<input id="searchkey" onkeydown="onclicksearch()" type="text" autocomplete="off" class="input-small nav-search-input" placeholder="请输入后回车 ..." />
																			<i class="icon-search nav-search-icon"></i>
																		</span>
																	</form>
																</div>
															-->
															<div class="messagebar-item-left">
																<form class="form-search" onsubmit="return false;">
																<div>
																	状态：
																	<select id="searchkey" name="select_status" class="select2" onchange="onchangestatus(this.value)"  data-placeholder="选择..." >
																		<option value="">&nbsp;</option>
																				<option value="已确认">已确认</option>
																				<option value="待确认">待确认</option>
																				<option value="未确认">未确认</option>
																				<option value="拒绝">拒绝</option>
																	</select><!--
																	&nbsp;&nbsp;&nbsp;&nbsp;
																	姓名：
																			<span class="input-icon">
																				<input id="searchkey" onkeydown="onclicksearch()" style="border:1px solid #6fb3e0;border-radius: 4px!important;height: 28px!important;" type="text" class=" input-small nav-search-input" placeholder="请输入后回车 ..." />
																				<i class="icon-search blue nav-search-icon"></i>
																			</span>-->
																			</div>
													   			</form>
															</div>
															</c:if>
												<c:if test="${usertype == '2'}">	
													<div class="messagebar-item-left">
															<form class="form-search" onsubmit="return false;">
															<div>
																班级：
																<select id="c_select" name="c_select" class="select2" onchange="onchangebanji(this.value)"  data-placeholder="选择..." >
																	<option value="">&nbsp;</option>
																	<c:forEach items="${classLeave}" var="cls">
																			<option value="${cls.org.id}">${cls.org.name}</option>
																	</c:forEach>
																</select><!--
																&nbsp;&nbsp;&nbsp;&nbsp;
																姓名：
																		<span class="input-icon">
																			<input id="searchkey" onkeydown="onclicksearch()" style="border:1px solid #6fb3e0;border-radius: 4px!important;height: 28px!important;" type="text" class=" input-small nav-search-input" placeholder="请输入后回车 ..." />
																			<i class="icon-search blue nav-search-icon"></i>
																		</span>-->
																		</div>
												   			</form>
												   			
												   			
											       </div>
											 </c:if>
															
															</div> 
														</div>
														</div>

														<div id="id-message-item-navbar" class="hide message-navbar align-center clearfix">
															<div class="message-bar">
															</div>

															<div>
																<div class="messagebar-item-left">
																	<a href="#" class="btn-back-message-list">
																		<i class="icon-arrow-left blue bigger-110 middle"></i>
																		<b class="bigger-110 middle">返回</b>
																	</a>
																</div>
																<c:if test="${usertype == '1'}">
																	<div class="messagebar-item-right">
																					<a onclick="deleteInfo()" href="#" title="删除">
																						<i class="icon-trash red icon-only bigger-130"></i>
																					</a>
																			</div>
																</c:if>
															</div>
														</div>

														<div id="id-message-new-navbar" class="hide message-navbar align-center clearfix">
															<div class="message-bar">
																<div class="message-toolbar">
																</div>
															</div>

															<div class="message-item-bar">
																<div class="messagebar-item-left">
																	<a href="#" class="btn-back-message-list no-hover-underline">
																		<i class="icon-arrow-left blue bigger-110 middle"></i>
																		<b class="middle bigger-110">取消</b>
																	</a>
																</div>
															</div>
														</div>

														<div class="message-list-container" style="height: 550px">
															<div class="message-list" id="message-list">
																<c:choose>
																	<c:when test="${null != pageList}">
																		<c:forEach items="${pageList.list}" var="i">
																			<div class="message-item ${i.status != 1 ?'message-unread':''}" onclick="onclickhang(this)">
																				<input type="hidden" id="lid" value="${i.id}">
																					<i class="message-star icon-star ${i.status != 1 ?'orange2':'icon-star-empty'}"></i>
																				<span class="sender" title="${i.name}">${i.name}</span>
																				<span class="time">${i.leaveType}</span>
																				<span class="summary">
																					<span class="text">
																						${i.leaveReason}
																					</span>
																			   </span>
																			</div>
																	    </c:forEach>	
																	</c:when>
																	<c:otherwise>
																		<div class="message-item message-unread">
																				<span class="sender">
																						暂无数据
																			   </span>
																			</div>
																	</c:otherwise>
																</c:choose>
															</div>
														</div><!-- /.message-list-container -->

														<div class="message-footer clearfix">
															<div class="pull-left"> 共${pageList.totalCount}条记录 </div>

															<div class="pull-right">
																<div class="inline middle"> 每页  ${pageList.pageSize}条,共（${pageList.pageCount}）页</div>

																&nbsp; &nbsp;
																<ul class="pagination middle">
																	<li id="first" class="disabled" onclick="onclickfirst()">
																		<a href="#" >
																			<i class="icon-step-backward middle"></i></a>
																	</li>
																	<li id="last" class="disabled" onclick="onclicklast()">
																		<a href="#" >
																			<i class="icon-caret-left bigger-140 middle"></i></a>
																	</li>
																	<li>
																		<span>
																			<input  id="pagevalue" value="1" maxlength="3" type="text" />
																		</span>
																	</li>

																	<li id="next" onclick="onclicknext()">
																		<a href="#" >
																			<i class="icon-caret-right bigger-140 middle"></i>
																		</a>
																	</li>

																	<li id="end" onclick="onclickend()">
																		<a href="#">
																			<i class="icon-step-forward middle"></i>
																		</a>
																	</li>
																</ul>
															</div>
														</div>

														<div class="hide message-footer message-footer-style2 clearfix">
															<div class="pull-right">
																<ul class="pagination middle">
																</ul>
															</div>
														</div>
													</div><!-- /.message-container -->
												</div><!-- /.tab-pane -->
											</div><!-- /.tab-content -->
										</div><!-- /.tabbable -->
									</div><!-- /.col -->
								</div><!-- /.row -->

								<form id="id-message-form" class="hide form-horizontal message-form  col-xs-12"  method="post" action="leaveinfo.htm?addLeaveInfo">
										<input type="hidden" id="classid" name="classid"></input>
										<div class="form-group">
											<label class="col-sm-3 control-label no-padding-right">班主任:</label>
											<div class="col-sm-9">
												<span>
													<select name="receiver.id" class="select2"  data-placeholder="选择..." >
														<option value="">&nbsp;</option>
														<c:forEach items="${teachers}" var="cls">
															<option value="${cls.key}">${cls.value}</option>
														</c:forEach>
													</select>
												</span>
											</div>
										</div>
										<c:if test="${usertype != '1'}">
										<input type="hidden" name="leaveUser.id"></input>
											<div class="hr hr-18 dotted"></div>
											<div class="form-group">
												<label class="col-sm-3 control-label no-padding-right">请假人:</label>
												<div class="col-sm-9">
												<span class="input-icon">
												<input type="hidden" name="receiverId" disabled="disabled"></input>
												<input type="text" onclick="findBack()" name="receicerName"
													id="form-field-recipient" value="" placeholder="请选择接收人" /> <i
													class="icon-user"></i> </span>
												</div>
											</div>
									  </c:if>
									  <c:if test="${usertype == '1'}">
									 	 <input type="hidden" name="leaveUser.id" value="${studentId}"></input>
									  </c:if>
										<div class="hr hr-18 dotted"></div>
										<div class="form-group">
											<label class="col-sm-3 control-label no-padding-right">时&nbsp;&nbsp;&nbsp;&nbsp;间:</label>
											<div class="col-sm-6 ">
												<div class="input-icon block col-xs-5 no-padding" >
													<input id="txtB" name="startDate" style="height: 90%" name="birthday" 
													 type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'txtE\')}'})"> 
													<i class="icon-time"></i>
												</div>
												<div class="input-icon block col-xs-5 no-padding">
													<input id="txtE" name="endDate"  style="height: 90%" name="birthday"
													  type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'txtB\')}'})">
													<i class="icon-time"></i>
												</div>
											</div>
										</div>
									
										<div class="hr hr-18 dotted"></div>
										<div class="form-group">
											<label class="col-sm-3 control-label no-padding-right">类&nbsp;&nbsp;&nbsp;&nbsp;型:</label>
											<div class="col-sm-6 col-xs-12">
												<div class="block col-xs-12 no-padding">
													<input name="leaveType" type="radio" value="事假" checked="checked" class="ace" />
													<span class="lbl">事假&nbsp</span>
													<input name="leaveType" type="radio" value="病假" class="ace" />
													<span class="lbl">病假&nbsp</span>
													<input name="leaveType" type="radio" value="探亲假" class="ace" />
													<span class="lbl">探亲假&nbsp</span>
													<input name="leaveType" type="radio" value="其他" class="ace" />
													<span class="lbl">其他&nbsp</span>
												</div>
											</div>
										</div>

										<div class="hr hr-18 dotted"></div>

										<div class="form-group">
											<label class="col-sm-3 control-label no-padding-right">
												事&nbsp;&nbsp;&nbsp;&nbsp;由:
											</label>
											<div class="col-sm-9">
												<textarea  name="leaveReason" maxlength="50" class="col-xs-8" name="comment" id="comment"></textarea>
											</div>
										</div>
									<div class="hr hr-18 dotted"></div>
										<div class="form-group">
											<label class="col-sm-3 control-label no-padding-right">
											</label>
												<span class="inline btn-send-message">
													<button type="button" class="btn btn-sm btn-primary no-border">
														<span class="bigger-110">提交</span>
														<i class="icon-arrow-up icon-on-right"></i>
													</button>
												</span>
										</div>
									
								</form>
	
								<div class="hide message-content" id="id-message-content" style="padding-left: 50px;">
									
								</div><!-- /.message-content -->

								<!-- PAGE CONTENT ENDS -->
			</div><!-- /.col -->
	</div>
