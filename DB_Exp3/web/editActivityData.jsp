<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑活动数据</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 20px;
        }

        h2 {
            color: #333;
            text-align: center;
            margin-bottom: 20px;
        }

        .form-container {
            max-width: 600px;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        label {
            display: block;
            font-size: 16px;
            margin-bottom: 10px;
            color: #333;
        }

        input[type="date"],
        input[type="number"] {
            width: 100%;
            padding: 12px;
            margin-bottom: 20px;
            font-size: 16px;
            border-radius: 5px;
            border: 1px solid #ccc;
            background-color: #f9f9f9;
        }

        input[type="number"]:focus,
        input[type="date"]:focus {
            border-color: #007bff;
            outline: none;
            background-color: #f1f1f1;
        }

        button {
            width: 100%;
            padding: 12px;
            font-size: 16px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        button:hover {
            background-color: #45a049;
        }

        .back-link {
            display: block;
            margin-top: 20px;
            padding: 12px 20px;
            background-color: #007BFF;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            text-align: center;
        }

        .back-link:hover {
            background-color: #0056b3;
        }

        .error-message {
            color: red;
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>

<body>

<h2>编辑活动数据</h2>

<%
    // 从请求中获取当前活动数据
    Map<String, Object> activity = (Map<String, Object>) request.getAttribute("activity");
    if (activity == null) {
%>
<p class="error-message">无法加载活动数据，请返回重试。</p>
<a href="ActivityDataServlet" class="back-link">返回活动数据列表</a>
<%
} else {
%>

<div class="form-container">
    <form action="ActivityDataServlet" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="activity_id" value="<%= activity.get("id") %>">

        <label for="activity_date">日期:</label>
        <input type="date" id="activity_date" name="activity_date" value="<%= activity.get("activity_date") %>" required>

        <label for="steps">步数:</label>
        <input type="number" id="steps" name="steps" value="<%= activity.get("steps") %>" required>

        <label for="calories_burned">消耗热量 (卡路里):</label>
        <input type="number" step="0.1" id="calories_burned" name="calories_burned" value="<%= activity.get("calories_burned") %>" required>

        <label for="distance">距离 (公里):</label>
        <input type="number" step="0.1" id="distance" name="distance" value="<%= activity.get("distance") %>" required>

        <label for="active_minutes">活跃分钟数:</label>
        <input type="number" id="active_minutes" name="active_minutes" value="<%= activity.get("active_minutes") %>" required>

        <button type="submit">保存修改</button>
    </form>

    <a href="ActivityDataServlet" class="back-link">取消并返回</a>
</div>

<%
    }
%>

</body>
</html>
