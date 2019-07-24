
	//当前显示图片的下标
	var collapseIndex = 1;
	/*
	 * index 当前移动的下标  -1 为从左往右，0为从右往左
	 * cell 显示标签的宽度
	 * fullWidth 整个div的宽度
	 * SumImg 显示图片的张数
	 */
	function collapse(index, cell, fullWidth, SumImg) {
		if (index == 0) {
			index = collapseIndex + 1;
		} else if (index == -1) {
			index = collapseIndex - 1;
		}
		
		//到最后一张时重新开始
		if (index > 5) {
			index = 1;
		}
		if (index < 2) {
			index = 5;
		}

		collapseIndex = index;
		if ($(".collapseDiv > .d" + index).position().left < fullWidth / 2) {

			for (var i = index; i <= SumImg; i++) {
				$(".collapseDiv > .d" + i).animate({
					left : (fullWidth - (SumImg + 1 - i) * cell) + 'px'
				});
			}
		} else {
			for (var i = index; i > 0; i--) {
				$(".collapseDiv > .d" + i).animate({
					left : (i - 1) * cell + 'px'
				});
			}
		}
	}


var PAGE = (function() {
	
	var loadingShow = function($scope){
	    var loadingContainer = $("div.loading");
	    if (loadingContainer.length <= 0) {

	        loadingContainer = $("<div>", { Class: "loadingWhenSave" });
	        var img = $("<img>", { src: "https://cdn.sjedu.cn/img/loading.gif" });
	        img.css({
	        	paddingTop: $("#"+$scope.controllerName).height()/2
	        });
	        loadingContainer.html("");
	        loadingContainer.append(img).css({
	            position: "absolute", //"absolute",
	            zIndex: "9999",
	            textAlign: "center",
	            backgroundColor: "#fff",
	            opacity: "0.1",
	            top: "30%",
	            left: $("#"+$scope.controllerName).offset().left+200,
	            height: $("#"+$scope.controllerName).height(),
	            width: $("#"+$scope.controllerName).width()-400
	        });
	        //document.body.appendChild(loadingContainer);
	        loadingContainer.appendTo('body');
	    }
	    //$(loadingContainer).show();
	}
	
    var buildPage = function($scope,data) {
    	$(".loadingWhenSave").remove();
    	$scope.page = data.page;
    	if(undefined == $scope.page){
    		$scope.page = {};
    	}
    	$scope.page.current_num = data.data.length;
    	
    	if(data.data.length > 0){
    		$scope.page.current_index = 1;
    	}else{
    		$scope.page.current_index = 0;
    	}
    	$scope.page.first = ($scope.page.current-1) * $scope.page.number + $scope.page.current_index;
    	$scope.page.last = ($scope.page.current-1) * $scope.page.number + $scope.page.current_num;

    	$scope.pageArray = [];
    	if($scope.page.pages <= 10){
    		$scope.page.noprev = "hide";
    		$scope.page.nonext = "hide";
    		for(var i=1;i<$scope.page.current;i++){
    			$scope.pageArray.push({currentIndex:i});
    		}
    		$scope.pageArray.push({currentIndex:i,activeIndex:"active"});
    		for(var i=$scope.page.current+1;i<=$scope.page.pages;i++){
    			$scope.pageArray.push({currentIndex:i});
    		}
    	}else if($scope.page.current <= 5){
    		$scope.page.noprev = "hide";
    		for(var i=1;i<$scope.page.current;i++){
    			$scope.pageArray.push({currentIndex:i});
    		}
    		$scope.pageArray.push({currentIndex:$scope.page.current,activeIndex:"active"});
    		for(var i=$scope.page.current+1;i<=10;i++){
    			$scope.pageArray.push({currentIndex:i});
    		}
    	}else if($scope.page.current > $scope.page.pages-1){
    		$scope.page.nonext = "hide";
    		for(var i=$scope.page.current-4;i<$scope.page.current;i++){
    			$scope.pageArray.push({currentIndex:i});
    		}
    		$scope.pageArray.push({currentIndex:$scope.page.current,activeIndex:"active"});
    		for(var i=$scope.page.current+1;i<=$scope.page.pages;i++){
    			$scope.pageArray.push({currentIndex:i});
    		}
    	}else if($scope.page.current > $scope.page.pages-2){
    		$scope.page.nonext = "hide";
    		for(var i=$scope.page.current-3;i<$scope.page.current;i++){
    			$scope.pageArray.push({currentIndex:i});
    		}
    		$scope.pageArray.push({currentIndex:$scope.page.current,activeIndex:"active"});
    		for(var i=$scope.page.current+1;i<=$scope.page.pages;i++){
    			$scope.pageArray.push({currentIndex:i});
    		}
    	}else if($scope.page.current > $scope.page.pages-3){
    		$scope.page.nonext = "hide";
    		for(var i=$scope.page.current-2;i<$scope.page.current;i++){
    			$scope.pageArray.push({currentIndex:i});
    		}
    		$scope.pageArray.push({currentIndex:$scope.page.current,activeIndex:"active"});
    		for(var i=$scope.page.current+1;i<$scope.page.current+3;i++){
    			$scope.pageArray.push({currentIndex:i});
    		}
    	}else if($scope.page.current > 5){
    		for(var i=$scope.page.current-2;i<$scope.page.current;i++){
    			$scope.pageArray.push({currentIndex:i});
    		}
    		$scope.pageArray.push({currentIndex:$scope.page.current,activeIndex:"active"});
    		for(var i=$scope.page.current+1;i<$scope.page.current+3;i++){
    			$scope.pageArray.push({currentIndex:i});
    		}
    	}
    	$scope.$apply();
    }
    
    var iniNullPage = function($scope) {
    	if($scope.controllerName==null || $scope.controllerName == ""){
    		console.log("$scope.controllerName is null");
    	}
    	
    	$scope.paging = function(turning){
    		switch (turning) {
    		case 'U':
    			if($scope.page.current > 1){
    				$scope.page.current = $scope.page.current - 1;
    				loadingShow($scope);
                	$scope.find();
    			}else{
    				$scope.page.noprev = "hide";
    			}
    			break;
    		case 'D':
    			if($scope.page.current < $scope.page.pages){
    				$scope.page.current = $scope.page.current + 1;
    				loadingShow($scope);
                	$scope.find();
    			}else{
    				$scope.page.nonext = "hide";
    			}
    			break;
    		case 'S':
    			$scope.page.current = 1;
    			loadingShow($scope);
            	$scope.find();
    			break;
    		case 'E':
    			$scope.page.current = $scope.page.pages;
    			loadingShow($scope);
            	$scope.find();
    			break;
    		case 'G':
    			loadingShow($scope);
            	$scope.find();
    			break;

    		default:
    			$scope.page.current = turning;
    			loadingShow($scope);
        		$scope.find();
    		}
    		
        }
        
        $scope.order = function(target){
        	var cls = target.getAttribute('class');
        	$scope.page.column = target.getAttribute('column');
        	
        	if("sorting" == cls){
            	$("#"+$scope.controllerName+" .sorting_asc").attr("class","sorting");
            	$("#"+$scope.controllerName+" .sorting_desc").attr("class","sorting");
        		target.setAttribute('class','sorting_asc');
        		$scope.page.order = "asc";
        	}else if("sorting_asc" == cls){
        		target.setAttribute('class','sorting_desc');
        		$scope.page.order = "desc";
        	}else if("sorting_desc" == cls){
        		target.setAttribute('class','sorting');
        		$scope.page.order = "";
        		$scope.page.column = null;
        	}
        	loadingShow($scope);
        	$scope.find();
        }
        
        $scope.checkAll = function(target){
        	$("#"+$scope.controllerName+" input[name='dataPk']").each(function(){ 
        		if($(this).prop("checked")){
        			//$(this).removeAttr("checked")
        			$(this).prop('checked',false);
            	}else{
            		//$(this).attr("checked","checked")
            		$(this).prop('checked',true);
            	}
        	});
        	
        	
        	/*
        	if("checked" == $("#"+$scope.controllerName+" .isCheckll").attr("checked")){
        		$("#"+$scope.controllerName+" input[type='checkbox']").attr("checked","checked")
        	}else{
        		$("#"+$scope.controllerName+" input[type='checkbox']").removeAttr("checked")
        	}*/
        	
        }
        
        $scope.enterKeyDownPage = function(event){
        	if(event.keyCode == "13")    
            {
        		$scope.page.current = 1;
        		loadingShow($scope);
        		$scope.find();
            }
        }
        
        $scope.enterKeyDownNum = function(event){
        	if(event.keyCode == "13")    
            {
        		loadingShow($scope);
        		$scope.find();
            }
        }
        
        //loadingShow($scope);
      
        if(undefined == $scope.page){
        	$scope.page = {};
        	
        	/*
        	var height = $("#sc_ids .contentModalBody").height() - $("#"+$scope.controllerName).height() - 50;
        	
        	$scope.page.number = Math.floor(height/155)*5;
        	if($scope.page.number<10){
        		$scope.page.number = 10;
        	}
        	
        	/*
        	var height = $("#sc_ids .contentModalBody").height()-150;
        	console.log(height);
        	if(height < 400){
        		$scope.page.number = 10;
        	}else if(height < 600 && height >=400){
        		$scope.page.number = 15;
        	}else if(height < 800 && height >=600){
        		$scope.page.number = 20;
        	}else if(height < 1000 && height >=800){
        		$scope.page.number = 25;
        	}else if(height < 1200 && height >=1000){
        		$scope.page.number = 30;
        	}else if(height < 1400 && height >=1200){
        		$scope.page.number = 35;
        	}else if(height >=1400){
        		$scope.page.number = 40;
        	}else{
        		$scope.page.number = ($("#sc_ids .contentModalBody").height()-100)/40;
        	}*/
        	
        	
        }
        
        
        //$scope.find();
    	       
    }
    
    var iniPage = function($scope){
    	loadingShow($scope);
    	iniNullPage($scope);
    	$scope.find();
    }
    
    return{  
    	loadingShow:loadingShow,
    	buildPage:buildPage,  
    	iniNullPage:iniNullPage,
    	iniPage:iniPage  
    }; 
       
})();
