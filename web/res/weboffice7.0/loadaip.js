 /*
    var s = ""
	s += "<OBJECT id=HWPostil1 align='middle' style='LEFT: 0px; WIDTH: 100%; TOP: 0px; HEIGHT: 680px'"
	s += "classid=clsid:FF1FE7A0-0578-4FEE-A34E-FB21B277D561 codebase='./HWPostil.ocx#version=3,0,9,8'>"
	s += "<PARAM NAME='_Version' VALUE='65536'>"
	s += "<PARAM NAME='_ExtentX' VALUE='17410'>"
	s += "<PARAM NAME='_ExtentY' VALUE='10874'>"
	s += "<PARAM NAME='_StockProps' VALUE='0'>"
	<object id=WebOffice1 height=768 width='100%' style='LEFT: 0px; TOP: 0px'  classid='clsid:E77E049B-23FC-4DB8-B756-60529A35FAD5' codebase='weboffice_v6.0.5.0.cab#version=6,0,5,0'>"
s +="<param name='_ExtentX' value='6350'><param name='_ExtentY' value='6350'>"
s +="</OBJECT
	s += "</OBJECT>"
	document.write(s) 
	 
 */
var s = "";
if(navigator.userAgent.indexOf("MSIE")>0){
	s = "<OBJECT id='WebOffice1'  style='LEFT: 0px; WIDTH: 100%; TOP: 0px; HEIGHT: 768px'"
		+ "classid=clsid:E77E049B-23FC-4DB8-B756-60529A35FAD5 codebase='./res/weboffice7.0/WebOffice.cab#Version=7,0,0,0'>"
		+ "<PARAM NAME='_ExtentX' VALUE='6350'>"
		+ "<PARAM NAME='_ExtentY' VALUE='6350'>"
		+ "</OBJECT>";
}

if(navigator.userAgent.indexOf("Chrome")>0){
	s = "<object id='WebOffice1' type='application/x-itst-activex'  border='0'"
		+ "style='LEFT: 0px; WIDTH: 100%; TOP: 0px;'"
		+ "progid='./res/weboffice7.0/WebOffice.cab#Version=7,0,0,0'"
		+ "clsid='{E77E049B-23FC-4DB8-B756-60529A35FAD5}'"
		+ "event_NotifyCtrlReady='WebOffice1_NotifyCtrlReady'>"
		+ "</object>";	
}

if(navigator.userAgent.indexOf("Firefox")>0){
	s = "<object id='WebOffice1' type='application/x-itst-activex' border='0'"
		+ "style='LEFT: 0px; WIDTH: 100%; TOP: 0px; HEIGHT: 768px'" 
		+ "progid='./res/weboffice7.0/WebOffice.cab#Version=7,0,0,0'"
		+ "clsid='{E77E049B-23FC-4DB8-B756-60529A35FAD5}'"
		+ "event_NotifyCtrlReady='WebOffice1_NotifyCtrlReady'>"
		+ "</object>";	
}
document.write(s);