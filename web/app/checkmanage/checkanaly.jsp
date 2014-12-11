<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script language="javascript" type="text/javascript" src="./res/script_/date/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="./res/script_/highcharts-3.0.5/js/highcharts.js"></script>
<script type="text/javascript" src="./res/script_/highcharts-3.0.5/js/highcharts-more.js"></script>
<script type="text/javascript">
function onclickanaly(){
var txtB = $("#txtB").val();
var txtE = $("#txtE").val();
if(!txtB || !txtE){
	bootbox.alert("请输入考勤时间范围！");
	return;
}

var sid = $("input[name='receiverId']").val(); 
var sname = $("input[name='receicerName']").val(); 
	if(sid && sname){
	    $.ajax({
	        url: "./checkManage.htm?getStudentInfo&sId="+sid+"&classId="+g_classid+"&sDate="+txtB+"&eDate="+txtE,
	        cache: false,
	        dataType: "json",
	        type: "POST",
	        contentType: "application/x-www-form-urlencoded;charset=utf-8",
	        success: function(data) {
	        	studentanaly(data[1],data[2],data[3],data[0]);
	       }
	    });
		
	}else{
	    $.ajax({
	        url: "./checkManage.htm?getClassInfo&classId="+g_classid+"&sDate="+txtB+"&eDate="+txtE,
	        cache: false,
	        dataType: "json",
	        type: "POST",
	        contentType: "application/x-www-form-urlencoded;charset=utf-8",
	        success: function(data) {
				banjianaly(data[1],data[2],data[0]);
	       }
	    });
	}
}

function banjianaly(xname,ydata,title){
	//if(!ydata){
		//ydata = [67, 33,99];
	//}
	//if(!xname){
		//xname = ['2013-03-01', '2013-03-05','2013-03-08'];
	//}
    $('#analydiv').highcharts({
        title: {
            text: title+'考勤图',
            x: -20 //center
        },
        xAxis: {
            categories: xname
        },
        yAxis: {
            min:0,
            max:100,
            allowDecimals:false,
            title: {
                text: '出勤率'
            },
            labels: {
                formatter: function() {
                    return this.value +'%'
                }
            }
            
        },
        tooltip: {
            formatter: function(event) {
                    return '<b>'+ this.point.name +'</b><br/>'+
                    this.x+":"+this.y+"%";
            }
        },
        series: [{
            name: '考勤日期',
            data: ydata
        }]
    });
};

function studentanaly(xname,ydata1,ydata2,title){
    //var xname = ['2013-03-01', '2013-03-05','2013-03-08'];
    //var ydata1 = [1, null,1];
    //var ydata2 = [null,1,null];
    $('#analydiv').highcharts({
         chart: {
	        type: 'bubble'
	    },
        title: {
            text: title+'考勤图',
            x: -20 //center
        },
        xAxis: {
            categories: xname
        },
        yAxis: {
            title:{text:null},
            labels:{enabled:false}
        },
        tooltip: {
            formatter: function() {
                    return '<b>'+ this.point.name +'</b><br/>'+
                    this.x +":"+this.series.name;
            }
        },
        series: [{
            name: '已到',
            data: ydata1
            },{name:'未到',data:ydata2 }
             ]
    });
};

function findBack(){
	if(!g_classid){
		bootbox.alert("请先选择班级！");
		return;
	}
	selected_stuId = null;
	selected_stuName = null;
	$.dialogACE({
		url:"pLetter.htm?goto_studentsPage",
		title:"选择统计学生",
		callBack:function(dialog){
		if(!selected_stuId){
			alert("请选择数据！");
		}else{
			 $("input[name='receiverId']").val(selected_stuId); 
			 $("input[name='receicerName']").val(selected_stuName); 
			 $(dialog).dialog("destroy"); 
		}
		},
		width:800,
		height:640});
}

function back(){
	$("#chckanaly").css('display','none');
	$("#chckmain").css('display','block');
}
</script>
		<div class="widget-box">
		<div class="widget-header widget-header-small">
			<h5 class="lighter">请输入统计条件</h5>
		</div>
		<div class="widget-body">
			<form class="form-horizontal message-form" >
			<div class="row" style="height: 140px">
				<div class="col-xs-12">
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right"
								for="form-field-recipient">学生:</label>
							<div class="col-sm-7">
								<span class="input-icon">
								<input type="hidden" name="receiverId"></input>
								<input type="text" onclick="findBack()" name="receicerName"
									id="form-field-recipient" value="" placeholder="请选择学生" /> <i
									class="icon-user"></i> </span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right">考勤时间:</label>
							<div class="col-sm-7">
								<div class="input-icon block col-xs-5 no-padding" >
									<input id="txtB" name="startDate" style="height: 90%" name="birthday" 
									 type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'txtE\')}'})"> 
									<i class="icon-time"></i>
								</div>
								<div class="input-icon block col-xs-5 no-padding">
									<input id="txtE" name="endDate"  style="height: 90%" name="birthday"
									  type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'txtB\')}',maxDate:'#F{$dp.$D(\'txtB\',{M:2})}'})">
									<i class="icon-time"></i>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label no-padding-right"></label>
							<div class="col-sm-7 ">
								<span class="input-group-btn">
								  <button type="button" class="btn btn-purple btn-sm" onclick="onclickanaly()">统计<i
										class="icon-bar-chart icon-on-right bigger-110"></i></button>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										 <button type="reset" class="btn btn-info btn-sm">重置<i
										class="icon-wrench icon-on-right bigger-110"></i></button>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										 <button type="button" class="btn btn-sm" onclick="back()">返回<i
										class="icon-reply icon-on-right bigger-110"></i></button>
										
								</span>
							</div>
						</div>
					</div>
				</div>
			</form>
		 </div>
	</div>
	
	<div class="widget-box">
		<div class="widget-header widget-header-small">
			<h5 class="lighter">统计结果</h5>
		</div>
		<div class="widget-body">
			<div id="analydiv" style="height: 400px;width: 1000px" ></div>
		</div>
	</div>
