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
														href="${pageContext.request.contextPath}/admin/main"
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


				<div class="row" style="margin-top: 50px">
						<div data-role="carousel" data-cls-bullet="bullet-big"
								data-auto-start="true" data-period="3000" data-duration="1000"
								data-bullets-position="right" data-effect="slide"
								data-effect-func="easeInQuart" data-height="21/9"
								data-controls-on-mouse="true" data-cls-bullet="bullet-big"
								data-cls-bullet-on="bg-mauve drop-shadow"
								data-cls-slides="rounded" data-cls-controls="fg-mauve"
								data-control-next="<span class='mif-chevron-right fg-mauve'></span>"
								data-control-prev="<span class='mif-chevron-left fg-mauve'></span>">
								<div class="slide p-14 pl-8 pr-8">

										<div class="row flex-align-center h-90">
												<div class="cell-md-4 text-center">
														<span class='mif-dollars icon mif-10x fg-mauve'></span>
												</div>
												<div class="cell-md-8">
														<h1 class="text-light">Create a Fund Line</h1>
														<p class="mt-4 mb-2" style="color: #601f80">Start by
																creating a Fund Line and add funds to invest in your
																Portfolio(s).</p>
														<p class="mt-1 mb-4" style="color: #601f80">One Fund
																Line Can Serve Multiple Portfolio(s).</p>

														<button class="button"
																style="background-color: #A072BE; color: white">Create
																FundLine</button>

														<p class="mt-4 mb-4" style="color: #601f80">You can
																also Manage Existing Fund Line(s) from below.</p>
														<button class="button"
																style="background-color: #59D3AF; color: white">Manage
																FundLine(s)</button>
												</div>
										</div>



								</div>

								<div class="slide p-14 pl-8 pr-8">

										<div class="row flex-align-center h-90">
												<div class="cell-md-4 text-center">
														<span class='mif-database icon mif-10x fg-mauve'></span>
												</div>
												<div class="cell-md-8">
														<h1 class="text-light">Create Portfolio</h1>
														<p class="mt-4 mb-1" style="color: #601f80">Create a
																Portfolio to manage your Investments.</p>
														<p class="mt-1 mb-1" style="color: #601f80">Portfolio
																is funded by any of the Fund Lines already created.</p>
														<p class="mt-1 mb-4" style="color: #601f80">Multiple
																Portfolios can be created for investing in different
																themes</p>
														<button class="button"
																style="background-color: #A072BE; color: white">Create
																Portfolio</button>

														<p class="mt-4 mb-4" style="color: #601f80">You can
																also Manage your Portfolio(s) from below.</p>
														<button class="button"
																style="background-color: #59D3AF; color: white">Manage
																Portfolio(s)</button>
												</div>
										</div>



								</div>

								<div class="slide p-14 pl-8 pr-8">

										<div class="row flex-align-center h-90">
												<div class="cell-md-4 text-center">
														<span class='mif-chart-line icon mif-10x fg-mauve'></span>
												</div>
												<div class="cell-md-8">
														<h1 class="text-light">Scrip(s) Insights</h1>
														<p class="mt-4 mb-1" style="color: #601f80">Analyze
																the Universe Scrips in an Insightful layout</p>
														<p class="mt-1 mb-4" style="color: #601f80">Perform
																Scrip(s) comparison on important parameters to judge
																Relative Performance</p>
														<button class="button"
																style="background-color: #A072BE; color: white">Scrip
																Analysis</button>

														<p class="mt-4 mb-4" style="color: #601f80">
														<p class="mt-1 mb-4" style="color: #601f80">Determine
																Futuristic Return Ratio(s) for Scrip(s) at an Investment
																Price.</p>
														</p>









														<button class="button"
																style="background-color: #59D3AF; color: white">Returns
																CAGR</button>
												</div>
										</div>



								</div>

						</div>



				</div>

		</div>

		<!-- Metro 4 -->
		<script src="https://cdn.metroui.org.ua/v4.3.2/js/metro.min.js"></script>
</body>
</html>