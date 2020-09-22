<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
		uri="http://www.springframework.org/security/tags"%>

<html>

<head>
<title>Access Denied</title>
</head>

<body>
		<h2>Access to requested resource denied - Contact System Admin.</h2>
		<hr>
		<br>
		<br>


		<input type="button" value="Home"
				onclick="window.location.href = '${pageContext.request.contextPath}/'; return false;">
</body>


</html>