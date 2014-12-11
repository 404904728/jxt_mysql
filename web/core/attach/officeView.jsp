<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script src="./res/script_/hmq/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="./res/weboffice7.0/main.js"></script>
<script type="text/javascript" src="./res/script_/hmq/hmq.js"></script>
<script type="text/javascript">
$(function(){
	bToolBar_New_onclick();//隐藏新建
	bToolBar_Open_onclick();//隐藏打开
	bToolBar_Save_onclick();//隐藏保存
	bToolBar_vPrintView_onclick();//隐藏打印预览
	bToolBar_vPrint_onclick();//隐藏打印
	SetCustomToolBtn(1,"保存到系统");
	var type=${docType};
	var docType=type.substring(0,3);
	$("#WebOffice1").height($(self).height()-20);
	var obj= document.getElementById('WebOffice1');
	obj.LoadOriginalFile(hmq.httpPath("weboffice/webofficeIo.htm?id="+${id}),docType);
});

//保存到服务器方法
function WebOffice1_NotifyToolBarClick(iIndex){
	if(32777==iIndex){
		SaveDoc();
	}
}
function SaveDoc() {
	var returnValue; // 保存页面的返回值
	document.all.WebOffice1.HttpInit(); // 初始化Http引擎
	document.all.WebOffice1.HttpAddPostString("id",${id});// 添加相应的Post元素 
	document.all.WebOffice1.HttpAddPostCurrFile("file ",""); // 添加上传文件
	returnValue = document.all.WebOffice1.HttpPost(hmq.httpPath("weboffice/webofficeSave.htm"));// 提交上传文件
}
</script>
<script language=javascript event=NotifyToolBarClick(iIndex) for=WebOffice1>
	WebOffice1_NotifyToolBarClick(iIndex);
</script>
</head>
<body>
<script type="text/javascript" src="./res/weboffice7.0/loadaip.js"></script>
</body>
