<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html lang="en">
<head>
<title>Portfolio Overview</title>
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
						<!-- PF Actions and Navigation Row -->
						<div class="row">
								<!-- PF Actions Cell -->
								<div class="cell-10">
										<div class="multi-action">
												<button class="action-button rotate-minus bg-red fg-white"
														onclick="$(this).toggleClass('active')" data-role="hint"
														data-hint-text="Portfolio Actions"
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

														<c:url var="listHoldings" value="/pf/listHoldings">
																<c:param name="pid" value="${pf.pid}"></c:param>

														</c:url>

														<li class="bg-blue"><a href="${addTxn}"
																data-role="hint" data-hint-text="Buy Scrips"
																data-cls-hint="bg-cyan fg-white drop-shadow"
																data-hint-position="bottom"><span
																		class="mif-folder-plus"></span></a></li>

														<li class="bg-orange"><a href="${listHoldings}"
																data-role="hint" data-hint-text="Holdings Statement"
																data-cls-hint="bg-orange fg-white drop-shadow"
																data-hint-position="bottom"><span
																		class="mif-featured-play-list"></span></a></li>

														<li class="bg-teal"><a href="${listTxn}"
																data-role="hint" data-hint-text="Transactions Diary"
																data-cls-hint="bg-teal fg-white drop-shadow"
																data-hint-position="bottom"><span
																		class="mif-library"></span></a></li>

												</ul>
										</div>
								</div>

								<!-- Navigation Cell -->
								<div class="cell-2">
										<div class="split-button ">
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
																						style="background: transparent; margin-left: -5px; vertical-align: middle;">
																						<span class="mif-exit icon mif-2x fg-red"></span>
																						<span
																								style="padding-left: 3px; align-content: center;">Logout</span>
																				</button>


																		</form:form>
																</div>
														</li>
												</ul>
										</div>


								</div>
						</div>

				</div>
				<!-- Cards Row-->
				<div class="row">
						<!-- PF basic Card Cell -->
						<div class="cell-4">
								<div>

										<!--PF Ovw  -->
										<form:form method="GET" modelAttribute="pfStats"
												class="form-horizontal">
												<input type="hidden" name="pid" value="${pfStats.pid}" />



												<div class="card" style="width: 300px; padding-top: -50px">
														<div class="card-header">
																<h4>${pfStats.name}</h4>
																<div class="remark primary"
																		style="padding-top: 10px; margin: 2px">${pfStats.description}
																		<div align="right" style="padding-top: 10px">
																				<h7 style="color: #125699"> ${pfStats.broker} <small><cite>
																								<h8 style="color: #000058"> Broker </h8>
																				</cite> </small> </h7>
																		</div>

																</div>
														</div>
														<div class="card-content p-2">
																<c:if test="${pfStats.flid == 0}">

																		<div class="row" style="padding-left: 15px">

																				<c:url var="flAssgn" value="/pf/assgnFL">
																						<c:param name="pid" value="${pfStats.pid}"></c:param>
																				</c:url>
																				<a href="${flAssgn}"
																						class="button flat-button bg-lightmauve-hover "
																						data-role="hint" data-hint-position="right"
																						data-cls-hint="bg-white fg-darkviolet drop-shadow"
																						data-hint-text="It is imperative to assign a Fund line
																										to a Portfolio before conducting Transactions
																										in the same."><span
																						class="mif-checkmark"></span> Assign FundLine </a>

																		</div>

																</c:if>


																<c:if test="${pfStats.flid > 0}">
																		<div class="row" style="padding-left: 15px">
																				<c:url var="detFLlink" value="/fl/FLdetailsShow">
																						<c:param name="flid" value="${pfStats.flid}"></c:param>

																				</c:url>

																				<c:url var="addFlItem" value="/fl/addFLItem">
																						<c:param name="flid" value="${pfStats.flid}"></c:param>
																				</c:url>

																				<a href="${detFLlink}" role="button"><h5>${pfStats.flName}</h5></a><span
																						style="padding: 12px"> <a
																						href="${addFlItem}"><span
																								class="mif-money icon mif-lg fg-green "></span>TopUp</a>

																				</span>

																				<blockquote class="right-side">
																						<h5 style="color: #000058">
																								Rs. ${pfStats.flBalance} <small><cite>
																												<h8 style="color: #000058"> Fund Line
																												Balance</h8>
																								</cite> </small>
																						</h5>
																				</blockquote>

																				<blockquote>
																						<div data-role="progress" data-type="buffer"
																								data-value="${pfStats.flUtilization}"
																								data-buffer="100"></div>
																						<h5 style="color: #004a00">
																								${pfStats.flUtilization}% <small
																										style="color: #000058"> <h7> Fund
																										Line Fullfilment</h7>
																								</small>
																						</h5>


																				</blockquote>


																				<blockquote class="right-side">
																						<h6 style="color: #8000fc">
																								${pfStats.fllastDDate} <small><cite>
																												<h8 style="color: #6017a9"> Last
																												Addition Date</h8>
																								</cite> </small>
																						</h6>
																				</blockquote>

																		</div>
																</c:if>



														</div>
												</div>

										</form:form>

								</div>
						</div>

						<!-- PF Stats Card Cell -->
						<div class="cell-4">
								<div>
										<!--PF Stats -->
										<form:form method="GET" modelAttribute="pfStats"
												class="form-horizontal">

												<c:if test="${pfStats.flid > 0}">
														<!-- Check binding For PF Stats -->
														<input type="hidden" name="pid" value="${pfStats.pid}" />

														<div class="card" style="width: 320px; padding-top: -20px">

																<div class="card-content p-2">

																		<h6
																				style="color: #325254; background-color: aliceBlue; padding: 10px">Quick
																				Stats</h6>



																		<blockquote class="right-side">
																				<h3 style="color: #000058">
																						${pfStats.totalInv} <small><cite> <h7
																												style="color: #000058"> Investments
																										(Rs.)</h7>
																						</cite> </small>
																				</h3>
																		</blockquote>
																		<div class="row">

																				<div class="cell-4">
																						<blockquote>
																								<h4 style="color: #000058">
																										${pfStats.numScrips} <small
																												style="color: #000058"> <h8>Scrips</h8>
																										</small>
																								</h4>
																						</blockquote>
																				</div>
																				<div class="cell-8">
																						<blockquote class="right-side">
																								<div id="donut_val" data-role="donut"
																										data-hole=".8" data-animate="10"
																										data-fill="#0f4880"
																										data-value="${pfStats.top5Per}"
																										class="mx-auto"></div>
																								<div>
																										<small><cite> <h7
																																style="color: #0f4880">Top 5
																														Holdings</h7>
																										</cite> </small>
																								</div>
																								<div>
																										<small><cite> <h7
																																style="color: #0f4880">Investment -
																														Rs. ${pfStats.top5Inv}</h7>
																										</cite> </small>
																								</div>
																						</blockquote>
																				</div>

																		</div>

																		<div class="row">


																				<div class="cell-8">
																						<blockquote class="left-side">
																								<div id="donut_val" data-role="donut"
																										data-hole=".8" data-animate="10"
																										data-fill="#006400"
																										data-value="${pfStats.maxSectorPer}"
																										class="mx-auto"></div>
																								<small><cite> <h7
																														style="color: #008b8b">Top Sector</h7> <h7
																														style="color: #008b8b">-
																												${pfStats.maxSector}</h7>
																								</cite> </small>
																								<div>
																										<small><cite> <h7
																																style="color: #008b8b">Investment -
																														Rs. ${pfStats.maxSectorInv}</h7>
																										</cite> </small>
																								</div>
																						</blockquote>
																				</div>

																				<div class="cell-4">
																						<blockquote>
																								<h4 style="color: #000058">
																										${pfStats.numSectors} <small
																												style="color: #000058"> <h8>Sectors</h8>
																										</small>
																								</h4>
																						</blockquote>
																				</div>

																				<div class="cell-11 offset-1">
																						<blockquote class="right-side">
																								<h6 style="color: #8000fc">
																										${pfStats.lastTradeDate} <small><cite>
																														<h8 style="color: #6017a9"> Last
																														Trade Date</h8>
																										</cite> </small>
																								</h6>
																						</blockquote>
																				</div>

																		</div>


																</div>
														</div>
												</c:if>
										</form:form>

								</div>
						</div>

						<!-- Top Holdings Ovw Card CEll -->
						<div class="cell-4">

								<div>
										<!--Holdings Top n -->
										<form:form method="GET" modelAttribute="pfStats"
												class="form-horizontal">

												<c:if test="${pfStats.flid > 0}">

														<div class="card" style="padding-top: -20px">
																<div class="card-content p-2">

																		<h6
																				style="color: #325254; background-color: #faf0ff; padding: 10px">
																				Holdings Stats</h6>
																</div>
																<div class="cell-10 offset-1">
																		<h6 style="padding-left: 85px; color: #4f3c75">Top
																				5 Holdings</h6>
																</div>

																<c:if test="${top5H != null}">
																		<div class="row"
																				style="padding-top: -10px; padding-left: 10px; padding-right: 10px; padding-bottom: 10px">

																				<table class="table compact striped">
																						<thead>
																								<tr>
																										<th style="text-align: center;">Scrip</th>
																										<th style="text-align: center;">Allocation</th>
																										<th style="text-align: center;">%</th>
																										<th style="text-align: center;">Amount
																												(Rs.)</th>

																								</tr>
																						</thead>
																						<tbody>
																								<c:forEach var="top5HI" items="${top5H}">
																										<tr>
																												<td style="text-align: center;">${top5HI.scCode}</td>
																												<td style="text-align: center;">
																														<div data-role="progress"
																																data-type="buffer"
																																data-cls-bar="bg-lightmauve"
																																data-cls-buffer="bg-white"
																																data-small="true"
																																data-value="${top5HI.perPF}"
																																data-buffer="100"></div>
																												</td>
																												<td>${top5HI.perPF}</td>


																												<td style="text-align: center;">${top5HI.totalInvestment}</td>

																										</tr>

																								</c:forEach>


																						</tbody>
																				</table>



																		</div>
																</c:if>

																<div class="cell-12"
																		style="padding-top: -10px; padding-left: 5px; padding-right: 5px; padding-bottom: 5px">
																		<table class="table compact">
																				<thead>
																						<tr>
																								<th># Trades</th>
																								<th># Buys</th>
																								<th># Sells</th>
																								<th>Txn. Cost (Rs.)</th>
																						</tr>
																				</thead>
																				<tbody>
																						<tr>
																								<td style="text-align: center;">${pfStats.numTrades}</td>
																								<td style="text-align: center;">${pfStats.numBTrades}</td>
																								<td style="text-align: center;">${pfStats.numSTrades}</td>
																								<td style="text-align: center;">${pfStats.txnCost}</td>
																						</tr>

																				</tbody>
																		</table>
																</div>


														</div>



												</c:if>
										</form:form>

								</div>
						</div>

				</div>




		</div>

		<!-- </div> -->

		<!-- Metro 4 -->
		<script src="https://cdn.metroui.org.ua/v4.3.2/js/metro.min.js"></script>
</body>
</html>