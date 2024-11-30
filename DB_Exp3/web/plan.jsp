<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>我的健身计划</title>
</head>
<body>
<h2>我的健身计划</h2>

<!-- 查看计划按钮 -->
<form action="PlanServlet" method="get">
    <button type="submit">查看我的计划</button>
</form>

<!-- 生成计划表单 -->
<h3>生成新的计划</h3>
<form action="PlanServlet" method="post">
    <input type="hidden" name="action" value="generate">
    <label>计划名称:
        <input type="text" name="plan_name" placeholder="如：早晨锻炼" required>
    </label><br>
    <label>运动:
        <select name="exercise_name">
            <option value="Running">跑步</option>
            <option value="Strength">力量训练</option>
            <option value="Yoga">瑜伽</option>
            <option value="Swimming">游泳</option>
            <option value="Cycling">骑行</option>
        </select>
    </label><br>
    <label>时长:
        <input type="number" name="duration" placeholder="分钟" required>
    </label><br>
    <label>消耗热量:
        <input type="number" name="calories_burned" placeholder="卡路里" required>
    </label><br>
    <label>日期:
        <select name="days">
            <option value="Monday">Monday</option>
            <option value="Tuesday">Tuesday</option>
            <option value="Wednesday">Wednesday</option>
            <option value="Thursday">Thursday</option>
            <option value="Friday">Friday</option>
            <option value="Saturday">Saturday</option>
            <option value="Sunday">Sunday</option>
        </select>
    </label><br>
    <button type="submit">生成计划</button>
</form>

<!-- 动态加载计划 -->
<h3>本周训练计划</h3>
<%
    List<Map<String, Object>> plans = (List<Map<String, Object>>) request.getAttribute("plans");
    if (plans != null && !plans.isEmpty()) {
%>
<%
    for (Map<String, Object> plan : plans) {
%>
<div style="border: 1px solid #ccc; padding: 10px; margin-bottom: 10px;">
    <strong>计划名称:</strong> <%= plan.get("plan_name") %><br>
    <strong>日期:</strong> <%= plan.get("day_of_week") %><br>
    <strong>运动:</strong> <%= plan.get("exercise_name") %><br>
    <strong>时长:</strong> <%= plan.get("duration") %> 分钟<br>
    <strong>消耗热量:</strong> <%= plan.get("calories_burned") %> 卡路里<br>
    <form action="PlanServlet" method="post" style="display:inline;">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="plan_id" value="<%= plan.get("id") %>">
        <button type="submit">删除</button>
    </form>
    <form action="editPlan.jsp" method="get" style="display:inline;">
        <input type="hidden" name="plan_id" value="<%= plan.get("id") %>">
        <button type="submit">编辑</button>
    </form>
</div>
<% } %>
<% } else { %>
<p>暂无计划，请点击“查看我的计划”按钮加载。</p>
<% } %>
</body>
</html>
