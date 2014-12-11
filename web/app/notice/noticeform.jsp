<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type='text/javascript' src='../res/script_/easyui-1.3.6/jquery.min.js'></script>
<link rel='stylesheet' type='text/css' href='../res/script_/easyui-1.3.6/themes/bootstrap/easyui.css'/>
<link rel='stylesheet' type='text/css' href='../res/script_/easyui-1.3.6/themes/icon.css'/>
<script type='text/javascript' src='../res/script_/easyui-1.3.6/jquery.easyui.min.js'></script>
<script type='text/javascript' src='../res/script_/easyui-1.3.6/locale/easyui-lang-zh_CN.js'></script>
<script type='text/javascript' src='../res/script_/hmq/easyUtil.js'></script>
<script type='text/javascript' src='../res/script_/hmq/hmq.js'></script>
<link rel="stylesheet" href="../res/css/form/form.css"/>
<script type="text/javascript">
    jQuery(function($) {
        $("#noticeTitle").focus().select();
    })
    function clickCancel() {
        document.getElementById("id-message-form").reset();
        parent.reloadNotice('menuPage.htm?noticeInfoPage&type=${noticeType}');
    }

    //作业通知选择学生
    function findStudentType0(ID) {
        if ($.isEmpty($(ID).val())) {
            $("#selectStudent").combobox("clear");
            $("#selectStudent").combobox("loadData", {});
        } else {
            $("#selectStudent").combobox("reload", "../stuinfo/findStudentByClassId.htm?id=" + $(ID).val());
        }
    }
    function sendNoticeStu(b) {
        $("input[name='draft']").val(b);
        $("#id-message-form").formAjax(function(data) {
            alert(data.msg);
            if (data.type == 0) {
                parent.reloadNotice('menuPage.htm?noticeInfoPage&type=${noticeType}');
            }
        }, false);
    }
    function vaildLeng() {
        var check = $('input[name="shortMsg"]:checked').val();
        if (check == 'true') {
            $("#selfsmsspan").show();
            var now = 116 - $("#txtcontent").val().length;
            if (now < 0) {
                $("#txtcontent").val($("#txtcontent").val().substring(0, 116));
                $("#txtcontent").parent().find("span").html("注意：内容不能超过116个字<b>(0)</b>");
            } else {
                $("#txtcontent").parent().find("span").html("注意：内容不能超过116个字<b>(" + now + ")</b>");
            }
        } else {
            $("#selfsmsspan").hide();
            $("#cksms").attr("checked", false);
            $("#txtcontent").parent().find("span").html("");
        }
    }
    function vaildLengzb() {
        var now = 116 - $("#txtcontent").val().length;
        if (now < 0) {
            $("#txtcontent").val($("#txtcontent").val().substring(0, 116));
            $("#txtcontent").parent().find("b").html("(0)");
        } else {
            $("#txtcontent").parent().find("b").html("(" + now + ")");
        }

    }
    function Operation() {
        var values = $('#selectStudent').combobox('getValues');
        if (values.length == 0) {
            alert("您还未选择学生，不能进行操作")
        } else {

        }
    }
</script>
<body>
<h3>
    你可以在下面选择所要发送的人或角色
</h3>

