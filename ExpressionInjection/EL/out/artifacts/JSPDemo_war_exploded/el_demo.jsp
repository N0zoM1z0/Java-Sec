<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.reflect.Method" %>
<!DOCTYPE html>
<html>
<head>
    <title>EL 表达式 Demo</title>
</head>
<body>
<h1>EL 表达式示例</h1>

<h2>1. 基本值解析</h2>
<%
    request.setAttribute("name", "Alice");
    request.setAttribute("age", 25);
%>
<p>名字：${name}</p>
<p>年龄：${age}</p>

<h2>2. 运算符</h2>
<p>加法：${2 + 3}</p>
<p>逻辑与：${true && false}</p>
<p>比较：${10 > 5}</p>

<h2>3. 隐式对象</h2>
<p>Request URI: ${requestScope['javax.servlet.forward.request_uri']}</p>
<p>Session ID: ${session.id}</p>

<h2>4. 调用方法</h2>
<%
    // 在 request 中放置一个 Java 对象
    request.setAttribute("str", "Hello, EL!");
%>
<p>字符串长度：${str.length()}</p>
<p>转为大写：${str.toUpperCase()}</p>

<h2>5. 访问 Map 或 List</h2>
<%
    java.util.Map<String, String> map = new java.util.HashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    request.setAttribute("map", map);
%>
<p>Map 的 key1 值：${map['key1']}</p>

<%
    java.util.List<String> list = java.util.Arrays.asList("A", "B", "C");
    request.setAttribute("list", list);
%>
<p>List 的第一个元素：${list[0]}</p>
<h2>6. 调用 Java 方法</h2>
<p>计算平方根（16）： ${Math.sqrt(16)}</p>
<%
    java.util.Date currentDate = new java.util.Date();
    request.setAttribute("currentDate",currentDate);
%>
<p>当前时间： ${currentDate}</p>
<h2>7. 反射调用 Java 方法</h2>
<%
    Class<?> c = Math.class;
    Method method = c.getDeclaredMethod("sqrt", double.class);
    method.setAccessible(true);
    double res = (double) method.invoke(null,41);
    request.setAttribute("res",res);
%>
<p>计算平方根（41）： ${res}</p>
</body>
</html>
