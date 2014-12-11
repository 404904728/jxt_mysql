<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    $(function() {
        var grid_selector = "#grid-table-message";
        var pager_selector = "#grid-pager-message";
        jQuery(grid_selector).jqGrid({
                    url:'scoreManage.htm?findDataDetail&id=${classId}&title='+encodeURIComponent('${title}'),datatype: "json",
                    mtype:"post",height:300,
                    colModel:[
                        {name:'smsId',label:"报告ID",index:'smsId',hidden:true, width:60},
                        {name:'name',index:'name',label:"接收人名字", width:60},
                        {name:'recipient',label:"接收人电话",index:'recipient',width:80},
                        {name:'content',label:"信息内容",index:'content',width:100},
                        {name:'sendTime',index:'sendTime',label:"推送时间", width:60},
                        //{name:'markTime',index:'markTime',label:"返回标识时间", width:100},
                        {name:'mark',index:'mark',label:"报告标识", width:60,formatter:function(cellvalue, options, rowObject) {
                            if (cellvalue == 3 || cellvalue == '3') {
                                return "运营商未返回";
                            } else if (cellvalue == 1 || cellvalue == '1') {
                                return "成功";
                            } else if (cellvalue == 2 || cellvalue == '2') {
                                return "失败";
                            }
                        }
                        },
                        {name:'report',index:'report',label:"状态报告值", width:60}
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

<div class="row">
    <div class="col-xs-12" style="width:800px">
        <table id="grid-table-message"></table>
        <div id="grid-pager-message"></div>
    </div>
</div>
