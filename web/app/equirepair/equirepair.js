$(document).ready(function(){
	
	var personId = 2;
	if(1 == personId){
		init('','1','');
	}else{
		init('','2','');
	}
	
});

function showNoticeMessage(obj) {
	$(obj).parent().children("i:eq(0)").removeClass('message-star icon-star orange2');
	$(obj).parent().children("i:eq(0)").addClass('message-star icon-star-empty light-grey');
	var queryFlg = $("#queryFlg").val();
	var emailId = $(obj).children("input:eq(0)").val();
	var parent=$(obj).parent();
	if (parent.hasClass('message-inline-open')) {
		parent.removeClass('message-inline-open').find(
				'.message-content').remove();
		return;
	}
	parent.append('<div class="message-loading-overlay"><i class="icon-spin icon-spinner orange2 bigger-160"></i></div>');
	$.ajax({
        type:"post",
        url:"requimentRepair.htm?emailInfos_",
        data:{"emailId":emailId,"queryFlg":queryFlg},
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
function sendEmail(){
	$("#testid").css('display','none');
	$("#write").css('display','block');
	$.hmqRefreshPage("write","requimentRepair.htm?writeRepairEmail_");
}
function acceptEmail(){
	
	$("#testid").css('display','block');
	$("#write").css('display','none');
	init('','1','');
}

function querySendEmail(){
//	$("#message-list").empty();
	$("#testid").css('display','block');
	$("#write").css('display','none');
	init('','2','');
}

function onclicksearch(_this){
	var event=arguments.callee.caller.arguments[0]||window.event;
	if (event.keyCode == 13) {
		var queryFlg = $("#queryFlg").val();
		var queryConternt = encodeURI(encodeURI($(_this).val()));
		init('',queryFlg,queryConternt);
	    return;
    }
}
function init(page,status,recommon){
	page = page == '' ? 1 : page;
	$("#queryFlg").val(status);
	$.ajax({
			type: "POST",
			url: 'requimentRepair.htm?queryReceiveEmail_&rows=10&page=' + page  + '&status=' + status + '&recommon=' + recommon,
			dataType: "json",
			cache: false,
			success: function(root){
				if(root == undefined){
					return;
				}
				//添加未读邮件标示
				if("1" == status){
					$("#id-message-infobar").empty();
					var emailCount = "<span class='grey bigger-110'>您有 </span><span class='blue bigger-150'>"+ root.emailCount +"</span><span class='grey bigger-110'> 条未读设备报修记录</span>"
					$("#id-message-infobar").append(emailCount);
				}else{
					$("#id-message-infobar").empty();
					var emailCount = "<span class='grey bigger-110'>您共发送  </span><span class='blue bigger-150'>"+ root.emailCount +"</span><span class='grey bigger-110'> 条设备报修记录</span>"
					$("#id-message-infobar").append(emailCount);
				}
				//处理邮件正文
				$("#message-list").empty();
				var resList = root.equList;
				if(resList.length == 0){
					$("#message-list").append("暂无邮件");
				}
				var emailnr = "";
				for(var i = 0; i < resList.length; i++){
					var msg = resList[i];
					emailnr += "<div class='message-item message-unread'>";
					if("1" == status){
						if(msg.repairStatus == 1){
							emailnr += "<i class='message-star icon-star orange2'></i>";
						}else{
							emailnr += "<i class='message-star icon-star-empty light-grey'></i>";
						}
					}else{
						emailnr += "<i class='message-star icon-star-empty light-grey'></i>";
					}
					
					emailnr += "<span class='sender' title='Alex John Red Smith'>"+ msg.sendTea.name +"老师</span>" +
					"<span class='time'>"+ msg.repairTime.substring(5,10) +"</span>" + 
					"<span class='summary' onclick='showNoticeMessage(this)'>"+
					"<span class='badge badge-pink mail-tag'></span>"+
					"<span class='text'> "+ msg.repairTitle +" </span><input type='hidden' value='"+ msg.id +"'></span></div>";
				}
				$("#message-list").append(emailnr);
				//页码
				$('#pageToRead').empty();
				var page_html =  pageHtml(root.totalCount,root.totalPage,root.pageNo,status,recommon);
				$('#pageToRead').append(page_html);

			}
			 
			
		}); 
}

/**页码html total： 总数 page： 当前页数 pageSize： 每页大小*/
function pageHtml(total,pages,nowpage,cityName,cityCode){
	var page_html = "";
	if(pages == 1){
		page_html = "<div class='pull-left'> 总邮件数 "+ total +" 封 </div><div class='pull-right'><div class='inline middle'> 每页10条 共 "+ pages +" 页 </div>&nbsp; &nbsp;" +
				"<ul class='pagination middle'><li class='disabled'><span><i class='icon-step-backward middle'></i></span></li><li class='disabled'><span><i class='icon-caret-left bigger-140 middle'></i>" +
				"</span></li><li><span><input value=\""+ nowpage +"\" maxlength=\""+ pages +"\" type='text' /></span></li><li class='disabled'><span><i class='icon-caret-right bigger-140 middle'></i></span></li>" +
				"<li class='disabled'><span><i class='icon-step-forward middle'></i></span></li></ul></div>";
	}else if(nowpage == pages){
		page_html = "<div class='pull-left'> 总邮件数 " + total + " 封 </div><div class='pull-right'><div class='inline middle'> 每页10条 共 " + pages +" 页 </div>&nbsp; &nbsp;" +
					"<ul class='pagination middle'><li><a href='javascript:void(0)' onclick='init(\"1\",\""+ cityName +"\",\""+ cityCode +"\")'><i class='icon-step-backward middle'></i></a></li>" +
					"<li><a href='javascript:void(0)' onclick='init(\""+ (nowpage-1) +"\",\""+ cityName +"\",\""+ cityCode +"\")'><i class='icon-caret-left bigger-140 middle'></i></a></li>" +
					"<li><span><input value=\""+ nowpage +"\" maxlength=\""+ pages +"\" type='text' /></span></li><li class='disabled'><span><i class='icon-caret-right bigger-140 middle'></i></span></li>" +
					"<li class='disabled'><span><i class='icon-step-forward middle'></i></span></li></ul></div>";
	}else if(nowpage <= 1){
		page_html = "<div class='pull-left'> 总邮件数 " + total + " 封 </div><div class='pull-right'><div class='inline middle'> 每页10条 共 " + pages +" 页 </div>&nbsp; &nbsp;" +
					"<ul class='pagination middle'><li class='disabled'><span><i class='icon-step-backward middle'></i></span></li><li class='disabled'><span><i class='icon-caret-left bigger-140 middle'></i>" +
					"</span></li><li><span><input value=\""+ nowpage +"\" maxlength=\""+ pages +"\" type='text' /></span></li>" +
					"<li><a href='javascript:void(0)' onclick='init(\""+ (nowpage+1) +"\",\""+ cityName +"\",\""+ cityCode +"\")'><i class='icon-caret-right bigger-140 middle'></i></a></li>" +
					"<li><a href='javascript:void(0)' onclick='init(\""+ (pages) +"\",\""+ cityName +"\",\""+ cityCode +"\")'><i class='icon-step-forward middle'></i></a></li></ul></div>";
	}else{
		page_html = "<div class='pull-left'> 总邮件数 " + total + " 封 </div><div class='pull-right'><div class='inline middle'> 每页10条 共 " + pages +" 页 </div>&nbsp; &nbsp;" +
					"<ul class='pagination middle'><li><a href='javascript:void(0)' onclick='init(\"1\",\""+ cityName +"\",\""+ cityCode +"\")'><i class='icon-step-backward middle'></i></a></li>" +
					"<li><a href='javascript:void(0)' onclick='init(\""+ (nowpage-1) +"\",\""+ cityName +"\",\""+ cityCode +"\")'><i class='icon-caret-left bigger-140 middle'></i></a></li>" +
					"<li><span><input value=\""+ nowpage +"\" maxlength=\""+ pages +"\" type='text' /></span></li>" +
					"<li><a href='javascript:void(0)' onclick='init(\""+ (nowpage+1) +"\",\""+ cityName +"\",\""+ cityCode +"\")'><i class='icon-caret-right bigger-140 middle'></i></a></li>" +
					"<li><a href='javascript:void(0)' onclick='init(\""+ (pages) +"\",\""+ cityName +"\",\""+ cityCode +"\")'><i class='icon-step-forward middle'></i></a></li></ul></div>";
	}
	return page_html;
}