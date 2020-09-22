<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>FundLine Overview</title>
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
				style="width: 900px; padding: 10px">


				<div id="depheader" class="panel panel-info"
						style="padding-left: 10px; background-color: #e6f7ff; height: 30px;">
						<h5 class="text-info">FundLine Overview</h5>

				</div>

				<div
						style="padding-top: 5px; padding-left: 35px; padding-bottom: 10px; padding-right: 25px">
						<div class="row" style="padding-top: -10px">
								<form:form method="GET" modelAttribute="fl"
										class="form-horizontal">

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


														<div>

																<!-- Check for registration error -->
																<c:if test="${formSucc != null}">

																		<div
																				class="alert alert-success col-xs-offset-1 col-xs-10">
																				${formSucc}</div>

																</c:if>

														</div>
												</div>
										</div>


										<input type="hidden" name="pid" value="${fl.flid}" />



										<div class="jumbotron jumbotron-fluid"
												style="background-color: #EBF5FB; padding: 20px">

												<div class="row" style="text-align: center;">
														<div class="col-sm-6" style="background-color: #EBF5FB;">
																<h4>${fl.name}</h4>
														</div>

														<div class="col-sm-6"
																style="background-color: #EBF5FB; color: #D68910;">
																<h4>Current Balance - Rs. ${fl.balance}</h4>
														</div>
												</div>
												<div class="row" style="text-align: center;">
														<h6>${fl.desc}</h6>
												</div>
												<div class="row" style="padding: 5px">

														<div class="col-sm-3" style="background-color: #EAF2F8;">Content1</div>
														<div class="col-sm-3" style="background-color: #F5EEF8;">Content2</div>
														<div class="col-sm-3" style="background-color: #E8F8F5;">Content3</div>
														<div class="col-sm-3" style="background-color: #FDEDEC;">Content4</div>
												</div>

										</div>
								</form:form>
						</div>



				</div>

				<div>
						<c:url var="addFlItem" value="/fl/addFLItem">
								<c:param name="flid" value="${fl.flid}"></c:param>
						</c:url>
						<a href="${addFlItem}" class="btn btn-success" role="button">
								Manage Fund(s)</a>

				</div>

				<br> <br>


				<!-- Fund Line Items  -->
				<div id="flIAssgn" class="panel panel-info"
						style="padding-left: 10px; background-color: #e6f7ff; height: 30px;">
						<h5 class="text-info">FundLine Items</h5>

				</div>


				<div id="container"
						style="padding: 5px; height: 150px; overflow: auto;">



						<!--  add our html table here -->
						<table class="table table-hover">

								<tr>
										<th>Type</th>
										<th>Date (yyyy-mm-dd)</th>
										<th>Amount</th>
										<th>Description</th>
										<th>Action</th>
								</tr>

								<c:forEach var="flItem" items="${flItems}">

										<!-- 	Construct Update Link -->

										<c:url var="deleteFLILink" value="/fl/deleteFli">
												<c:param name="fliid" value="${flItem.fl_i_id}"></c:param>
												<!--Fund Line ID to Populate model   -->
												<c:param name="flid" value="${flItem.flid}"></c:param>
										</c:url>

										<c:url var="updateFLILink" value="/fl/updateFli">
												<c:param name="fliid" value="${flItem.fl_i_id}"></c:param>
												<!--Fund Line ID to Populate model   -->
												<c:param name="flid" value="${flItem.flid}"></c:param>
										</c:url>


										<tr>
												<td>${flItem.type}</td>
												<td>${flItem.date}</td>
												<td>${flItem.amount}</td>
												<td>${flItem.desc}</td>

												<td><a href="${updateFLILink}">Update</a> | <a
														href="${deleteFLILink}"
														onclick="if(!(confirm('Are you sure you want to delete this Fund Line Item?'))) return false">Delete</a></td>




										</tr>

								</c:forEach>
						</table>
				</div>

		</div>

		<div>
				<table>

						<tr>
								<td><input type="button" value="Back"
										class="btn btn-warning"
										onclick="window.location.href = '${pageContext.request.contextPath}/pf/main'; return false;">
								<td>
								<td><input type="button" value="Home" class="btn btn-info"
										onclick="window.location.href = '${pageContext.request.contextPath}/'; return false;">
								<td>
						</tr>

				</table>
		</div>

</body>

</html>