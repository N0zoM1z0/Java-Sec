package controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

//@WebServlet("/CoachServlet")
public class CoachServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FitnessManagement";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("manageClients".equals(action)) {
            manageClients(request, response);
        } else if ("viewActivitySummary".equals(action)) {
            viewActivitySummary(request, response);
        }else if ("exportActivitySummary".equals(action)) {
            exportActivitySummary(request, response);
        }
    }

    private void manageClients(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int coachId = (int) session.getAttribute("user_id");

        List<Map<String, Object>> clients = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT u.user_id, u.username, u.bmi, u.email " +
                    "FROM Users u " +
                    "JOIN UserCoach uc ON u.user_id = uc.user_id " +
                    "WHERE uc.coach_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, coachId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> client = new HashMap<>();
                client.put("user_id", rs.getInt("user_id"));
                client.put("username", rs.getString("username"));
                client.put("bmi", rs.getFloat("bmi"));
                client.put("email", rs.getString("email"));
                clients.add(client);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 将学员列表传递到 JSP 页面
        request.setAttribute("clients", clients);
        request.getRequestDispatcher("manageClients.jsp").forward(request, response);
    }

    private void viewActivitySummary(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int coachId = (int) session.getAttribute("user_id");

        List<Map<String, Object>> activitySummary = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT ad.user_id, u.username, ad.activity_date, ad.steps, ad.calories_burned, ad.active_minutes " +
                    "FROM ActivityData ad " +
                    "JOIN Users u ON ad.user_id = u.user_id " +
                    "JOIN UserCoach uc ON uc.user_id = u.user_id " +
                    "WHERE uc.coach_id = ? " +
                    "ORDER BY ad.activity_date DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, coachId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("user_id", rs.getInt("user_id"));
                record.put("username", rs.getString("username"));
                record.put("activity_date", rs.getDate("activity_date"));
                record.put("steps", rs.getInt("steps"));
                record.put("calories_burned", rs.getFloat("calories_burned"));
                record.put("active_minutes", rs.getInt("active_minutes"));
                activitySummary.add(record);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 将学员健身数据传递到 JSP 页面
        request.setAttribute("activitySummary", activitySummary);
        request.getRequestDispatcher("activitySummary.jsp").forward(request, response);
    }

    private void exportActivitySummary(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int coachId = (int) session.getAttribute("user_id");
        System.out.println("[DEBUG] Exporting activity data for coach_id: " + coachId); // Debugging

        List<Map<String, Object>> activityData = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT ad.user_id, u.username, ad.activity_date, ad.steps, ad.calories_burned, ad.active_minutes " +
                    "FROM ActivityData ad " +
                    "JOIN Users u ON ad.user_id = u.user_id " +
                    "JOIN UserCoach uc ON uc.user_id = u.user_id " +
                    "WHERE uc.coach_id = ? " +
                    "ORDER BY ad.activity_date DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, coachId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("user_id", rs.getInt("user_id"));
                record.put("username", rs.getString("username"));
                record.put("date", rs.getDate("activity_date"));
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

        // 设置响应的内容类型为CSV，并指定下载文件名
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"activity_data.csv\"");

        // 使用UTF-8编码，并添加BOM以确保Excel正确读取
        OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
        PrintWriter out = new PrintWriter(writer);

        // 向文件开头添加BOM标记（防止Excel出现乱码）
        out.write("\uFEFF");

        // 写入CSV头（表头已改为英文）
        out.println("Username,Date,Steps,Calories Burned,Active Minutes");
//        out.println("1,2,3,4,5");

        // 写入数据，日期格式化为yyyy-MM-dd
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String, Object> record : activityData) {
            String username = (String) record.get("username");
            Date date = (Date) record.get("date");
            int steps = (int) record.get("steps");
            float calories = (float) record.get("calories_burned");
            int activeMinutes = (int) record.get("active_minutes");

            // 格式化日期为 yyyy-MM-dd，确保 Excel 能够正确解析
            String formattedDate = (date != null) ? dateFormat.format(date) : "";

            // 输出每一行数据
            out.printf("%s,%s,%d,%.2f,%d\n", username, formattedDate, steps, calories, activeMinutes);
        }
        out.flush();
    }
}
