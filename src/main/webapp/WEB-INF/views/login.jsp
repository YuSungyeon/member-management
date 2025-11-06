<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Register</title></head>
<body>
<h2>Register</h2>
<form method="post" action="/register">
    <input type="text" name="username" placeholder="Username" required/><br/>
    <input type="password" name="password" placeholder="Password" required/><br/>
    <input type="email" name="email" placeholder="Email" required/><br/>
    <button type="submit">Register</button>
</form>
<p style="color:red;">${error}</p>
<a href="/login">Back to login</a>
</body>
</html>
