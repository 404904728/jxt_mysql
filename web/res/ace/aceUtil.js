//重写弹出框标题
$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
	_title : function(title) {
		var $title = this.options.title || '&nbsp;'
		if (("title_html" in this.options) && this.options.title_html == true)
			title.html($title);
		else
			title.text($title);
	}
}));
jQuery.extend({
	baseDialogACE:function(options){
		var dialog=null;
		if(options.div)dialog=$("#"+options.div);
		else dialog=$("<div id='myDialogAce'></div>");
		var buttonOK={
				text: "确认",
			    "class" : "btn btn-primary btn-xs",
				click: function() {
					if(options.div)
						$("#"+options.div).addClass("hide");
					options.callBack(this);
				} 
		};
		var buttonCancel={
				text: "取消",
				"class" : "btn btn-xs",
				click: function() {
					if(options.div)
						$("#"+options.div).addClass("hide");
					$( this ).dialog( "destroy" ); 
				} 
		};
		var buttons=null;
		if(options.button)
			buttons=[options.button,buttonOK,buttonCancel];
		else
			buttons=[buttonOK,buttonCancel];
		$(dialog).dialog({
			modal: true,show: { effect: "puff", duration:500 },//puff，fade
			height:options.height==null?500:options.height,
			width:options.width==null?1000:options.width,
			resizable:false,//draggable:false,
			close: function(event, ui) {
				if(options.div)
					$("#"+options.div).addClass("hide");
				$( this ).dialog( "destroy" ); 
			},
			title: "<div class='widget-header widget-header-small'><h4 class='smaller'><i class='icon-ok'></i>"+options.title+"</h4></div>",
			title_html: true,
			buttons:buttons
		});
	}
})
/**
 * 弹出模态框
 */
jQuery.extend({
	dialogACE : function(options) {
		if(options.url){
			if(options.frame){
				$.baseDialogACE(options);
	    		$("#myDialogAce").html("<iframe id='"+options.frameId+"' style='border: 0px' width='100%' height='100%' src='"+options.url+"'></iframe>");
	    		return;
			}
			$.ajax({
		        type:"post",url:options.url,
		        data: options.parameters,
		        success:function(html){
		        	$.baseDialogACE(options);
		        	$("#myDialogAce").html(html);
		        },   
		        error:function(){
		        	alert("系统错误，请尝试刷新页面");
		        }   
		    });  
		}
		if(options.div){
			$("#"+options.div).removeClass("hide");
			$.baseDialogACE(options);
		}
	}
})

/**
 * 
 * ACE表单验证提示信息扩展
 * 
 */
jQuery.validator.addMethod("phone", function(value, element) {
	return (/^1[3|4|5|7|8][0-9]\d{4,8}$/.test(value));
}, "请输入正确的手机号码");
jQuery.validator.addMethod("cname", function(value, element) {
	return (/^[\u4e00-\u9fa5]+$/i.test(value));
}, "请输入正确的手机号码");
jQuery.extend(jQuery.validator.messages, {
	required : "该字段必填",
	remote : "请修正该字段",
	email : "请输入正确格式的电子邮件",
	url : "请输入合法的网址",
	date : "请输入合法的日期",
	dateISO : "请输入合法的日期 (ISO).",
	number : "请输入合法的数字",
	phone : "请输入正确的手机号码",
	cname:"请输入汉字",
	digits : "只能输入整数",
	creditcard : "请输入合法的信用卡号",
	equalTo : "请再次输入相同的值",
	accept : "请输入拥有合法后缀名的字符串",
	maxlength : jQuery.validator.format("请输入一个 长度最多是 {0} 的字符串"),
	minlength : jQuery.validator.format("请输入一个 长度最少是 {0} 的字符串"),
	rangelength : jQuery.validator.format("请输入 一个长度介于 {0} 和 {1} 之间的字符串"),
	range : jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
	max : jQuery.validator.format("请输入一个最大为{0} 的值"),
	min : jQuery.validator.format("请输入一个最小为{0} 的值")
});

/**
 * AJAX提交
 */
jQuery.fn.submitACE=function(callBack){
	$.ajax({
		url : $(this).attr("action"),
		data : $(this).serialize(),
		cache : false,
		dataType : "json",
		type : "POST",
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		success : function(data) {
			callBack(data);
		},
		error : function(XmlHttpRequest,textStatus, errorThrown) {
			alert("系统出错，尝试刷新页面");
		}
	});
}

/**
 * 
 * ACE表单验证
 * 
 */
jQuery.fn.validACE = function(parameter, callBack) {
	$(this)
			.validate(
					{
						// debug:true,//调试模式，只验证不提交
						errorElement : 'lable',// 用什么标签标记错误
						errorClass : 'error',
						focusInvalid : false,
						rules : parameter,
						invalidHandler : function(event, validator) {
							
						},
						highlight : function(element) {
							//$(element).parent().parent().parent().find(".error").removeClass("ok");
							$(element).parent().find(".error").removeClass("ok");
						},
						success : function(e) {// 验证通过后
							e.html("&nbsp;").addClass("ok");
						},
						errorPlacement : function(error, element) {
							//console.log(element);
							element.parent().append(error);
							//error.insertAfter(element);
							//error.insertAfter(element.parent().parent());
						},
						submitHandler : function(form) {// 手动提交.用其他方式替代默认的submit
							$.ajax({
									url : $(form).attr("action"),
									data : $(form).serialize(),
									cache : false,
									dataType : "json",
									type : "POST",
									contentType : "application/x-www-form-urlencoded;charset=utf-8",
									success : function(data) {
										callBack(data);
									},
									error : function(XmlHttpRequest,
											textStatus, errorThrown) {
										alert("系统出错，尝试刷新页面");
									}
								});
						}
					})
}