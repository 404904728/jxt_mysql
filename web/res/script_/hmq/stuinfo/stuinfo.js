$(document).ready(function(){
	$('#dg').datagrid({  
//        nowrap: false,  
//        striped: true,  
//        border: true,  
//        collapsible:false,//是否可折叠的  
//        fit: true,//自动大小  
		width: 1265,
        height: 650,
//        url:'listApp.action',
        //sortName: 'code',
        //sortOrder: 'desc',
        fitColumns: true,
        remoteSort:false,
//        idField:'fldId', 
        toolbar:'#toolbar',
        singleSelect:true,//是否单选  
        pagination:true,//分页控件 
        rownumbers:true,//行号  
    });  
	
	
 	test();
    
    
	
});

function submitForm(){
	url = "http://www.baidu.com"
	$('#studentdetail').ajaxSubmit({
		type:"POST",
 	  		success: function(){
				alert(1);
 	  		}
 	}); 
}

function test(){
	alert(11);
	var paer = $('#dg').datagrid('getPager'); 
	alert(paer);
    paer.pagination({
        pageSize: 30,//每页显示的记录条数，默认为10  
        pageList: [10,20,30,40,50,60],//可以设置每页记录条数的列表
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',  
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',  
        /*onBeforeRefresh:function(){ 
            $(this).pagination('loading'); 
            alert('before refresh'); 
            $(this).pagination('loaded'); 
        }*/ 
    });  
}



var url;
function newUser(){
//    $('#dlg').dialog('open').dialog('setTitle','New User');
//    $('#fm').form('clear');
//    url = 'save_user.php';
	alert(0);
    
}
function editUser(){
//    var row = $('#dg').datagrid('getSelected');
//    if (row){
//        $('#dlg').dialog('open').dialog('setTitle','Edit User');
//        $('#fm').form('load',row);
//        url = 'update_user.php?id='+row.id;
//    }
	alert(1);
}
function saveUser(){
    $('#fm').form('submit',{
        url: url,
        onSubmit: function(){
            return $(this).form('validate');
        },
        success: function(result){
            var result = eval('('+result+')');
            if (result.errorMsg){
                $.messager.show({
                    title: 'Error',
                    msg: result.errorMsg
                });
            } else {
                $('#dlg').dialog('close');        // close the dialog
                $('#dg').datagrid('reload');    // reload the user data
            }
        }
    });
}
function destroyUser(){
    var row = $('#dg').datagrid('getSelected');
    if (row){
        $.messager.confirm('Confirm','Are you sure you want to destroy this user?',function(r){
            if (r){
                $.post('destroy_user.php',{id:row.id},function(result){
                    if (result.success){
                        $('#dg').datagrid('reload');    // reload the user data
                    } else {
                        $.messager.show({    // show error message
                            title: 'Error',
                            msg: result.errorMsg
                        });
                    }
                },'json');
            }
        });
    }
}