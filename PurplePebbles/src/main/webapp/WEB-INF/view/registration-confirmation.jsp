<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>

<title>Insert title here</title>
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
<body>

		<div>

				<div id="Confirmationbox" style="margin-top: 50px;"
						class="mainbox col-md-3 col-md-offset-2 col-sm-6 col-sm-offset-2">

						<div class="panel panel-info">

								<div class="panel-heading">
										<div class="panel-title">Registration Confirmation</div>
								</div>

								<div class="alert alert-success">
										<strong>Success!</strong> User Registration for ${username}
										successful!
								</div>

						</div>

						<div>
								<a href="${pageContext.request.contextPath}/"
										class="btn btn-primary" role="button" aria-pressed="true">Back
										to Login Page</a>
						</div>

				</div>
		</div>


</body>
</html>