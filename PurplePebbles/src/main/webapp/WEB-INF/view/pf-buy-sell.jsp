<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
		content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<!-- Metro 4 -->
<link rel="stylesheet"
		href="https://cdn.metroui.org.ua/v4.3.2/css/metro-all.min.css">
</head>
<body>
		<!-- Main Page Responsive Container -->
		<div id="main-page-container" class="container">


				<div class="grid">

						<div class="row" style="padding-left: 450px;">
								<div class="form-group">
										<div class="split-button "
												style="padding-left: 10px; padding-top: 20px">
												<button class="button primary outline">Navigate</button>
												<button class="split dropdown-toggle primary outline"></button>
												<ul class="d-menu" data-role="dropdown">
														<c:url var="backtoCurrPF" value="/pf/PFdetailsShow">
																<c:param name="pid" value="${newTxn.pid}"></c:param>
														</c:url>

														<li><a href="${backtoCurrPF}"><span
																		class="mif-backward icon mif-lg fg-orange"></span>Back</a></li>
														<li class="divider"></li>
														<li><a
																href="${pageContext.request.contextPath}/pf/main"><span
																		class="mif-database icon mif-lg fg-cyan"></span>Portfolios</a></li>
														<li><a href="${pageContext.request.contextPath}/"><span
																		class="mif-home icon mif-lg fg-green"></span>Home</a></li>
														<li class="divider"></li>
														<li><a
																href="${pageContext.request.contextPath}/logout"><span
																		class="mif-exit icon mif-lg fg-red"></span>Logout</a></li>
												</ul>
										</div>

								</div>
						</div>

						<!-- Main Row-->
						<div class="row" style="padding-top: -20px">
								<div data-role="panel" data-width="390"
										data-title-caption="Transact for Portfolio"
										data-title-icon="<span class='mif-apps'></span>"
										data-cls-title="bg-cyan fg-white"
										data-cls-title-icon="bg-green fg-white"
										style="border-color: #005271">
										<form:form
												action="${pageContext.request.contextPath}/pf/processPFTxn"
												modelAttribute="newTxn" class="form-horizontal"
												method="POST">

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


												<form:hidden path="pid" value="${newTxn.pid}" />
												<form:hidden path="TCode" value="${newTxn.tCode}" />

												<div class="form-group" style="padding: 5px">
														<div>
																<label class="fg-blue">Date </label>
														</div>
														<form:input data-role="datepicker"
																placeholder="Transaction Date" path="txnDate"
																class="info" data-effect="slide" />
														<small class="text-muted">Transaction
																Date(mm/dd/yyyy)</small>
												</div>


												<div class="form-group" style="padding: 5px">
														<div>
																<label class="fg-blue">Select Scrip </label>
														</div>
														<form:select path="scCode" class="info" data-role="select">
																<form:options items="${scrips}" var="scripI"
																		itemValue="scCode" itemLabel="desc" />
														</form:select>
														<small class="text-muted">Select the Scrip from
																List to Transact</small>
												</div>

												<div class="form-group" style="padding: 5px">
														<label class="fg-blue">Avg. Price/Unit in Rs. </label>
														<form:input placeholder="Average Price/Unit in Rs."
																path="ppu" class="info" />
														<small class="text-muted ">Enter the Avg.
																Price/Unit of Transacted Scrip in Rs.</small>
												</div>

												<div class="form-group"
														style="padding-top: 5px; padding-left: 5px; padding-right: 5px">
														<label class="fg-blue"># Units</label>
														<form:input placeholder="Number of Units Transacted"
																path="numUnits" class="info" />
														<small class="text-muted">Enter the Number of
																Units Transacted.</small>
												</div>


												<div class="form-group">
														<button class="button success">Submit</button>
												</div>
										</form:form>
								</div>
						</div>

				</div>
		</div>

		<!-- Metro 4 -->
		<script src="https://cdn.metroui.org.ua/v4.3.2/js/metro.min.js"></script>
</body>
</html>