$.extend($.fn.validatebox.defaults.rules, {  
	isMobile: {  
        validator: function(value,param){  
			if((value == null) ||(value == '')) {
				return true;
			}
			var patrn=/(^\d{11,12}$|^\d{6,8}$|^\d{2,4}-\d{6,8}$|^\d{2,4}-\d{2,4}-\d{6,8}$|^\d{6,8}-\d+$|^\d{2,4}-\d{6,8}-\d+$|^\d{2,4}-\d{2,4}-\d{6,8}-\d+$)/;
			if (!patrn.exec(value)) return false
			return true;
        },  
        message: '请输入正确的手机号吗'  
    },
    validNo:{
    	validator: function(value,param){
			if((value == null) ||(value == '')) {
				return false;
	    	}else{
	    		var _valid = $.ajax( {
					url :hmq.path("user.do?validNo"),
					dataType : "json",
					data : {"no":value},
					async : false,
					cache : false,
					type : "post"
				}).responseText;
				return _valid == "false";
	    	}
    	},
    	message: '您输出的账号已经存在'  
    }
});  
