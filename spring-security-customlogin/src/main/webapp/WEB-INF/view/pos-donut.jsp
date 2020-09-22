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

<script
		src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js"></script>


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

						<!-- <div class="cell-10">
								<h3 style="color: #4465a1">My Holdings- Chart View</h3>
						</div>
 -->
						<!-- Navigation Buttons -->
						<div class="cell-2 offset-10" style="padding-bottom: 30px">
								<div class="split-button" style="padding-top: 15px">
										<button class="button primary outline">Navigate</button>
										<button class="split dropdown-toggle primary rounded outline"></button>
										<ul class="d-menu" data-role="dropdown">
												<c:url var="backtoHoldings" value="/pf/listHoldings">
														<c:param name="pid" value="${pid}"></c:param>
												</c:url>
												<c:url var="backtoCurrPF" value="/pf/PFdetailsShow">
														<c:param name="pid" value="${pid}"></c:param>
												</c:url>

												<li><a href="${backtoHoldings}"><span
																class="mif-backward icon mif fg-orange"></span>Back</a></li>
												<li><a href="${backtoCurrPF}"><span
																class="mif-import-contacts icon mif fg-pink"></span>Curr.
																PF</a></li>
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

				<!-- Donut Chart-->

				<div class="row" style="margin-top: -10px">
						<div class="cell-7">
								<div class="row">

										<h4
												style="padding-top: 20px; color: #4465a1; padding-left: 60px">
												Portfolio Holdings by Weight (%)</h4>
										<div style="margin: 0 auto; height: 50vh; width: 50vw;">
												<canvas id="pfDonut"></canvas>
										</div>

								</div>

								<div class="row">

										<h4
												style="padding-top: 20px; color: #4465a1; padding-left: 60px">
												Portfolio Holdings by Value (Rs.)</h4>
										<div style="margin: 0 auto; height: 50vh; width: 50vw;">
												<canvas id="pfValBar"></canvas>
										</div>

								</div>
						</div>
						<!-- Small Table -->
						<div class="cell-5">

								<div class="card" style="padding-top: -20px; margin-right: 40px">
										<div class="card-content p-2">

												<h6
														style="color: #325254; background-color: #faf0ff; padding: 10px">
														Portfolio Holdings Summary</h6>
										</div>

										<table class="table compact striped">
												<thead>
														<tr>
																<th style="text-align: center;">Scrip</th>
																<th style="text-align: center;">Allocation</th>
																<th style="text-align: center;">%</th>
																<th style="text-align: center;">Amount (Rs.)</th>

														</tr>
												</thead>
												<tbody>
														<c:forEach var="holding" items="${holdings}">
																<tr>
																		<td style="text-align: center;">${holding.scCode}</td>
																		<td style="text-align: center;">
																				<div data-role="progress" data-type="buffer"
																						data-cls-bar="bg-lightmauve"
																						data-cls-buffer="bg-white" data-small="true"
																						data-value="${holding.perPF}" data-buffer="100"></div>
																		</td>
																		<td>${holding.perPF}</td>


																		<td style="text-align: center;">${holding.totalInvestment}</td>

																</tr>

														</c:forEach>


												</tbody>
										</table>


								</div>
						</div>


						<!-- Donut Script  -->
						<script>
														var ctx = document.getElementById('pfDonut').getContext(
																	'2d');
														   	
											   		   	function dynamicColors() 
											   		   	       {
															   	    var r = Math.floor(Math.random() * 255);
															   	    var g = Math.floor(Math.random() * 255);
															   	    var b = Math.floor(Math.random() * 255);
															   	    return "rgba(" + r + "," + g + "," + b + ", 0.5)";
															   	}
															   	
													   function poolColors(a) 
															   	{
															   	    var pool = [];
															   	    for(i = 0; i < a; i++)
															   	    {
															   	        pool.push(dynamicColors());
															   	    }
															   	    return pool;
															   	}
								   				   
			
													   	function getColors(length){
													   	    let pallet = ["#DC7633", "#34495E", "#839192","#16A085", "#76D7C4", "#2980B9", "#E74C3C", "#76448A", "#996633", "#ED8216", "#18EDE5", "#A397D7", "#F012BE", "#ffdead", "#4e5c87", "#b0c4de"];
													   	    let colors = [];
								
													   	    for(let i = 0; i < length; i++) {
													   	      colors.push(pallet[i % pallet.length]);
													   	    }
								
													   	    return colors;
													   	  }   	    
								   	  
						         
												         
														var pfDonut = new Chart(ctx, {
															type : 'doughnut',
															data : {
																
																labels: ${datapfwt.keySet()},
																datasets : [{
															   				    data: ${datapfwt.values()},
															   				    backgroundColor: getColors(${datapfwt.values()}.length),
															   			    	borderColor: getColors(${datapfwt.values()}.length),
															   			        hoverBorderColor: getColors(${datapfwt.values()}.length),
															   				 
															   				   /* backgroundColor: poolColors(${datapfwt.values()}.length),
															   			    	borderColor: poolColors(${datapfwt.values()}.length),
															   			        hoverBorderColor: poolColors(${datapfwt.values()}.length),
															   			        borderWidth : 1*/
									
																
																} ]
															},
															options: {
													            		responsive: true,
															            title:
															            {
															                display: false,
															                text: "Portfolio Holdings by Percentage",
															                fontSize: 16,
															                fontColor:'#2b4595',
															                position: 'bottom'
														        			                
															            },
													            	   cutoutPercentage: 65,
													           		  legend: 
													           		  {
													            		display: true,
													            		position: 'left',
													            					            	
													            	  }
													            	  
															}
													        
														});	      
			       							  </script>

						<!-- PF Value Bar Chart Script  -->
						<script>
														var ctx = document.getElementById('pfValBar').getContext(
																	'2d');
														   	
											   		  
			
													   	function getColors(length){
													   		let pallet = ["#DC7633", "#34495E", "#839192","#16A085", "#76D7C4", "#2980B9", "#E74C3C", "#76448A", "#996633", "#ED8216", "#18EDE5", "#A397D7", "#F012BE", "#ffdead", "#4e5c87", "#b0c4de"];
													   	    let colors = [];
								
													   	    for(let i = 0; i < length; i++) {
													   	      colors.push(pallet[i % pallet.length]);
													   	    }
								
													   	    return colors;
													   	  }   	    
								   	  
						         
												         
														var pfValBar = new Chart(ctx, {
															type : 'horizontalBar',
															data : {
																
																labels: ${datapfval.keySet()},
																datasets : [{
															   				    data: ${datapfval.values()},
															   					backgroundColor: getColors(${datapfwt.values()}.length),
															   			    	borderColor: getColors(${datapfval.values()}.length),
															   			        hoverBorderColor: getColors(${datapfval.values()}.length),
															   			        label: "Investments (Rs.)",
															   				   /* backgroundColor: poolColors(${datapfwt.values()}.length),
															   			    	borderColor: poolColors(${datapfwt.values()}.length),
															   			        hoverBorderColor: poolColors(${datapfwt.values()}.length),
															   			        borderWidth : 1*/
									
																
																} ]
															},
															options: {
															      legend: { display: false },
															      title: {
															        display: false,
															        text: 'Predicted world population (millions) in 2050'
															      }
															    }
													        
														});	      
			       							  </script>

				</div>

		</div>


		<!-- Metro 4 -->
		<script src="https://cdn.metroui.org.ua/v4.3.2/js/metro.min.js"></script>
</body>

</html>
