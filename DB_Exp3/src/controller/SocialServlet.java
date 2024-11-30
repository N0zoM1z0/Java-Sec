package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

//@WebServlet("/SocialServlet")
public class SocialServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FitnessManagement";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "post":
                createPost(request, response);
                break;
            case "like":
                likePost(request, response);
                break;
            case "comment":
                addComment(request, response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("viewPost".equals(action)){
            viewPost(request, response);
        }
        else if("loadPosts".equals(action)){
            loadPosts(request, response);
        }
        else{

        }
    }

    private void createPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("user_id");
        String content = request.getParameter("content");
        String imageUrl = request.getParameter("image_url"); // 图片 URL 可为空

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO SocialPosts (user_id, content, image_url, created_at) VALUES (?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, content);
            stmt.setString(3, imageUrl);
            stmt.executeUpdate();

            response.sendRedirect("social.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("发布动态失败：" + e.getMessage());
        }
    }

    private void loadPosts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Map<String, Object>> posts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT p.post_id, u.username, p.content, p.image_url, p.created_at, " +
                    "(SELECT COUNT(*) FROM PostLikes WHERE post_id = p.post_id) AS like_count, " +
                    "(SELECT COUNT(*) FROM PostComments WHERE post_id = p.post_id) AS comment_count " +
                    "FROM SocialPosts p JOIN Users u ON p.user_id = u.user_id ORDER BY p.created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> post = new HashMap<>();
                post.put("post_id", rs.getInt("post_id"));
                post.put("username", rs.getString("username"));
                post.put("content", rs.getString("content"));
                post.put("image_url", rs.getString("image_url"));
                post.put("created_at", rs.getTimestamp("created_at"));
                post.put("like_count", rs.getInt("like_count"));
                post.put("comment_count", rs.getInt("comment_count"));
                posts.add(post);
            }

            request.setAttribute("posts", posts);
            request.getRequestDispatcher("social.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("加载动态失败：" + e.getMessage());
        }
    }

    private void likePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("user_id");
        int postId = Integer.parseInt(request.getParameter("post_id"));

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO PostLikes (post_id, user_id, created_at) VALUES (?, ?, NOW()) " +
                    "ON DUPLICATE KEY UPDATE post_id = post_id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();

            response.sendRedirect("social.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("点赞失败：" + e.getMessage());
        }
    }

    private void addComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("user_id");
        int postId = Integer.parseInt(request.getParameter("post_id"));
        String content = request.getParameter("content");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO PostComments (post_id, user_id, content, created_at) VALUES (?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.setString(3, content);
            stmt.executeUpdate();

            response.sendRedirect("social.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("评论失败：" + e.getMessage());
        }
    }

    private void viewPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int postId = Integer.parseInt(request.getParameter("post_id"));

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // 获取动态详情
            String postSql = "SELECT p.post_id, u.username, p.content, p.image_url, p.created_at " +
                    "FROM SocialPosts p JOIN Users u ON p.user_id = u.user_id WHERE p.post_id = ?";
            PreparedStatement postStmt = conn.prepareStatement(postSql);
            postStmt.setInt(1, postId);
            ResultSet postRs = postStmt.executeQuery();

            Map<String, Object> post = null;
            if (postRs.next()) {
                post = new HashMap<>();
                post.put("post_id", postRs.getInt("post_id"));
                post.put("username", postRs.getString("username"));
                post.put("content", postRs.getString("content"));
                post.put("image_url", postRs.getString("image_url"));
                post.put("created_at", postRs.getTimestamp("created_at"));
            }

            // 获取动态的评论
            String commentSql = "SELECT c.comment_id, u.username, c.content, c.created_at " +
                    "FROM PostComments c JOIN Users u ON c.user_id = u.user_id WHERE c.post_id = ? ORDER BY c.created_at ASC";
            PreparedStatement commentStmt = conn.prepareStatement(commentSql);
            commentStmt.setInt(1, postId);
            ResultSet commentRs = commentStmt.executeQuery();

            List<Map<String, Object>> comments = new ArrayList<>();
            while (commentRs.next()) {
                Map<String, Object> comment = new HashMap<>();
                comment.put("comment_id", commentRs.getInt("comment_id"));
                comment.put("username", commentRs.getString("username"));
                comment.put("content", commentRs.getString("content"));
                comment.put("created_at", commentRs.getTimestamp("created_at"));
                comments.add(comment);
            }

            // 将动态和评论传递到 JSP
            request.setAttribute("post", post);
            request.setAttribute("comments", comments);
            request.getRequestDispatcher("viewPost.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("加载动态详情失败：" + e.getMessage());
        }
    }


}
