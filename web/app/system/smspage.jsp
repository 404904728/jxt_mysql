<%--
  Created by IntelliJ IDEA.
  User: cqmonster
  Date: 14-8-26
  Time: 下午4:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    $(function() {
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        jQuery(grid_selector).jqGrid({
                    url:'smsManage.htm?findData',datatype: "json",
                    mtype:"post",height: 300,
                    colModel:[
                        {name:'smsId',label:"报告ID",index:'smsId', width:100},
                        {name:'recipient',label:"接收人电话",index:'recipient',width:80},
                        {name:'name',index:'name',label:"发送人名字", width:100},
                        {name:'date',index:'date',label:"本地推送时间", width:150},
                        {name:'markTime',index:'markTime',label:"返回标识时间", width:150},
                        {name:'mark',index:'mark',label:"报告标识", width:150,formatter:function(cellvalue, options, rowObject) {
                            if (cellvalue == null) {
                                return "运营商未返回";
                            } else if (cellvalue == 1) {
                                return "成功";
                            } else if (cellvalue == 2) {
                                return "失败";
                            }
                        }
                        },
                        {name:'report',index:'report',label:"状态报告值", width:80}
                    ],
                    viewrecords : true,rowNum:10,rowList:[10,20,30],
                    pager : pager_selector,altRows: true,//toppager: true,
                    multiselect: false,multiboxonly: true,autowidth: true,//multikey: "ctrlKey",
                    loadComplete : function() {
                        var table = this;
                        setTimeout(function() {
                            updatePagerIcons(table);
                            enableTooltips(table);
                        }, 0);
                    }
                });
        jQuery(grid_selector).jqGrid('navGrid', pager_selector, {});//navButtons
    })
    function searchSmsKey() {
        var event = arguments.callee.caller.arguments[0] || window.event;
        if (event.keyCode == 13) {
            searchSms();
        }
    }
    function searchSms() {
        jQuery("#grid-table").jqGrid('setGridParam', {
            postData:{'searchKey':$("#smsKey").val()},
             page:1//发送数据
        }).trigger("reloadGrid"); //重新载入
    }
</script>
<div class="page-header">
    <h1>
        短信管理
        <small>
            <i class="icon-double-angle-right"></i>
            您可以在这里查询所有短信的发送情况
        </small>
    </h1>
</div>
<!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->

        <div class="widget-box">
            <div class="widget-header widget-header-small">
                <h5 class="lighter">查询搜索</h5>
            </div>
            <div class="widget-body">
                <div class="widget-main">
                    <form class="form-search" onsubmit="return false;">
                        <div class="row">
                            <div class="col-xs-12 col-sm-8">
                                <div class="input-group">
                                    <input type="text" id="smsKey" onkeydown="searchSmsKey()"
                                           class="form-control search-query"
                                           placeholder="请输入关键字查询，您可以输入名字或电话号码，时间（例2014-09-10）"/>
										<span class="input-group-btn">
											<button type="button" onclick="searchSms()"
                                                    class="btn btn-purple btn-sm">
                                                查询
                                                <i class="icon-search icon-on-right bigger-110"></i>
                                            </button>
										</span>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>


        <table id="grid-table"></table>

        <div id="grid-pager"></div>
        <!-- PAGE CONTENT ENDS -->
    </div>
    <!-- /.col -->
</div>
<!-- /.row -->