<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>

<head>
<meta charset="ISO-8859-1">
<title>Student- Confirmation!</title>


</head>
<body>

	<h5>${message}</h5>
	<h2>Student Name: ${student.firstName} ${student.lastName}</h2>

	<h3>Details</h3>
	Country: ${student.country }
	<br> Programming Course Enrolled : ${student.prLanguage }

	<h4>Operating Systems</h4>
	<ul>
		<c:forEach var="ositem" items="${student.opSys}">
			<li>${ositem}</li>
		</c:forEach>
	</ul>

	<h4>Free Passes : ${student.freePasses}</h4>

	<h4>Postal Code : ${student.postalCode}</h4>

	<h4>Course Code : ${student.courseCode}</h4>

</body>

</html>