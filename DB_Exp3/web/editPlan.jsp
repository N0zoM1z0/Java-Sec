<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>调整健身计划</title>
    <link rel="stylesheet" href="styles.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 20px;
        }

        h2 {
            text-align: center;
            color: #333;
            font-size: 28px;
            margin-bottom: 20px;
        }

        form {
            max-width: 480px;
            margin: 0 auto;
            padding: 30px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            border: 1px solid #ddd;
        }

        label {
            font-size: 16px;
            color: #333;
            margin-bottom: 10px;
            display: block;
        }

        input, select, button {
            width: 100%;
            padding: 12px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
            background-color: #f9f9f9;
        }

        input:focus, select:focus, button:focus {
            outline: none;
            border-color: #4CAF50;
            background-color: #f1f1f1;
        }

        button {
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }

        button:hover {
            background-color: #45a049;
        }

        .back-container {
            text-align: center;
            margin-top: 30px;
        }

        .back-link {
            display: inline-block;
            text-align: center;
            padding: 12px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 16px;
        }

        .back-link:hover {
            background-color: #0056b3;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group input, .form-group select {
            font-size: 16px;
        }

    </style>
</head>

<body>
<h2>调整健身计划</h2>

<form action="PlanServlet" method="post">
    <input type="hidden" name="action" value="edit">
    <input type="hidden" name="plan_id" value="<%= request.getParameter("plan_id") %>">

    <!-- 运动名称 -->
    <div class="form-group">
        <label for="exercise_name">运动名称:</label>
        <select id="exercise_name" name="exercise_name" required>
            <option value="Running">跑步</option>
            <option value="Strength">力量训练</option>
            <option value="Yoga">瑜伽</option>
            <option value="Swimming">游泳</option>
            <option value="Cycling">骑行</option>
        </select>
    </div>

    <!-- 时长 -->
    <div class="form-group">
        <label for="duration">时长(分钟):</label>
        <input type="number" name="duration" id="duration" placeholder="例如：30" required>
    </div>

    <!-- 消耗热量 -->
    <div class="form-group">
        <label for="calories_burned">消耗热量 (卡):</label>
        <input type="number" step="0.1" name="calories_burned" id="calories_burned" placeholder="例如：250.0" required>
    </div>

    <!-- 日期 -->
    <div class="form-group">
        <label for="day_of_week">选择日期:</label>
        <select name="day_of_week" id="day_of_week" required>
            <option value="Monday">星期一</option>
            <option value="Tuesday">星期二</option>
            <option value="Wednesday">星期三</option>
            <option value="Thursday">星期四</option>
            <option value="Friday">星期五</option>
            <option value="Saturday">星期六</option>
            <option value="Sunday">星期天</option>
        </select>
    </div>

    <!-- 保存按钮 -->
    <button type="submit">保存更改</button>
</form>

<!-- 返回按钮 -->
<div class="back-container">
    <a href="ActivityDataServlet" class="back-link">返回活动数据列表</a>
</div>
</body>

</html>
