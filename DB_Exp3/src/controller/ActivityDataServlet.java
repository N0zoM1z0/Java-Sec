package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

//@WebServlet("/ActivityDataServlet")
public class ActivityDataServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FitnessManagement";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                addActivityData(request, response);
                break;
            case "update":
                updateActivityData(request, response);
                break;
            case "delete":
                deleteActivityData(request, response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        if("edit".equals(action)) {
            loadActivityForEdit(request, response);
        }
        else if("trends".equals(action)) {
            loadActivityTrends(request, response);
//            System.out.println("loading activity trends...");
        }
        else{
            loadActivityData(request, response);
        }
    }

    private void addActivityData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("user_id");
        String activityDate = request.getParameter("activity_date");
        int steps = Integer.parseInt(request.getParameter("steps"));
        float caloriesBurned = Float.parseFloat(request.getParameter("calories_burned"));
        float distance = Float.parseFloat(request.getParameter("distance"));
        int activeMinutes = Integer.parseInt(request.getParameter("active_minutes"));

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO ActivityData (user_id, activity_date, steps, calories_burned, distance, active_minutes) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, activityDate);
            stmt.setInt(3, steps);
            stmt.setFloat(4, caloriesBurned);
            stmt.setFloat(5, distance);
            stmt.setInt(6, activeMinutes);
            stmt.executeUpdate();

            response.sendRedirect("ActivityDataServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("添加活动数据失败：" + e.getMessage());
        }
    }

    private void loadActivityData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("user_id");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM ActivityData WHERE user_id = ? ORDER BY activity_date DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            List<Map<String, Object>> activityDataList = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> activity = new HashMap<>();
                activity.put("id", rs.getInt("id"));
                activity.put("activity_date", rs.getDate("activity_date"));
                activity.put("steps", rs.getInt("steps"));
                activity.put("calories_burned", rs.getFloat("calories_burned"));
                activity.put("distance", rs.getFloat("distance"));
                activity.put("active_minutes", rs.getInt("active_minutes"));
                activityDataList.add(activity);
            }

            request.setAttribute("activityDataList", activityDataList);
            request.getRequestDispatcher("activityData.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("加载活动数据失败：" + e.getMessage());
        }
    }

    private void updateActivityData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int activityId = Integer.parseInt(request.getParameter("activity_id"));
        String activityDate = request.getParameter("activity_date");
        int steps = Integer.parseInt(request.getParameter("steps"));
        float caloriesBurned = Float.parseFloat(request.getParameter("calories_burned"));
        float distance = Float.parseFloat(request.getParameter("distance"));
        int activeMinutes = Integer.parseInt(request.getParameter("active_minutes"));

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE ActivityData SET activity_date = ?, steps = ?, calories_burned = ?, distance = ?, active_minutes = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, activityDate);
            stmt.setInt(2, steps);
            stmt.setFloat(3, caloriesBurned);
            stmt.setFloat(4, distance);
            stmt.setInt(5, activeMinutes);
            stmt.setInt(6, activityId);
            stmt.executeUpdate();

            response.sendRedirect("ActivityDataServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("更新活动数据失败：" + e.getMessage());
        }
    }

    private void deleteActivityData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int activityId = Integer.parseInt(request.getParameter("activity_id"));

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "DELETE FROM ActivityData WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, activityId);
            stmt.executeUpdate();

            response.sendRedirect("ActivityDataServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("删除活动数据失败：" + e.getMessage());
        }
    }

    private void loadActivityForEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int activityId = Integer.parseInt(request.getParameter("activity_id"));
        int userId = (int) session.getAttribute("user_id");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM ActivityData WHERE id = ? AND user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, activityId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Map<String, Object> activity = new HashMap<>();
                activity.put("id", rs.getInt("id"));
                activity.put("activity_date", rs.getDate("activity_date"));
                activity.put("steps", rs.getInt("steps"));
                activity.put("calories_burned", rs.getFloat("calories_burned"));
                activity.put("distance", rs.getFloat("distance"));
                activity.put("active_minutes", rs.getInt("active_minutes"));

                request.setAttribute("activity", activity);
                request.getRequestDispatcher("editActivityData.jsp").forward(request, response);
            } else {
                response.getWriter().println("无法加载活动数据，请返回重试。");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("加载活动数据失败：" + e.getMessage());
        }
    }

    private void loadActivityTrends(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("user_id");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT activity_date, SUM(steps) AS total_steps, SUM(calories_burned) AS total_calories, SUM(distance) AS total_distance " +
                    "FROM ActivityData WHERE user_id = ? GROUP BY activity_date ORDER BY activity_date ASC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            // 手动构建 JSON 字符串，避免编码问题
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");
            while (rs.next()) {
                if (jsonBuilder.length() > 1) jsonBuilder.append(",");
                jsonBuilder.append("{")
                        .append("\"date\":\"").append(rs.getDate("activity_date")).append("\",")
                        .append("\"steps\":").append(rs.getInt("total_steps")).append(",")
                        .append("\"calories\":").append(rs.getFloat("total_calories")).append(",")
                        .append("\"distance\":").append(rs.getFloat("total_distance"))
                        .append("}");
            }
            jsonBuilder.append("]");

            // 确保使用 UTF-8 编码
            request.setAttribute("trendDataJson", jsonBuilder.toString());
            request.getRequestDispatcher("activityTrends.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("加载趋势数据失败：" + e.getMessage());
        }
    }


}
