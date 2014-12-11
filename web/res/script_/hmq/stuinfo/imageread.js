$(document).ready(function() {
//	queryStuentImage('image_query_to_read','stuinfo.htm?queryImage_&stuId=' + studentId);
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
	
//	$('.ace-thumbnails [data-rel="colorbox"]').colorbox({onComplete:function(){  
//		alert(2220);
//	}});  
	
	
});

function deleteImage(imgId){
	
	if(confirm("请确认是否删除此图片") == true){
		$.ajax({
			type: "POST",
			url: 'stuinfo.htm?deleteImg_&imgId=' + imgId,
			dataType: "json",
			cache: false,
			success: function(msg){
				if(msg != null && msg.length != 0){
					if(msg.flg == '1'){
						alert('删除成功');
						$.hmqHomePage("stuinfo.htm?queryImage_&stuId=" + $.trim($("#stuid_value").val()));
					}else if(msg.flg == '2'){
						alert('删除失败');
					}else{
						alert("系统错误, 请联系管理员");
					}
				}else{
					alert("系统错误, 请联系管理员");
				}
		    }
		});
	}
	
}
/**
 * 返回按钮 控制在查看相册时返回到学生信息页面
 */
function rollBackStuInfo(){
	$("#studentByClassMess").css('display','block');
	$("#studentImage").css('display','none');
//	$.hmqHomePage("app/studentinfo/studentinfos.jsp");
}