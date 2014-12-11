<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!-- <script src="./res/highcharts/js/highcharts.js"></script> -->
<!-- <script src="./res/highcharts/js/modules/exporting.js"></script> -->
<div class="row">
    <div class="col-xs-12" style="height:10px"></div>
    <div class="col-xs-12">
        <div class="row">
            <div class="col-sm-12">
                <div id="profile" class="tab-pane in active">
                    <table id="grid-table" style="width:100%"></table>
                    <div id="grid-pager"></div>
                </div>
            </div>
<!--             <div class="col-sm-12"> -->

<!--                 <div id="container"></div> -->

<!--             </div> -->
        </div>
        <!-- PAGE CONTENT ENDS -->
    </div>
    <!-- /.col -->
</div>
<script type="text/javascript">
    var classObj = null;
    var classId = null;
    jQuery(function($) {
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        jQuery(grid_selector).jqGrid({
                    url:'smsManage.htm?count',datatype: "json",
                    mtype:"post",height:450,
                    colModel:[
                        {name:'start',label:"开始时间",index:'start', width:100},
                        {name:'end',label:"结束时间",index:'end', width:70},
                        {name:'count',label:"信息量",index:'count', width:70},
                        {name:'billing',label:"计费量",index:'billing', width:70},
                        {name:'push',label:"网络信息量",index:'push', width:70}
                    ],
                    viewrecords : true,rowNum:10,rowList:[10,20,30],autowidth: true,
                    pager : pager_selector,altRows: true,//toppager: true,
                    multiselect: false,//multikey: "ctrlKey",multiboxonly: true,
                    loadComplete : function() {
                        var table = this;
                        setTimeout(function() {
                            updatePagerIcons(table);
                            enableTooltips(table);
                            setTimeout(function() {
                                $("#classStudentNum").html(jQuery("#grid-table").jqGrid("getGridParam", "records"));
                            }, 500)
                        }, 0);
                    }
                });
        jQuery(grid_selector).jqGrid('navGrid', pager_selector, {});//navButtons
        //countSmsPie();
    });
    /**function countSmsPie() {
        $('#container').highcharts({
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false
                    },
                    title: {
                        text: '短信图形统计'
                    },
                    tooltip: {
                        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                color: '#000000',
                                connectorColor: '#000000',
                                format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                            }
                        }
                    },
                    series: [
                        {
                            type: 'pie',
                            name: '占比',
                            data: [
                                ['短信数',23],
                                {
                                    name: '计费数',
                                    y: 30,
                                    sliced: true,
                                    selected: true
                                },
                            ]
                        }
                    ]
                });**/
    //}
</script>