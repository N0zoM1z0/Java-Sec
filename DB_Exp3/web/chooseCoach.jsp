<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>选择教练</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f9f9f9;
            text-align: center; /* 新增：使页面内容居中 */
        }

        h2 {
            text-align: center;
            color: #333;
            font-size: 24px;
            margin-bottom: 20px;
        }

        .coach-form {
            max-width: 600px;
            margin: 0 auto;
            padding: 30px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            text-align: left; /* 新增：使表单中的内容对齐左边 */
        }

        .coach-form label {
            display: block;
            font-size: 18px;
            margin-bottom: 10px;
            color: #333;
        }

        select, button {
            width: 100%;
            padding: 12px;
            margin: 10px 0;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
            background-color: #f9f9f9;
        }

        select:focus, button:focus {
            outline: none;
            border-color: #007bff;
            background-color: #f1f1f1;
        }

        button {
            background-color: #4CAF50;
            color: white;
            cursor: pointer;
            border: none;
        }

        button:hover {
            background-color: #45a049;
        }

        .no-coaches {
            text-align: center;
            color: #888;
            font-size: 18px;
            margin-top: 20px;
        }

        .back-link {
            display: inline-block;
            margin-top: 30px;
            padding: 12px 20px;
            background-color: #007BFF;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            text-align: center;
            font-size: 16px;
        }

        .back-link {
            display: inline-block;
            margin-top: 30px;
            padding: 12px 20px;
            background-color: #007BFF;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            text-align: center;
            font-size: 16px;
            /* 居中对齐 */
            width: auto;
        }
    </style>
</head>

<body>
<h2>选择教练</h2>

<%
    List<Map<String, Object>> coaches = (List<Map<String, Object>>) request.getAttribute("coaches");
    if (coaches != null && !coaches.isEmpty()) {
%>
<div class="coach-form">
    <form action="UserCoachServlet" method="post">
        <input type="hidden" name="action" value="chooseCoach">
        <label for="coach_id">选择教练:
            <select name="coach_id" id="coach_id" required>
                <% for (Map<String, Object> coach : coaches) { %>
                <option value="<%= coach.get("user_id") %>"><%= coach.get("username") %></option>
                <% } %>
            </select>
        </label><br>
        <button type="submit">绑定教练</button>
    </form>
</div>
<%
} else {
%>
<p class="no-coaches">当前暂无可选择的教练。</p>
<%
    }
%>

<a href="dashboard.jsp" class="back-link">返回控制面板</a>

</body>
</html>
