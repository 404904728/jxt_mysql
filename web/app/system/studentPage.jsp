<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<link rel="stylesheet" href="./res/script_/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="./res/script_/zTree/js/jquery.ztree.core-3.5.js"></script>
<div class="row">
    <div class="col-xs-12">
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
                                    <input type="text" id="studentInput" onkeydown="searchQueryKey()"
                                           class="form-control search-query" placeholder="请输入关键字查询，您可以输入名字或电话号码"/>
										<span class="input-group-btn">
											<button type="button" onclick="searchstudent()"
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
    </div>

    <div class="col-xs-12" style="height:10px"></div>
    <div class="col-xs-12">
        <div class="row">
            <div class="col-sm-3">
                <div class="widget-box">
                    <div class="widget-header header-color-blue2">
                        <h4 class="lighter smaller">班级组织</h4>
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
                                学生数据
                                <span class="badge badge-danger" id="classStudentNum">0</span>
                            </a>

                        </li>
                        <li class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                班级/年级信息
                                <i class="icon-caret-down bigger-110 width-auto"></i>
                            </a>
                            <ul class="dropdown-menu dropdown-info">
                                <li><a data-toggle="tab" onclick="showclassmsg()" href="#classInfoMsg"><i
                                        class="green icon-globe bigger-110"></i>查看信息</a></li>
                                <li><a data-toggle="tab" onclick="insertnj()" href="#classInfoMsg"><i
                                        class="green icon-globe bigger-110"></i>新增年级</a></li>
                                <li><a data-toggle="tab" onclick="updatenj()" href="#classInfoMsg"><i
                                        class="green icon-edit bigger-110"></i>修改年级</a></li>
                                <li><a data-toggle="tab" onclick="deletenj()" href="#classInfoMsg"><i
                                        class="green icon-remove bigger-110"></i>删除年级</a></li>
                                <li><a data-toggle="tab" onclick="insertclass()" href="#classInfoMsg"><i
                                        class="green icon-group bigger-110"></i>新增班级</a></li>
                                <li><a data-toggle="tab" onclick="updateclass()" href="#classInfoMsg"><i
                                        class="green icon-edit bigger-110"></i>修改班级</a></li>
                                <li><a data-toggle="tab" onclick="deleteclass()" href="#classInfoMsg"><i
                                        class="green icon-remove bigger-110"></i>删除班级</a></li>
                            </ul>
                        </li>
                        <li class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                学生操作 &nbsp;
                                <i class="icon-caret-down bigger-110 width-auto"></i>
                            </a>

                            <ul class="dropdown-menu dropdown-info">
                                <li>
                                    <a data-toggle="tab" onclick="insertOrUpdateStudent()" href="#stuInfoMsg"><i
                                            class="green icon-user bigger-110"></i>新增学生</a>
                                </li>
                                <li>
                                    <a data-toggle="tab" onclick="updateStudent()" href="#stuInfoMsg"><i
                                            class="green icon-edit bigger-110"></i>修改学生</a>
                                </li>
                                <li><a data-toggle="tab" onclick="querySpacialStu()" href="#stuInfoMsg"><i
                                        class="green icon-external-link bigger-110"></i>删除查询</a></li>
                            </ul>
                        </li>
                    </ul>

                    <div class="tab-content">
                        <div id="profile" class="tab-pane in active" style="height: 475px">
                            <div class="col-xs-12">
                                <table id="grid-table" style="width:100%"></table>
                                <div id="grid-pager"></div>
                            </div>
                        </div>
                        <div id="classInfoMsg" class="tab-pane" style="height: 475px">
                        </div>
                        <div id="stuInfoMsg" class="tab-pane" style="height: 475px">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- PAGE CONTENT ENDS -->
    </div>
    <!-- /.col -->
</div>
<div id="dialog-message" class="hide">
    <form action="" method="post">
        <table>
            <tr>
                <th width="20%">姓名</th>
                <td><input type="text" name="name"/></td>
            </tr>
            <tr>
                <th>账号</th>
                <td><input type="text" name="no"/></td>
            </tr>
            <tr>
                <th>家长</th>
                <td><input type="text" name="parentName"/></td>
            </tr>
            <tr>
                <th>班级</th>
                <td><input type="text" name=""/></td>
            </tr>
        </table>
        注意：初始密码为123456
    </form>
    <div class="hr hr-12 hr-double"></div>
