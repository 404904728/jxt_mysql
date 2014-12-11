var Easy={
	addTabHref:function(tabId,tabTitle,tabHref){/**添加标签*/
		/*var exists=$('#'+tabId).tabs('exists',tabTitle);
		if(exists){
			$('#'+tabId).tabs('select',tabTitle);
			return;
		};*/
		Easy.easyCloseAll('centerTab');
		$('#'+tabId).tabs('add',{  
		   title:tabTitle,  
		   href:tabHref,
		   closable:true 
		});  
	},
	addTabFrame:function(tabId,tabTitle,tabHref){/**添加标签*/
		var exists=$('#'+tabId).tabs('exists',tabTitle);
		if(exists){
			$('#'+tabId).tabs('select',tabTitle);
			return;
		};
		$('#'+tabId).tabs('add',{  
		   title:tabTitle,  
		   content:'<iframe frameborder="0" width="100%" height="100%" name="home" id="home" src="'+tabHref+'"></iframe>',
		   closable:true 
		});  
	},
	easyReload:function(tabId) {/**刷新当前标签*/
		var tab = $('#'+tabId).tabs('getSelected');
		var content = tab.panel('options').content;
		tab.panel('refresh', content);
	},
	easyClose:function(tabId) {/**关闭当前标签*/
		var tab = $('#'+tabId).tabs('getSelected');
		var title = tab.panel('options').title;
		$('#'+tabId).tabs('close', title);
	},
	easyCloseOther:function(tabId) {/**关闭其他标签*/
		var tabs = $('#'+tabId).tabs('tabs');/** 获取所有标签 */
		var tabSelected = $('#'+tabId).tabs('getSelected');/** 获取当前选择的标签 */
		var index = tabs.length;/** 有多少标签,包含首页 */
		for ( var i = index - 1; i > 0; i--) {
			if (tabs[i] == tabSelected) {
				continue;
			}
			$('#'+tabId).tabs('close', i);
		}
	},
	 easyCloseAll:function(tabId) {/** 关闭所有标签*/
		var tabs = $('#'+tabId).tabs('tabs');/** 获取所有标签 */
		var index = tabs.length;
		for ( var i = index - 1; i > 0; i--) {
			$('#'+tabId).tabs('close', i);
		}
	},
	hmqDialog:function(data,objId,fn){
		var easyData=data;
		if(data.type==null){
			var easyData=eval("("+data+")");//转换为json字符串
		}
		var type = easyData.type;
		var msg = easyData.msg;
		var icon = null;
		switch (type) {
		case 0:icon = "success";break
		case 1:icon = "info";break;
		case 2:icon = "warning";break;
		case 3:icon = "question";break;
		case 4:icon = "error";break;
		}
		if($.isFunction(objId)){
			/*** title：显示在窗口头部的标题文本;msg：显示在窗口中的文本。
			 * icon：显示的图片，可选值：error,question,info,warning。
			 * fn：当窗口关闭时触发的回调函数 */
			$.messager.alert("提示",msg,icon,objId);
		}else{
			$("#"+objId).val(easyData.id);//添加后返回数据
			$.messager.alert("提示",msg,icon,fn);
		}
	},
	hmqDialogEror:function(msg,fn){
		$.messager.alert("提示",msg,"error",fn);
	},
	hmqDialogInfo:function(msg,fn){
		$.messager.alert("提示",msg,"info",fn);
	},
	hmqDialogWarning:function(msg,fn){
		$.messager.alert("提示",msg,"warning",fn);
	},
	hmqConfirm:function(title,msg,fnYes,fnNo){
		$.messager.confirm(title,msg, function(r){
			if (r){
				fnYes();
			}else{
				fnNo();
			}
		});
	}
}
/**
 * datagrid的options添加pagerBtns属性用于直接定义分页工具栏自定义按钮，用法与toolbar一样
 */
$.fn.datagrid.defaults.onLoadSuccess = function(){
	var opts = $.data(this,"datagrid").options;
	var panel=$.data(this,"datagrid").panel;
    if(opts.pagination){//有没有分页属性
    	if(opts.pagerBtns){//看看前台页面表格中有没有该属性
    		var pager = $(panel).find(".datagrid-pager");//找到该表格下的分页
    		var pOpts = pager.pagination("options");
    	    if(!pOpts.buttons){//如果过没有该属性则添加进去
    	     pager.pagination({buttons:opts.pagerBtns});
    	   }
    	}
    }
 };
 
 /****************添加标签  iframe方式*******************************
  * tabTitle 标题
  * tabHref  url
  * */
 jQuery.fn.EasyaddTabFrame = function(tabTitle,tabHref) {
	 var exists=$(this).tabs('exists',tabTitle);
		if(exists){
			$(this).tabs('select',tabTitle);
			return;
		};
		$(this).tabs('add',{  
		   title:tabTitle,  
		   content:'<iframe frameborder="0" width="100%" height="100%" name="home" id="home" src="'+tabHref+'"></iframe>',
		   closable:true 
		});  
 }
 
 /****************添加标签方式*******************************
  * tabTitle 标题
  * tabHref  url
  * */
 jQuery.fn.EasyaddTabHref= function(tabTitle,tabHref) {
	 var exists=$(this).tabs('exists',tabTitle);
		if(exists){
			$(this).tabs('select',tabTitle);
			return;
		};
		//Easy.easyCloseAll('centerTab');
		$(this).tabs('add',{  
		   title:tabTitle,  
		   href:tabHref,
		   closable:true 
		});  
 }

 /*****************添加easyui的小图标按钮****************************
  * 
  * 
  */
 jQuery.extend( {
		addIcon: function(icon,title,fn) {
	 		var html="";
	 		html+="<a title='"+title+"' onclick='"+fn+"'  class='l-btn l-btn-plain'>";
	 		html+="<span class='l-btn-left'><span class='l-btn-text'>";
	 		html+="<span class='l-btn-empty icon-color-"+icon+"'>&nbsp;</span></span></span>"+title+"</a>";
	 		return html;
		}
	})
