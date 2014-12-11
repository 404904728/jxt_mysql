<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<link rel="stylesheet" href="./res/script_/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="./res/script_/zTree/js/jquery.ztree.core-3.5.js"></script>
<script src="./res/highcharts/js/highcharts.js"></script>
<script src="./res/highcharts/js/modules/exporting.js"></script>
<div class="row">
    <div class="col-xs-12" style="height:10px"></div>
    <div class="col-xs-12">
        <div class="row">
            <div class="col-sm-3">
                <div class="widget-box">
                    <div class="widget-header header-color-blue2">
                        <h4 class="lighter smaller">组织</h4>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main padding-8" style="height: 500px">
                            <!-- <div id="tree1" class="tree"></div> -->
                            <ul id="studentTree" class="ztree" style="height:480px;overflow: auto;"></ul>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-9">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="studentTab">
                        <li class="active">
                            <a data-toggle="tab" href="#profile">
                                短信统计
                                <span class="badge badge-danger" id="classStudentNum">0</span>
                            </a>
                        </li>
                        <li><a data-toggle="tab" href="#chartshow"> <span class="bigger-110">图形展示</span>
                        </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div id="profile" class="tab-pane in active" style="height: 475px">
                            <div class="col-xs-12">
                                <table id="grid-table" style="width:100%"></table>
                                <div id="grid-pager"></div>
                            </div>
                        </div>
                        <div id="chartshow" class="tab-pane" style="height: 475px">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- PAGE CONTENT ENDS -->
    </div>
    <!-- /.col -->
</div>
<script type="text/javascript">
    var classObj = null;
    var classId = null;
    jQuery(function($) {
        $.fn.zTree.init($("#studentTree"), {
                    async: {
                        enable: true,
                        url:"org.htm?findsto",
                        autoParam:["id"]
                    },
                    callback: {
                        onMouseUp: onMouseUp
                    }
                });
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
    });
    /**
     *点击班级
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onMouseUp(event, treeId, treeNode) {
        if ($.isEmpty(treeNode))return;
        var typeId = treeNode.id.split(":");
        classId = typeId[1];
        jQuery("#grid-table").jqGrid('setGridParam', {
                    postData:{'oid':typeId[1]} //发送数据
                }).trigger("reloadGrid"); //重新载入
        setTimeout(function() {
            $("#classStudentNum").html(jQuery("#grid-table").jqGrid("getGridParam", "records"));
        }, 500)
        classObj = treeNode;
    }
</script>