<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>学员健身数据</title>
    <link rel="stylesheet" href="css/styles.css">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        header {
            background-color: #333;
            color: white;
            padding: 15px;
            text-align: center;
        }
        h2 {
            margin: 0;
        }
        .container {
            max-width: 1000px;
            margin: 30px auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
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
            padding: 12px;
            text-align: center;
        }
        th {
            background-color: #f8f8f8;
            font-weight: bold;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        a {
            text-decoration: none;
            color: #007bff;
        }
        a:hover {
            text-decoration: underline;
        }
        .btn {
            display: inline-block;
            background-color: #5cb85c;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            text-align: center;
            font-size: 16px;
            margin-top: 20px;
        }
        .btn:hover {
            background-color: #4cae4c;
        }
        .back-link {
            margin-top: 20px;
            display: block;
            text-align: center;
        }
        .no-data {
            color: #888;
            font-size: 16px;
            text-align: center;
            margin-top: 20px;
        }
        .debug-info {
            color: red;
            text-align: center;
        }
    </style>
</head>
<body>

<header>
    <h2>学员健身数据汇总</h2>
</header>

<div class="container">

    <%
        // 调试：检查是否从 Servlet 中接收到了数据
        Object rawData = request.getAttribute("activitySummary");
        if (rawData == null) {
            out.println("<p class='debug-info'>Debug: activitySummary is null.</p>");
        } else if (!(rawData instanceof List)) {
            out.println("<p class='debug-info'>Debug: activitySummary is not a List. Actual type: " + rawData.getClass().getName() + "</p>");
        } else {
            List<Map<String, Object>> activitySummary = (List<Map<String, Object>>) rawData;
            if (activitySummary.isEmpty()) {
                out.println("<p class='no-data'>暂无健身数据。</p>");
            } else {
    %>

    <!-- 数据表格 -->
    <table>
        <thead>
        <tr>
            <th>用户名</th>
            <th>日期</th>
            <th>步数</th>
            <th>消耗热量 (卡)</th>
            <th>活跃分钟数</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Map<String, Object> record : activitySummary) {
                // 提取数据并处理
                String username = (String) record.get("username");
                Date date = (Date) record.get("activity_date");
                int steps = (record.get("steps") != null) ? (int) record.get("steps") : 0;
                float calories = (record.get("calories_burned") != null) ? (float) record.get("calories_burned") : 0.0f;
                int activeMinutes = (record.get("active_minutes") != null) ? (int) record.get("active_minutes") : 0;
        %>
        <tr>
            <td><%= username %></td>
            <td><%= date != null ? date.toString() : "无数据" %></td>
            <td><%= steps %></td>
            <td><%= calories %></td>
            <td><%= activeMinutes %></td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <!-- 导出按钮 -->
    <div class="btn-container">
        <a href="CoachServlet?action=exportActivitySummary" class="btn">导出为CSV</a>
    </div>

    <%
            }
        }
    %>

    <!-- 返回控制面板链接 -->
    <div class="back-link">
        <a href="dashboard.jsp">返回控制面板</a>
    </div>

</div> <!-- .container -->

</body>
</html>