</div>
<div id="dialogTip" class="hide">请先选择所需要删除的行</div>
<script type="text/javascript">
var classObj = null;
var classId = null;
jQuery(function($) {
    $.fn.zTree.init($("#studentTree"), {
                async: {
                    enable: true,
                    url:"org.htm?findStudent",
                    autoParam:["id"]
                },
                callback: {
                    onMouseUp: onMouseUp
                }
            });

    var grid_selector = "#grid-table";
    var pager_selector = "#grid-pager";
    jQuery(grid_selector).jqGrid({
                url:'user.htm?findStudent',datatype: "json",
                mtype:"post",height:380,
                colModel:[
                    {name:'id',label:"ID",index:'id',hidden:true,width:40},
                    {name:'name',label:"学生姓名",index:'name', width:100},
                    {name:'selftel',label:"监护人电话",index:'selftel',width:100},
                    {name:'parentName',label:"监护人",index:'parentName', width:70},
                    {name:'sex',index:'sex',label:"性别", width:50,formatter:function(cellvalue, options, rowObject) {
                        if (cellvalue == 1)return "男";
                        else if (cellvalue == 0)return "女";
                        else return "保密";
                    }},
                    {name:'studentCode',index:'studentCode',label:"学号", width:80},
                    {name:'org',index:'org',label:"班级", width:100, sortable:false,formatter:function(cellvalue, options, rowObject) {
                        if (cellvalue != null) {
                            return cellvalue.name;
                        }
                    }}
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

    /** jQuery(grid_selector).jqGrid('navButtonAdd',pager_selector,{
     caption:"",
     buttonicon:"icon-plus-sign purple",
     position:"first",
     title:"添加学生",
     onClickButton:function(){
     $.dialogACE({
     url:"systemPage.htm?sutdentformPage",
     title:"添加学生",
     width:600,
     height:400,
     callBack:function(dialog){
     $("#studentform").submit();
     }
     })
     }
     }) */
    jQuery(grid_selector).jqGrid('navButtonAdd', pager_selector, {
                caption:"",
                buttonicon:"icon-trash red",
                position:"first",
                title:"删除学生",
                onClickButton:function() {
                    var selectId = $(grid_selector).jqGrid('getGridParam', 'selrow');
                    if ($.isEmpty(selectId)) {
                        bootbox.confirm("你需要先选择删除的行?", function(result) {

                        });
                        return;
                    }

                    bootbox.dialog({
                                message: "<span class='blue bigger-110'>您确定删除该学生信息吗?</span><br/><div class='radio'><label><input name='stype' checked='checked' type='radio' value='1' class='ace'/><span class='lbl'>转学 </span></label>"
                                        + "<label><input name='stype' type='radio' value='2' class='ace'/><span class='lbl'>退学 </span></label>"
                                        + "<label><input name='stype' type='radio' value='3' class='ace'/><span class='lbl'>休学 </span></label>"
                                        + "<label><input name='stype' type='radio' value='4' class='ace'/><span class='lbl'>辞退 </span></label>"
                                        + "<label><input name='stype' type='radio' value='5' class='ace'/><span class='lbl'>其他 </span></label>"
                                        + "</div>",
                                buttons:
                                {
                                    "success" :
                                    {
                                        "label" : "<i class='icon-ok'></i>确定",
                                        "className" : "btn-sm btn-success",
                                        "callback": function() {
                                            var v = $("input[name='stype']:checked").val();
                                            $.hmqAJAX("stuinfo.htm?deleteById", function(data) {
                                                alert(data.msg);
                                                jQuery("#grid-table").jqGrid('setGridParam').trigger("reloadGrid");
                                            }, {"sId":selectId,"deleteType":v});
                                        }
                                    },
                                    "click" :
                                    {
                                        "label" : "<i class='icon-reply'></i>取消",
                                        "className" : "btn-sm btn-primary",
                                        "callback": function() {
                                        }
                                    }
                                }
                            });
                }
            }
    )
});
function showclassmsg() {
    if ($.isEmpty(classId)) {
        bootbox.alert("请先选择要查看的班级");
        return;
    }
    $.hmqRefreshPage("classInfoMsg", "systemPage.htm?orgInfoPage&oId=" + classId);
}
function searchstudent() {
    jQuery("#grid-table").jqGrid('setGridParam', {
                postData:{'searchKey':$("#studentInput").val()} //发送数据
            }).trigger("reloadGrid"); //重新载入
}
function searchQueryKey() {
    var event = arguments.callee.caller.arguments[0] || window.event;
    if (event.keyCode == 13) {
        jQuery("#grid-table").jqGrid('setGridParam', {
                    postData:{'searchKey':$("#studentInput").val()} //发送数据
                }).trigger("reloadGrid"); //重新载入
    }
}
function onMouseUp(event, treeId, treeNode) {
    if ($.isEmpty(treeNode))return;
    var typeId = treeNode.id.split(":");
    if (typeId[0] == 3) {//班级
        classId = typeId[1];
        showclassmsg();
        jQuery("#grid-table").jqGrid('setGridParam', {
                    postData:{'cId':typeId[1]} //发送数据
                }).trigger("reloadGrid"); //重新载入
        setTimeout(function() {
            $("#classStudentNum").html(jQuery("#grid-table").jqGrid("getGridParam", "records"));
        }, 500)//设置学生条数
        classObj = treeNode;
    } else if (typeId[0] == 2) {
        classObj = null;
        if (!$.isEmpty($("#orgParentId").html())) {
            var treeObj = $.fn.zTree.getZTreeObj("studentTree");
            var selectNodes = treeObj.getSelectedNodes();//获取选中的节点
            //console.log(selectNodes);
            $("#orgParentId").html('<input name="parent.id" class="editable hidden" value="' + typeId[1] + '"/>' + treeNode.name)
        }
    } else {
        classObj = null;
    }
}
function getztreeselect() {
    var treeObj = $.fn.zTree.getZTreeObj("studentTree");
    var selectNodes = treeObj.getSelectedNodes();//获取选中的节点
    return selectNodes;
}
function insertOrUpdateStudent() {
    $.hmqRefreshPage("stuInfoMsg", "systemPage.htm?sutdentformPage");
}
//修改学生信息
function updateStudent() {
    var selectId = $("#grid-table").jqGrid('getGridParam', 'selrow');
    if ($.isEmpty(selectId)) {
        bootbox.confirm("你需先选择要修改信息的学生?", function(result) {

        });
    } else {
        $.hmqRefreshPage("stuInfoMsg", "systemPage.htm?sutdentformPage&sId=" + selectId);
    }
}
//查询删除
function querySpacialStu() {
    $.hmqRefreshPage("stuInfoMsg", "systemPage.htm?querySpecialStu");
}
///** 0:校级 1：阶段 2：年级3：班级 4:老师组织结构*/
function insertnj() {//添加年级
    var selectNodes = getztreeselect();//获取选中的节点
    if (selectNodes.length == 0 || selectNodes[0].id.split(":")[0] != 1) {
        bootbox.alert("请先选择阶段");
        return;
    }
    $.hmqRefreshPage("classInfoMsg", "org.htm?njform&save=true&id=" + selectNodes[0].id.split(":")[1]);
}
function updatenj() {
    var selectNodes = getztreeselect();//获取选中的节点
    if (selectNodes.length == 0 || selectNodes[0].id.split(":")[0] != 2) {
        bootbox.alert("请先选择要修改的年级");
        return;
    }
    $.hmqRefreshPage("classInfoMsg", "org.htm?njform&save=false&id=" + selectNodes[0].id.split(":")[1]);
}
function deletenj() {
    var selectNodes = getztreeselect();//获取选中的节点
    if (selectNodes.length == 0 || selectNodes[0].id.split(":")[0] != 2) {
        bootbox.alert("请先选择年级");
        return;
    }
    $.hmqAJAX("org/delclassornj.htm", function(data) {
        bootbox.alert(data.msg);
        if (data.type == 0) {
            $.hmqHomePage("menuPage.htm?studentPage");
        }
    }, {"id":selectNodes[0].id.split(":")[1],"nj":true})
}
function insertclass() {//添加班级
    var selectNodes = getztreeselect();//获取选中的节点
    if (selectNodes.length == 0 || selectNodes[0].id.split(":")[0] != 2) {
        bootbox.alert("请先选择年级");
        return;
    }
    $.hmqRefreshPage("classInfoMsg", "systemPage.htm?orgform&oId=" + selectNodes[0].id.split(":")[1]);
}
function updateclass() {
    var selectNodes = getztreeselect();//获取选中的节点
    if (selectNodes.length == 0 || selectNodes[0].id.split(":")[0] != 3) {
        bootbox.alert("请先选择要修改的班级班级");
        return;
    }
    $.hmqRefreshPage("classInfoMsg", "systemPage.htm?classform&oId=" + selectNodes[0].id.split(":")[1]);
}
function deleteclass() {
    var selectNodes = getztreeselect();//获取选中的节点
    if (selectNodes.length == 0 || selectNodes[0].id.split(":")[0] != 3) {
        bootbox.alert("请先选择班级");
        return;
    }
    $.hmqAJAX("org/delclassornj.htm", function(data) {
        bootbox.alert(data.msg);
        if (data.type == 0) {
            $.hmqHomePage("menuPage.htm?studentPage");
        }
    }, {"id":selectNodes[0].id.split(":")[1],"nj":false})
}
</script>