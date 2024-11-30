package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

//@WebServlet("/UserCoachServlet")
public class UserCoachServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FitnessManagement";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("chooseCoach".equals(action)) {
            chooseCoach(request, response);
        } else if ("rateCoach".equals(action)) {
            rateCoach(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("viewClients".equals(action)) {
            viewClients(request, response);
        } else if ("loadCoaches".equals(action)) {
            loadCoaches(request, response);
        }else if ("exportActivityData".equals(action)) {
            exportActivityData(request, response);
        }
    }

    private void chooseCoach(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getSession().getAttribute("user_id");
        int coachId = Integer.parseInt(request.getParameter("coach_id"));

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // 检查是否已经绑定教练
            String checkSql = "SELECT COUNT(*) FROM UserCoach WHERE user_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            boolean alreadyBound = checkRs.getInt(1) > 0;

            // 如果已绑定教练，先删除原绑定记录
            if (alreadyBound) {
                String deleteSql = "DELETE FROM UserCoach WHERE user_id = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                deleteStmt.setInt(1, userId);
                deleteStmt.executeUpdate();
            }

            // 绑定新的教练
            String sql = "INSERT INTO UserCoach (user_id, coach_id) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, coachId);
            stmt.executeUpdate();

            response.sendRedirect("dashboard.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("绑定教练失败：" + e.getMessage());
        }
    }

    private void rateCoach(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getSession().getAttribute("user_id");
        int coachId = Integer.parseInt(request.getParameter("coach_id"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO CoachRatings (user_id, coach_id, rating, comment) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, coachId);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);
            stmt.executeUpdate();

            response.sendRedirect("dashboard.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("评分失败：" + e.getMessage());
        }
    }

    private void viewClients(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int coachId = (int) request.getSession().getAttribute("user_id");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT u.user_id, u.username, u.bmi FROM Users u JOIN UserCoach uc ON u.user_id = uc.user_id WHERE uc.coach_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, coachId);
            ResultSet rs = stmt.executeQuery();

            List<Map<String, Object>> myUsers = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("user_id", rs.getInt("user_id"));
                user.put("username", rs.getString("username"));
                user.put("bmi", rs.getFloat("bmi"));
                myUsers.add(user);
            }
            request.setAttribute("myUsers", myUsers);
            request.getRequestDispatcher("manageClients.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("加载学员列表失败：" + e.getMessage());
        }
    }

    private void loadCoaches(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT user_id, username FROM Users WHERE role = 'coach'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            List<Map<String, Object>> coaches = new ArrayList<>();

            System.out.println("教练列表加载中...");
            if (coaches.isEmpty()) {
                System.out.println("当前无可用教练");
            } else {
                for (Map<String, Object> coach : coaches) {
                    System.out.println("教练: " + coach.get("username"));
                }
            }

            while (rs.next()) {
                Map<String, Object> coach = new HashMap<>();
                coach.put("user_id", rs.getInt("user_id"));
                coach.put("username", rs.getString("username"));
                coaches.add(coach);
            }
            request.setAttribute("coaches", coaches);
            request.getRequestDispatcher("chooseCoach.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("加载教练失败：" + e.getMessage());
        }
    }

    private void exportActivityData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("user_id");
        System.out.println("[DEBUG] Exporting activity data for user_id: " + userId); // Debugging

        List<Map<String, Object>> activityData = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT activity_date, steps, calories_burned, active_minutes FROM ActivityData WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            // Debugging: Check if any data is returned
            if (!rs.isBeforeFirst()) {
                System.out.println("[DEBUG] No activity data found for user_id: " + userId);
            }

            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("activity_date", rs.getDate("activity_date"));
                record.put("steps", rs.getInt("steps"));
                record.put("calories_burned", rs.getFloat("calories_burned"));
                record.put("active_minutes", rs.getInt("active_minutes"));
                activityData.add(record);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Check if data was found before exporting
        if (activityData.isEmpty()) {
            System.out.println("[DEBUG] No data to export for user_id: " + userId);
        }

        // Set response content type and download filename
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"activity_data.csv\"");

        PrintWriter out = response.getWriter();

        // Write CSV header
        out.println("日期,步数,消耗热量(卡),活跃分钟数");

        // Write data rows
        for (Map<String, Object> record : activityData) {
            String date = record.get("activity_date").toString();
            int steps = (int) record.get("steps");
            float calories = (float) record.get("calories_burned");
            int activeMinutes = (int) record.get("active_minutes");

            out.printf("%s,%d,%.2f,%d\n", date, steps, calories, activeMinutes);
        }
        out.flush();
    }

}
