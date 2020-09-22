<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Student Registration Form</title>

<style type="text/css">
.error {
	color: red
}
</style>


</head>
<body>
	<form:form modelAttribute="student" action="processForm" method="POST">

		First Name: <input type="text" name="firstName"
			placeholder="FirstName" />
		<br>
		<br>
		Last Name (*): <input type="text" name="lastName"
			placeholder="LastName" />
		<form:errors path="lastName" cssClass="error"></form:errors>
		<br>
		<br>
		<!--Manual Values in DDLB  -->
		<%-- <form:select path="country">
			<form:option value="BR" label="Brazil"></form:option>
			<form:option value="IN" label="India"></form:option>
			<form:option value="DE" label="Germany"></form:option>
			<form:option value="US" label="USA"></form:option>
		</form:select> --%>

		<!--Values bound by Properties file  -->
		<form:select path="country">
			<form:options items="${thecountryOptions}" />
		</form:select>
		<br>
		<br>
		<!--Manual Values for RB  -->
		<label>Enroll for Programming Language</label>
		<br>
		<%-- <form:radiobutton path="prLanguage" value="Java" />Java
		<form:radiobutton path="prLanguage" value="C#" />C#
		<form:radiobutton path="prLanguage" value="ABAP" />ABAP
		<form:radiobutton path="prLanguage" value="PHP" />PHP --%>

		<!--Values bound by Properties file  -->
		<form:radiobuttons path="prLanguage" items="${theprLanguageOptions}" />

		<br>
		<br>
		<!--Manual Values for CB  -->
		<label>Familiar with OS-Select from below</label>
		<br>
		<%-- <form:checkbox path="opSys" value="Linux" />Linux
		<form:checkbox path="opSys" value="Windows" />Windows
		<form:checkbox path="opSys" value="Mac OS" />Mac OS
		<form:checkbox path="opSys" value="Android" />Android --%>

		<!--Values bound by Properties file  -->
		<form:checkboxes items="${theOSOptions }" path="opSys" />
		<br>
		<br>
		
		Free Passes: <form:input path="freePasses" />
		<form:errors path="freePasses" cssClass="error"></form:errors>
		<br>
		<br>
		Postal Code: <form:input path="postalCode" />
		<form:errors path="postalCode" cssClass="error"></form:errors>


		<br>
		<br>
		Course Code: <form:input path="courseCode" />
		<form:errors path="courseCode" cssClass="error"></form:errors>


		<br>
		<br>
		<input type="submit" />

	</form:form>

</body>
</html>