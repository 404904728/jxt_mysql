<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="./res/ace/assets/js/select2.min.js"></script>	

<script type="text/javascript">
var selected_stuId;
var selected_stuName;
var resubmit = false;
$("input[name='stype']").click(function(){
	 $("input[name='receiverId']").val(null); 
	 $("input[name='receicerName']").val(null); 
	 $("#c_select").val(null); 
	 $("span .select2-chosen").text(''); 
	 var v = $("input[name='stype']:checked").val(); 
	 $("input[name='receiverType']").val(v);
	  if(v == 1 || v == '1'){
	  	$("#xsdiv").find("input[name='receiverId']").removeAttr("disabled");
	  	$("#xsdiv").find("input[name='receicerName']").removeAttr("disabled");
	  	$("#xsdiv").show();
	  	
	  	$("#jsdiv").find("input[name='receiverId']").attr("disabled","disabled");
	  	$("#jsdiv").find("input[name='receicerName']").attr("disabled","disabled");
	  	$("#jsdiv").hide();
	  }else if(v == 2 || v == '2'){
	  	$("#jsdiv").find("input[name='receiverId']").removeAttr("disabled");
	  	$("#jsdiv").find("input[name='receicerName']").removeAttr("disabled");
		$("#jsdiv").show();

	  	$("#xsdiv").find("input[name='receiverId']").attr("disabled","disabled");
	  	$("#xsdiv").find("input[name='receicerName']").attr("disabled","disabled");
	   	$("#xsdiv").hide();
	  }
	});


$("#t_select").css('width','210px').select2({allowClear:true})
.on('change', function(){
	$(this).closest('form').validate().element($(this));
}); 

$("#c_select").css('width','210px').select2({allowClear:true})
.on('change', function(){
	$(this).closest('form').validate().element($(this));
}); 

function findBack(){
	if($.isEmpty($("select[name='cls']").val())){
		bootbox.alert("请先选择班级！");
		return;
	}
	selected_stuId = null;
	selected_stuName = null;
	$.dialogACE({
		url:"pLetter.htm?goto_studentsPage",
		title:"选择接收学生",
		callBack:function(dialog){
		if(!selected_stuId){
			alert("请选择数据！");
		}else{
			 $("input[name='receiverId']").val(selected_stuId); 
			 $("input[name='receicerName']").val(selected_stuName); 
			 $(dialog).dialog("destroy"); 
		}
		},
		width:800,
		height:640});
}

function findBackjs(){
	selected_teaId = null;
	selected_teaName = null;
	$.dialogACE({
		url:"pLetter.htm?goto_teachersPage",
		title:"选择接收教师",
		callBack:function(dialog){
		if(!selected_teaId){
			alert("请选择数据！");
		}else{
			 $("input[name='receiverId']").val(selected_teaId); 
			 $("input[name='receicerName']").val(selected_teaName); 
			 $(dialog).dialog("destroy"); 
		}
		},
		width:800,
		height:640});
	
}

function submitForm(){
	if(resubmit){
		return false;
	}
	resubmit = true;
	$("#send-letter-form").submit();
}

$("#send-letter-form").validACE({
	"receiverId":{required : true},
	"content":{required : true}
},function(data){
	resubmit = false;
	bootbox.alert(data.msg,function(){
	if(data.type == 0 || data.type == '0'){
		sendboxflag=false;
		document.getElementById("send-letter-form").reset();
		$("#s2id_t_select .select2-chosen").text("");
		$("#s2id_c_select .select2-chosen").text("");
	}});
});
</script>

<div class="row"> 
<div class="col-xs-12">
	<div class="widget-box">
		<div class="widget-header widget-header-small">
			<h5 class="lighter">请输入发送内容</h5>
	</div>
	<div class="widget-body">
		<form id="send-letter-form" class="form-horizontal message-form" action="pLetter.htm?savaSendLetter" method="post">
			<c:if test="${usertype == '1'}">
				<div class="form-group">
				<input type="hidden" name="receiverType" value="2"></input>
					<label class="col-sm-3 control-label no-padding-right">教师:</label>
					<div class="col-sm-6">
						<span>
							<select id="t_select" name="receiverId" class="select2"  data-placeholder="选择..." >
								<option value="">&nbsp;</option>
								<c:forEach items="${teachers}" var="cls">
									<option value="${cls.key}">${cls.value}</option>
								</c:forEach>
							</select>
						</span>
					</div>
				</div>
			</c:if>
			<c:if test="${usertype == '2'}">
			<input type="hidden" name="receiverType" value="1"></input>
			
				<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right"
						for="form-field-subject">类型:</label>
					<div class="col-sm-6">
							<div class="radio" style="padding-top:1px">
								<label>
									<input name="stype" type="radio" value="2" class="ace" />
									<span class="lbl"> 教师 </span>
								</label>
															<label>
									<input name="stype" type="radio" checked="checked" value="1"  class="ace" />
									<span class="lbl">学生</span>
								</label>
							</div>
					</div>
				</div>
				
			<div id="xsdiv">
				<div class="hr hr-18 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right"
						for="form-field-subject">班级选择:</label>
					<div class="col-sm-6 col-xs-12">
								<select  name="cls" id="c_select" class="select2">
									<option value="">&nbsp;</option>
									<c:forEach items="${classe}" var="cls">
										<option value="${cls.org.id}">${cls.org.name}</option>
									</c:forEach>
								</select>
					</div>
				</div>
				<div class="hr hr-18 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right"
						for="form-field-recipient">接收人:</label>
					<div class="col-sm-6">
						<span class="input-icon">
						<input type="hidden" name="receiverId"></input>
						<input type="text" onclick="findBack()" name="receicerName"
							id="form-field-recipient" value="" placeholder="请选择接收人" /> <i
							class="icon-user"></i> </span>
					</div>
				</div>
			</div>
			<div id="jsdiv" style="display:none;">
				<div class="hr hr-18 dotted"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right"
						for="form-field-recipient">接收人:</label>
					<div class="col-sm-6">
						<span class="input-icon">
						<input type="hidden" name="receiverId" disabled="disabled"></input>
						<input type="text" onclick="findBackjs()" name="receicerName" disabled="disabled"
							id="form-field-recipient" value="" placeholder="请选择接收人" /> <i
							class="icon-user"></i> </span>
					</div>
				</div>
			</div>
		 </c:if>
			<div class="hr hr-18 dotted"></div>
		
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right"> <span
					class="inline space-24 hidden-480"></span> 内容:
				</label>
				<div class="col-sm-6">
					<textarea maxlength="500" style="width: 80%;height:120px" name="content"></textarea>
				</div>
			</div>
			<div class="hr hr-18 dotted"></div>
			
			<div class="form-group">
									<label class="col-sm-3 control-label no-padding-right"> <span
						class="inline space-24 hidden-480"></span> 
					</label>
					<div class="col-sm-6">
					<span class="inline btn-send-message">
						<button type="button" onclick="submitForm()" class="btn btn-sm btn-primary no-border" style="margin-right: 5px">
							<span class="bigger-120">发送</span>
							<i class="icon-arrow-right icon-on-right"></i>
						</button>
						</span>
					</div>
			</div>
		</form>
	</div>
	</div>
</div>
</div>
