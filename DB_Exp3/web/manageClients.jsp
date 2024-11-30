<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理我的学员</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<h2>管理我的学员</h2>

<%
    List<Map<String, Object>> clients = (List<Map<String, Object>>) request.getAttribute("clients");
    if (clients != null && !clients.isEmpty()) {
%>
<table border="1">
    <thead>
    <tr>
        <th>学员 ID</th>
        <th>姓名</th>
        <th>邮箱</th>
    </tr>
    </thead>
    <tbody>
    <% for (Map<String, Object> client : clients) { %>
    <tr>
        <td><%= client.get("user_id") %></td>
        <td><%= client.get("username") %></td>
        <td><%= client.get("email") %></td>
    </tr>
    <% } %>
    </tbody>
</table>
<%
} else {
%>
<p>当前没有绑定的学员。</p>
<%
    }
%>
<a href="dashboard.jsp">返回</a>
</body>
</html>
