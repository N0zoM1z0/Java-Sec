package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

//@WebServlet("/PlanServlet")
public class PlanServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FitnessManagement";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "generate":
                generatePlan(request, response);
                break;
            case "delete":
                deletePlan(request, response);
                break;
            case "edit":
                editPlan(request, response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        loadPlans(request, response);
    }

    private void generatePlan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("user_id");
        String planName = request.getParameter("plan_name");
        String exerciseName = request.getParameter("exercise_name");
        int duration = Integer.parseInt(request.getParameter("duration"));
        float caloriesBurned = Float.parseFloat(request.getParameter("calories_burned"));
        String dayOfWeek = request.getParameter("days");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO Plans (user_id, plan_name, exercise_name, duration, calories_burned, day_of_week) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, userId);
            stmt.setString(2, planName);  // 插入计划名称
            stmt.setString(3, exerciseName);
            stmt.setInt(4, duration);
            stmt.setFloat(5, caloriesBurned);
            stmt.setString(6, dayOfWeek);
            stmt.executeUpdate();

            response.sendRedirect("PlanServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("生成计划失败：" + e.getMessage());
        }
    }

    private void deletePlan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int planId = Integer.parseInt(request.getParameter("plan_id"));

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "DELETE FROM Plans WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, planId);
            stmt.executeUpdate();

            response.sendRedirect("PlanServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("删除计划失败：" + e.getMessage());
        }
    }

    private void editPlan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        int planId = Integer.parseInt(request.getParameter("plan_id"));
        String exerciseName = request.getParameter("exercise_name");
        int duration = Integer.parseInt(request.getParameter("duration"));
        float caloriesBurned = Float.parseFloat(request.getParameter("calories_burned"));
        String dayOfWeek = request.getParameter("day_of_week");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE Plans SET exercise_name = ?, duration = ?, calories_burned = ?, day_of_week = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, exerciseName);
            stmt.setInt(2, duration);
            stmt.setFloat(3, caloriesBurned);
            stmt.setString(4, dayOfWeek);
            stmt.setInt(5, planId);
            stmt.executeUpdate();

            response.sendRedirect("PlanServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("更新计划失败：" + e.getMessage());
        }
    }

    private void loadPlans(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("user_id");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM Plans WHERE user_id = ? ORDER BY day_of_week";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            List<Map<String, Object>> plans = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> plan = new HashMap<>();
                plan.put("id", rs.getInt("id")); // 数据库的自动递增 ID
                plan.put("plan_name", rs.getString("plan_name"));
                plan.put("day_of_week", rs.getString("day_of_week"));
                plan.put("exercise_name", rs.getString("exercise_name"));
                plan.put("duration", rs.getInt("duration"));
                plan.put("calories_burned", rs.getFloat("calories_burned"));
                plans.add(plan);
            }

            // 将计划数据放入请求属性，传递给 JSP
            request.setAttribute("plans", plans);
            request.getRequestDispatcher("plan.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("加载计划失败：" + e.getMessage());
        }
    }

}
