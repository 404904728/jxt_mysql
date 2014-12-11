<div class="page-header">
	<h1>
		学生信息
		<small>
			<i class="icon-double-angle-right"></i>
			在该页您可以看到学生基本信息与近几次成绩的统计
		</small>
	</h1>
</div><!-- /.page-header -->
<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->

		<div class="tabbable">
			<ul class="nav nav-tabs" id="studentSelfPageTab">
				<li class="active">
					<a data-toggle="tab" href="#faq-tab-1">
						<i class="green icon-user bigger-120"></i>
						学生基本信息
					</a>
				</li>
				<li>
					<a data-toggle="tab" href="#faq-tab-3" onclick="countScort()">
						<i class="orange icon-credit-card bigger-120"></i>
						学生成绩统计
					</a>
				</li>
			</ul>

			<div class="tab-content no-border padding-24">
				<div id="faq-tab-1" class="tab-pane fade in active">
					<h4 class="blue">
						<i class="icon-ok bigger-110"></i>
						学生基本信息
					</h4>
					<div class="space-8"></div>
					<div id="faq-list-1" class="panel-group accordion-style1 accordion-style2">
						<div id="user-profile-1" class="user-profile row">
							<div class="col-xs-12 col-sm-3 center">
								<div>
									<span class="profile-picture">
										<img id="avatar" class="editable img-responsive" alt=""  style="height:225px" src="${student.headPic.htmlUrl}" />
									</span>
									<div class="space-4"></div>
								</div>
			
								<div class="space-6"></div>
			
								<div class="profile-contact-info">
									<div class="profile-contact-links align-left">
										<a href="javascript:changeHeadImg()" class="btn btn-sm btn-block btn-success">
											<i class="icon-plus-sign bigger-120"></i>
											<span class="bigger-110">更换头像</span>
										</a>
			
										<a href="#" class="btn btn-sm btn-block btn-primary">
											<i class="icon-envelope-alt bigger-110"></i>
											<span class="bigger-110">发送私信</span>
										</a>
									</div>
								</div>
							</div>
							<div class="col-xs-12 col-sm-9">
								<div class="profile-user-info profile-user-info-striped">
									<div class="profile-info-row">
										<div class="profile-info-name"> 姓名 </div>
			
										<div class="profile-info-value">
											<span class="editable" id="username">${student.name}</span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name">登录账号 </div>
			
										<div class="profile-info-value">
											<span class="editable" id="username">${student.no}</span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name">性别</div>
										<div class="profile-info-value">
											<span class="editable" id="age">${student.sex==0?'女':'男'}&nbsp;</span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name">学籍号</div>
										<div class="profile-info-value">
											<span class="editable" id="age">${student.studentCode}&nbsp;</span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name">民族</div>
										<div class="profile-info-value">
											<span class="editable" id="age">${student.nation}&nbsp;</span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name"> 生日</div>
										<div class="profile-info-value">
											<span class="editable" id="age">${student.birthday}&nbsp;</span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name">籍贯</div>
										<div class="profile-info-value">
											<i class="icon-map-marker light-orange bigger-110"></i>
											<span class="editable" id="country">${student.address}&nbsp;</span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name">家庭住址</div>
										<div class="profile-info-value">
											<i class="icon-map-marker light-orange bigger-110"></i>
											<span class="editable" id="country">${student.address}&nbsp;</span>
										</div>
									</div>
			
									<div class="profile-info-row">
										<div class="profile-info-name">监护人信息</div>
			
										<div class="profile-info-value">
											<span class="editable" id="login">关系：${student.parentRelation}</span>
											<span class="editable" id="login">姓名：${student.parentName}</span>
											<span class="editable" id="login">电话：${student.selftel}</span>
										</div>
									</div>
			
									<div class="profile-info-row">
										<div class="profile-info-name">所属班级</div>
			
										<div class="profile-info-value">
											<span class="editable" id="about">${student.org.parent.name}<a>${student.org.name}</a></span>
										</div>
									</div>
									<div class="profile-info-row">
										<div class="profile-info-name">班级主任</div>
			
										<div class="profile-info-value">
											<span class="editable" id="about">${student.leader}&nbsp;</span>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div id="faq-tab-3" class="tab-pane fade">
					<h4 class="blue">
						<i class="orange icon-credit-card bigger-110"></i>
						学生成绩统计
					</h4>
					<div class="space-8"></div>
					<div id="container"></div>
				</div>
			</div>
		</div>
		<!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div>
<script src="./res/script_/highcharts-3.0.5/js/highcharts.js"></script>
<script src="./res/script_/highcharts-3.0.5/js/modules/exporting.js"></script>
<script type="text/javascript">
var highchart=null;
function countScort(){
    //legend: {
   // layout: 'vertical',
   // align: 'right',
  //  verticalAlign: 'middle',
  //  borderWidth: 0
//},
	if(!$.isEmpty(highchart))return;
	setTimeout(function(){
	    $.ajax({
	        url: "./scoreManage.htm?analyCjByStudentNameAndOrg",
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
	},500);
}
</script>