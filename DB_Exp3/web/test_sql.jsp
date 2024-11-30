<%@ page import="java.sql.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>SQL Query Test</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1 {
            color: #4CAF50;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
<h1>SQL Query Test</h1>
<%
    // Database connection parameters
    String CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    String URL = "jdbc:mysql://localhost:3306/mysql"; // Replace with your database name
    String USER = "root";
    String PASSWORD = "root";

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    // User input (simulated for test)
    String userInput = "root' OR 1=1 #"; // Vulnerable input example

    try {
        // Load JDBC driver
        Class.forName(CLASS_NAME);

        // Establish connection
        connection = DriverManager.getConnection(URL, USER, PASSWORD);

        // Create statement
        statement = connection.createStatement();

        // Vulnerable SQL query
        String sql = "SELECT host, user FROM mysql.user WHERE user = '" + userInput + "'";
        out.println("<p><strong>SQL Query Executed:</strong> " + sql + "</p>");

        // Execute query
        resultSet = statement.executeQuery(sql);

        // Display results in a table
        out.println("<table>");
        out.println("<tr><th>Host</th><th>User</th></tr>");
        while (resultSet.next()) {
            String host = resultSet.getString("host");
            String user = resultSet.getString("user");
            out.println("<tr><td>" + host + "</td><td>" + user + "</td></tr>");
        }
        out.println("</table>");
    } catch (Exception e) {
        out.println("<p style='color: red;'>Error occurred while executing SQL query: " + e.getMessage() + "</p>");
    } finally {
        // Clean up resources
        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
        if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
    }
%>
</body>
</html>
