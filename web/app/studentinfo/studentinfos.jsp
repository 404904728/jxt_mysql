<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="core.cq.hmq.modal.SessionModal"%>
<%
	SessionModal sessionModal = (SessionModal)request.getSession().getAttribute("sessionModal");
	String userType_ = sessionModal.getUserType();
%>

		<script src="./res/ace/assets/js/select2.min.js"></script>	
		<script type="text/javascript">
			

			var $path_base = "/";//this will be used in gritter alerts containing images
			var userType_js = '<%=userType_%>';
			 $('body').resize(function(){   
		    	   $("#grid-table").setGridWidth($("#studentscoregrid").width() - 8,true);
	   		 });
		</script>
		
		<script src="res/script_/hmq/stuinfo/sevform.js"></script>
		<script src="res/script_/hmq/stuinfo/studentinfo.js"></script>
		
			<div id="studentByClassMess">
			
			<div class="alert alert-info" style="height: 50px;">
				<!-- <table id="classMessage" style="display: none;"><tr><td><label for="form-field-select-1">班级列表:</label>
					</td><td>
						<select class="form-control" id="form-field-select-1" style="width: 200px" onchange="queryStuByClass()">
						</select>
						
					</td></tr></table> -->
				<form class="form-horizontal" role="form" onsubmit="return false;">
					<div class="form-group" id="classMessage">
							<div class="col-sm-9">
								<span> 班级:&nbsp;&nbsp;<select id="form-field-select-1" class="select2"
									data-placeholder="选择..." onchange="queryStuByClass()">
								</select>
								</span>
							</div>
						</div>
				</form>
				<div id="home_message" style=" width:200px;margin-left: 5px;display: none;">
				</div>
			</div>
			<div id="studentscoregrid" class="widget-box">
				<div class="widget-header widget-header-small  header-color-green">
					<h4 class="smaller">学生详情</h4>
				</div>
				<div class="widget-body" style="height: 498px">
					<table id="grid-table"></table>
					
					<div id="grid-pager"></div>
				</div>
			</div>
				
			</div>
			<div id="studentImage"  style="display:none;" class="widget-main padding-8">
			</div>
			<div id="readStuMeaasge" style="display:none;" class="widget-main padding-8">
				
			</div>
			
