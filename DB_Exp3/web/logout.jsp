<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>退出登录</title>
</head>
<body>
<%
    // 销毁会话
    session = request.getSession(false);
    if (session != null) {
        session.invalidate();
    }

    // 重定向到登录页面
    response.sendRedirect("login.jsp");
%>
</body>
</html>
