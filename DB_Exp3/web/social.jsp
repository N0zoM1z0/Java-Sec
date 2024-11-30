<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>社交互动</title>
</head>
<body>
<h2>社交互动</h2>

<!-- 发布动态 -->
<h3>发布动态</h3>
<form action="SocialServlet" method="post">
    <input type="hidden" name="action" value="post">
    <label>动态内容:
        <textarea name="content" required></textarea>
    </label><br>
    <label>图片 URL:
        <input type="text" name="image_url">
    </label><br>
    <button type="submit">发布动态</button>
</form>

<hr>

<!-- 动态列表 -->

<!-- 按钮：加载动态 -->
<form action="SocialServlet" method="get">
    <input type="hidden" name="action" value="loadPosts">
    <button type="submit">加载动态列表</button>
</form>

<h3>动态列表</h3>
<%
    List<Map<String, Object>> posts = (List<Map<String, Object>>) request.getAttribute("posts");
    if (posts != null && !posts.isEmpty()) {
        for (Map<String, Object> post : posts) {
%>
<div>
    <strong><%= post.get("username") %></strong> <em><%= post.get("created_at") %></em><br>
    <p><%= post.get("content") %></p>
<%--    <% if (post.get("image_url") != null) { %>--%>
<%--    <img src="<%= post.get("image_url") %>" alt="动态图片" width="200"><br>--%>
<%--    <% } %>--%>
    <p>点赞数: <%= post.get("like_count") %>, 评论数: <%= post.get("comment_count") %></p>
    <!-- 点赞按钮 -->
    <form action="SocialServlet" method="post" style="display:inline;">
        <input type="hidden" name="action" value="like">
        <input type="hidden" name="post_id" value="<%= post.get("post_id") %>">
        <button type="submit">点赞</button>
    </form>
    <!-- 评论表单 -->
    <form action="SocialServlet" method="post" style="display:inline;">
        <input type="hidden" name="action" value="comment">
        <input type="hidden" name="post_id" value="<%= post.get("post_id") %>">
        <input type="text" name="content" placeholder="添加评论">
        <button type="submit">评论</button>
    </form>
    <!-- 查看动态按钮 -->
    <form action="SocialServlet" method="get" style="display:inline;">
        <input type="hidden" name="action" value="viewPost">
        <input type="hidden" name="post_id" value="<%= post.get("post_id") %>">
        <button type="submit">查看动态</button>
    </form>
</div>
<hr>
<%
    }
} else {
%>
<p>暂无动态。</p>
<%
    }
%>
</body>
</html>
