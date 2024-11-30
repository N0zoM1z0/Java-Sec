<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>用户注册</title>
</head>
<body>
<h2>用户注册</h2>
<form action="UserServlet" method="post">
    <input type="hidden" name="action" value="register">
    <label>用户名: <input type="text" name="username" required></label><br>
    <label>邮箱: <input type="email" name="email" required></label><br>
    <label>密码: <input type="password" name="password" required></label><br>
    <label>身高（米）: <input type="number" step="0.01" name="height" placeholder="例如 1.75" required></label><br>
    <label>体重（公斤）: <input type="number" step="0.1" name="weight" placeholder="例如 70.5" required></label><br>
    <label>目标:
        <select name="target" required>
            <option value="减脂">减脂</option>
            <option value="增肌">增肌</option>
            <option value="维持">维持</option>
        </select>
    </label><br>
    <label>身份:
        <select name="role" required>
            <option value="USER">用户</option>
            <option value="COACH">教练</option>
        </select>
    </label><br>
    <button type="submit">注册</button>
</form>
</body>
</html>
