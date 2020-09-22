<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
		content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>My Holdings</title>
<!-- Metro 4 -->
<link rel="stylesheet"
		href="https://cdn.metroui.org.ua/v4.3.2/css/metro-all.min.css">
</head>
<body>
		<!-- Main Page Responsive Container -->
		<!-- <div id="main-page-container" class="container"> -->
		<!-- Main Grid-->
		<div class="grid" style="padding-left: 20px">
				<!-- Navigation Row -->
				<div class="row">
						<!-- Navigation Buttons -->
						<div class="cell-8">
								<h3 style="color: #4465a1">Holdings Statement</h3>
						</div>

						<!-- Holdings Buttons- Charts etc -->
						<div class="cell-2">
								<%-- 	<c:url var="pfposDonut" value="/pf/PFposDonut">
										<c:param name="pid" value="${pid}"></c:param>
								</c:url>
								<ul class="t-menu open horizontal compact"
										style="padding-top: 1px">
										<li><a href="${pfposDonut}" data-role="hint"
												data-hint-text="Chart-Positions"
												data-cls-hint="bg-white fg-darkorange drop-shadow"
												data-hint-position="bottom"><span
														class="mif-chart-pie icon mif fg-orange"></span></a></li>
								</ul> --%>

								<div class="multi-action">
										<button class="action-button rotate-minus bg-red fg-white"
												onclick="$(this).toggleClass('active')" data-role="hint"
												data-hint-text="Portfolio Actions"
												data-cls-hint="bg-red fg-white drop-shadow"
												data-hint-position="bottom">
												<span class="icon"><span class="mif-plus"></span></span>
										</button>
										<ul class="actions drop-right">
												<c:url var="pfposDonut" value="/pf/PFposDonut">
														<c:param name="pid" value="${pid}"></c:param>
												</c:url>

												<li class="bg-blue"><a href="${pfposDonut}"
														data-role="hint" data-hint-text="Chart-Positions"
														data-cls-hint="bg-cyan fg-white drop-shadow"
														data-hint-position="bottom"><span
																class="mif-chart-pie"></span></a></li>

												<%-- <li class="bg-orange"><a href="${listHoldings}"
														data-role="hint" data-hint-text="Holdings Statement"
														data-cls-hint="bg-orange fg-white drop-shadow"
														data-hint-position="bottom"><span
																class="mif-featured-play-list"></span></a></li>

												<li class="bg-teal"><a href="${listTxn}"
														data-role="hint" data-hint-text="Transactions Diary"
														data-cls-hint="bg-teal fg-white drop-shadow"
														data-hint-position="bottom"><span class="mif-library"></span></a></li> --%>

										</ul>
								</div>

						</div>

						<div class="cell-2 " style="padding-bottom: 30px">
								<div class="split-button" style="padding-top: 15px">
										<button class="button primary outline">Navigate</button>
										<button class="split dropdown-toggle primary rounded outline"></button>
										<ul class="d-menu" data-role="dropdown">
												<c:url var="backtoCurrPF" value="/pf/PFdetailsShow">
														<c:param name="pid" value="${pid}"></c:param>
												</c:url>

												<li><a href="${backtoCurrPF}"><span
																class="mif-backward icon mif fg-orange"></span>Back</a></li>
												<li class="divider"></li>
												<li><a
														href="${pageContext.request.contextPath}/pf/main"><span
																class="mif-database icon mif fg-cyan"></span>Portfolios</a></li>
												<li><a href="${pageContext.request.contextPath}/"><span
																class="mif-home icon mif fg-green"></span>Home</a></li>

										</ul>
								</div>
						</div>
				</div>

				<!-- Holdings Table Row-->
				<div class="row">

						<div style="margin-left: 50px;">

								<table class="table compact striped table-border row-hover mt-4"
										data-role="table" data-show-search="true"
										data-show-rows-steps="false" data-search-min-length="1"
										data-search-threshold="300" data-rows="20"
										data-search-fields="scCode"
										data-info-wrapper=".my-info-wrapper"
										data-pagination-wrapper=".my-pagination-wrapper">
										<thead>
												<tr>
														<th data-name="scCode" class="sortable-column sort-asc">Scrip
																Name</th>

														<th>Allocation</th>
														<th data-name="percentage"
																class="sortable-column sort-asc" data-format="number"
																data-cls-column=" bg-transparent fg-darkmauve text-center text-medium">%</th>
														<th data-name="totalInv" class="sortable-column sort-asc"
																data-show="true" data-format="money"
																data-cls-column=" bg-transparent fg-darkmauve text-center text-bold">
																Investments</th>
														<th data-name="units" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">#
																Units</th>

														<th>Avg. PPU</th>
														<th>Adj. PPU</th>


														<th data-name="totalDiv" class="sortable-column sort-asc"
																data-show="true" data-format="money"
																data-cls-column=" text-center">Dividend(s)</th>
														<th data-name="divY" class="sortable-column sort-asc"
																data-show="true" data-format="money"
																data-cls-column=" text-center">Dividend Yield</th>

														<th data-cls-column=" text-center">Action(s)</th>
												</tr>
										</thead>
										<tbody>
												<c:forEach var="holdingsI" items="${holdings}">
														<tr>
																<c:url var="showTradesforScrip"
																		value="/pf/showTradesforScrip">
																		<c:param name="pid" value="${pid}"></c:param>
																		<c:param name="scCode" value="${holdingsI.scCode}"></c:param>
																</c:url>
																<td style="text-align: center;"><a
																		href="${showTradesforScrip}" role="button"><h7
																						style="color: #4465a1">${holdingsI.scCode}</h7></a></td>
																<td style="text-align: center;">
																		<div data-role="progress" data-type="buffer"
																				data-cls-bar="bg-lightmauve"
																				data-cls-buffer="bg-white" data-small="true"
																				data-value="${holdingsI.perPF}" data-buffer="100"></div>
																</td>
																<td>${holdingsI.perPF}</td>
																<td>${holdingsI.totalInvestment}</td>
																<td>${holdingsI.numUnits}</td>
																<td>${holdingsI.avgPPU}</td>
																<td>${holdingsI.adjPPU}</td>

																<td>${holdingsI.totalDiv}</td>
																<td>${holdingsI.divY}</td>

																<td style="text-align: center;">
																		<!-- Prepare the Links for Toggle Button Here --> <c:url
																				var="buyStockinPF" value="/pf/buyStockinPF">
																				<c:param name="pid" value="${pid}"></c:param>
																				<c:param name="scCode" value="${holdingsI.scCode}"></c:param>
																		</c:url> <c:url var="sellStockinPF" value="/pf/sellStockinPF">
																				<c:param name="pid" value="${pid}"></c:param>
																				<c:param name="scCode" value="${holdingsI.scCode}"></c:param>
																		</c:url> <c:url var="adjdividend" value="/pf/adjDividend">
																				<c:param name="pid" value="${pid}"></c:param>
																				<c:param name="scCode" value="${holdingsI.scCode}"></c:param>
																		</c:url> <c:url var="adjbonus" value="/pf/adjBonus">
																				<c:param name="pid" value="${pid}"></c:param>
																				<c:param name="scCode" value="${holdingsI.scCode}"></c:param>
																		</c:url> <c:url var="adjsplit" value="/pf/adjsplit">
																				<c:param name="pid" value="${pid}"></c:param>
																				<c:param name="scCode" value="${holdingsI.scCode}"></c:param>
																		</c:url>

																		<ul class="t-menu open horizontal compact">
																				<li><a href="${buyStockinPF}" data-role="hint"
																						data-hint-text="Buy"
																						data-cls-hint="bg-orange fg-white drop-shadow"
																						data-hint-position="bottom"><span
																								class="mif-add-shopping-cart icon mif fg-orange"></span></a></li>
																				<li><a href="${sellStockinPF}" data-role="hint"
																						data-hint-text="Sell"
																						data-cls-hint="bg-darkcyan fg-white drop-shadow"
																						data-hint-position="bottom"><span
																								class="mif-shopping-basket2 icon mif fg-cyan"></span></a></li>
																				<li><a href="${adjdividend}" data-role="hint"
																						data-hint-text="Adjust Dividend(s)"
																						data-cls-hint="bg-red fg-white drop-shadow"
																						data-hint-position="bottom"><span
																								class="mif-book-reference icon mif fg-red"></span></a></li>

																				<li><a href="${adjsplit}" data-role="hint"
																						data-hint-text="Adjust Split"
																						data-cls-hint="bg-teal fg-white drop-shadow"
																						data-hint-position="bottom"><span
																								class="mif-flow-branch icon mif fg-teal"></span></a></li>

																				<li><a href="${adjbonus}" data-role="hint"
																						data-hint-text="Adjust Bonus"
																						data-cls-hint="bg-brown fg-white drop-shadow"
																						data-hint-position="bottom"><span
																								class="mif-magic-wand icon mif fg-brown"></span></a></li>


																		</ul>

																</td>




														</tr>

												</c:forEach>

										</tbody>
								</table>
								<p class="h3  text-center my-info-wrapper "></p>
								<div class="d-flex flex-justify-center my-pagination-wrapper"></div>


						</div>

				</div>
		</div>


		<!-- Metro 4 -->
		<script src="https://cdn.metroui.org.ua/v4.3.2/js/metro.min.js"></script>
</body>
</html>