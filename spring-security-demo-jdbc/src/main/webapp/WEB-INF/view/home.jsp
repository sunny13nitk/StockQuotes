<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<html>

<head>
<title>luv2code Company Home Page</title>
</head>

<body>
	<h2>luv2code Company Home Page</h2>
	<hr>

	<p>Welcome to the luv2code company home page!</p>


	<!--Display User Name and Roles  -->

	<hr>

	<p>
		User :
		<security:authentication property="principal.username" />

		<br> <br> Role(s):
		<security:authentication property="principal.authorities" />


	</p>

	<security:authorize access="hasRole('ADMIN')">
		<p>
			<a href="${pageContext.request.contextPath}/admin/main"> Admin
				Portal</a>

		</p>

	</security:authorize>


	<security:authorize access="hasRole('MANAGER')">
		<p>
			<a href="${pageContext.request.contextPath}/managers/main">
				Managers Portal</a>

		</p>
	</security:authorize>

	<!-- Logout Button -->

	<form:form action="${pageContext.request.contextPath}/logout"
		method="POST">
		<input type="submit" value="logout">


	</form:form>

</body>

</html>