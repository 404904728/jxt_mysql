<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
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
	var url ='pLetter.htm?findPrivateLetterByInboxId&id='+id;
	var html = '';
	$.ajax({
	    url: url,
	    cache: false,
	    dataType: "json",
	    type: "POST",
	    contentType: "application/x-www-form-urlencoded;charset=utf-8",
	    success: function(data) {
	        if(data){
				for(var i = 0; i < data.length;i++){
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
				$("#"+pid+" .dialogs").html(html);
	        }else{$("#"+pid+" .dialogs").html("暂无数据")}
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
							$("#detailinfo .dialogs").html("")
							refreshdetail(plid,'detailinfo');
							$("#sendcontent").val('');
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
</span></div>
</div>
</div>
</form>
</div>
</div>
</div>
<div id="gridbox" class="widget-box">
<div class="widget-header widget-header-flat  header-color-green2">
<h4 class="smaller"><i class="icon-list"></i> 心语列表</h4>
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
<h4 class="smaller"><i class="icon-comment"></i> 心语详情</h4>
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
<form onsubmit="return false;">
<div class="form-actions">
<div class="input-group"><input id="sendcontent" type="text" 
	placeholder="请输入你的留言 ..." 
	class="form-control" name="message" /> <span class="input-group-btn">
<button class="btn btn-sm btn-info no-radius" type="button" onclick="onclickSend()"><i
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