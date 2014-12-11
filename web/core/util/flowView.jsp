<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<script type="text/javascript" src="./res/js/raphael/raphael-min.js"></script>
<script type="text/javascript" src="./res/js/raphael/util.js"></script>
<script>
$(function(){
	HMQ_R._paper=Raphael("paper",3000,5000);
	$.hmqAJAX("flow.htm?flowData",function(data){
		for(var i=0;i<data.length;i++){
			HMQ_R.createRect(data[i].x,data[i].y,data[i].name,HMQ_R.S,data[i].type).id=data[i].name;
		}
		for(var i=0;i<data.length;i++){
			var names=data[i].toName.split(",");
			jj:for(var j=0;j<names.length;j++){
				if($.isEmpty(names[j])||HMQ_R._paper.getById(names[j])==null){
					continue jj;
				}
				//console.log(data[i].name+"-------------"+names[j]);
				HMQ_R.creatLine(HMQ_R._paper.getById(data[i].name),HMQ_R._paper.getById(names[j]),HMQ_R.S);
			}
		}
	},{"xmlName":"contractApp.xml"})
})
function getTitle(){
	return "<font color='#7FFF00'>绿色</font>:已完成,<font color='#00FFFF'>蓝色</font>:正在执行,<font color='#BCD2EE'>灰色</font>:未执行"
}
function geUrl(){
	return WebJs.action("workflow/Workflow!findLog?difinitonId="+difinitonId+"&busId"+busId);
}
</script>
<div class="easyui-layout" data-options="fit:true" style="">
		<!-- <div data-options="region:'south',split:true" style="height:200px;">
			<table class="easyui-datagrid" title="日志信息"
			data-options="singleSelect:true,collapsible:true,url:getUrl(),fit:true">
				<thead>
					<tr>
						<th data-options="field:'processDate',width:80">处理时间</th>
						<th data-options="field:'currentuser',width:100">处理人</th>
						<th data-options="field:'currentNode',width:80,align:'right'">当前活动</th>
						<th data-options="field:'nextNode',width:80,align:'right'">下一步活动</th>
					</tr>
				</thead>
		  </table>
		</div> -->
		 <!--<div data-options="region:'west',split:true" title="信息" style="width:300px;"></div> -->
		<div data-options="region:'center',title:getTitle()">
			<div id="paper">
			
			</div>
		</div>
	</div>
</body>
</html>