<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Login</title></head>
<body>
<h2>Login</h2>
<form method="post" action="/login">
    <input type="text" name="username" placeholder="Username" required/><br/>
    <input type="password" name="password" placeholder="Password" required/><br/>
    <button type="submit">Login</button>
</form>
<p style="color:red;">${error}</p>
<a href="/register">Register</a>
</body>
</html>
