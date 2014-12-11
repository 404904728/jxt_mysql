<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <script src="res/ace/assets/js/jquery.colorbox-min.js"></script>

<div class=" message-content" id="id-message-content">
	<div class="message-header clearfix">
		<div class="pull-left">
			<div class="space-4"></div>
			<input type="hidden" id="detaileQueryFlg" value="${queryFlg}">
			<i class="message-star icon-star-empty light-grey"></i>
			&nbsp;
			<img class="middle" alt="" src="download/${equRepair.sendHeadpic}.all" width="32" />
			&nbsp;
			<a href="javascript:void(0)" class="sender">${equRepair.sendTeaName}老师</a>
			&nbsp;
			<i class="icon-time bigger-110 orange middle"></i>
			<!-- <span class="time">2014/02/18&nbsp;&nbsp;22:15</span> -->
			<span class="time">${equRepair.repairtime}</span>
		</div>
	
		<div class="action-buttons pull-right">
			
			<c:if test="${queryFlg == '1' || queryFlg == 1}">
				<a href="javascript:void(0)" title="不需维修" onclick="updateStatus('1',${equRepair.id})">
					<i class="icon-reply green icon-only bigger-130"></i>
				</a>
				<a href="javascript:void(0)" title="维修完成" onclick="updateStatus('2',${equRepair.id})">
					<i class="icon-mail-forward blue icon-only bigger-130"></i>
				</a> 
			</c:if>
			<a href="javascript:void(0)" onclick="deleteEmail(${equRepair.id})">
				<i class="icon-trash red icon-only bigger-130"></i>
			</a>
		</div>
	</div>
	
	<div class="hr hr-double"></div>
	
	<div class="message-body">
		<p>
			<span class="blue bigger-100">状态： ${equRepair.cackcontent}, 处理人： ${equRepair.receivertea} </span>
		</p>
		<p>
			${equRepair.repaircontent}
		</p>
		<div id="gridbox" class="widget-box">
	</div>
	
	<div class="hr hr-double"></div>
	
	<div class="message-attachment clearfix">
		<div class="attachment-title">
			<span class="blue bolder bigger-110">附件列表</span>
			&nbsp;
			<c:if test="${!empty equRepair.soundid}">
			<a onclick="clickyuyin('download/${equRepair.soundid}.mp3')" href="javascript:void(0)">
				<i class="icon-volume-up red bigger-130"></i>
			</a>
			</c:if>
			<span class="grey"> </span>
		</div>
	
		&nbsp;
		<c:if test="${!empty equRepair.atcid}">
			<ul id="image_query_to_read" class="ace-thumbnails">
				<li>
						<a href="download/${equRepair.atcid}.jpg" data-rel="colorbox">
							<img alt="150x150" width="30" height="30" src="download/${equRepair.atcid}.jpg" />
						</a>
				</li>
			</ul>
		</c:if></div>
	</div>
</div>
<script type="text/javascript">
jQuery(function($){
	var colorbox_params = {
			reposition : true,
			scalePhotos : true,
			scrolling : false,
			previous : '<i class="icon-arrow-left"></i>',
			next : '<i class="icon-arrow-right"></i>',
			close : '&times;',
			current : '{current} of {total}',
			maxWidth : '100%',
			maxHeight : '100%',
			onOpen : function() {
				document.body.style.overflow = 'hidden';
			},
			onClosed : function() {
				document.body.style.overflow = 'auto';
			},
			onComplete : function() {
				$.colorbox.resize();
			}
		};
	
		$('.ace-thumbnails [data-rel="colorbox"]').colorbox(colorbox_params);
		$("#cboxLoadingGraphic").append("<i class='icon-spinner orange'></i>");
});
/**
 * 删除邮件
 */
function deleteEmail(id){
	var detaileQueryFlg = $("#detaileQueryFlg").val();
	if("" != id && null != id){
		if(confirm("确定删除此报修记录？") == true){
			$.ajax({
		        type:"post",
		        url:"requimentRepair.htm?deleteEuqiEmail_",
		        data:{"emailId":id,"status":detaileQueryFlg},
		        dataType: "json",
				cache: false,
		        success:function(data){
		        	if(data.type == 0){
		        		bootbox.alert("删除成功");
		        		if("1" == detaileQueryFlg){
		        			init('','1','');
		        		}else{
		        			init('','2','');		        			
		        		}
		        	}else{
		        		bootbox.alert("删除失败");
		        		if("1" == queryFlg){
		        			init('','1','');
		        		}else{
		        			init('','2','');        			
		        		}
		        	}
		        }
		    });
		}
	}
}
/**
 * 维修人员反馈维修状态
 */
function updateStatus(flg,id){
	var detaileQueryFlg = $("#detaileQueryFlg").val();
	if("" != id && null != id){
		$.ajax({
	        type:"post",
	        url:"requimentRepair.htm?updateBackStatus_",
	        data:{"emailId":id,"state":flg},
	        dataType: "json",
			cache: false,
	        success:function(data){
	        	if(data.type == 0){
	        		bootbox.alert("状态修改成功");
	        		if("1" == detaileQueryFlg){
	        			init('','1','');
	        		}else{
	        			init('','2','');		        			
	        		}
	        	}else{
	        		bootbox.alert("状态修改失败");
	        		if("1" == queryFlg){
	        			init('','1','');
	        		}else{
	        			init('','2','');		        			
	        		}
	        	}
	        }
	    });
	}
}
function clickyuyin(val){
	$.addVoiceObject("gridbox",val);
}

</script>