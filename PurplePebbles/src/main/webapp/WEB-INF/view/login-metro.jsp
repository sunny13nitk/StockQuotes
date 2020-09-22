<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
		uri="http://www.springframework.org/security/tags"%>


<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
		content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>My App - Login Form</title>
<!-- Metro 4 -->
<link rel="stylesheet"
		href="https://cdn.metroui.org.ua/v4.3.2/css/metro-all.min.css">
<style>
.login-form {
		width: 350px;
		height: auto;
		top: 50%;
		margin-top: -160px;
		background-color: #EEE6F2
}
</style>
</head>

<body class="h-vh-100 bg-white">

		<form:form
				action="${pageContext.request.contextPath}/authenticateUser"
				method="POST"
				class="login-form  p-6 mx-auto border bd-default win-shadow"
				data-role="validator" data-clear-invalid="2000"
				data-on-error-form="invalidForm"
				data-on-validate-form="validateForm">
				<span class="mif-vpn-lock mif-4x place-right fg-mauve"
						style="margin-top: -10px;"></span>
				<h2 class="text-light">Login to app</h2>
				<hr class="thin mt-4 mb-4 bg-white">
				<div class="form-group">
						<input type="text" data-role="input" name="username"
								placeholder="username" data-prepend="<span class='mif-user'>">
				</div>
				<div class="form-group">
						<input type="password" data-role="input" name="password"
								data-prepend="<span class='mif-key'>"
								placeholder="Enter your password..."
								data-validate="required minlength=6">
				</div>
				<div class="form-group mt-10">

						<button class="button"
								style="background-color: #C3A6F7; color: black">Logon</button>

						<a
								href="${pageContext.request.contextPath}/register/showRegistrationForm"
								class="button" role="button" aria-pressed="true"
								style="background-color: #E7D2F1; color: black"> Register
								New User </a>


				</div>
		</form:form>

		<script src="../../metro/js/metro.js"></script>
		<script>
			function invalidForm() {
				var form = $(this);
				form.addClass("ani-ring");
				setTimeout(function() {
					form.removeClass("ani-ring");
				}, 1000);
			}

			function validateForm() {
				$(".login-form").animate({
					opacity : 0
				});
			}
		</script>

		<!-- Metro 4 -->
		<script src="https://cdn.metroui.org.ua/v4.3.2/js/metro.min.js"></script>
</body>