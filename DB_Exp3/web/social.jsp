<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>社交互动</title>
    <link rel="stylesheet" href="css/styles.css">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f7fb; /* 浅灰色背景，给人清新感 */
            margin: 0;
            padding: 0;
            color: #333;
        }

        h2 {
            text-align: center;
            margin-top: 30px;
            color: #333;
        }

        h3 {
            margin-top: 30px;
            color: #007bff; /* 蓝色标题 */
        }

        .container {
            width: 100%;
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        label {
            font-size: 16px;
            color: #555;
        }

        input[type="text"], textarea {
            padding: 12px;
            font-size: 16px;
            border: 1px solid #ddd;
            border-radius: 5px;
            width: 100%;
            box-sizing: border-box;
        }

        textarea {
            resize: vertical;
            height: 100px;
        }

        button {
            padding: 12px;
            font-size: 16px;
            background-color: #007bff; /* 按钮背景蓝色 */
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #0056b3; /* 按钮悬停时的深蓝色 */
        }

        .post {
            background-color: #fff;
            border: 1px solid #ddd;
            padding: 20px;
            margin-bottom: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .post-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .post-header strong {
            font-size: 18px;
            color: #333;
        }

        .post-header em {
            font-size: 14px;
            color: #777;
        }

        .post-content {
            margin-top: 10px;
            font-size: 16px;
            color: #555;
        }

        .post-actions {
            margin-top: 15px;
        }

        .post-actions form {
            margin-bottom: 10px;
        }

        .post-image {
            max-width: 100%;
            height: auto;
            margin-top: 10px;
        }

        .no-data {
            color: #888;
            font-size: 16px;
            text-align: center;
            margin-top: 20px;
        }

        .btn-container {
            text-align: center;
            margin-top: 20px;
        }

        /* 点赞按钮样式 */
        .like-button {
            background-color: #28a745; /* 绿色 */
            color: white;
        }

        .like-button:hover {
            background-color: #218838; /* 深绿色 */
        }

        /* 评论按钮样式 */
        .comment-button {
            background-color: #007bff; /* 蓝色 */
            color: white;
        }

        .comment-button:hover {
            background-color: #0056b3; /* 深蓝色 */
        }

        /* 查看动态按钮样式 */
        .view-button {
            background-color: #6c757d; /* 灰色 */
            color: white;
        }

        .view-button:hover {
            background-color: #5a6268; /* 深灰色 */
        }

        /* 评论框和按钮样式 */
        .post-actions form input[type="text"] {
            width: 80%; /* 评论框宽度 */
            padding: 8px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .post-actions form button {
            width: 15%;
            margin-left: 5%;
            font-size: 14px;
        }

        /* 返回控制面板按钮 */
        .back-button {
            background-color: #6c757d; /* 灰色背景 */
            color: white;
            padding: 10px 20px;
            font-size: 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-align: center;
            display: inline-block;
            margin-top: 20px;
            text-decoration: none;
        }

        .back-button:hover {
            background-color: #5a6268; /* 深灰色 */
        }
    </style>
</head>
<body>

<div class="container">
    <h2>社交互动</h2>

    <!-- 发布动态 -->
    <h3>发布动态</h3>
    <form action="SocialServlet" method="post">
        <input type="hidden" name="action" value="post">

        <label for="content">动态内容:</label>
        <textarea name="content" id="content" required></textarea>

        <label for="image_url">图片 URL:</label>
        <input type="text" name="image_url" id="image_url">

        <button type="submit">发布动态</button>
    </form>

    <hr>

    <!-- 按钮：加载动态 -->
    <form action="SocialServlet" method="get" style="text-align:center;">
        <input type="hidden" name="action" value="loadPosts">
        <button type="submit">加载动态列表</button>
    </form>

    <h3>动态列表</h3>
    <%
        List<Map<String, Object>> posts = (List<Map<String, Object>>) request.getAttribute("posts");
        if (posts != null && !posts.isEmpty()) {
            for (Map<String, Object> post : posts) {
    %>

    <div class="post">
        <div class="post-header">
            <strong><%= post.get("username") %></strong>
            <em><%= post.get("created_at") %></em>
        </div>
        <div class="post-content">
            <p><%= post.get("content") %></p>
            <%-- 显示图片，如果有的话 --%>
            <% if (post.get("image_url") != null) { %>
            <img src="<%= post.get("image_url") %>" alt="动态图片" class="post-image">
            <% } %>
        </div>

        <!-- 点赞按钮 -->
        <div class="post-actions">
            <form action="SocialServlet" method="post">
                <input type="hidden" name="action" value="like">
                <input type="hidden" name="post_id" value="<%= post.get("post_id") %>">
                <button type="submit" class="like-button">点赞</button>
            </form>
        </div>

        <!-- 评论框和评论按钮 -->
        <div class="post-actions">
            <form action="SocialServlet" method="post" style="display: flex; align-items: center;">
                <input type="hidden" name="action" value="comment">
                <input type="hidden" name="post_id" value="<%= post.get("post_id") %>">
                <input type="text" name="content" placeholder="添加评论">
                <button type="submit" class="comment-button">评论</button>
            </form>
        </div>

        <!-- 查看动态按钮 -->
        <div class="post-actions">
            <form action="SocialServlet" method="get">
                <input type="hidden" name="action" value="viewPost">
                <input type="hidden" name="post_id" value="<%= post.get("post_id") %>">
                <button type="submit" class="view-button">查看动态</button>
            </form>
        </div>
    </div>
    <hr>
    <%
        }
    } else {
    %>
    <p class="no-data">暂无动态。</p>
    <%
        }
    %>

    <!-- 返回控制面板按钮 -->
    <div class="btn-container">
        <a href="dashboard.jsp" class="back-button">返回控制面板</a>
    </div>
</div> <!-- .container -->

</body>
</html>
