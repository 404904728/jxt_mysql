/*******************************************************************************
 * 
 * 自定义插件
 * 
 * 
 * 
 */
jQuery.extend( {
	hmqKindClick : function(obj) {
		alert(1);
	}
});
KindEditor.lang( {
	hmqProty : '属性',
	hmqInput : '输入框',
	hmqSelect : '下拉框'
});
/*******************************************************************************
 * KindEditor - WYSIWYG HTML Editor for Internet Copyright (C) 2014-2015
 * 
 * 
 * 自定义插件属性
 * 
 * @author 何建
 ******************************************************************************/
KindEditor.plugin('hmqProty', function(K) {
	var self = this, name = 'hmqProty';
	self.clickToolbar(name, function() {

	});
});
/*******************************************************************************
 * KindEditor - WYSIWYG HTML Editor for Internet Copyright (C) 2014-2015
 * 
 * onkeyup 是随便写的不然会把前面的事件搞乱 输入框插件
 * 
 * @author 何建
 ******************************************************************************/
KindEditor.plugin('hmqInput',
				function(K) {
					var self = this, name = 'hmqInput';
					self.clickToolbar(name,function() {
					var textId = hmq.formatDateString(new Date());
					self.insertHtml('<input type="text"  id=text' + textId + '></input>');});
				});
/*******************************************************************************
 * KindEditor - WYSIWYG HTML Editor for Internet Copyright (C) 2014-2015
 * 
 * 
 * 下拉框插件
 * 
 * @author 何建
 ******************************************************************************/
KindEditor.plugin('hmqSelect',
				function(K) {
					var self = this, name = 'hmqSelect';
					self.clickToolbar(name,function() {
					self.insertHtml('<select name="ss" onfocus="console.log(222)" onkeyup="return"></select>');});
				});
