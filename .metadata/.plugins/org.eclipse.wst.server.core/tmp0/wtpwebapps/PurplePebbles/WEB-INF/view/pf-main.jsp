
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Manage Employee Profile(s)</title>
<meta charset="utf-8">
<meta name="viewport"
		content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Reference Bootstrap files -->
<link rel="stylesheet"
		href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>

<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</head>

<body style="padding: 10px">
		<div id="wrapper" class="panel panel-info"
				style="width: 1000px; padding: 10px">
				<div id="header" class="panel panel-info"
						style="padding: 10px; background-color: #e6f7ff; height: 70px;">
						<h3 style="padding: -15px">Portfolios for User - ${userName}</h3>
				</div>

				<div>

						<!-- Place for messages: error, alert etc ... -->
						<div class="form-group">
								<div class="col-xs-15">
										<div>

												<!-- Check for registration error -->
												<c:if test="${formError != null}">

														<div class="alert alert-danger col-xs-offset-1 col-xs-10">
																${formError}</div>

												</c:if>

										</div>


										<div>

												<!-- Check for registration error -->
												<c:if test="${formSucc != null}">

														<div class="alert alert-success col-xs-offset-1 col-xs-10">
																${formSucc}</div>

												</c:if>

										</div>
								</div>
						</div>
						<a href="${pageContext.request.contextPath}/pf/createPF"
								class="btn btn-warning" role="button">Create Portfolio</a>

				</div>

				<div id="container" style="padding: 15px">
						<!--  add our html table here -->
						<table class="table table-hover">

								<tr>
										<th>Name</th>
										<th>Description</th>
										<th>Broker</th>
										<th>Action</th>
								</tr>

								<c:forEach var="ipf" items="${pfList}">

										<!-- 	Construct Update Link -->

										<c:url var="updPFlink" value="/pf/updatePF">
												<c:param name="pid" value="${ipf.pid}"></c:param>

										</c:url>

										<c:url var="detPFlink" value="/pf/PFdetailsShow">
												<c:param name="pid" value="${ipf.pid}"></c:param>

										</c:url>

										<c:url var="delPFlink" value="/pf/deletePF">
												<c:param name="pid" value="${ipf.pid}"></c:param>

										</c:url>


										<tr>
												<td>
														<!-- Portolio Details Navigation --> <a
														href="${detPFlink}" class="text-info">${ipf.name}</a>
												</td>
												<td>${ipf.desc}</td>
												<td>${ipf.broker}</td>
												<td><a href="${updPFlink}">Update</a> | <a
														href="${delPFlink}"
														onclick="if(!(confirm('Are you sure you want to delete this Portfolio?'))) return false">Delete</a></td>

										</tr>

								</c:forEach>
						</table>
				</div>

		</div>
		<br>
		<br>
		<div>
				<table>

						<tr>
								<td><form:form
												action="${pageContext.request.contextPath}/logout"
												method="POST">

												<input class="btn btn-danger" type="submit" value="Logout">

										</form:form>
								<td>
								<td><input type="button" value="Home" class="btn btn-info"
										onclick="window.location.href = '${pageContext.request.contextPath}/'; return false;">
								<td>
						</tr>

				</table>
		</div>
</body>

</html>

