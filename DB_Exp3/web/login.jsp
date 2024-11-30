<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户登录</title>
    <link rel="stylesheet" href="css/styles.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7fc;
            margin: 0;
            padding: 20px;
        }

        h2 {
            text-align: center;
            color: #333;
            font-size: 28px;
            margin-bottom: 30px;
        }

        .login-form {
            max-width: 400px;
            margin: 0 auto;
            padding: 40px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            border: 1px solid #ddd;
        }

        .login-form label {
            font-size: 16px;
            color: #333;
            margin-bottom: 10px;
            display: block;
        }

        .login-form input {
            width: 100%;
            padding: 12px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
            background-color: #f9f9f9;
        }

        .login-form input:focus {
            outline: none;
            border-color: #007bff;
            background-color: #fff;
        }

        .login-form button {
            width: 100%;
            padding: 12px;
            background-color: #007bff;
            color: white;
            font-size: 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .login-form button:hover {
            background-color: #0056b3;
        }

        .register-link {
            text-align: center;
            margin-top: 20px;
        }

        .register-link a {
            text-decoration: none;
            color: #007bff;
            font-weight: bold;
        }

        .register-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>

<body>
<h2>用户登录</h2>

<div class="login-form">
    <form action="UserServlet" method="post">
        <input type="hidden" name="action" value="login">

        <!-- 用户名 -->
        <label for="username">用户名:</label>
        <input type="text" name="username" id="username" required placeholder="请输入用户名">

        <!-- 密码 -->
        <label for="password">密码:</label>
        <input type="password" name="password" id="password" required placeholder="请输入密码">

        <!-- 登录按钮 -->
        <button type="submit">登录</button>
    </form>

    <!-- 注册链接 -->
    <div class="register-link">
        <p>还没有账号？ <a href="register.jsp">点击这里注册</a></p>
    </div>
</div>
</body>

</html>
