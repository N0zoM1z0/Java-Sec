<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>管理我的学员</title>
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
    <h2>学员管理</h2>
</header>

<div class="container">

    <%
        // 获取学员数据
        List<Map<String, Object>> clients = (List<Map<String, Object>>) request.getAttribute("clients");
        if (clients != null && !clients.isEmpty()) {
    %>

    <!-- 学员数据表格 -->
    <table>
        <thead>
        <tr>
            <th>学员 ID</th>
            <th>姓名</th>
            <th>邮箱</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Map<String, Object> client : clients) {
                Integer userId = (Integer) client.get("user_id");
                String username = (String) client.get("username");
                String email = (String) client.get("email");
        %>
        <tr>
            <td><%= userId %></td>
            <td><%= username %></td>
            <td><%= email %></td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <%
    } else {
    %>
    <p class="no-data">当前没有绑定的学员。</p>
    <%
        }
    %>

    <!-- 返回控制面板链接 -->
    <div class="back-link">
        <a href="dashboard.jsp">返回控制面板</a>
    </div>

</div> <!-- .container -->

</body>
</html>
