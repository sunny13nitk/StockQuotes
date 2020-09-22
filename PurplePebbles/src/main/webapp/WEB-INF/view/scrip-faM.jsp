<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
		content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>Scrips Universe</title>
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
						<div class="cell-10">
								<h3 style="color: #4465a1">
										SCRIPS UNIVERSE <small style="color: #4465a1">All
												Ratios Computed for last </small> <small
												style="color: #4465a1; font-weight: bold;">${currDur}</small>
								</h3>
								<div class="row">
										<div class="cell-10">
												<div class="remark primary"
														style="padding-top: 10px; margin: 2px; color: #4465a1; font-weight: medium;">
														Snapshot of our Scrips Universe for Average Computed
														values. All figures in CAGR % or Average as Applicable.
														All amounts in Rs. Crores.</div>

										</div>
										<div class="cell-2">
												<button class="button pink cycle outline"
														onclick="$('#t-inspector').data('table').toggleInspector()"
														data-role="hint" data-hint-text="Personalize Table View"
														data-hint-position="bottom">
														<span class="mif-eye mif-2x fg-pink"></span>
												</button>

												<a class="button alert cycle outline" data-role="hint"
														data-hint-position="bottom" data-hint-text="Back"
														data-cls-hint="bg-red fg-white drop-shadow"
														style="margin-left: 3px"
														href="${pageContext.request.contextPath}/scrips/main">
														<span class="mif-backward icon mif-1x fg-alert"></span>
												</a>
										</div>

								</div>


						</div>

						<%-- 		<!-- Holdings Buttons- Charts etc -->
						<div class="cell-3">

								<div class="multi-action">
										<button class="action-button rotate-minus bg-red fg-white"
												onclick="$(this).toggleClass('active')" data-role="hint"
												data-hint-text="Actions"
												data-cls-hint="bg-red fg-white drop-shadow"
												data-hint-position="bottom">
												<span class="icon"><span class="mif-plus"></span></span>
										</button>
										<ul class="actions drop-right">
												<c:url var="pfposDonut" value="/pf/PFposDonut">
														<c:param name="pid" value="${pid}"></c:param>
												</c:url>

												<li class="bg-blue"><a href="${pfposDonut}"
														data-role="hint" data-hint-text="Charts"
														data-cls-hint="bg-cyan fg-white drop-shadow"
														data-hint-position="bottom"><span
																class="mif-chart-pie"></span></a></li>

												<li class="bg-orange"><a href="${listHoldings}"
														data-role="hint" data-hint-text="Holdings Statement"
														data-cls-hint="bg-orange fg-white drop-shadow"
														data-hint-position="bottom"><span
																class="mif-featured-play-list"></span></a></li>

												<li class="bg-teal"><a href="${listTxn}"
														data-role="hint" data-hint-text="Transactions Diary"
														data-cls-hint="bg-teal fg-white drop-shadow"
														data-hint-position="bottom"><span class="mif-library"></span></a></li>

										</ul>
								</div>

						</div> --%>

						<div class="cell-2 " style="padding-bottom: 30px">


								<ul class="t-menu open horizontal compact open  fg-white"
										style="background: #6699CC">
										<c:forEach var="durationI" items="${durations}">
												<c:url var="scripsfa" value="/scrips/fa">
														<c:param name="did" value="${durationI.duration}"></c:param>
												</c:url>
												<li style="margin-right: 5px"><a href="${scripsfa}">
																${durationI.duration} </a></li>
										</c:forEach>
								</ul>


								<%-- <form:form
										action="${pageContext.request.contextPath}/scrips/fap"
										modelAttribute="selDur" class="form-horizontal" method="POST">

										<div class="form-group" style="padding: 5px">
												<div>
														<label style="color: #274472">Compare for Duration</label>


												</div>
												<div class="row">

														<div class="cell-8">
																<form:select path="Duration" class="input-small info"
																		data-role="select" data-cls-option="fg-cyan"
																		clearButton="false"
																		data-cls-selected-item="bg-teal fg-white"
																		data-cls-selected-item-remover="bg-darkTeal fg-white">
																		<form:options items="${durations}" var="durationI"
																				itemValue="id" itemLabel="duration" />
																</form:select>
														</div>
														<div class="cell-4">
																<div class=row>
																		<button class="button info cycle outline small"
																				data-role="hint" data-hint-position="bottom"
																				data-hint-text="Go"
																				data-cls-hint="bg-cyan fg-white drop-shadow">
																				<span class="mif-checkmark"></span>
																		</button>

																		<a class="button alert cycle outline small"
																				data-role="hint" data-hint-position="bottom"
																				data-hint-text="Back"
																				data-cls-hint="bg-red fg-white drop-shadow"
																				style="margin-left: 3px"
																				href="${pageContext.request.contextPath}/scrips/main">
																				<span class="mif-home icon mif-1x fg-alert"></span>
																		</a>

																</div>
														</div>
												</div>



										</div>



								</form:form> --%>
						</div>
				</div>

				<!-- Scrips BA Table Row-->
				<div class="row">

						<div style="margin-left: 10px;">



								<table class="table compact striped table-border row-hover mt-4"
										id="t-inspector" data-role="table" data-show-search="true"
										data-show-rows-steps="false" data-search-min-length="1"
										data-horizontal-scroll="true" data-thousand-separator=","
										data-search-threshold="300" data-rows="-1"
										data-search-fields="SCCode, Sector"
										data-horizontal-scroll="true"
										data-info-wrapper=".my-info-wrapper"
										data-thousand-separator=","
										data-pagination-wrapper=".my-pagination-wrapper">
										<thead>
												<tr>

														<th data-name="SCCode" class="sortable-column sort-asc">Scrip
																Name</th>

														<th data-name="Sector" class="sortable-column sort-asc">Sector
														</th>

														<th data-name="UPH" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">
																P Hold.</th>


														<th data-name="SalesGR" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">Rev</th>

														<th data-name="PATGR" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">PAT
														</th>

														<th data-name="OPM_Avg" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">OPM
														</th>

														<th data-name="TTMSales" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">TTM
																Rev</th>


														<th data-name="PEGR" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">PEGR
														</th>


														<th data-name="INT_DEP_BY_SALES_Avg"
																class="sortable-column sort-asc" data-format="number"
																data-cls-column=" text-center">Int+Dep/Rev</th>

														<th data-name="WCCycle_Avg"
																class="sortable-column sort-asc" data-format="number"
																data-cls-column=" text-center">WCC/Rev</th>

														<th data-name="SSGRAvg" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">SSGR</th>

														<th data-name="DivYield" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">Div.
																Yield</th>

														<th data-name="DivGR" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">Div.
																Gr.</th>

														<th data-name="ROCEAvg" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">
																ROCE</th>

														<th data-name="ROEAvg" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">
																ROE</th>

														<th data-name="FCF_CFO_Avg"
																class="sortable-column sort-asc" data-format="number"
																data-cls-column=" text-center">FCF/CFO</th>



														<th data-name="CurrPE" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">Cur.
																PE</th>

														<th data-name="PEG" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">PEG</th>

														<th data-name="MCapToSales"
																class="sortable-column sort-asc" data-format="number"
																data-cls-column=" text-center">MCap/Rev</th>

														<th data-name="CPS" class="sortable-column sort-asc"
																data-show="false" data-format="number"
																data-cls-column=" text-center">CPS</th>

														<th data-name="DERatio" class="sortable-column sort-asc"
																data-format="number" data-cls-column=" text-center">D/E</th>


												</tr>
										</thead>
										<tbody>
												<c:forEach var="scripsBAI" items="${scripsBA}">

														<tr>

																<c:url var="scripDetails"
																		value="/scrips/showScripDetails">
																		<c:param name="scCode" value="${scripsBAI.SCCode}"></c:param>
																</c:url>
																<td style="text-align: center;"><a
																		href="${scripDetails}" role="button"><h7
																						style="color: #4465a1">${scripsBAI.SCCode}</h7></a></td>

																<c:url var="secOVW" value="/scrips/showSector">
																		<c:param name="sector" value="${scripsBAI.sector}"></c:param>
																</c:url>
																<td style="text-align: center;"><a href="${secOVW}"
																		role="button"><h7 style="color: #4465a1">${scripsBAI.sector}</h7></a></td>


																<td>${scripsBAI.UPH}</td>
																<td>${scripsBAI.salesGR}</td>
																<td>${scripsBAI.PATGR}</td>
																<td>${scripsBAI.OPM_Avg}</td>
																<td>${scripsBAI.TTMSales}</td>
																<td>${scripsBAI.PEGR}</td>
																<td>${scripsBAI.INT_DEP_BY_SALES_Avg}</td>
																<td>${scripsBAI.WCCycle_Avg}</td>
																<td>${scripsBAI.SSGRAvg}</td>
																<td>${scripsBAI.divYield}</td>
																<td>${scripsBAI.divGR}</td>

																<td>${scripsBAI.ROCEAvg}</td>
																<td>${scripsBAI.ROEAvg}</td>
																<td>${scripsBAI.FCF_CFO_Avg}</td>
																<td>${scripsBAI.currPE}</td>
																<td>${scripsBAI.PEG}</td>
																<td>${scripsBAI.MCapToSales}</td>
																<td>${scripsBAI.CPS}</td>
																<td>${scripsBAI.DERatio}</td>
														</tr>

												</c:forEach>

										</tbody>
								</table>

								<p class="h5  text-center my-info-wrapper "></p>
								<div class="d-flex flex-justify-center my-pagination-wrapper"></div>


						</div>

				</div>
		</div>


		<!-- Metro 4 -->
		<script src="https://cdn.metroui.org.ua/v4.3.2/js/metro.min.js"></script>
</body>
</html>