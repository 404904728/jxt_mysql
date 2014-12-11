<link rel="stylesheet" type="text/css" href="./res/css/form/form.css"/>
<script type="text/javascript">
$(function(){
	
})
function roleFormSubmit(){
	$("#roleForm").formAjax(function(data){
		Easy.hmqDialog(data,function(){
			$("#roleTable").datagrid("reload");
			$("#roleWin").window("close");
		})
	});
}
</script>
<form id="roleForm" action="role.htm?saveOrUpdate" method="post">
	<input type="hidden" name="id" value="${role.id}">
	<table class="form">
		<tr>
			<th width="30%">角色名称</th>
			<td width="70%">
				<input type="text"  name="name" value="${role.name}" class="easyui-validatebox" data-options="required:true"/>
			</td>
		</tr>
		<tr>
			<th>角色描述</th>
			<td>
				<input type="text"  name="desc" value="${role.desc}" class="easyui-validatebox" data-options="required:true"/>
			</td>
		</tr>
		<tr>
			<th>是否可用</th>
			<td>
				是:<input type="radio" name="use" ${role.use==true?'checked':''}/>
				否:<input type="radio" name="use" ${role.use==false?'checked':''}/>
			</td>
		</tr>
	</table>
</form>
<div style="margin-top:5px;text-align:right;padding:10px;">
	<input type="button" value="保存" onclick="roleFormSubmit()" style="width:60px;height:30px"/> 
</div>