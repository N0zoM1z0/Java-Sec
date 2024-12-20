<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的健身计划</title>
    <link rel="stylesheet" href="css/styles.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7fc;
            margin: 0;
            padding: 20px;
        }

        h2 {
            text-align: center;
            color: #333;
            font-size: 28px;
            margin-bottom: 20px;
        }

        h3 {
            color: #555;
            font-size: 24px;
            margin-bottom: 20px;
            text-align: center; /* 确保标题居中 */
        }

        .form-container {
            max-width: 600px;
            margin: 0 auto; /* 表单居中 */
        }

        form {
            padding: 20px;
            background-color: white;
            border: 1px solid #ddd;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        label {
            display: block;
            margin-bottom: 10px;
            font-size: 16px;
            color: #333;
        }

        input, select, button {
            width: 100%;
            padding: 12px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }

        input[type="number"] {
            -webkit-appearance: none;
            -moz-appearance: none;
        }

        button {
            background-color: #007BFF;
            color: white;
            font-size: 16px;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #0056b3;
        }

        .plan-card {
            border: 1px solid #ddd;
            padding: 20px;
            margin-bottom: 15px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
            display: flex;
            flex-direction: column;
            justify-content: space-between; /* 确保按钮在底部 */
        }

        .plan-card strong {
            font-size: 16px;
            color: #333;
        }

        /* 删除按钮 */
        .plan-card .delete-btn {
            background-color: #e03e3e; /* 红色 */
            color: white;
            padding: 8px 15px; /* 调整按钮大小 */
            border-radius: 5px;
            font-size: 14px;
            cursor: pointer;
            border: none; /* 去掉边框 */
            transition: background-color 0.3s;
            margin-right: 15px; /* 增加按钮间距 */
        }

        .plan-card .delete-btn:hover {
            background-color: #c63333; /* 暗红色 */
        }

        /* 编辑按钮 */
        .plan-card .edit-btn {
            background-color: #28a745; /* 绿色 */
            color: white;
            padding: 8px 15px; /* 调整按钮大小 */
            border-radius: 5px;
            font-size: 14px;
            cursor: pointer;
            border: none; /* 去掉边框 */
            transition: background-color 0.3s;
        }

        .plan-card .edit-btn:hover {
            background-color: #218838; /* 暗绿色 */
        }

        /* 按钮容器 */
        .button-container {
            display: flex;
            justify-content: space-between; /* 按钮分散排列 */
            margin-top: 15px; /* 按钮间距 */
        }

        .plans-container {
            max-width: 800px;
            margin: 0 auto;
        }

        .no-plans {
            text-align: center;
            font-size: 18px;
            color: #888;
            margin-top: 30px;
        }

        .back-link {
            display: inline-block;
            text-align: center;
            padding: 12px 20px;
            background-color: #007BFF;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-top: 30px;
            font-size: 16px;
        }

        .back-link:hover {
            background-color: #0056b3;
        }

        /* 小屏幕优化 */
        @media (max-width: 768px) {
            form {
                padding: 15px;
            }

            h2, h3 {
                font-size: 24px;
            }

            button {
                font-size: 14px;
            }

            .plan-card {
                padding: 15px;
            }
        }
    </style>
</head>

<body>
<h2>我的健身计划</h2>

<!-- 查看计划按钮 -->
<div class="form-container">
    <form action="PlanServlet" method="get">
        <button type="submit">查看我的计划</button>
    </form>
</div>

<!-- 生成计划表单 -->
<div class="form-container">
    <h3>生成新的计划</h3>
    <form action="PlanServlet" method="post">
        <input type="hidden" name="action" value="generate">
        <label>计划名称:</label>
        <input type="text" name="plan_name" placeholder="如：早晨锻炼" required>

        <label>运动:</label>
        <select name="exercise_name">
            <option value="Running">跑步</option>
            <option value="Strength">力量训练</option>
            <option value="Yoga">瑜伽</option>
            <option value="Swimming">游泳</option>
            <option value="Cycling">骑行</option>
        </select>

        <label>时长 (分钟):</label>
        <input type="number" name="duration" placeholder="分钟" required>

        <label>消耗热量 (卡路里):</label>
        <input type="number" name="calories_burned" placeholder="卡路里" required>

        <label>日期:</label>
        <select name="days">
            <option value="Monday">Monday</option>
            <option value="Tuesday">Tuesday</option>
            <option value="Wednesday">Wednesday</option>
            <option value="Thursday">Thursday</option>
            <option value="Friday">Friday</option>
            <option value="Saturday">Saturday</option>
            <option value="Sunday">Sunday</option>
        </select>

        <button type="submit">生成计划</button>
    </form>
</div>

<!-- 动态加载计划 -->
<h3>本周训练计划</h3>
<div class="plans-container">
    <%
        List<Map<String, Object>> plans = (List<Map<String, Object>>) request.getAttribute("plans");
        if (plans != null && !plans.isEmpty()) {
    %>
    <% for (Map<String, Object> plan : plans) { %>
    <div class="plan-card">
        <strong>计划名称:</strong> <%= plan.get("plan_name") %><br>
        <strong>日期:</strong> <%= plan.get("day_of_week") %><br>
        <strong>运动:</strong> <%= plan.get("exercise_name") %><br>
        <strong>时长:</strong> <%= plan.get("duration") %> 分钟<br>
        <strong>消耗热量:</strong> <%= plan.get("calories_burned") %> 卡路里<br>

        <!-- 按钮容器 -->
        <div class="button-container">
            <form action="PlanServlet" method="post" style="display:inline;">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="plan_id" value="<%= plan.get("id") %>">
                <button type="submit" class="delete-btn">删除</button>
            </form>
            <form action="editPlan.jsp" method="get" style="display:inline;">
                <input type="hidden" name="plan_id" value="<%= plan.get("id") %>">
                <button type="submit" class="edit-btn">编辑</button>
            </form>
        </div>
    </div>
    <% } %>
    <% } else { %>
    <p class="no-plans">暂无计划，请点击“查看我的计划”按钮加载。</p>
    <% } %>
</div>

<div style="text-align: center;">
    <a href="dashboard.jsp" class="back-link">返回控制面板</a>
</div>

</body>

</html>
