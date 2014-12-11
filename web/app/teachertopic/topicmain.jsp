<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="./res/ace/assets/js/select2.min.js"></script>
<script src="./res/ace/assets/js/jquery.colorbox-min.js"></script>
<script type="text/javascript">
   /** 向下滚动一次加载10条记录 */
   var start = 0;
   var totalcount = 0;
   var plid;
	var grid_selector = "#grid-table_s";
	var pager_selector = "#grid-pager_s";
	var userType = "${userType}"
	var gridurl = './teachertopic.htm?findAllTopic';
	
	function clickyuyin(val) {
		$.addVoiceObject("gridbox", val);
	}
	if(userType == '1'){
		var banjiId = "${banjiId}"
			gridurl += '&classId='+banjiId;
	}

	$(grid_selector).jqGrid({
		url : gridurl,
		datatype : "json",
		mtype : "post",
		height : 350,
		colNames : [ '编号', '发起人', '标题', '内容', '回复数', '时间' ],
		colModel : [ {
			name : 'id',
			index : 'id',
			width : 60,
			hidden : true,
			sorttype : "int",
			sortable : false,
			editable : false
		}, {
			name : 'tea.name',
			index : 'tea.name',
			width : 70,
			editable : false
		}, {
			name : 'title',
			index : 'title',
			width : 70,
			sortable : false,
			editable : false
		}, {
			name : 'content',
			index : 'content',
			width : 70,
			sortable : false,
			editable : false
		}, {
			name : 'reCount',
			index : 'reCount',
			width : 70,
			sortable : false,
			editable : false
		}, {
			name : 'writeDate',
			index : 'writeDate',
			width : 60,
			sortable : false,
			editable : false
		} ],

		viewrecords : true,
		rowNum : 10,
		rowList : [ 10, 20, 30 ],
		pager : pager_selector,
		altRows : true,
		multiselect : false,
		multiboxonly : false,
		loadComplete : function() {
			setTimeout(function() {
				updatePagerIcons(this);
			}, 0);
		},

		autowidth : true,
		ondblClickRow : ondbclick
	});

	function onclicksearch() {
		//var url = jQuery(grid_selector).jqGrid('getGridParam','url');
		var url = gridurl;
		var sv = $("#searchKey").val();
		if (sv) {
			url = url + "&searchKey=" + encodeURIComponent(sv);
		}
		jQuery(grid_selector).jqGrid('setGridParam', {
			url : url
		}).trigger("reloadGrid");

	}
	
	function addTeacherTop(){
		var classId = $("#c_select").val();
		if(null == classId || "" == classId){
			bootbox.alert("请选择班级后新增心语");
			return;
		}
		$("#teachertopic").css('display','none');
		$("#addTeacherTopic").css('display','block');
		$.hmqRefreshPage("addTeacherTopic","teachertopic.htm?initAddTeacherTopic_&classId=" + classId);
	}

	function enterEventIntc() {
		var event = arguments.callee.caller.arguments[0] || window.event;
		if (event.keyCode == 13) {
			onclicksearch();
		}
	}

	$(function() {
		$('body').resize(
				function() {
						$(grid_selector).setGridWidth(
								$("#gridbox").width() - 8, true);
				});
	});

	$('#detailinfo .dialogs').slimScroll({
		height : '520px'
	});
	$("#detailinfo .dialogs").scroll(function(){
		var top =  $(this)[0].scrollTop;  
		var heitht = $(this)[0].scrollHeight;
		var divh = $("div .dialogs").height();
		var sendformh = $("#sendform").height();
		if((top+divh+sendformh) >= heitht && start < totalcount){
			appenddetail('detailinfo');
		}
		});
	
	function ondbclick(id) {
		$("#alert-info_s").addClass('hide');
		$("#detailinfo").removeClass('hide');
		$("#detailinfo .dialogs").html("");
		start = 0;
		refreshdetail(id, 'detailinfo');
	}

	function queryCheckByClass(v){
		var newUrl = "";
		 if(v){
			 //gridurl += '&classId='+v
			 newUrl = gridurl + '&classId='+v;
		  }else{
			//return;
			  newUrl = gridurl;
		  }
		  jQuery(grid_selector).jqGrid('setGridParam',{url: newUrl}).trigger("reloadGrid");

	}
	
	function refreshdetail(id, pid) {
		if (!id) {
			return;
		}
		plid = id;
		var end = start+10;
		var url = './teachertopic.htm?findTeacherTopicByid&pid=' + id +'&start='+start+'&end='+end;
		var html = '';
		$.ajax({
					url : url,
					cache : false,
					dataType : "json",
					type : "POST",
					contentType : "application/x-www-form-urlencoded;charset=utf-8",
					success : function(data) {
						start =  start+10;
						if (data) {
							totalcount = data[2];
							var datalist = data[1];
							for ( var i = 0; i < datalist.length; i++) {
								var onehtml = '';
								onehtml += '<div class="itemdiv dialogdiv">';
								onehtml += '<div class="user"><img alt="头像" src="'+datalist[i].headpic+'"/></div>';
								onehtml += '<div class="body">';
								if (i % 2 == 0) {
									onehtml += '<div class="time"><i class="icon-time"></i> <span class="green">'
											+ datalist[i].sendtime + '</span>';
								} else {
									onehtml += '<div class="time"><i class="icon-time"></i> <span class="blue">'
											+ datalist[i].sendtime + '</span>';
								}
								onehtml += '</div>';
								onehtml += '<div class="name"><a href="#">'
										+ datalist[i].sendname + '</a></div>';
								onehtml += '<div class="text">'
										+ datalist[i].content;
								if (datalist[i].voice) {
									var url = 'download/' + datalist[i].voice
											+ '.mp3';
									onehtml += '<a style="float:right" href="javascript:void(0)" onclick="clickyuyin(\''
											+ url
											+ '\')"><i class="icon-volume-up red bigger-130"></i></a></div>';
								} else {
									onehtml += '</div>';
								}

								onehtml += '<ul class="ace-thumbnails">';
								if(null != datalist[i].pictrueids){
									onehtml += '<div style="height:8px"></div>';
									var pics = datalist[i].pictrueids;
									for(var j = 0; j < pics.length; j++){
										onehtml += '<a href="download/'+pics[j]+'.jpg"  data-rel="colorbox">';
										onehtml += '<img width="60px" height="60px"src="download/'+pics[j]+'.jpg"/>';
										onehtml += '</a> &nbsp;&nbsp;&nbsp;';
									}
								}
								onehtml += '</ul>';
								
								onehtml += '</div>';
								onehtml += '</div>';
								html += onehtml;
							}
							$("#" + pid + " .dialogs").html(html);
							$('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
						} else {
							$("#" + pid + " .dialogs").html("暂无数据")
						}
					}
				});
	}

	function appenddetail(divid) {
		if (!plid) {
			return;
		}
		var end = start+10;
		var url = './teachertopic.htm?findTeacherTopicByid&pid=' + plid +'&start='+start+'&end='+end;
		var html = '';
		$.ajax({
					url : url,
					cache : false,
					dataType : "json",
					type : "POST",
					contentType : "application/x-www-form-urlencoded;charset=utf-8",
					success : function(data) {
						if (data) {
							var data = data[1];
							var i = 0;
							if(start == 0){
								var i = 1;
							}
							for ( ;i < data.length; i++) {
								var onehtml = '';
								onehtml += '<div class="itemdiv dialogdiv">';
								onehtml += '<div class="user"><img alt="头像" src="'+data[i].headpic+'"/></div>';
								onehtml += '<div class="body">';
								if (i % 2 == 0) {
									onehtml += '<div class="time"><i class="icon-time"></i> <span class="green">'
											+ data[i].sendtime + '</span>';
								} else {
									onehtml += '<div class="time"><i class="icon-time"></i> <span class="blue">'
											+ data[i].sendtime + '</span>';
								}
								onehtml += '</div>';
								onehtml += '<div class="name"><a href="#">'
										+ data[i].sendname + '</a></div>';
								onehtml += '<div class="text">'
										+ data[i].content;
								if (data[i].voice) {
									var url = 'download/' + data[i].voice
											+ '.mp3';
									onehtml += '<a style="float:right" href="javascript:void(0)" onclick="clickyuyin(\''
											+ url
											+ '\')"><i class="icon-volume-up red bigger-130"></i></a></div>';
								} else {
									onehtml += '</div>';
								}

								onehtml += '<ul class="ace-thumbnails">';
								if(null != data[i].pictrueids){
									onehtml += '<div style="height:8px"></div>';
									var pics = data[i].pictrueids;
									for(var j = 0; j < pics.length; j++){
										onehtml += '<a href="download/'+pics[j]+'.jpg"  data-rel="colorbox">';
										onehtml += '<img width="60px" height="60px"src="download/'+pics[j]+'.ztb" />';
										onehtml += '</a> &nbsp;&nbsp;&nbsp;';
									}
								}
								onehtml += '</ul>';
								
								onehtml += '</div>';
								onehtml += '</div>';
								html += onehtml;
							}
							start = start+10;
							$("#" + divid + " .dialogs").append(html);
						    $('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
						}
					}
				});
	}

	
	function onclickSend() {
		var v = $("#sendcontent").val();
		if (!v) {
			return;
		}
		$.ajax({
			url : './teachertopic.htm?reTeacherTopicByid&id=' + plid + '&content='
					+ encodeURIComponent(v),
			cache : false,
			dataType : "json",
			type : "POST",
			contentType : "application/x-www-form-urlencoded;charset=utf-8",
			success : function(data) {
				if (data) {
					bootbox.alert(data.msg, function() {
						if (data.type == 0 || data.type == '0') {
							//$("#detailinfo .dialogs").html("");
							start = totalcount;
							appenddetail('detailinfo');
							$("#sendcontent").val('');
						}
					});
				}
			}
		});
	}
	$(".select2").css('width','180px').select2({allowClear:true})
	.on('change', function(){
		$(this).closest('form').validate().element($(this));
	}); 


	var colorbox_params = {
			reposition:true,
			scalePhotos:true,
			scrolling:false,
			previous:'<i class="icon-arrow-left"></i>',
			next:'<i class="icon-arrow-right"></i>',
			close:'&times;',
			current:'{current} of {total}',
			maxWidth:'100%',
			maxHeight:'100%',
			onOpen:function(){
				document.body.style.overflow = 'hidden';
			},
			onClosed:function(){
				document.body.style.overflow = 'auto';
			},
			onComplete:function(){
				$.colorbox.resize();
			}
		};
</script>

<div id="teachertopic" class="row">
				<div class="col-sm-7">
							<div class="alert alert-info" style="height: 55px;">
								<form class="form-horizontal" role="form" onsubmit="return false;">
									<div class="form-group" id="classMessage">
											<div class="col-sm-9">
												<span>班级:&nbsp;&nbsp;
													<c:if test="${userType == '2'}">
														<select id="c_select" class="select2"
															data-placeholder="选择..." onchange="queryCheckByClass(this.value)">
															<option value="">&nbsp;</option>
															<c:forEach items="${banjis}" var="cls">
																<option value="${cls.org.id}">${cls.org.name}</option>
															</c:forEach>
														</select>
													</c:if>
												</span>
												<c:if test="${userType == '1'}">
													<span>${banjiName}</span>
												</c:if>
											</div>
										</div>
								</form>
							</div>
							<div class="row">
								<div class="col-xs-12">
									<div class="widget-box">
										<div class="widget-header widget-header-small">
											<h5 class="lighter">主题名称</h5>
										</div>
								<div class="widget-body">
									<div class="widget-main">
									<form class="form-search" onsubmit="return false;">
										<div class="row">
										<div class="col-xs-12 col-sm-8">
										<div class="input-group"><input type="text" id="searchKey"
											class="form-control search-query" placeholder="Search ..."
											autocomplete="off" onkeydown="enterEventIntc()" /> <span
											class="input-group-btn">
										<button  type="button" class="btn btn-purple btn-sm"
											onclick="onclicksearch()">查询 <i
											class="icon-search icon-on-right bigger-110"></i></button>
										</span>
											<c:if test="${userType == '2'}">
												&nbsp;&nbsp;&nbsp;&nbsp;
												<span 
													class="input-group-btn">
												<button  type="button" class="btn btn-info btn-sm"
													onclick="addTeacherTop()">新增 <i
													class="icon-edit icon-on-right bigger-110"></i></button>
												</span>
											</c:if>
										</div>
										</div>
										</div>
										</form>
									</div>
								</div>
							</div>
									<div id="gridbox" class="widget-box">
										<div
											class="widget-header widget-header-flat  header-color-green2">
											<h4 class="smaller">
												<i class="icon-list"></i> 心语列表
											</h4>
										</div>

										<div class="widget-body" style="height: 450px">
											<table id="grid-table_s"></table>
											<div id="grid-pager_s"></div>

										</div>
									</div>
								</div>
							</div>
					</div>

						<div class="col-sm-5">

							<div class="widget-box">
								<div class="widget-header widget-header-flat header-color-blue2">
									<h4 class="smaller">
										<i class="icon-comment"></i> 心语详情
									</h4>
								</div>
								<div class="widget-body" style="height: 620px">
									<div class="widget-main no-padding">
										<div id="alert-info_s" class="alert alert-info">
											<div class="hr"></div>
											<button type="button" class="close" data-dismiss="alert">
												<i class="icon-remove"></i>
											</button>
											选择<strong>心语列表</strong>记录后，在此处展示心语详细情况。 <br> <br>
										</div>
										<div id="detailinfo" class="hide">
											<div class="dialogs">
												<!-- 添加心语信息 -->
											</div>
											<form id="sendform" onsubmit="return false;">
												<div class="form-actions">
													<div class="input-group">
														<input id="sendcontent" type="text" maxlength="50"
															placeholder="请输入你的留言 ..." class="form-control"
															name="message" /> <span class="input-group-btn">
															<button class="btn btn-sm btn-info no-radius"
																type="button" onclick="onclickSend()">
																<i class="icon-share-alt"></i> 发送
															</button>
														</span>
													</div>
												</div>
											</form>

										</div>
									</div>
								</div>
							</div>
						</div>
		</div>
		<div id="addTeacherTopic"></div>