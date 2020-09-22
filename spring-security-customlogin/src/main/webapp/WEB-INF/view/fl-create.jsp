<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html lang="en">

<head>
<title>User Registration Form</title>
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

<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<link rel="stylesheet"
		href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<script src="https://unpkg.com/gijgo@1.9.11/js/gijgo.min.js"
		type="text/javascript"></script>
<link href="https://unpkg.com/gijgo@1.9.11/css/gijgo.min.css"
		rel="stylesheet" type="text/css" />

<style>
.error {
		color: red
}
</style>
</head>

<body>

		<div>

				<div id="pfCreate" style="margin-top: 50px;"
						class="mainbox col-md-4 col-md-offset-1 col-sm-6 col-sm-offset-2">

						<div class="panel panel-primary">

								<div class="panel-heading">
										<div class="panel-title">Create Fund Line</div>
								</div>

								<div style="padding-top: 30px" class="panel-body">

										<!-- Create Fund Line -->
										<form:form
												action="${pageContext.request.contextPath}/fl/processCreateFL"
												modelAttribute="newFL" class="form-horizontal" method="POST">

												<!-- Place for messages: error, alert etc ... -->
												<div class="form-group">
														<div class="col-xs-15">
																<div>

																		<!-- Check for registration error -->
																		<c:if test="${formError != null}">

																				<div
																						class="alert alert-danger col-xs-offset-1 col-xs-10">
																						${formError}</div>

																		</c:if>

																</div>
														</div>

														<div class="col-xs-15">
																<div>

																		<!-- Check for registration success -->
																		<c:if test="${formSucc != null}">

																				<div
																						class="alert alert-success col-xs-offset-1 col-xs-10">
																						${formSucc}</div>

																		</c:if>

																</div>
														</div>
												</div>

												<!-- User name -->
												<div style="margin-bottom: 25px;" class="input-group">
														<span class="input-group-addon" style="width: 40px"><i
																class="glyphicon glyphicon-envelope"></i></span>

														<form:input path="name" placeholder="Fund Line Name (*)"
																class="form-control" />
												</div>

												<div style="margin-bottom: 25px; height: 80px"
														class="input-group">
														<span class="input-group-addon" style="width: 40px"><i
																class="glyphicon glyphicon-link"></i></span>

														<form:textarea path="desc" placeholder="Description (*)"
																class="form-control" />
												</div>



												<!-- Create Button -->
												<div style="margin-top: 10px" class="form-group">
														<div class="col-sm-6 controls">
																<button type="submit" class="btn btn-warning"
																		style="height: 30px; font-size: 15px;">Save
																		Fund Line</button>
														</div>
												</div>
										</form:form>

								</div>

						</div>
						<br> <br>
						<div>
								<table>

										<tr>
												<td><form:form
																action="${pageContext.request.contextPath}/logout"
																method="POST">

																<input class="btn btn-danger" type="submit"
																		value="Logout" style="height: 40px; width: 80px">

														</form:form>
												<td>
												<td><input type="button" value="Home"
														class="btn btn-info" style="height: 40px; width: 80px"
														onclick="window.location.href = '${pageContext.request.contextPath}/'; return false;">
												<td>
										</tr>

								</table>
						</div>
				</div>

		</div>

</body>



</html>