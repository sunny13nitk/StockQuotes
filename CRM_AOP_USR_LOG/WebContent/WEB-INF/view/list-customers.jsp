<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>List Customers</title>
<link type="text/css" rel="stylesheet"
		href="${pageContext.request.contextPath}/resources/css/style.css">
</head>

<body>
		<div id="wrapper">

				<div id="header">
						<h2>Customer Relationship Manager</h2>
				</div>

				<div id="container">

						<div id="content">

								<!--Add Customer Button  -->
								<input type="button" value="Add Customer"
										onclick="window.location.href = 'addCustomer'; return false;"
										class="add-button">


								<!--  add a search box -->
								<form:form action="searchbyName" method="POST"
										modelAttribute="theSearchName">
                					   Search customer: <input type="text"
												name="theSearchName" />
										<input type="submit" value="Search" class="add-button" />
								</form:form>

								<!--Customer List Table  -->
								<table>
										<tr>
												<th>First Name</th>
												<th>Last Name</th>
												<th>Email</th>
												<th>Action</th>
										</tr>

										<c:forEach var="cust" items="${customers}">

												<!-- Prepare Url Link for Update Customer  -->
												<c:url var="updateCustomerLink"
														value="/customer/showupdateCustomer">
														<c:param name="customerId" value="${cust.id}"></c:param>

												</c:url>

												<!-- Prepare Url Link for Delete Customer  -->
												<c:url var="deleteCustomerLink"
														value="/customer/deleteCustomer">
														<c:param name="customerId" value="${cust.id}"></c:param>

												</c:url>

												<tr>
														<td>${cust.firstName}</td>
														<td>${cust.lastName}</td>
														<td>${cust.email}</td>

														<td><a href="${updateCustomerLink}">Update</a> | <a
																href="${deleteCustomerLink}"
																onclick="if(!(confirm('Are you sure you want to delete this Customer?'))) return false">Delete</a></td>
												</tr>

										</c:forEach>

								</table>

						</div>


						<div style="">
								<p>
										<a href="${pageContext.request.contextPath}/home/logout">Logout</a>
								</p>
						</div>
				</div>


		</div>

</body>

</html>