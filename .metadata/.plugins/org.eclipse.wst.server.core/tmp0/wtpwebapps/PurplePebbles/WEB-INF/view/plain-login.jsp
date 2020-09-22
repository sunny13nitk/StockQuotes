<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>User Login</title>

<style type="text/css">
.failed {
		color: red
}
</style>
</head>
<body>
		<form:form
				action="${pageContext.request.contextPath}/authenticateUser"
				method="POST">

				<!-- Check for Login Error -->
				<c:if test="${param.error != null}">
						<i class="failed">Sorry! You have Entered an Invalid
								Username/Password</i>

				</c:if>

				<p>
						User Name: <input type="text" name="username" />
				</p>


				<p>
						Password: <input type="password" name="password" />
				</p>

				<input type="submit" value="Login">
		</form:form>
</body>
</html>