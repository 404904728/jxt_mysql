<%@page language="java" pageEncoding="utf-8" %>
<%@page import="com.mchange.v2.c3p0.PooledDataSource" %>
<%@page import="core.cq.hmq.dao.util.BeanUtil" %>
<meta http-equiv="Expires" Content="0"/>
<meta http-equiv="pragma" content="no-cache"/>
<%
    Runtime r = Runtime.getRuntime();
    Object ds = BeanUtil.getBean("dataSource");
    PooledDataSource ps = null;
    if (ds instanceof PooledDataSource) {
        ps = (PooledDataSource) ds;
    }
%>
<div class="place">
    <span>位置：</span>
    <ul class="placeul">
        <li><a href="#">首页</a></li>
        <li><a href="#">系统管理</a></li>
        <li><a href="#">系统状态</a></li>
    </ul>
</div>

<div class="mainindex">
    <div class="xline"></div>
    <div class="welinfo">
        <b>空闲内存:<%=r.freeMemory() / 1024 / 1024%>M</b>
        <b>最大内存:<%=r.maxMemory() / 1024 / 1024%>M</b>
        <b>总内存:<%=r.totalMemory() / 1024 / 1024%>M</b>
    </div>
    <div class="xline"></div>
    <div class="welinfo">
        <b>可用处理器:<%=r.availableProcessors()%>
        </b>
    </div>
    <div class="xline"></div>
    <div class="welinfo">
        <%if (ps != null) { %>
        <b>数据库总连接数:<%=ps.getNumConnectionsAllUsers()%>
        </b>
        <b>数据库连接数:<%=ps.getNumConnectionsDefaultUser()%>
        </b>
        <b>未释放连接数:<%=ps.getNumBusyConnectionsDefaultUser()%>
        </b>
        <b>空闲连接数:<%=ps.getNumIdleConnectionsDefaultUser()%>
        </b>

        <%} else {%>
        <b>非c3p0连接池，无法监控到数据库连接情况 <b>
                <%} %>
    </div>
</div>
<!-- <tr> -->
<!-- <th>Max-Thread</th> -->
<%-- 		<td><%=FrontInterceptor.getMaxThread()%></td> --%>
<!-- <th>Current-Thread</th> -->
<%-- 		<td><%=FrontInterceptor.getCurThread()%></td> --%>
<!-- </tr> -->