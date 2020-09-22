<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security"
		uri="http://www.springframework.org/security/tags"%>

<html lang="en">
<head>
<title>MY App</title>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta charset="UTF-8">
<meta name="viewport"
		content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<!-- Metro 4 -->
<link rel="stylesheet"
		href="https://cdn.metroui.org.ua/v4.3.2/css/metro-all.min.css">



</head>
<body>

		<!-- <div class="container-fluid pos-fixed fixed-top z-top bg-hero"
				id="header"> -->
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
										<li><a
												href="${pageContext.request.contextPath}/scrips/main"
												class="app-bar-item">Scrips</a></li>
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


				<div style="margin-top: 80px">
						<!-- Col 1 Controls  -->
						<div class="cell-8">
								<div class="row">
										<!--Tile  -->
										<div class="cell-2 offset-1"
												style="padding-top: 20px; padding-left: 40px">

												<a href="${pageContext.request.contextPath}/fl/createFL"
														data-role="tile" data-size="small"
														class="bg-transparent border
														bd-amber border-radius-4 ">
														<span class='mif-coins icon mif-5x fg-amber '></span>
												</a>
										</div>

										<!--Content  -->
										<div class="cell-6" style="margin-top: -2px;">
												<h6 style="color: #601f80">Create a Fundline</h6>
												<small style="color: #601f80">Brief: <cite
														title="Brief">Set up a New Fundline and Add funds
																to Transact in Portfolio</cite>
												</small>



										</div>
								</div>
								<c:if test="${numFL > 0}">
										<div class="row">
												<!--Tile  -->
												<div class="cell-2 offset-1"
														style="padding-top: 20px; padding-left: 40px">

														<a href="${pageContext.request.contextPath}/fl/listFl"
																data-role="tile" data-size="small"
																class="bg-transparent border
														bd-teal border-radius-4 ">
																<span class='mif-cog icon mif-5x fg-teal '></span>
														</a>
												</div>

												<!--Content  -->
												<div class="cell-6" style="margin-top: -2px;">
														<h6 style="color: #601f80">Manage FundLines -
																${numFL}</h6>

														<small style="color: #601f80">Brief: <cite
																title="Brief">Top up/Withdraw Funds from Fund
																		Line</cite>
														</small>



												</div>
										</div>
								</c:if>
						</div>

						<!-- Col 2 Controls  -->
						<div class="cell-6"></div>
				</div>

		</div>

		<!-- Metro 4 -->
		<script src="https://cdn.metroui.org.ua/v4.3.2/js/metro.min.js"></script>
</body>
</html>