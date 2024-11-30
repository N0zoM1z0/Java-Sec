<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户控制面板</title>
    <link rel="stylesheet" href="css/styles.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #4CAF50;
            color: white;
            padding: 20px 0;
            text-align: center;
            font-size: 24px;
            font-weight: bold;
        }

        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        .info-section {
            margin-bottom: 30px;
        }

        .info-section h2 {
            font-size: 20px;
            color: #333;
            text-align: center;
            margin-bottom: 20px;
        }

        /* 美化表格 */
        .info-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .info-table th,
        .info-table td {
            padding: 10px;
            text-align: left;
            border: 1px solid #ddd;
        }

        .info-table th {
            background-color: #f4f4f4;
            font-weight: bold;
        }

        .info-table td {
            background-color: #f9f9f9;
        }

        .role-info {
            margin-top: 10px;
            font-size: 18px;
            color: #333;
            font-weight: bold;
            text-align: center;
        }

        .menu {
            display: grid;
            grid-template-columns: 1fr 1fr; /* 两列布局 */
            gap: 20px; /* 两列之间的间隔 */
            list-style: none;
            padding: 0;
            margin-top: 40px;
        }

        .menu li {
            text-align: center;
            background-color: #fff;
            border: 1px solid #ddd; /* 外围框 */
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            transition: box-shadow 0.3s ease;
        }

        .menu li:hover {
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2); /* 鼠标悬停时增加阴影 */
        }

        .menu a {
            text-decoration: none;
            color: #007bff;
            font-size: 16px;
            display: block;
        }

        .menu a:hover {
            text-decoration: underline;
        }

        .btn {
            display: inline-block;
            background-color: #f0f0f0;
            color: #333;
            padding: 12px 20px;
            border-radius: 5px;
            text-align: center;
            font-size: 16px;
            margin-top: 10px;
            border: 1px solid #ccc;
        }

        .btn:hover {
            background-color: #e0e0e0;
        }

        .back-link {
            margin-top: 20px;
            text-align: center;
        }

        .back-link a {
            color: #007bff;
            text-decoration: none;
        }

        .back-link a:hover {
            text-decoration: underline;
        }

        /* 居中对齐菜单标题 */
        .menu-section h2 {
            text-align: center;
            font-size: 20px;
            color: #333;
        }
    </style>
</head>

<body>
<%
    session = request.getSession(false);
    if (session == null || session.getAttribute("user_id") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String username = (String) session.getAttribute("username");
    String target = (String) session.getAttribute("target");
    float height = (float) session.getAttribute("height");
    float weight = (float) session.getAttribute("weight");
    float bmi = (float) (weight / (height * height));
    String role = (String) session.getAttribute("role"); // 获取用户角色

    // 动态问候语
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    String greeting = (hour < 12) ? "早上好" : (hour < 18) ? "下午好" : "晚上好";

    // 如果是用户，查询绑定的教练信息
    String coachName = null;
    Integer coachId = null;

    if ("user".equals(role)) {
        int userId = (int) session.getAttribute("user_id");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/FitnessManagement", "root", "root")) {
            String sql = "SELECT uc.coach_id, u.username AS coach_name " +
                    "FROM Users u " +
                    "JOIN UserCoach uc ON u.user_id = uc.coach_id " +
                    "WHERE uc.user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                coachId = rs.getInt("coach_id");
                coachName = rs.getString("coach_name");
                session.setAttribute("coachId", coachId); // 更新Session中的教练信息
                session.setAttribute("coachName", coachName);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
%>

<header>
    <h1><%= greeting %>，<%= username %>！欢迎回来！</h1>
</header>

<div class="container">
    <!-- 个人信息展示表格 -->
    <div class="info-section">
        <h2>个人信息</h2>
        <table class="info-table">
            <tr>
                <th>目标</th>
                <td><%= target %></td>
            </tr>
            <tr>
                <th>身高</th>
                <td><%= height %> 米</td>
            </tr>
            <tr>
                <th>体重</th>
                <td><%= weight %> 公斤</td>
            </tr>
            <tr>
                <th>BMI</th>
                <td><%= bmi %></td>
            </tr>
            <tr>
                <th>身份</th>
                <td><%= role.equals("coach") ? "教练" : "用户" %></td>
            </tr>
        </table>
    </div>

    <div class="menu-section">
        <h2>功能菜单</h2>
        <ul class="menu">
            <% if (role.equals("user")) { %>
            <!-- 用户专属功能（每列三个）-->
            <li><a href="plan.jsp">我的健身计划</a></li>
            <li><a href="activityData.jsp">我的健身数据</a></li>
            <li><a href="social.jsp">社交互动</a></li>

            <li><a href="UserCoachServlet?action=exportActivityData">导出我的健身数据为CSV</a></li>

            <!-- 我的教练功能 -->
            <% if (coachName != null) { %>
            <li><a href="viewCoach.jsp">我的教练: <%= coachName %></a></li>
            <% } else { %>
            <li>我的教练: 未选择</li>
            <% } %>
            <li><a href="UserCoachServlet?action=loadCoaches">选择教练</a></li>
            <% } else if (role.equals("coach")) { %>
            <!-- 教练专属功能 -->
            <li><a href="CoachServlet?action=manageClients">查看我的学员</a></li>
            <li><a href="CoachServlet?action=viewActivitySummary">查看学员健身数据</a></li>
            <li><a href="CoachServlet?action=exportActivitySummary">导出学员健身数据为CSV</a></li>

            <%
                Integer studentCount = (Integer) session.getAttribute("studentCount");
                if (studentCount != null) {
            %>
            <li>学员总数: <%= studentCount %></li>
            <% } %>
            <% } %>
        </ul>
    </div>

    <div class="back-link">
        <a href="logout.jsp">退出登录</a>
    </div>
</div>

</body>
</html>
