<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    $(function() {

    })
    function viewDetail(id) {
        $.dialogACE({
                    url:"smsManage.htm?showdetail",parameters:{"id":id},
                    title:"短信发送详细",width:800,height:400,
                    callBack:function(dialog) {
                        $(dialog).dialog("destroy");
                    }
                })
    }
</script>
<div class=" message-content" id="id-message-content">
    <div class="message-header clearfix" style="height:40px">
        <div class="pull-left">

            <div class="space-4"></div>
            <i class="icon-star-empty orange2 mark-star"></i>
            &nbsp;
            <img class="middle" alt="John's Avatar" src="${notice.teacherInfo.headpic.htmlUrl}" width="32"/>
            &nbsp;
            <a href="#" class="sender">${notice.teacherInfo.name}</a>
            &nbsp;
            <i class="icon-time bigger-110 orange middle"></i>
            <span class="time">${notice.date}</span>
        </div>
    </div>
    <div class="hr hr-double"></div>
    <div class="message-body">
        <p>
            ${notice.content}
        </p>
    </div>
    <div class="hr hr-double"></div>
    <c:if test="${notice.shortMsg}">
    <div class="message-attachment clearfix">
        <div class="attachment-title">
            <span class="blue bolder bigger-110">短信统计</span>
            &nbsp;
            <span class="grey">(共发送：<font color="blue">${totalSms}</font>人)</span>

            <div class="action-buttons inline" title="点击查看详细">
                <a href="#" onclick="viewDetail(${notice.id})">
                    <i class="icon-search bigger-125 blue"></i>
                </a>
            </div>
        </div>

        </c:if>
    </div>