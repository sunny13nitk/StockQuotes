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
				<!-- Main Grid-->
				<div class="grid">
						<!-- Main Row-->
						<div class="row">
								<!-- Left column for Cards  -->
								<div id="col-cards" class="colspan-4">
										<!--Cards Cell  -->

										<div class="row">
												<div>
														<!--PF Ovw  -->
														<form:form method="GET" modelAttribute="pf"
																class="form-horizontal">
																<input type="hidden" name="pid" value="${pf.pid}" />



																<div class="card"
																		style="width: 300px; padding-top: -50px">
																		<div class="card-header">
																				<h4>${pf.name}</h4>
																				<div class="remark primary"
																						style="padding: 10px; margin: 2px">${pf.desc}</div>
																		</div>
																		<div class="card-content p-2">
																				<c:if test="${pf.fundLine ==null}">

																						<div class="row" style="padding-left: 15px">

																								<c:url var="flAssgn" value="/pf/assgnFL">
																										<c:param name="pid" value="${pf.pid}"></c:param>
																								</c:url>
																								<a href="${flAssgn}"
																										class="button flat-button bg-lightmauve-hover "
																										data-role="hint" data-hint-position="right"
																										data-cls-hint="bg-white fg-darkviolet drop-shadow"
																										data-hint-text="It is imperative to assign a Fund line
																										to a Portfolio before conducting Transactions
																										in the same."><span
																										class="mif-checkmark"></span> Assign FundLine
																								</a>

																						</div>

																				</c:if>


																				<c:if test="${pf.fundLine !=null}">
																						<div class="row" style="padding-left: 15px">
																								<c:url var="detFLlink" value="/fl/FLdetailsShow">
																										<c:param name="flid"
																												value="${pf.fundLine.flid}"></c:param>

																								</c:url>

																								<a href="${detFLlink}" role="button">${flName}</a>
																								<span style="padding: 5px">
																										<h6 style="color: #000058">Rs.
																												${flBalance}</h6>
																								</span>

																						</div>

																				</c:if>



																		</div>
																</div>

														</form:form>
												</div>
										</div>

										<div id="cardStatsRow" class="row">


												<div>
														<!--PF Stats -->
														<form:form method="GET" modelAttribute="pf"
																class="form-horizontal">
																<input type="hidden" name="pid" value="${pf.pid}" />

																<div class="card"
																		style="width: 300px; padding-top: -20px">

																		<div class="card-content p-2">

																				<h6
																						style="color: #325254; background-color: aliceBlue; padding: 10px">Quick
																						Stats</h6>

																				<c:if test="${pf.fundLine !=null}">
																						<!-- Check binding For PF Stats -->

																						<blockquote class="right-side">
																								<h4 style="color: #000058">
																										15.28 Lacs <small><cite> <h7
																																style="color: #000058"> Investments
																														(INR)</h7>
																										</cite> </small>
																								</h4>
																						</blockquote>

																						<blockquote>
																								<h5 style="color: #000058">
																										15 <small style="color: #000058"> <h8>Scrips</h8>
																										</small>
																								</h5>
																						</blockquote>


																						<blockquote class="right-side">
																								<div id="donut_val" data-role="donut"
																										data-hole=".8" data-animate="10"
																										data-fill="#0f4880" data-value="45"
																										class="mx-auto"></div>
																								<small><cite> <h7
																														style="color: #0f4880">Top 5 Holdings</h7>
																								</cite> </small>
																						</blockquote>

																						<blockquote>
																								<div data-role="progress" data-type="buffer"
																										data-value="84" data-buffer="100"></div>
																								<h5 style="color: #004a00">
																										84.37% <small style="color: #000058">
																												<h7> Fund Line Fullfilment</h7>
																										</small>
																								</h5>


																						</blockquote>



																				</c:if>



																		</div>
																</div>

														</form:form>
												</div>




										</div>







								</div>


								<!-- Right column for Tables  -->


								<div id="col-cards" class="colspan-8">
										<div class="grid-rightpanel">
												<div class="row" id="buttonbar">
														<div class="multi-action" style="padding-top: 20px">
																<button
																		class="action-button rotate-minus bg-red fg-white"
																		onclick="$(this).toggleClass('active')"
																		data-role="hint" data-hint-text="Portfolio Actions"
																		data-cls-hint="bg-red fg-white drop-shadow"
																		data-hint-position="bottom">
																		<span class="icon"><span class="mif-plus"></span></span>
																</button>
																<ul class="actions drop-right">
																		<c:url var="addTxn" value="/pf/showAddTxn">
																				<c:param name="pid" value="${pf.pid}"></c:param>

																		</c:url>

																		<c:url var="listTxn" value="/pf/listTxn">
																				<c:param name="pid" value="${pf.pid}"></c:param>

																		</c:url>

																		<li class="bg-blue"><a href="${addTxn}"
																				data-role="hint" data-hint-text="Buy/Sell Scrips"
																				data-cls-hint="bg-cyan fg-white drop-shadow"
																				data-hint-position="bottom"><span
																						class="mif-folder-plus"></span></a></li>
																		<li class="bg-teal"><a href="${listTxn}"
																				data-role="hint" data-hint-text="Transactions Diary"
																				data-cls-hint="bg-teal fg-white drop-shadow"
																				data-hint-position="bottom"><span
																						class="mif-library"></span></a></li>

																</ul>
														</div>

														<div class="split-button "
																style="padding-left: 550px; padding-top: 20px">
																<button class="button primary outline">Navigate</button>
																<button
																		class="split dropdown-toggle primary rounded outline"></button>
																<ul class="d-menu" data-role="dropdown">
																		<li><a
																				href="${pageContext.request.contextPath}/pf/main"><span
																						class="mif-database icon mif-lg fg-cyan"></span>Portfolios</a></li>
																		<li><a href="${pageContext.request.contextPath}/"><span
																						class="mif-home icon mif-lg fg-green"></span>Home</a></li>
																		<li class="divider"></li>
																		<li>
																				<div style="padding: 5px">
																						<form:form
																								action="${pageContext.request.contextPath}/logout"
																								method="POST">

																								<button class="button flat-button"
																										style="background: transparent;">
																										<span class="mif-exit icon mif-lg fg-red"></span>
																										Logout
																								</button>


																						</form:form>
																				</div>
																		</li>
																</ul>
														</div>


												</div>
												<div class="row" id="pftable"></div>
										</div>






								</div>
						</div>

				</div>


		</div>

		<!-- Metro 4 -->
		<script src="https://cdn.metroui.org.ua/v4.3.2/js/metro.min.js"></script>

</body>
</html>