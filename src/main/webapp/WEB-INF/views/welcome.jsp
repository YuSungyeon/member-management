<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Welcome</title></head>
<body>
<h2>Welcome, ${sessionScope.user.username}!</h2>
<a href="/logout">Logout</a>
</body>
</html>
