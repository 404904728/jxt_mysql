<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
$("#grid-table_student").jqGrid({
	url:'./pLetter.htm?obtainStudentByClassId&cid='+$("#c_select").val(),
	datatype: "json",
	mtype : "post",
	height: 343,
	colNames:['编号','学生学号','学生姓名','性别', '家长姓名'],
	colModel:[
		{name:'id',index:'id', width:10,hidden:true,sorttype:"int",sortable:false, editable: false},
		{name:'studentCode',index:'studentCode', width:200,sortable:false, editable: false},
		{name:'name',index:'name', width:200,editable: false},
		{name:'sex',index:'sex', width:150, sortable:false,editable: false,formatter:function(cell,options,rowdata){
			if(cell == 1){
				return '男';
			}else if(cell == 0){
				return '女';
			}else{
				return '未知';
			}
			}},
		{name:'parentName',index:'parentName', width:200, sortable:false,editable: false} 
	], 

	viewrecords : true,
	rowNum:10,
	rowList:[10,20,30],
	pager : $("#grid-pager__student"),
	altRows: true,
	multiselect: false,
    multiboxonly: false,
	loadComplete : function() {
		setTimeout(function(){
			updatePagerIcons(this);
		}, 0);
	},
	ondblClickRow: function(rowid,iRow,iCol,e){
		 $("input[name='receiverId']").val(rowid); 
		 $("input[name='receicerName']").val($("#grid-table_student").getRowData(rowid).name);  
		 $("#myDialogAce").dialog("destroy"); 
	},
	onSelectRow:function(rowid,status){
		selected_stuId = rowid;
		selected_stuName = $("#grid-table_student").getRowData(rowid).name;
	}
});

function onclicksearch_student(){
	var url = './pLetter.htm?obtainStudentByClassId&cid='+$("#c_select").val();
	var sv = $("#studengName").val();
	if(sv){
		url = url+"&searchKey="+encodeURIComponent(sv);
	}
		$("#grid-table_student").jqGrid('setGridParam',{url: url,page:0}).trigger("reloadGrid");
}

function enterEventIntc(){
	var event=arguments.callee.caller.arguments[0]||window.event;
	if (event.keyCode == 13) 
    {      
		onclicksearch_student();
    }
}
</script>
<div>
	<div class="row">
		<div class="col-sm-12">
		<div class="row">
		<div class="col-xs-12">
		<div class="widget-box">
		<div class="widget-header widget-header-small">
		<h5 class="lighter">学生姓名查询</h5>
		</div>
		<div class="widget-body">
		<div class="widget-main">
		<form class="form-search" onsubmit="return false;">
		<div class="row">
		<div class="col-xs-12 col-sm-8">
		<div class="input-group"><input type="text" id="studengName" onkeydown="enterEventIntc()"
			class="form-control search-query" placeholder="Search ..."
			autocomplete="off"/> <span
			class="input-group-btn">
		<button type="button" class="btn btn-purple btn-sm"
			onclick="onclicksearch_student()">查询 <i
			class="icon-search icon-on-right bigger-110"></i></button>
		</span></div>
		</div>
		</div>
		</form>
		</div>
		</div>
		</div>
		<div class="widget-box">
		<div class="widget-header widget-header-flat  header-color-green2">
		<h4 class="smaller"><i class="icon-list"></i> 学生信息列表</h4>
		</div>
		
		<div class="widget-body" style="height: 455px">
		<table id="grid-table_student"></table>
		<div id="grid-pager__student"></div>
		</div>
		</div>
		</div>
		</div>
		</div>
	</div>

</div>