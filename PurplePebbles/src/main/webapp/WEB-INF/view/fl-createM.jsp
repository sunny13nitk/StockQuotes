<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security"
		uri="http://www.springframework.org/security/tags"%>

<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
		content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>My App</title>
<!-- Metro 4 -->
<link rel="stylesheet"
		href="https://cdn.metroui.org.ua/v4.3.2/css/metro-all.min.css">
</head>
<body>
		<!-- Main Page Responsive Container -->

		<div class="grid">

				<div class="row">
						<div data-role="appbar" data-expand="true"
								style="background-color: #A072BE; color: white; padding: 5px">
								<span class="mif-swarm icon mif-3x fg-white"
										style="padding-right: 10px; padding-bottom: -5px"></span> My
								App
								<ul class="app-bar-menu ml-auto text-upper">
										<li><a href="${pageContext.request.contextPath}/"><span
														class="mif-home icon mif-2x fg-white"></span></a></li>
										<li><a href="${pageContext.request.contextPath}/fl/main"
												class="app-bar-item">Fund Line</a></li>
										<li><a href="${pageContext.request.contextPath}/pf/main"
												class="app-bar-item">Portfolios</a></li>
										<li><a href="m4q-about.html" class="app-bar-item">Scrips</a></li>
										<security:authorize access="hasRole('ADMIN')">
												<li><a
														href="${pageContext.request.contextPath}/scrips/main"
														class="app-bar-item">Admin</a></li>
										</security:authorize>
										<li><a href="support.html" class="app-bar-item">Support</a></li>
										<li><span class="mif-user icon mif-2x fg-white"></span>
												<ul class="d-menu place-right "
														style="background: transparent; padding: 10px"
														data-role="dropdown">

														<li><security:authorize access="isAuthenticated()">
																		<security:authentication property="principal.username" />
																</security:authorize></li>


												</ul></li>

										<li>
												<div style="background: transparent; padding-top: 10px">
														<form:form
																action="${pageContext.request.contextPath}/logout"
																method="POST">

																<button class="button flat-button"
																		style="background: transparent; color: white"
																		data-role="hint" data-hint-position="bottom"
																		data-cls-hint="bg-transparent fg-mauve"
																		data-hint-text="Logout">
																		<span class="mif-exit icon mif-2x fg-white"></span>
																</button>

														</form:form>
												</div>
										</li>
								</ul>




						</div>

				</div>

				<!-- Main Row-->
				<div style="margin-top: 80px">

						<div class="card" style="width: 400px;">
								<div class="card-header">

										<div class="remark primary "
												style="padding-top: 10px; margin: 2px">
												<h4>New Fundline Creation</h4>


										</div>
										<form:form
												action="${pageContext.request.contextPath}/fl/processCreateFL"
												modelAttribute="newFL" class="form-horizontal" method="POST">
												<!-- Place for messages: error, alert etc ... -->
												<div class="form-group">
														<div class="col-xs-15">
																<div>

																		<!-- Check for Form error -->
																		<c:if test="${formError != null}">

																				<div class="remark alert"
																						style="padding-top: 10px; margin: 2px">${formError}
																				</div>
																		</c:if>

																</div>
														</div>

														<div class="col-xs-15">
																<div>

																		<!-- Check for registration success -->
																		<c:if test="${formSucc != null}">

																				<div class="remark success"
																						style="padding-top: 10px; margin: 2px">${formSucc}
																				</div>

																		</c:if>

																</div>
														</div>
												</div>

												<div class="form-group" style="padding: 5px">

														<form:input type="text" data-role="input"
																data-clear-button-icon="<span class='mif-cancel fg-red'></span>"
																placeholder="Short Name for Fund Line" path="name"
																class="info" />
														<small class="text-muted ">Enter short description
																for FundLine </small>
												</div>

												<div class="form-group"
														style="padding-top: 5px; padding-left: 5px; padding-right: 5px">


														<form:textarea data-role="textarea" data-auto-size="true"
																data-max-height="200" path="desc" class="info"
																placeholder="Fund Line Description"
																data-clear-button-icon="<span class='mif-cancel fg-red'></span>"></form:textarea>

														<small class="text-muted">Description/Purpose/Summary
																for Fund Line</small>
												</div>


												<div class="form-group">
														<button class="button success"
																style="background-color: #59D3AF; color: white">Submit</button>
														<!-- <button class="button success">Submit</button> -->

														<input type="button" value="Back" class="button"
																style="background-color: #59BAD3; color: white"
																onclick="window.location.href = '${pageContext.request.contextPath}/fl/main'; return false;">

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