<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>CRM Application - Demo</title>
<link type="text/css" rel="stylesheet"
		href="${pageContext.request.contextPath}/resources/css/add-customer-style.css">
</head>
</head>
<body>
		<div id="container">
				<h3>Application Logon</h3>

				<form:form modelAttribute="user" action="logon" method="POST">

						<table>
								<tbody>
										<tr>
												<td><label>User Name: </label></td>
												<td><form:input path="userName" /></td>
										</tr>

										<tr>
												<td><label>Password: </label></td>
												<td><form:password path="password" /></td>
										</tr>

										<tr>
												<td><label></label></td>
												<td><input type="submit" value="Logon" class="save" /></td>
										</tr>
								</tbody>
						</table>

				</form:form>

				<div style="">
						<p>
								<a href="${pageContext.request.contextPath}/home/addUser">Register</a>
						</p>
				</div>
		</div>
</body>
</html>