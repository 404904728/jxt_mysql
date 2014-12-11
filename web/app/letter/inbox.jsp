<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>	
<div class="row">
<div class="col-sm-7">
<div class="row">
<div class="col-xs-12">
<div class="widget-box">
<div class="widget-header widget-header-small">
<h5 class="lighter">发送人姓名查询</h5>
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
</span></div>
</div>
</div>
</form>
</div>
</div>
</div>
<div id="gridbox" class="widget-box">
<div class="widget-header widget-header-flat  header-color-green2">
<h4 class="smaller"><i class="icon-list"></i> 收件信息列表</h4>
</div>

<div class="widget-body" style="height: 525px">
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
<h4 class="smaller"><i class="icon-comment"></i> 详情及历史</h4>
</div>
<div class="widget-body" style="height: 620px">
<div class="widget-main no-padding">
<div id="alert-info_s" class="alert alert-info">
<div class="hr"></div>
<button type="button" class="close" data-dismiss="alert"><i
	class="icon-remove"></i></button>
选择<strong>信息列表</strong>记录后，在此处展示私信留言详细情况。 <br>
<br>
</div>
<div id="detailinfo" class="hide">
	<div class="dialogs">
</div>
<div class="form-actions" class="hide">
<div class="input-group"><input id="sendcontent" type="text" 
	placeholder="请输入你的留言 ..." 
	class="form-control" name="message" /> <span class="input-group-btn">
<button class="btn btn-sm btn-info no-radius" type="button" onclick="onclickSend()"><i
	class="icon-share-alt"></i> 发送</button>
</span></div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>


