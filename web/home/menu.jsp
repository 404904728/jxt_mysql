<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel='stylesheet' type='text/css'
	href='./res/css/icon/icon.css' />
<script type="text/javascript">
$(function() {
   $("#menuTree").tree({
	   url: 'menu.htm?findData',
       onClick:function(node) {
           if (!$.isEmpty(node.attributes)) {
               if (node.attributes.lastIndexOf("html") > 0) {
                   window.open(node.attributes);
               } else {
                   if (node.attributes.indexOf("form") > 0) {
                       window.open(node.attributes);
                   }else{
                   	$("#homeLayout").layout("panel", "center").panel("refresh", node.attributes+"&menuId="+node.id);
                   }
               }
           }
       }             
  });
})
</script>
<div id="aa" class="easyui-accordion" data-options="fit:true">  
    <div title="菜单" iconCls="icon-save" style="padding:10px;">  
       <ul id="menuTree" class="easyui-tree"></ul>
    </div>  
</div>  