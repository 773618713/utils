<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>标题</title>
</head>
<body>
	<div ng-app="myApp" ng-controller='MainController'>
		<div class='helloWorld'></div>
		<input type="text" ng-model="firstName">  
		<!-- <div ng-include="'html2/main.html'"></div> -->
	</div>
	<script type="text/javascript" src="assets/js/jquery/1.11.3/jquery.min.js"></script>
	<script type="text/javascript" src="assets/js/angular/1.2.0/angular.min.js"></script>
	<script type="text/javascript" src="assets/js/angular/1.2.0/angular-route.min.js"></script>
	<script src="config1/js/require/require.js"></script>
	<script src="config1/requireConfig.js"></script>
	<script src="config1/angularJsConfig.js"></script>
</body>
</html>