<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的健身数据</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        header {
            background-color: #333;
            color: #fff;
            padding: 15px 0;
            text-align: center;
        }
        h2 {
            margin-bottom: 20px;
        }
        h3 {
            margin-top: 30px;
            color: #333;
        }
        .container {
            max-width: 900px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: center;
        }
        th {
            background-color: #f8f8f8;
        }
        button {
            background-color: #5cb85c;
            color: white;
            border: none;
            padding: 8px 16px;
            font-size: 14px;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 10px;
        }
        button:hover {
            background-color: #4cae4c;
        }
        input[type="date"], input[type="number"] {
            padding: 8px;
            margin: 10px 0;
            width: 100%;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        form {
            margin: 20px 0;
        }
        .actions {
            display: flex;
            justify-content: space-around;
        }
        .actions form {
            margin: 0;
        }
        .no-data {
            color: #888;
            font-size: 16px;
            text-align: center;
            margin-top: 20px;
        }
        .btn-container {
            text-align: center;
            margin-top: 30px;
        }
    </style>
</head>
<body>

<header>
    <h2>我的健身数据</h2>
</header>

<div class="container">

    <!-- 查看健身数据按钮 -->
    <form action="ActivityDataServlet" method="get">
        <button type="submit">查看我的健身数据</button>
    </form>

    <!-- 添加健身数据表单 -->
    <h3>添加健身数据</h3>
    <form action="ActivityDataServlet" method="post">
        <input type="hidden" name="action" value="add">
        <label for="activity_date">日期:</label>
        <input type="date" id="activity_date" name="activity_date" required><br>

        <label for="steps">步数:</label>
        <input type="number" id="steps" name="steps" required><br>

        <label for="calories_burned">消耗热量:</label>
        <input type="number" id="calories_burned" step="0.1" name="calories_burned" required><br>

        <label for="distance">距离 (公里):</label>
        <input type="number" id="distance" step="0.1" name="distance" required><br>

        <label for="active_minutes">活跃分钟数:</label>
        <input type="number" id="active_minutes" name="active_minutes" required><br>

        <button type="submit">添加健身数据</button>
    </form>

    <!-- 显示健身数据 -->
    <h3>健身数据列表</h3>
    <%
        List<Map<String, Object>> activityDataList = (List<Map<String, Object>>) request.getAttribute("activityDataList");
        if (activityDataList != null && !activityDataList.isEmpty()) {
    %>
    <table>
        <tr>
            <th>日期</th>
            <th>步数</th>
            <th>消耗热量</th>
            <th>距离</th>
            <th>活跃分钟数</th>
            <th>操作</th>
        </tr>
        <%
            for (Map<String, Object> activity : activityDataList) {
        %>
        <tr>
            <td><%= activity.get("activity_date") %></td>
            <td><%= activity.get("steps") %></td>
            <td><%= activity.get("calories_burned") %> 卡</td>
            <td><%= activity.get("distance") %> 公里</td>
            <td><%= activity.get("active_minutes") %> 分钟</td>
            <td class="actions">
                <form action="ActivityDataServlet" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="activity_id" value="<%= activity.get("id") %>">
                    <button type="submit">删除</button>
                </form>
                <form action="ActivityDataServlet" method="get" style="display:inline;">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="activity_id" value="<%= activity.get("id") %>">
                    <button type="submit">编辑</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>

    <% } else { %>
    <p class="no-data">暂无健身数据，请添加。</p>
    <% } %>

    <div class="btn-container">
        <form action="ActivityDataServlet" method="get">
            <input type="hidden" name="action" value="trends">
            <button type="submit">查看趋势图表</button>
        </form>
    </div>
</div>

</body>
</html>
