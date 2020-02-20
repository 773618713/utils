<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>标题</title>
<base href="http://localhost/" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<link href="assets/js/bootstrap/3.3.4/css/bootstrap.min.css"
	rel="stylesheet">
<link href="assets/css/style.css" rel="stylesheet">
<link href="assets/css/bootstrap-switch.css" rel="stylesheet">
</head>
<body>
	<div class="ng-view" ng-app="app">
		<div
			style="text-align: center; margin-left: auto; margin-right: auto; margin-top: 200px;">
			<img alt="" src="assets/img/loading_1.gif"><br /> 如果您等待时间过长，<a
				href="javascript:window.location.reload()">请点击这里</a>
		</div>
	</div>
</body>

<script type="text/javascript"
	src="assets/js/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript"
	src="assets/js/angular/1.2.0/angular.min.js"></script>
<script type="text/javascript"
	src="assets/js/angular/1.2.0/angular-route.min.js"></script>
<script type="text/javascript" src="assets/js/util.js"></script>
<script data-main="config/loader.js"
	src="assets/js/require/require.min.js"></script>
<script type="text/javascript" src="config/app.js"></script>

</html>