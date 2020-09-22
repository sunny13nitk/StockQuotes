<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Reports</title>
</head>
<body>

		<h2>Reports</h2>


		<table>

				<tr>
						<td><form:form
										action="${pageContext.request.contextPath}/logout"
										method="POST">

										<input type="submit" value="Logout">

								</form:form>
						<td>
						<td><input type="button" value="Home"
								onclick="window.location.href = '${pageContext.request.contextPath}/'; return false;">
						<td>
				</tr>

		</table>
</body>
</html>