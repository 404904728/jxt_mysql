<%--
  Created by IntelliJ IDEA.
  User: cqmonster
  Date: 14-8-27
  Time: 下午3:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    $(function() {
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        jQuery(grid_selector).jqGrid({
                    url:'smsManage.htm?findDataDetail&id=${snId}',datatype: "json",
                    mtype:"post",height:300,
                    colModel:[
                        {name:'smsId',label:"报告ID",index:'smsId', width:60},
                        {name:'recipient',label:"接收人电话",index:'recipient',width:80},
                        {name:'name',index:'name',label:"发送人名字", width:100},
                        {name:'sendTime',index:'sendTime',label:"推送时间", width:100},
                        {name:'markTime',index:'markTime',label:"返回标识时间", width:100},
                        {name:'mark',index:'mark',label:"报告标识", width:100,formatter:function(cellvalue, options, rowObject) {
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
</script>

<!-- /.page-header -->

<div class="row">
    <div class="col-xs-12" style="width:800px">
        <!-- PAGE CONTENT BEGINS -->
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
        <!-- PAGE CONTENT ENDS -->
    </div>
    <!-- /.col -->
</div>
<!-- /.row -->