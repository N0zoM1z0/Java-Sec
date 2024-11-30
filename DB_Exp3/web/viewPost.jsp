<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>查看动态</title>
</head>
<body>
<h2>动态详情</h2>

<%
  Map<String, Object> post = (Map<String, Object>) request.getAttribute("post");
  List<Map<String, Object>> comments = (List<Map<String, Object>>) request.getAttribute("comments");
%>
<div>
  <strong>发布者:</strong> <%= post.get("username") %><br>
  <strong>发布时间:</strong> <%= post.get("created_at") %><br>
  <p><%= post.get("content") %></p>
<%--  <% if (post.get("image_url") != null) { %>--%>
<%--  <div style="overflow: visible; display: block;">--%>
<%--    <img src="<%= post.get("image_url") %>" alt="动态图片" width="200" height="150">--%>
<%--  </div>--%>

<%--  <% } %>--%>
</div>

<hr>

<h3>评论</h3>
<%
  if (comments != null && !comments.isEmpty()) {
    for (Map<String, Object> comment : comments) {
%>
<div>
  <strong><%= comment.get("username") %>:</strong> <%= comment.get("content") %><br>
  <em><%= comment.get("created_at") %></em>
</div>
<hr>
<%
  }
} else {
%>
<p>暂无评论。</p>
<%
  }
%>

<!-- 添加评论 -->
<h3>添加评论</h3>
<form action="SocialServlet" method="post">
  <input type="hidden" name="action" value="comment">
  <input type="hidden" name="post_id" value="<%= post.get("post_id") %>">
  <label>评论内容:
    <textarea name="content" required></textarea>
  </label><br>
  <button type="submit">发表评论</button>
</form>

<!-- 返回按钮 -->
<a href="SocialServlet?action=loadPosts">返回动态列表</a>
</body>
</html>
