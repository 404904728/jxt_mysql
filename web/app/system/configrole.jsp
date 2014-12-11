<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="./res/script_/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="./res/script_/zTree/js/jquery.ztree.all-3.5.min.js"></script>
<div class="row">
	<div class="col-xs-12">
		<div class="clearfix">
			<div class="pull-left alert alert-success no-margin">
				<button type="button" class="close" data-dismiss="alert">
					<i class="icon-remove"></i>
				</button>
				<i class="icon-umbrella bigger-120 blue"></i>
				点击下面的树可以选择教师，关联到该角色
			</div>
		</div>
	</div>
	<div class="col-xs-12" style="height:10px"></div>
	<div class="col-xs-12">
		<div class="row">
			<div class="col-sm-6">
				<div class="widget-box">
					<div class="widget-header header-color-blue2">
						<h4 class="lighter smaller">学校组织</h4>
					</div>
					<div class="widget-body">
						<div class="widget-main padding-8" style="height:350px;overflow: auto;">
							<!-- <div id="tree1" class="tree"></div> -->
							<ul id="configroleteachertree" class="ztree"></ul>
						</div>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="widget-box">
					<div class="widget-header widget-header-flat">
						<h4>已选择</h4>
					</div>
					<div class="widget-body">
						<div class="widget-main" style="height:350px;overflow: auto;">
							<div class="row">
								<div class="col-xs-12">
									<ul id="selectTeacher" class="list-unstyled spaced">
												<c:forEach items="${role2user }" var="r2u">
													<li class="teacher${r2u[0]}"> 
														<i class="icon-circle green"></i>
														 <span id="${r2u[0]}" class="teacherroleid">${r2u[1]}</span>
													</li>
												</c:forEach>
<!-- 										<li> -->
<!-- 											<i class="icon-circle green"></i> -->
<!-- 											Even more space -->
<!-- 										</li> -->
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div><!-- /span -->
		</div>
		<script type="text/javascript">
			var $assets = "assets";//this will be used in fuelux.tree-sampledata.js
		</script>

		<!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div>
<script type="text/javascript">
jQuery(function($){
	$(".teacherroleid").each(function(){
		arrayObj.push($(this).attr("id"));
	})
	$.fn.zTree.init($("#configroleteachertree"),{
		check: {
			enable: true
		},
		async: {
			enable: true,
			url:"org.htm?findTeacherByRole&rId="+${role.id},
			autoParam:["id"],
		},
		callback: {
			onMouseUp: configroleOnMouseUp,
			onCheck:function(event,treeId,treeNode){
				var type=treeNode.id.split(":");
				if(type[0]=="teacherInfo"){//老师
					if(treeNode.checked){//选中
						arrayObj.push(type[1])
						var html='<li class="teacher'+type[1]+'"><i class="icon-circle green"></i>';
						html+='&nbsp;'+treeNode.name+'</li>'
						$("#selectTeacher").append(html);
					}else{//取消
						$.removeList(arrayObj,type[1]);
						$(".teacher"+type[1]).remove();
					}
				}else{//点击部门
					var child=treeNode.children;
					if(!$.isEmpty(child)){
						if(treeNode.checked){
							for(var i=0;i<child.length;i++){
								if(arrayObj.indexOf(child[i].id.split(":")[1])>-1){
									continue;
								}else{
									arrayObj.push(child[i].id.split(":")[1])
									var html='<li class="teacher'+child[i].id.split(":")[1]+'"><i class="icon-circle green"></i>';
									html+='&nbsp;'+child[i].name+'</li>'
									$("#selectTeacher").append(html);
								}
							}
						}else{
							for(var i=0;i<child.length;i++){
								$.removeList(arrayObj,child[i].id.split(":")[1]);
								$(".teacher"+child[i].id.split(":")[1]).remove();
							}
						}
					}
				}
			}
		}
	});
});
function configroleOnMouseUp(event, treeId, treeNode) {
	
}
</script>