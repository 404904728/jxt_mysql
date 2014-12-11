<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
<!--
function deleteDraftNotice(id){
	bootbox.confirm("您确定删除该通知的草稿吗?", function(result) {
		if(result) {
			$.hmqAJAX("notice/del.htm",function(data){
				alert(data.msg);
				$("#draft"+id).remove();
			},{"id":id});
		}
	});
}
//-->
</script>
<div class=" message-content" id="id-message-content">
	<div class="message-header clearfix">
		<div class="pull-left">
			<div class="space-4"></div>
	
			<i class="icon-star-empty orange2 mark-star"></i>
			&nbsp;
			<img class="middle" alt="John's Avatar" src="${notice.teacherInfo.headpic.htmlUrl}" width="32" />
			&nbsp;
			<a href="#" class="sender">${notice.teacherInfo.name}</a>
			&nbsp;
			<i class="icon-time bigger-110 orange middle"></i>
			<span class="time">${notice.date}</span>
		</div>
	
		<div class="action-buttons pull-right">
			<a href="#">
				<i class="icon-reply green icon-only bigger-130"></i>
			</a>
	
			<a href="#">
				<i class="icon-mail-forward blue icon-only bigger-130"></i>
			</a>
	
			<a href="javascript:deleteDraftNotice(${notice.id})">
				<i class="icon-trash red icon-only bigger-130"></i>
			</a>
		</div>
	</div>
	
	<div class="hr hr-double"></div>
	
	<div class="message-body">
		<p>
			${notice.content}
		</p>
	</div>
	
	<div class="hr hr-double"></div>
	
	<div class="message-attachment clearfix">
		<div class="attachment-title">
			<span class="blue bolder bigger-110">附件列表</span>
			&nbsp;
			<span class="grey">(2个文件, 4.5 MB)</span>
			<div class="inline position-relative">
				<a href="#" data-toggle="dropdown" class="dropdown-toggle">
					&nbsp;
					<i class="icon-caret-down bigger-125 middle"></i>
				</a>
				<ul class="dropdown-menu dropdown-lighter">
					<li>
						<a href="#">下载</a>
					</li>
					<li>
						<a href="#">在线查看</a>
					</li>
				</ul>
			</div>
		</div>
	
		&nbsp;
		<ul class="attachment-list pull-left list-unstyled">
			<li>
				<a href="#" class="attached-file inline">
					<i class="icon-file-alt bigger-110 middle"></i>
					<span class="attached-name middle">放假通告.doc</span>
				</a>
				<div class="action-buttons inline">
					<a href="#">
						<i class="icon-download-alt bigger-125 blue"></i>
					</a>
					<a href="#">
						<i class="icon-trash bigger-125 red"></i>
					</a>
				</div>
			</li>
	
			<li>
				<a href="#" class="attached-file inline">
					<i class="icon-film bigger-110 middle"></i>
					<span class="attached-name middle">放假原因.pdf</span>
				</a>
	
				<div class="action-buttons inline">
					<a href="#">
						<i class="icon-download-alt bigger-125 blue"></i>
					</a>
	
					<a href="#">
						<i class="icon-trash bigger-125 red"></i>
					</a>
				</div>
			</li>
		</ul>
	</div>
</div>