<script type="text/javascript">
var resubmit1 = false;
function clickyuyin(val){
	$.addVoiceObject("gridbox",val);
}

 function refreshdetail(id,pid){
	 if(!id){
		 return;
	 }
	 plid = id;
	var end = start;
	var url ='pLetter.htm?findPrivateLetterByInboxId&id='+id+'&start='+start+'&end='+end;;
	var html = '';
	$.ajax({
	    url: url,
	    cache: false,
	    dataType: "json",
	    type: "POST",
	    contentType: "application/x-www-form-urlencoded;charset=utf-8",
	    success: function(data) {
			if (data) {
				totalcount = data.t;
				historyCount = data.s;
				var datalist = data.r;
				for ( var i = 0; i < datalist.length; i++) {
						var onehtml = '';
						onehtml += '<div class="itemdiv dialogdiv">';
						onehtml += '<div class="user"><img alt="头像" src="'+datalist[i].headpic+'"/></div>';
						onehtml += '<div class="body">';
						if(i%2 == 0){
							onehtml += '<div class="time"><i class="icon-time"></i> <span class="green">'+datalist[i].sendtime+'</span>';
						}else{
							onehtml += '<div class="time"><i class="icon-time"></i> <span class="blue">'+datalist[i].sendtime+'</span>';
						}
						onehtml += '</div>';
						onehtml += '<div class="name"><a href="#">'+datalist[i].sendname+'</a></div>';
						onehtml += '<div class="text">'+datalist[i].content;
						if(datalist[i].voice){
							var url = 'download/'+datalist[i].voice+'.mp3';
							onehtml += '<a style="float:right" href="javascript:void(0)" onclick="clickyuyin(\''+url+'\')"><i class="icon-volume-up red bigger-130"></i></a></div>';
						}else{
							onehtml += '</div>';
						}
						onehtml += '</div>';
						onehtml += '</div>';
						html += onehtml;
					}
				$(".dialogs").html(html);
				$('.dialogs').slimScroll({
					height: '520px',
					start : 'bottom'
			    });
				$(".dialogs").scrollTop(520);
	        }else{$(".dialogs").html("暂无数据")}
	   }
	});
 }

 function appenddetail(id,pid){
	 if(!id){
		 return;
	 }
	var end = start;
	var url ='pLetter.htm?findPrivateLetterByInboxId&id='+id+'&start='+start+'&end='+end;;
	var html = '';
	$.ajax({
	    url: url,
	    cache: false,
	    dataType: "json",
	    type: "POST",
	    contentType: "application/x-www-form-urlencoded;charset=utf-8",
	    success: function(data) {
			var data = data.r;
			var i = 0;
			if(start == 0){
				var i = 1;
			}
				for(;i < data.length;i++){
						var onehtml = '';
						onehtml += '<div class="itemdiv dialogdiv">';
						onehtml += '<div class="user"><img alt="头像" src="'+data[i].headpic+'"/></div>';
						onehtml += '<div class="body">';
						if(i%2 == 0){
							onehtml += '<div class="time"><i class="icon-time"></i> <span class="green">'+data[i].sendtime+'</span>';
						}else{
							onehtml += '<div class="time"><i class="icon-time"></i> <span class="blue">'+data[i].sendtime+'</span>';
						}
						onehtml += '</div>';
						onehtml += '<div class="name"><a href="#">'+data[i].sendname+'</a></div>';
						onehtml += '<div class="text">'+data[i].content;
						if(data[i].voice){
							var url = 'download/'+data[i].voice+'.mp3';
							onehtml += '<a style="float:right" href="javascript:void(0)" onclick="clickyuyin(\''+url+'\')"><i class="icon-volume-up red bigger-130"></i></a></div>';
						}else{
							onehtml += '</div>';
						}
						onehtml += '</div>';
						onehtml += '</div>';
						html += onehtml;
					}
				totalcount += data.length;
				$(".dialogs").append(html);
				$(".dialogs").scrollTop(530);
	        }
	});
 }

 function beforedetail(id,pid){
	 if(!id){
		 return;
	 }
	var end = historyCount;
	start = end - 10;
	var url ='pLetter.htm?findPrivateLetterByInboxId&id='+id+'&start='+start+'&end='+end;;
	var html = '';
	$.ajax({
	    url: url,
	    cache: false,
	    dataType: "json",
	    type: "POST",
	    contentType: "application/x-www-form-urlencoded;charset=utf-8",
	    success: function(data) {
			historyCount = data.s;
			var data = data.r;
			var i = 0;
				for(;i < data.length;i++){
						var onehtml = '';
						onehtml += '<div class="itemdiv dialogdiv">';
						onehtml += '<div class="user"><img alt="头像" src="'+data[i].headpic+'"/></div>';
						onehtml += '<div class="body">';
						if(i%2 == 0){
							onehtml += '<div class="time"><i class="icon-time"></i> <span class="green">'+data[i].sendtime+'</span>';
						}else{
							onehtml += '<div class="time"><i class="icon-time"></i> <span class="blue">'+data[i].sendtime+'</span>';
						}
						onehtml += '</div>';
						onehtml += '<div class="name"><a href="#">'+data[i].sendname+'</a></div>';
						onehtml += '<div class="text">'+data[i].content;
						if(data[i].voice){
							var url = 'download/'+data[i].voice+'.mp3';
							onehtml += '<a style="float:right" href="javascript:void(0)" onclick="clickyuyin(\''+url+'\')"><i class="icon-volume-up red bigger-130"></i></a></div>';
						}else{
							onehtml += '</div>';
						}
						onehtml += '</div>';
						onehtml += '</div>';
						html += onehtml;
					}

				$(".dialogs .itemdiv").first().before(html);
	        }
	});
 }
 
	function onclickSend(){
		var v = $("#sendcontent").val();
		if(!v){
			return;
		}
		if(resubmit1){
			return false;
		}
		resubmit1 = true;
		$.ajax({
		    url: 'pLetter.htm?rePrivateLetter&id='+plid+"&content="+encodeURIComponent(v),
		    cache: false,
		    dataType: "json",
		    type: "POST",
		    contentType: "application/x-www-form-urlencoded;charset=utf-8",
		    success: function(data) {
		    	resubmit1 = false;
		        if(data){
					bootbox.alert(data.msg,function(){
						if(data.type == 0 || data.type == '0'){
							start = totalcount;
							appenddetail(plid,'detailinfo');
							$("#sendcontent").val('');
						}});
		   		}
		    }
		});
	}
</script>