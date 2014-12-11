<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="h" uri="/hmq-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
	<script type="text/javascript" src="app/equirepair/equirepair.js"></script>
<body>
	<input type="hidden" id="queryFlg" value="">
	<div class="main-container" id="main-container">
		<div class="main-container-inner">
			<div class="page-content">
				<div class="page-header">
					<h1>
						设备报修 <small> <i class="icon-double-angle-right"></i>
							此功能对学校内所有的损坏设备进行上报维修
						</small>
					</h1>
				</div>
				<!-- /.page-header -->

				<div class="row">
					<div class="col-xs-12">
						<!-- PAGE CONTENT BEGINS -->

						<div class="col-xs-12">
							<div class="tabbable">
								<ul class="nav nav-tabs">
									<li class="active"><a data-toggle="tab" href="#sent"
										data-target="sent" onclick="querySendEmail()"> <i
											class="orange icon-location-arrow bigger-130 "></i> <span
											class="bigger-110">报修发送</span>
									</a></li>
									<h:authoried permissionId="18">
										<li><a data-toggle="tab" href="#inbox"
											onclick="acceptEmail()" data-target="inbox"> <i
												class="blue icon-inbox bigger-130"></i> <span
												class="bigger-110">报修接收</span>
										</a></li>
									</h:authoried>
									<!-- <li class="dropdown"><a data-toggle="dropdown"
										class="dropdown-toggle" href="#"> 上报.. &nbsp; <i
											class="icon-caret-down bigger-110 width-auto"></i>
									</a>
										<ul class="dropdown-menu dropdown-info">
											<li><a data-toggle="tab" href="#write"
													data-target="write" onclick="sendEmail()"
													class="btn-new-mail"> <span class="bigger-110">上报设备报修</span>
												</a>
											</li>
										</ul></li> -->
										<li><a data-toggle="tab" href="#write"
												data-target="write" onclick="sendEmail()"
												class="btn-new-mail"> <span class="bigger-110">上报设备报修</span>
											</a>
										</li>
								</ul>

								<div class="tab-content no-border padding-24" id="testid">
									<div class="tab-pane in active">
										<div class="message-container">
											<div id="id-message-list-navbar"
												class="message-navbar align-center clearfix">
												<div class="message-bar">
													<div class="message-infobar" id="id-message-infobar">
													</div>
												</div>

												<div>
													<div class="messagebar-item-left">
														<label class="inline middle"> <span class="lbl">主题：</span>
														</label>
													</div>

													<div class="nav-search minimized">
														<form id="form-search" onsubmit="return false">
															<span class="input-icon"> <input id="searchkey"
																onkeydown="onclicksearch(this)" type="text"
																autocomplete="off" class="input-small nav-search-input"
																placeholder="请输入后回车 ..." /> <i
																class="icon-search nav-search-icon"></i>
															</span>
														</form>
													</div>
												</div>
											</div>
											<div class="message-list-container">
												<div class="message-list" id="message-list"></div>

											</div>
											<!-- /.message-list-container -->

											<div class="message-footer clearfix" id="pageToRead"></div>

										</div>
										<!-- /.message-container -->
									</div>
									<!-- /.tab-pane -->
								</div>
								<!-- /.tab-content -->
								<div id="write"></div>






							</div>
							<!-- /.tabbable -->
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
					<!-- PAGE CONTENT ENDS -->
				</div>
				<!-- /.col -->
			</div>
			<!-- /.row -->
		</div>
		<!-- /.page-content -->

	</div>
	<!-- /.main-container-inner -->

	</div>
	<!-- /.main-container -->


</body>
</html>