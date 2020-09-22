<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Portfolio Overview</title>
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
				style="width: 800px; padding: 10px">

				<!-- Portfolio Name  -->
				<div id="depheader" class="panel panel-info"
						style="padding-left: 10px; background-color: #e6f7ff; height: 30px;">
						<h5 class="text-info">Portfolio Overview</h5>

				</div>

				<div
						style="padding-top: 5px; padding-left: 35px; padding-bottom: 10px; padding-right: 25px">
						<div class="row" style="padding-top: -10px">
								<form:form method="GET" modelAttribute="pf"
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


										<input type="hidden" name="pid" value="${pf.pid}" />



										<div class="jumbotron jumbotron-fluid"
												style="background-color: #EBF5FB; padding: 20px">

												<div class="row" style="text-align: center;">
														<h4>${pf.name}</h4>
												</div>
												<div class="row" style="text-align: center;">
														<h5>${pf.desc}</h5>
												</div>
												<div class="row" style="padding: 5px">

														<div class="col-sm-3" style="background-color: #EAF2F8;">
																<c:if test="${pf.fundLine ==null}">

																		<div class="row" style="padding-left: 15px">

																				<c:url var="flAssgn" value="/pf/assgnFL">
																						<c:param name="pid" value="${pf.pid}"></c:param>
																				</c:url>
																				<a href="${flAssgn}" class="btn btn-danger"
																						role="button" data-toggle="tooltip"
																						data-placement="right"
																						title="It is imperative to assign a Fund line to a Portfolio before connducting Transactions in the same.">Assign
																						FundLine</a>

																		</div>

																</c:if>


																<c:if test="${pf.fundLine !=null}">
																		<div class="row" style="padding-left: 15px">
																				<c:url var="detFLlink" value="/fl/FLdetailsShow">
																						<c:param name="flid" value="${pf.fundLine.flid}"></c:param>

																				</c:url>

																				<a href="${detFLlink}" role="button"
																						style="background-color: #EAF2F8">${flName}</a>

																		</div>

																</c:if>
														</div>
														<div class="col-sm-3" style="background-color: #F5EEF8;">Content2</div>
														<div class="col-sm-3" style="background-color: #E8F8F5;">Content3</div>
														<div class="col-sm-3" style="background-color: #FDEDEC;">Content4</div>
												</div>

										</div>
								</form:form>
						</div>


						<div class="clearfix"></div>





				</div>



		</div>


		<%-- <!-- Employee Assignments  -->
				<div id="empAssgn" class="panel panel-info"
						style="padding-left: 10px; background-color: #e6f7ff; height: 30px;">
						<h5 class="text-info">Employees Assignment Details</h5>

				</div>


				<div id="container" style="padding: 5px">

						<div style="padding-bottom: 5px">

								<c:url var="addAssgn" value="/manager/Dept/assgnEmp">
										<c:param name="depid" value="${dep.depId}"></c:param>
								</c:url>
								<a href="${addAssgn}" class="btn btn-warning" role="button">Assign
										Employee(s)</a>

						</div>

						<!--  add our html table here -->
						<table class="table table-hover">

								<tr>
										<th>Name</th>
										<th>Email</th>
										<th>Action</th>
								</tr>

								<c:forEach var="tmpEmp" items="${dep.empInDept}">

										<!-- 	Construct Update Link -->

										<c:url var="deleteAssgnLink" value="/manager/Dept/deleteAssgn">
												<c:param name="depid" value="${dep.depId}"></c:param>
												<!--Employee ID to Populate model with refreshed Employee Entity  -->
												<c:param name="empid" value="${tmpEmp.empid}"></c:param>
										</c:url>


										<tr>
												<td>${tmpEmp.firstName}${tmpEmp.lastName}</td>
												<td>${tmpEmp.email}</td>

												<!-- Enable Delete Employee Assignment Only for Non Current Managers of Department -->
												<td class="text-info"><c:if
																test="${dep.managerEmp.empid != tmpEmp.empid}">
																<a href="${deleteAssgnLink}"
																		onclick="if(!(confirm('Are you sure you want to delete this Employee Assignment?'))) return false"
																		class="text-info">Delete Assignment</a>
														</c:if></td>
										</tr>

								</c:forEach>
						</table>
				</div>

		</div>

 --%>


		<br>
		<br>
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

		</div>
</body>

</html>