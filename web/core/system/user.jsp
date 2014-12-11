<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
$(function(){
	$("#orgTree").tree({  
	    url:'user.htm?findOrgUser',
	    onClick:function(node){
	    	var idtype=node.id.split(":")[0];
	    	if(idtype=="org"){
	    		//alert("部门"+node.id.split(":")[1])
		    }else if(idtype=="user"){
		    	//alert("用户"+node.id.split(":")[1])
			}
	    },onBeforeExpand:function(){
			//alert(1);
		 }
	});
})
</script>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'west',split:true" title="组织" style="width:200px;">
		<ul id="orgTree"></ul>
	</div>
	<div data-options="region:'center',title:'信息'">
	</div>
</div>
