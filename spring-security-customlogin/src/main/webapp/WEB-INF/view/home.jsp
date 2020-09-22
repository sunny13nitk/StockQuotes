<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
		uri="http://www.springframework.org/security/tags"%>

<html>

<head>
<title>Home Page</title>
</head>

<body>
		<h2>Home Page</h2>
		<hr>

		<!-- Display User Name and Role(s) -->
		<p>

				<security:authorize access="isAuthenticated()">
					User:<security:authentication property="principal.username" />
						<br>
						<br>
					Role(s):<security:authentication property="principal.authorities" />
				</security:authorize>

		</p>

		<hr>
		<div style="padding: 50px">

				<a href="${pageContext.request.contextPath}/fl/main">Fund
						Line(s) </a> <br> <br> <a
						href="${pageContext.request.contextPath}/pf/main">Portfolio
						Management</a> <br> <br>

				<security:authorize access="hasRole('ADMIN')">
						<a href="${pageContext.request.contextPath}/scrips/main">Scrips</a>
				</security:authorize>

				<br> <br>

				<security:authorize access="hasRole('MANAGER')">
						<a href="${pageContext.request.contextPath}/reports/main">Reports</a>
				</security:authorize>
		</div>
		<br>
		<br>
		<br>



		<form:form action="${pageContext.request.contextPath}/logout"
				method="POST">

				<input type="submit" value="Logout">

		</form:form>
</body>


</html>