<div style="height: 50px"></div>
<form id="id-message-form" action="../notice/saveOrUpdate.htm" method="post">
    <input type="hidden" name="noticeType" value="${noticeType}"/>
    <table class="form">
        <tr>
            <th width="15%">标题</th>
            <td width="35%">
                <input id="noticeTitle" type="text" name="title"/>
            </td>
            <th width="15%">通知类型</th>
            <td width="35%">
                <c:if test="${noticeType==0}">作业通知</c:if>
                <c:if test="${noticeType==1}">班级通知</c:if>
                <c:if test="${noticeType==2}">教务处通知</c:if>
                <c:if test="${noticeType==3}">学生处通知</c:if>
                <c:if test="${noticeType==4}">年级组通知</c:if>
                <c:if test="${noticeType==5}">招生办通知</c:if>
                <c:if test="${noticeType==6}">行政办通知</c:if>
                <c:if test="${noticeType==7}">研修室通知</c:if>
            </td>
        </tr>
        <tr>
            <th>通知等级</th>
            <td>
                <input name="grade" type="radio" checked="checked" value="0"/>普通
                <input name="grade" type="radio" value="1"/>重要
                <input name="grade" type="radio" value="2"/>紧急
            </td>
            <th>是否发送短信</th>
            <td>
                <c:if test="${noticeType==5}">
                    <input name="shortMsg" type="hidden" value="true"/>是
                    招生办通知默认发送短信 &nbsp;&nbsp;&nbsp;<input name="selfsms" type="checkbox" value="true"/>是否跟自己发送短信
                </c:if>
                <c:if test="${noticeType!=5}">
                    <input name="shortMsg" type="radio" onclick="vaildLeng()" value="true"/>是
                    <input name="shortMsg" type="radio" onclick="vaildLeng()" checked="checked" value="false"/>否
                    &nbsp;&nbsp;&nbsp;<p id="selfsmsspan" style="display: none;"><input id="cksms" name="selfsms" type="checkbox" value="true" />是否跟自己发送短信</p>
                </c:if>
            </td>
        </tr>
        <c:if test="${noticeType==0}">
            <tr>
                <th>选择班级</th>
                <td>
                    <select name="classIds" onchange="findStudentType0(this)">
                        <option value="">&nbsp;</option>
                        <c:forEach items="${team}" var="cls">
                            <option value="${cls.org.id}">${cls.org.name}</option>
                        </c:forEach>
                    </select>
                </td>
                <th>选择学生</th>
                <td>
                    <select id="selectStudent" class="easyui-combobox"
                            data-options="
						    valueField:'id',textField:'name'"
                            name="receive" multiple style="width:200px;">
                    </select>
                </td>
            </tr>
        </c:if>
        <c:if test="${noticeType==1}">
            <tr>
                <th>选择班级</th>
                <td>
                    <select name=classIds onchange="findStudentType0(this)">
                        <option value="">&nbsp;</option>
                        <c:forEach items="${team}" var="cls">
                            <option value="${cls[0]}">${cls[1]}</option>
                        </c:forEach>
                    </select>
                </td>
                <th>选择学生</th>
                <td>
                    <select id="selectStudent" class="easyui-combobox"
                            data-options="
						    valueField:'id',textField:'name',multiple:true"
                            name="receive" style="width:250px;">
                    </select>
                        <%--<a href="javascript:void(0)" class="easyui-linkbutton" onclick="Operation()">操作</a>--%>
                </td>
            </tr>
        </c:if>
        <c:if test="${noticeType==2||noticeType==3||noticeType==4||noticeType==6||noticeType==7}">
            <tr>
                <th>选择老师组织</th>
                <td>
                    <select id="selectTeacher" class="easyui-combotree"
                            data-options="
							url:'../role.htm?findTeacherFindBack&noticeType=${noticeType}',
							method:'post',
							onHidePanel:function(){
								$('#selectTeacherIds').combobox('reload','../teacherInfo/findTeacherByRole.htm?id='+$('#selectTeacher').combobox('getValues'));
							}"
                            multiple name="roleIds" style="width:350px;"></select>
                    <th>选择老师</th>
                <td>
                    <input id="selectTeacherIds" class="easyui-combobox" multiple name="receive" style="width:250px;"
                           data-options="
							valueField:'value',textField:'text',
							groupField:'group'">
                </td>
            </tr>
            <c:if test="${noticeType==2||noticeType==3||noticeType==4}">
                <tr>
                    <th>选择学生班级</th>
                    <td>
                        <select id="selectstudentclass" class="easyui-combotree" name="classIds" multiple
                                style="width:250px;"
                                data-options="
							url:'../org/findClassDatapush.htm?type=${noticeType}',
							method:'post',
						    onHidePanel:function(){
								$('#selectstudents').combobox('reload','../stuinfo/findstudentbyclassids.htm?id='+$('#selectstudentclass').combobox('getValues'));
							}">
                        </select>
                        <th>选择学生</th>
                    <td>
                        <input id="selectstudents" class="easyui-combobox" multiple name="receivestu"
                               style="width:250px;"
                               data-options="
							valueField:'value',textField:'text',
							groupField:'group'">
                    </td>
                </tr>
            </c:if>
        </c:if>
        <c:if test="${noticeType==5}"><!-- 招办通知 -->
        <tr style="height:60px ">
            <th>学生电话</th>
            <td colspan="3">
                <textarea rows="2" cols="" style="width:99%;height:60%" name="stutels"></textarea>
                <span style="color: blue">注意：多个号码请用英文逗号分割，如15100000000,15111111111</span>
            </td>
        </tr>
        <tr style="height:60px ">
            <th>老师电话</th>
            <td colspan="3">
                <textarea rows="2" cols="" style="width:99%;height:60%" name="teatels"></textarea>
                <span style="color: blue">注意：多个号码请用英文逗号分割</span>
            </td>
        </tr>
        </c:if>
        <tr>
            <th>通知内容</th>
            <td colspan="3">
                <c:if test="${noticeType==5}">
                    <textarea id="txtcontent" onkeyup="vaildLengzb()" rows="6" style="width:99%;height: 80%"
                              name="content"></textarea>
                    <span>注意：内容不能超过116个字<b></b></span>
                </c:if>
                <c:if test="${noticeType!=5}">
                    <textarea id="txtcontent" onkeyup="vaildLeng()" rows="6" style="width:99%;height: 80%"
                              name="content"></textarea>
                    <span></span>
                </c:if>
            </td>
        </tr>
    </table>
    <div style="margin-top:5px;text-align:right;padding:10px;">
        <input type="button" value="发送" onclick="sendNoticeStu(false)" style="width:60px;height:30px"/>
        <input type="button" value="取消" onclick="clickCancel()" style="width:60px;height:30px"/>
    </div>
    <!--	<div class="messagebar-item-right">-->
    <!-- 		<span class="inline btn-send-message"> -->
    <!-- 			<button type="button" onclick="sendNoticeStu(true)" class="btn btn-sm btn-primary no-border"> -->
    <!-- 				<i class="icon-save bigger-160"></i> -->
    <!-- 					保存草稿 -->
    <!-- 			</button> -->
    <!-- 		</span> -->
    <!--	</div> -->
</form>
<%--<div id="dlg" class="easyui-dialog" title="Toolbar and Buttons" style="width:400px;height:200px;padding:10px"--%>
<%--data-options="--%>
<%--iconCls: 'icon-save',--%>
<%--toolbar: [{--%>
<%--text:'Add',--%>
<%--iconCls:'icon-add',--%>
<%--handler:function(){--%>
<%--alert('add')--%>
<%--}--%>
<%--},'-',{--%>
<%--text:'Save',--%>
<%--iconCls:'icon-save',--%>
<%--handler:function(){--%>
<%--alert('save')--%>
<%--}--%>
<%--}],--%>
<%--buttons: [{--%>
<%--text:'Ok',--%>
<%--iconCls:'icon-ok',--%>
<%--handler:function(){--%>
<%--alert('ok');--%>
<%--}--%>
<%--},{--%>
<%--text:'Cancel',--%>
<%--handler:function(){--%>
<%--alert('cancel');;--%>
<%--}--%>
<%--}]--%>
<%--">--%>
<%--The dialog content.--%>
<%--</div>--%>
</body>