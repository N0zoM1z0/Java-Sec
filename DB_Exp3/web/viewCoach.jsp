<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>我的教练</title>
</head>
<body>
<h2>我的教练</h2>

<%
    String coachName = (String) session.getAttribute("coachName");
    Integer coachId = (Integer) session.getAttribute("coachId");

    if (coachName == null || coachId == null) {
%>
<p>您尚未选择教练。</p>
<a href="dashboard.jsp">返回控制面板</a>
<%
} else {
%>
<p>教练姓名: <%= coachName %></p>
<p>教练编号: <%= coachId %></p>

<h3>评价教练</h3>
<form action="UserCoachServlet" method="post">
    <input type="hidden" name="action" value="rateCoach">
    <input type="hidden" name="coach_id" value="<%= coachId %>">
    <label>评分 (1-5):
        <input type="number" name="rating" min="1" max="5" required>
    </label><br>
    <label>评论:
        <textarea name="comment" required></textarea>
    </label><br>
    <button type="submit">提交评价</button>
</form>
<a href="dashboard.jsp">返回控制面板</a>
<%
    }
%>
</body>
</html>
