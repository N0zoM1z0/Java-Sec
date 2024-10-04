<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream" %><%--
  Created by IntelliJ IDEA.
  User: n0zom1z0
  Date: 2024/10/4
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Arbitrary File Read</title>
</head>
<body>

</body>
</html>
<pre>
<%
    /*
       Arbitrary File Read Vulnerability
     */
    File file = new File(request.getRealPath("/") + request.getParameter("filename"));
    FileInputStream fis = new FileInputStream(file);
    int bt;
    while ((bt = fis.read()) !=-1 ){
        out.write(bt);
    }
    fis.close();
%>
</pre>