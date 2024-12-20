<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的教练</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 80%;
            max-width: 800px;
            margin: 40px auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
            color: #333;
        }
        p {
            font-size: 16px;
            color: #555;
        }
        .btn {
            display: inline-block;
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
            text-align: center;
            margin-top: 20px;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        form {
            margin-top: 20px;
            padding: 15px;
            background-color: #f4f4f4;
            border-radius: 8px;
        }
        label {
            display: block;
            font-weight: bold;
            margin-bottom: 8px;
        }
        input[type="number"], textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        textarea {
            height: 100px;
        }
        button[type="submit"] {
            background-color: #28a745;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
        }
        button[type="submit"]:hover {
            background-color: #218838;
        }
        .back-link {
            display: block;
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>我的教练</h2>

    <%
        String coachName = (String) session.getAttribute("coachName");
        Integer coachId = (Integer) session.getAttribute("coachId");

        if (coachName == null || coachId == null) {
    %>
    <p>您尚未选择教练。</p>
    <div class="back-link">
        <a href="dashboard.jsp" class="btn">返回控制面板</a>
    </div>
    <%
    } else {
    %>
    <p><strong>教练姓名:</strong> <%= coachName %></p>
    <p><strong>教练编号:</strong> <%= coachId %></p>

    <h3>评价教练</h3>
    <form action="UserCoachServlet" method="post">
        <input type="hidden" name="action" value="rateCoach">
        <input type="hidden" name="coach_id" value="<%= coachId %>">

        <label>评分 (1-5):</label>
        <input type="number" name="rating" min="1" max="5" required>

        <label>评论:</label>
        <textarea name="comment" required></textarea>

        <button type="submit">提交评价</button>
    </form>

    <div class="back-link">
        <a href="dashboard.jsp" class="btn">返回控制面板</a>
    </div>
    <%
        }
    %>
</div>

</body>
</html>
