package controller;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

//@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FitnessManagement";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");

        if ("register".equals(action)) {
            registerUser(request, response);
        } else if ("login".equals(action)) {
            loginUser(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String target = request.getParameter("target");
        float height = Float.parseFloat(request.getParameter("height"));
        float weight = Float.parseFloat(request.getParameter("weight"));
        String role = request.getParameter("role"); // 新增：角色
        float bmi = weight / (height * height);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO Users (username, email, password, target, height, weight, bmi, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, target);
            stmt.setFloat(5, height);
            stmt.setFloat(6, weight);
            stmt.setFloat(7, bmi); // 插入 BMI
            stmt.setString(8, role); // 插入角色
            stmt.executeUpdate();

            response.sendRedirect("login.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("注册失败：" + e.getMessage());
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("user_id", rs.getInt("user_id"));
                session.setAttribute("username", username);
                session.setAttribute("email", rs.getString("email"));
                session.setAttribute("password", rs.getString("password"));
                session.setAttribute("target", rs.getString("target"));
                session.setAttribute("height", rs.getFloat("height"));
                session.setAttribute("weight", rs.getFloat("weight"));
                session.setAttribute("bmi", rs.getFloat("bmi"));
                session.setAttribute("role", rs.getString("role")); // 新增：角色

                response.sendRedirect("dashboard.jsp");
            } else {
                response.getWriter().println("登录失败：用户名或密码错误");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("登录失败：" + e.getMessage());
        }
    }

}
