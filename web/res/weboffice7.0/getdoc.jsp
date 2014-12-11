<%@ page contentType="text/html;charset=GB2312"%>
<%@ page import="common.cq.hmq.service.AttachService"%>
<%@ page import="core.cq.hmq.dao.util.BeanUtil" %>
<%@ page import="common.cq.hmq.pojo.sys.Attach" %>
<%@ page import="java.io.*"%>
<%@ page import="core.cq.hmq.dao.util.BeanUtil" %>
<%
	final AttachService attachService = (AttachService) BeanUtil.getBean("attachService");
	Long id = Long.parseLong(request.getParameter("id"));
	Attach attach = attachService.getAttachWithFile(id);
	if(attach!=null){
		OutputStream ops = response.getOutputStream();
		
		/**********************************************
		即Servlet规范说明，不能既调用 response.getOutputStream()，
		又调用response.getWriter()，
		无论先调用哪一个，在调用第二个时候应会抛出 IllegalStateException，
		因为在jsp中，out变量是通过response.getWriter得到的，
		在程序中既用了response.getOutputStream，又用了out变量，*/
		out.clear();
		out = pageContext.pushBody();
		/*********************************************/
		try{
			int i = 0;
			byte[] bt = new byte[8192];
			InputStream fis = attach.getFile();
			while ((i = fis.read(bt)) != -1) {
				ops.write(bt, 0, i);
			}
			fis.close();
		}catch(Exception e){
			
		}
		ops.flush();
		ops.close();
	}
%>