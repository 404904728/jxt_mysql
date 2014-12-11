<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
var pager_selector_f = "#grid-pager_f";
$("#grid-table_f").jqGrid({
	url:'./pLetter.htm?obtainOutboxInfos',
	datatype: "json",
	autowidth: true,
	mtype : "post",
	height: 426,
	colNames:['编号','收件人姓名','收件人组织', '内容', '状态','时间'],
	colModel:[
		{name:'id',index:'id', width:60,hidden:true,sorttype:"int",sortable:false, editable: false},
		{name:'receicerName',index:'receicerName', width:70,editable: false},
		{name:'receicerOrg.name',index:'receicerOrg.name', width:70,sortable:false, editable: false},
		{name:'content',index:'content', width:70, sortable:false,editable: false},
		{name:'status',index:'status', width:70, sortable:false,editable: false,formatter:function(cell,options,rowdata){
			if(cell == false){
				return '未回复';
			}else{
				return '已回复';
			}
			}},
		{name:'sendTime',index:'sendTime', width:60, sortable:false,editable: false} 
	], 

	viewrecords : true,
	rowNum:10,
	rowList:[10,20,30],
	pager : pager_selector_f,
	altRows: true,
	multiselect: false,
    multiboxonly: false,
	loadComplete : function() {
		setTimeout(function(){
			updatePagerIcons(this);
		}, 0);
	},
	ondblClickRow: function(id){
		$("#alert-info").addClass('hide');
		$("#detailinfo_f").removeClass('hide');
		$("#detailinfo_f .dialogs").html("")
		refreshdetail(id,'detailinfo_f');
	}
});

	function onclicksearch_f(){
		//var url = jQuery(grid_selector).jqGrid('getGridParam','url');
		var url = './pLetter.htm?obtainOutboxInfos';
		var sv = $("#searchKey_f").val();
		if(sv){
			url = url+"&searchKey="+encodeURIComponent(sv);
		}
			jQuery("#grid-table_f").jqGrid('setGridParam',{url: url}).trigger("reloadGrid");
		
	}

	$('#detailinfo_f .dialogs').slimScroll({
		height: '520px'
    });
	
	function enterEventIntc_f(){
		var event=arguments.callee.caller.arguments[0]||window.event;
		if (event.keyCode == 13) 
	    {      
			onclicksearch_f();
	    }
	}

	function onclickSend_f(){
		var v = $("#sendcontent_f").val();
		if(!v){
			return;
		}
		$.ajax({
		    url: 'pLetter.htm?rePrivateLetter&id='+plid+"&content="+encodeURIComponent(v),
		    cache: false,
		    dataType: "json",
		    type: "POST",
		    contentType: "application/x-www-form-urlencoded;charset=utf-8",
		    success: function(data) {
		        if(data){
					bootbox.alert(data.msg,function(){
						if(data.type == 0 || data.type == '0'){
							$("#detailinfo_f .dialogs").html("")
							refreshdetail(plid,'detailinfo_f');
							$("#sendcontent_f").val('');
						}});
		   		}
		    }
		});
	}

</script>	
<div class="row">
	<div class="col-sm-7">
	<div class="row">
	<div class="col-xs-12">
	<div class="widget-box">
		<div class="widget-header widget-header-small">
		<h5 class="lighter">接收人姓名查询</h5>
		</div>
		<div class="widget-body">
			<div class="widget-main">
			<form class="form-search" onsubmit="return false;">
				<div class="row">
				<div class="col-xs-12 col-sm-8">
				<div class="input-group"><input type="text" id="searchKey_f"
					class="form-control search-query" placeholder="Search ..."
					autocomplete="off" onkeydown="enterEventIntc_f()"/> <span
					class="input-group-btn">
				<button type="button" class="btn btn-purple btn-sm" onclick="onclicksearch_f()">查询 <i
					class="icon-search icon-on-right bigger-110"></i></button>
				</span></div>
				</div>
				</div>
			</form>
			</div>
		</div>
	</div>
	<div id="gridbox_f" class="widget-box">
		<div class="widget-header widget-header-flat  header-color-green2">
		<h4 class="smaller"><i class="icon-list"></i> 发件信息列表</h4>
		</div>
		
		<div class="widget-body" style="height: 525px">
			<table id="grid-table_f"></table>
			<div id="grid-pager_f"></div>
		</div>
	</div>
	</div>
	</div>
</div>

<div class="col-sm-5">

<div class="widget-box">
	<div class="widget-header widget-header-flat header-color-blue2">
	<h4 class="smaller"><i class="icon-comment"></i> 详情及历史</h4>
	</div>
<div class="widget-body" style="height: 620px">
	<div class="widget-main no-padding">
	<div id="alert-info" class="alert alert-info">
	<div class="hr"></div>
	<button type="button" class="close" data-dismiss="alert"><i
		class="icon-remove"></i></button>
	选择<strong>信息列表</strong>记录后，在此处展示私信留言详细情况。 <br>
	<br>
	</div>
	<div id="detailinfo_f" class="hide">
			<div class="dialogs">
		</div>
		<form onsubmit="return false;">
		<div class="form-actions">
			<div class="input-group"><input id="sendcontent_f" type="text" 
				placeholder="Type your message here ..." 
				class="form-control" name="message" /> <span class="input-group-btn">
			<button class="btn btn-sm btn-info no-radius" type="button" onclick="onclickSend_f()"><i
				class="icon-share-alt"></i> 发送</button>
			</span></div>
			</div>
		</form>
	</div>
	</div>
</div>
</div>
</div>
</div>
