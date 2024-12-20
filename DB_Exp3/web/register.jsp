<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户注册</title>
    <link rel="stylesheet" href="css/styles.css">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        h2 {
            text-align: center;
            margin-top: 50px;
            color: #333;
        }
        .container {
            width: 100%;
            max-width: 600px;  /* 增加容器的宽度 */
            margin: 0 auto;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
        form {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        label {
            font-size: 16px;
            color: #555;
        }
        input, select, button {
            padding: 12px;
            font-size: 16px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        input[type="text"], input[type="email"], input[type="password"], input[type="number"], select {
            width: 100%;
        }
        button {
            background-color: #007bff;
            color: white;
            cursor: pointer;
            border: none;
            font-weight: bold;
        }
        button:hover {
            background-color: #0056b3;
        }
        .back-link {
            text-align: center;
            margin-top: 20px;
        }
        .back-link a {
            text-decoration: none;
            color: #007bff;
        }
        .back-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>用户注册</h2>

    <form action="UserServlet" method="post">
        <input type="hidden" name="action" value="register">

        <label for="username">用户名:</label>
        <input type="text" name="username" id="username" required>

        <label for="email">邮箱:</label>
        <input type="email" name="email" id="email" required>

        <label for="password">密码:</label>
        <input type="password" name="password" id="password" required>

        <label for="height">身高（米）:</label>
        <input type="number" step="0.01" name="height" id="height" placeholder="例如 1.75" required>

        <label for="weight">体重（公斤）:</label>
        <input type="number" step="0.1" name="weight" id="weight" placeholder="例如 70.5" required>

        <label for="target">目标:</label>
        <select name="target" id="target" required>
            <option value="减脂">减脂</option>
            <option value="增肌">增肌</option>
            <option value="维持">维持</option>
        </select>

        <label for="role">身份:</label>
        <select name="role" id="role" required>
            <option value="USER">用户</option>
            <option value="COACH">教练</option>
        </select>

        <button type="submit">注册</button>
    </form>

    <div class="back-link">
        <p>已经有账号了？ <a href="login.jsp">点击这里登录</a></p>
    </div>
</div>

</body>
</html>
