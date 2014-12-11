<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<script type="text/javascript">
$(function(){
	$("#attachGrid").datagrid({
		url:'attach.htm?attachData',
		pagination:true,
		fit:true,fitColumns:true,
		rownumbers:true,//singleSelect:true,
		pagerBtns:[{
			iconCls:'icon-add',
			handler:function(){alert('add')}
		},'-',{
			iconCls:'icon-save',
			handler:function(){alert('save')}
		}],
		onDblClickRow:function(rowIndex,rowData){
			var docType=$.isOffice(rowData.fileName);
			if(docType!=null){
				window.open("attach.htm?officeView&id="+rowData.id+"&docType='"+docType+"'","文档预览");
			}else{
				Easy.addTabFrame("attachTab","附件查阅","attach.htm?attachUpLoad");
			}
			
		},
		columns:[[
					{field:'suffix',title:'附件类型',width:100,align:'center'},  
					{field:'fileName',title:'附件名称',sortable:true,width:200,align:'center'},  
					{field:'sizeView',title:'附件大小',sortable:true,width:100,align:'center'},
					{field:'date',title:'上传日期',sortable:true,width:200,align:'center'}
		]],
		toolbar:[{  
            text:'附件上传',  
            iconCls:'icon-add',  
            handler:function(){
            	hmq.openWindow("attach.htm?attachUpLoad","600px","400px");
            	//Easy.addTabFrame("attachTab","附件上传","attach.htm?attachUpLoad");
            	// $("#homeLayout").layout("panel", "center").panel("refresh","attach.htm?attachUpLoad");
            }  
        },{  
            text:'附件删除',  
            iconCls:'icon-cut',  
            handler:function(){
	        	var selectRow=$("#attachGrid").datagrid("getSelected");
	        	if(selectRow){
	        		$.hmqAJAX("attach.do?del",function(data){
						Easy.hmqDialog(data,function(){
							$("#attachGrid").datagrid("load");
						})
		        	},{"id":selectRow.id});
	            }else{
					Easy.hmqDialogEror("请先选择要删除的记录");
	            }
            }  
        },'-',{  
            text:'修改描述',  
            iconCls:'icon-edit',  
            handler:function(){
            }  
        },'-',{  
            text:'下载附件',  
            iconCls:'icon-edit',  
            handler:function(){
        		var selectRow=$("#attachGrid").datagrid("getSelected");
            	if(selectRow){
            		window.open(selectRow.htmlUrl);
                }else{
					Easy.hmqDialogEror("请先选择要查看的记录");
                }
            	
            }  
        },'-',{  
            text:'webOffice',  
            iconCls:'icon-edit',  
            handler:function(){
        		var selectRow=$("#attachGrid").datagrid("getSelected");
            	if(selectRow){
            		window.open(hmq.path("attach.htm?officeView_&id="+selectRow.id+"&docType='"+selectRow.suffix+"'"));
                }else{
					Easy.hmqDialogEror("请先选择要查看的记录");
                }
            	
            }  
        }]
	});
})
</script>
<table id="attachGrid"></table>
<!--<div id="attachTab" class="easyui-tabs" data-options="fit:true">-->
<!--	<div title="附件列表" style="padding:3px">-->
<!--		-->
<!--	</div>-->
<!--</div>-->
</body>
</html>