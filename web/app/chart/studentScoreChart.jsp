<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>成绩统计</title>
		<script type="text/javascript" src="./res/script_/hmq/jquery.min.js"></script>
<script src="./res/script_/highcharts-3.0.5/js/highcharts.js"></script>
<script type="text/javascript">
var highchart;
$(function () {
	if(highchart)return;
	    $.ajax({
	        url: "./scoreManage.htm?analyCjByStudentNameAndOrg&orgId=${orgId}&name="+encodeURIComponent("${name}"),
	        cache: false,
	        dataType: "json",
	        type: "POST",
	        contentType: "application/x-www-form-urlencoded;charset=utf-8",
	        success: function(data) {
				highchart= $('#container').highcharts({
				        title: {
				            text: '近期月考成长曲线',
				            x: -20
				        },
				        xAxis: {
				            categories: data.x
				        },
				        tooltip: {
				            formatter: function(event) {
				                    return '<b>'+this.series.name+'</b><br/>'+
				                       this.point.name +'<br/>'+
				                       '排名：'+this.y;
				            }
				        },
				        yAxis: {
				            min:1,
				            reversed:true,
				            title: {
				                text: '年级排名'
				            }
				        },
				        series: data.y
				    });
	       }
	    });
    });
    

		</script>
	</head>
	<body>

<div id="container" style="width:100%;height:100% ;margin: 0 auto"></div>
	</body>
</html>