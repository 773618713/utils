(function(angular) {
	console.log("angularConfig.begin");
	var app = angular.module('myApp', []);
	
	
	app.controller('MainController', function($scope) {
	    $scope.firstName = "John";
	    $scope.lastName = "Doe";
	});
	
	app.directive('helloWorld', function() {  
		console.log("加载一个模块");
		
	    require(['html2/module'],function(returnValue){
	    	console.log("returnValue");
	    	console.log(returnValue);
	       // $('.test1 p').html(a)
	    })
		
	    return {  
	        restrict: 'C',  
	        templateUrl: 'html2/main.html',
	        scope:true,
	        replace: false  
	    };
	});
	
	

	
	console.log("angularConfig.end");
})(window.angular);