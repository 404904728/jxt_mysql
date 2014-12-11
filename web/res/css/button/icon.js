/**
 * <link rel="stylesheet" href="./res/css/button/font-awesome.min.css">
 * <link rel="stylesheet" href="./res/css/button/buttons.css"> 必须要引入上述css
 * HMQICON.del("updateRole("+rowData.id+")")
 * http://fortawesome.github.io/Font-Awesome/icons/#web-application地址icon
 */
var HMQICON = {
	T : 'button-primary',// 天蓝色
	L : 'button-action',// 绿色
	Q : 'button-highlight',// 浅黄色
	H : 'button-caution',// 红色
	Z : 'button-royal',// 紫色
	del : function(fn) {
		return HMQICON.iconButton(HMQICON.H,'cut','删除',fn);
	},
	set : function(fn) {
		return HMQICON.iconButton(HMQICON.T,'cog"','设置',fn);
	},
	iconButton:function(c,icon,title,fn){
		return '<a href="javascript:'+fn+'" class="button ' + c + '"><i class="fa fa-'+icon+'"></i>'+title+'</a>';
	}
	
}