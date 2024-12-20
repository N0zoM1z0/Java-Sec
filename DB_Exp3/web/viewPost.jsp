<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>查看动态</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f4f4;
      margin: 0;
      padding: 0;
    }
    .container {
      width: 80%;
      max-width: 900px;
      margin: 40px auto;
      background-color: white;
      padding: 20px;
      border-radius: 8px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }
    h2, h3 {
      color: #333;
    }
    .post-content {
      background-color: #fff;
      padding: 15px;
      margin-bottom: 20px;
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    }
    .post-content p {
      color: #555;
    }
    .comment {
      background-color: #f9f9f9;
      padding: 10px;
      margin-bottom: 10px;
      border-radius: 8px;
    }
    .comment em {
      color: #888;
      font-style: italic;
    }
    .comment strong {
      color: #007bff;
    }
    textarea {
      width: 100%;
      padding: 10px;
      margin-top: 10px;
      border: 1px solid #ddd;
      border-radius: 5px;
      resize: vertical;
    }
    button[type="submit"] {
      background-color: #28a745;
      color: white;
      border: none;
      padding: 10px 20px;
      border-radius: 5px;
      cursor: pointer;
      margin-top: 10px;
    }
    button[type="submit"]:hover {
      background-color: #218838;
    }
    .back-link {
      display: block;
      text-align: center;
      margin-top: 20px;
    }
    a {
      color: #007bff;
      text-decoration: none;
    }
    a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>

<div class="container">
  <h2>动态详情</h2>

  <%
    Map<String, Object> post = (Map<String, Object>) request.getAttribute("post");
    List<Map<String, Object>> comments = (List<Map<String, Object>>) request.getAttribute("comments");
  %>

  <div class="post-content">
    <strong>发布者:</strong> <%= post.get("username") %><br>
    <strong>发布时间:</strong> <%= post.get("created_at") %><br>
    <p><%= post.get("content") %></p>
    <%-- 如果有图片链接，可以解开下面的注释 --%>
    <%-- <img src="<%= post.get("image_url") %>" alt="动态图片" width="300" style="margin-top: 10px;"> --%>
  </div>

  <hr>

  <h3>评论</h3>

  <%
    if (comments != null && !comments.isEmpty()) {
      for (Map<String, Object> comment : comments) {
  %>
  <div class="comment">
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
    <label>评论内容:</label>
    <textarea name="content" required></textarea><br>
    <button type="submit">发表评论</button>
  </form>

  <div class="back-link">
    <a href="SocialServlet?action=loadPosts">返回动态列表</a>
  </div>
</div>

</body>
</html>
