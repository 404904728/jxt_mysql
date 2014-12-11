//普通详细
function showNoticeMessage(obj,nId) {
	var parent=$(obj).parent();
	if (parent.hasClass('message-inline-open')) {
		parent.removeClass('message-inline-open').find(
				'.message-content').remove();
		return;
	}
	parent.append('<div class="message-loading-overlay"><i class="icon-spin icon-spinner orange2 bigger-160"></i></div>');
	$.ajax({   
        type:"post",     
        url:"notice.htm?noticedetail",
        data:{"nId":nId},
        success:function(html){
        	setTimeout(function(){
        		parent.find('.message-loading-overlay').remove();
            	parent.addClass("message-inline-open");
            	parent.append(html);
        	},500)
        },   
        error:function(){   
            alert("系统错误，请尝试刷新页面");   
        }   
    });
}
//草稿箱
function draftNoticeMessage(obj,nId) {
	var parent=$(obj).parent();
	if (parent.hasClass('message-inline-open')) {
		parent.removeClass('message-inline-open').find(
				'.message-content').remove();
		return;
	}
	parent.append('<div class="message-loading-overlay"><i class="icon-spin icon-spinner orange2 bigger-160"></i></div>');
	$.ajax({   
        type:"post",     
        url:"notice.htm?noticedraft",
        data:{"nId":nId},
        success:function(html){
        	setTimeout(function(){
        		parent.find('.message-loading-overlay').remove();
            	var tagI=$(obj).attr("id");
            	$("#star"+tagI).removeClass("icon-star").addClass("icon-star-empty");
            	parent.addClass("message-inline-open");
            	parent.append(html);
        	},500)
        },   
        error:function(){   
            alert("系统错误，请尝试刷新页面");   
        }   
    });
}

//更改收件箱状态
function showNoticeMessageLook(obj,nId) {
	var parent=$(obj).parent();
	if (parent.hasClass('message-inline-open')) {
		parent.removeClass('message-inline-open').find(
				'.message-content').remove();
		return;
	}
	parent.append('<div class="message-loading-overlay"><i class="icon-spin icon-spinner orange2 bigger-160"></i></div>');
	$.ajax({   
        type:"post",     
        url:"notice.htm?noticedetaillook",
        data:{"nId":nId},
        success:function(html){
        	setTimeout(function(){
        		parent.find('.message-loading-overlay').remove();
            	var tagI=$(obj).attr("id");
            	$("#star"+tagI).removeClass("icon-star").addClass("icon-star-empty");
            	parent.addClass("message-inline-open");
            	parent.append(html);
        	},500)
        },   
        error:function(){   
            alert("系统错误，请尝试刷新页面");   
        }   
    });
}
function studentInfoType0(selectedId){
	$.dialogACE({
		url:"stuinfo/studentPage.htm",
		parameters:{"id":selectedId},
		title:'查找学生',
		callBack:function(dialog){
			$(dialog).dialog('destroy');
			ids+=selectstudentIds+"/";
			ids+=selectstudentNames;
			//$("input[name='receive']").val(selectstudentIds);
			//$("#receive").val(selectstudentNames);
		}
	});
}