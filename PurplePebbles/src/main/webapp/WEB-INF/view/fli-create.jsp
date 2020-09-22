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

				<div id="flICreate" style="margin-top: 50px;"
						class="mainbox col-md-3 col-md-offset-2 col-sm-6 col-sm-offset-2">

						<div class="panel panel-primary">

								<div class="panel-heading">
										<div class="panel-title">Add Fund Line Item</div>
								</div>

								<div style="padding-top: 30px" class="panel-body">

										<!-- Fund Line item Form -->
										<form:form
												action="${pageContext.request.contextPath}/fl/processCreateFLI"
												modelAttribute="newFLI" class="form-horizontal"
												method="POST">

												<form:hidden path="flid" value="${flid}" />

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

												<!-- Txn Type -->
												<div>
														<label>Transaction Type</label>
												</div>
												<div style="margin-bottom: 25px;" class="input-group">

														<form:select path="type" class="form-control">
																<form:options items="${txConfig}" var="configI"
																		itemValue="code" itemLabel="desc" />
														</form:select>

												</div>

												<!--Date Txn.  -->
												<div style="margin-bottom: 25px" class="input-group">


														<form:input id="datepicker" path="Date" placeholder="Date"
																class="form-control" />

														<script>
															$('#datepicker')
																	.datepicker(
																			{
																				uiLibrary : 'bootstrap4'
																			});
														</script>
												</div>

												<div style="margin-bottom: 25px;" class="input-group">
														<span class="input-group-addon" style="width: 40px"><i
																class="glyphicon glyphicon-envelope"></i></span>

														<form:textarea path="desc" style=" height: 60px"
																placeholder="Description (*)" class="form-control" />
												</div>


												<div style="margin-bottom: 25px" class="input-group">
														<span class="input-group-addon" style="width: 40px"><i
																class="glyphicon glyphicon-euro"></i></span>

														<form:input path="Amount" placeholder="Amount Rs. (*)"
																class="form-control" />
												</div>



												<!-- Create Button -->

												<div style="margin-top: 10px" class="form-group">
														<div class="col-sm-6 controls">
																<button type="submit" class="btn btn-success">Confirm</button>
														</div>
												</div>
												<%-- <div style="margin-top: 10px" class="form-group">
														
														<c:url var="confirmFLAdd" value="/fl/ConfirmaddFLItem">
																 <c:param name="flid" value="${flid}"></c:param> 
														</c:url>

														<div class="col-sm-6 controls">
																<a href="${confirmFLAdd}" class="btn btn-success"
																		role="button" type="submit"> Confirm</a>
														</div>
												</div> --%>

										</form:form>

								</div>

						</div>

						<div>
								<table>

										<tr>
												<td><input type="button" value="Fund Lines"
														class="btn btn-warning"
														onclick="window.location.href = '${pageContext.request.contextPath}/pf/main'; return false;">
												<td>
												<td><input type="button" value="Home"
														class="btn btn-info"
														onclick="window.location.href = '${pageContext.request.contextPath}/'; return false;">
												<td>
										</tr>

								</table>
						</div>
				</div>

		</div>

</body>



</html>