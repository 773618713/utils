/*
* Modifition History
* ==================
* Ver  	Modified By  Date        Description(TT/SR/Project)
* ---  	-----------  ----------  ------------------------------ 
* 2.1   QiuMing   	 2015/05/29  邮箱地址输入需为小写，身份证号字母需为大写。
* 2.3   QiuMing   	 2015/05/30  邮箱地址输入允许为小写。
*
*/

var VALIDATE = (function() {
	/*
	根据〖中华人民共和国国家标准 GB 11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
	    地址码表示编码对象常住户口所在县(市、旗、区)的行政区划代码。
	    出生日期码表示编码对象出生的年、月、日，其中年份用四位数字表示，年、月、日之间不用分隔符。
	    顺序码表示同一地址码所标识的区域范围内，对同年、月、日出生的人员编定的顺序号。顺序码的奇数分给男性，偶数分给女性。
	    校验码是根据前面十七位数字码，按照ISO 7064:1983.MOD 11-2校验码计算出来的检验码。

	出生日期计算方法。
	    15位的身份证编码首先把出生年扩展为4位，简单的就是增加一个19或18,这样就包含了所有1800-1999年出生的人;
	    2000年后出生的肯定都是18位的了没有这个烦恼，至于1800年前出生的,那啥那时应该还没身份证号这个东东，⊙﹏⊙b汗...
	下面是正则表达式:
	 出生日期1800-2099  (18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])
	 身份证正则表达式 /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i            
	 15位校验规则 6位地址编码+6位出生日期+3位顺序号
	 18位校验规则 6位地址编码+8位出生日期+3位顺序号+1位校验位
	 
	 校验位规则     公式:∑(ai×Wi)(mod 11)……………………………………(1)
	                公式(1)中： 
	                i----表示号码字符从由至左包括校验码在内的位置序号； 
	                ai----表示第i位置上的号码字符值； 
	                Wi----示第i位置上的加权因子，其数值依据公式Wi=2^(n-1）(mod 11)计算得出。
	                i 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1
	                Wi 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 1

	*/
	//身份证号合法性验证 
	//支持15位和18位身份证号
	//支持地址编码、出生日期、校验位验证
    var IdentityCodeValid = function(code) { 
        var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
        var tip = "";
        var pass= true;
        
        var regCard = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if(!code || !regCard.test(code)){
            tip = "身份证号格式错误";
            pass = false;
        }
        
       else if(!city[code.substr(0,2)]){
            tip = "地址编码错误";
            pass = false;
        }
        else{
            //18位身份证需要验证最后一位校验位
            if(code.length == 18){
                code = code.split('');
                //∑(ai×Wi)(mod 11)
                //加权因子
                var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
                //校验位
                var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
                var sum = 0;
                var ai = 0;
                var wi = 0;
                for (var i = 0; i < 17; i++)
                {
                    ai = code[i];
                    wi = factor[i];
                    sum += ai * wi;
                }
                var last = parity[sum % 11];
                if(parity[sum % 11] != code[17]){
                    tip = "校验位错误";
                    pass =false;
                }
            }
        }
        if(!pass) alert(tip);
        return pass;
    }
    
    var  checkPost = function(str){ //邮政编码判断
		var pattern = /^[1-9]{1}[0-9]{5}$/;
		if (pattern.test(str)) {
			return true;
	    }else {
	    	return false;
	    }
	}

    
    var IdentityCodeRepeatValid = function(obj) { 
    	var code = obj.val();
    	if(IdentityCodeValid(code)){
    		
    		$.post("datas/UserTeacher_cardValidate_data.json", {
                    "data": code
            }, function(data) {
            	console.log(data);
    			if(data.code != '0000'){
    				return false;
            	}else{
                    if(data.data){
                    	
            				$(obj).parent().children('span').addClass("hide");
            				$(obj).parent().removeClass("has-error");
            				$(obj).parent().addClass("has-success");
            				$(obj).parent().append('<span class="glyphicon glyphicon-ok form-control-feedback"></span>');
            			
                    }
            	}
    		},"json").error(function() {
    			return false;
    		});
    		
    	}else{
    		
    		return false;
    	}
    }
    
    var validateCode = function(obj) { 
    	var code = obj.val();
    	if(code > 999){
    		
    		$.post("datas/Util_validateCode_data.json", {
                    "data": code
            }, function(data) {
            	console.log(data);
    			if(data.code != '0000'){
    				return false;
            	}else{
                    
    				$(obj).parent().children('span').addClass("hide");
    				$(obj).parent().removeClass("has-error");
    				$(obj).parent().addClass("has-success");
    				$(obj).parent().append('<span class="glyphicon glyphicon-ok form-control-feedback"></span>');
            		
            	}
    		},"json").error(function() {
    			return false;
    		});
    		
    	}else{
    		
    		return false;
    	}
    }
    
    //全部是中文,并且长度至少为2
    var chinaDocValidate = function(str){
    	var reg = /^[\u4E00-\u9FA5]+$/; 
    	var isPass = reg.test(str);
    	if(isPass && str.length > 1){ 
	    	return true; 
    	}else{
    		return false;
    	}
    }
    
    /*
     *  验证规则：姑且把邮箱地址分成“第一部分@第二部分”这样
		第一部分：由字母、数字、下划线、短线“-”、点号“.”组成，
		第二部分：为一个域名，域名由字母、数字、短线“-”、域名后缀组成，
		而域名后缀一般为.xxx或.xxx.xx，一区的域名后缀一般为2-4位，如cn,com,net，现在域名有的也会大于4位
     */
    var emailCheck = function(str) {
    	var pattern = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
    	if (!pattern.test(str)) {
    		return false;
    	}
    	
    	//2.3 start
    	/*2.1 start
    	if (/[A-Z]/.test(str)) {
    		return false;
    	}
    	2.1 end*/
    	//2.3 end
    	
    	return true;
    }
    
    /*
     *  验证规则：长度大于等于6
		= /^(?!.*([a-z]{3,}|\d{3,}).*$)/i;
     */
    var passwordCheck = function(obj) {
    	var str = obj.val();
    	
    	 
    	if(str.length<6){
    		obj.attr("data-content","密码长度必须大于6位！");
    		obj.popover('show');
    		return false;
    	}else if(str.length > 20){
    		obj.attr("data-content","密码长度必须小于20位！");
    		obj.popover('show');
    		return false;
    	}
    	
        //正则表达式验证符合要求的
    	var modes = 0;
        if (/\d/.test(str)) modes++; //数字
        if (/[a-z]/.test(str)) modes++; //小写
        if (/[A-Z]/.test(str)) modes++; //大写  
        if (/\W/.test(str)) modes++; //特殊字符
        
        //逻辑处理
        if (modes < 3) {
        	obj.attr("data-content","密码必须大写字母、小写字母、数字，特殊字符中任意三种！");
    		obj.popover('show');
    		return false;
        }
    	
    	var re = /(\w)*(\w)\2{2}(\w)*/g;
        if(re.test(str)){
        	obj.attr("data-content","密码不能有三个连续相同的字母或数字！");
    		obj.popover('show');
     	   return false;
        }
        
        var isT = false;
   	    var attrt = str.split('');
        for(var i=1;i<attrt.length-1;i++){
       	 var code_up = attrt[i-1].charCodeAt();
       	 var code = attrt[i].charCodeAt();
       	 var code_next = attrt[i+1].charCodeAt();
       	 
       	 if(code_up+1==code && code == code_next-1){
       		 isT=true;
       		 break;
       	 }
       	 
       	 if(code_up-1==code && code == code_next+1){
       		 isT=true;
       		 break;
       	 }
       	 
        }
        
        if(isT){
       	obj.attr("data-content","密码不能有3个及以上连续的数字或字母!");
    		obj.popover('show');
    		return false;
        }
        
        
       
        obj.attr("data-content","");
		obj.popover('hide');
        return true;
    	
        
    }
    
    /*
     * 验证电话号码
		验证规则：区号+号码，区号以0开头，3位或4位
		号码由7位或8位数字组成
		区号与号码之间可以无连接符，也可以“-”连接
		如01088888888,010-88888888,0955-7777777 
     */
    var checkTel = function(str){
       var re = /^0\d{2,3}-?\d{7,8}$/;
       if(re.test(str)){
    	   return true;
       }else{
    	   return false;
       }
   }
    
   /*
    * 验证手机号码
	  验证规则：11位数字，以1开头。
    */
   var checkPhone = function(str) {
       var re = /^1\d{10}$/
       if (re.test(str)) {
    	   return true;
       } else {
    	   return false;
       }
   }

   
    var validating = function(obj) {
    		var validateNull = obj.attr("not-null");
    		if("yes" == validateNull){
    			if(obj.val() == null || obj.val() == ""){
    				return "error";
    			}
    		}else if(obj.val() == null || obj.val() == ""){
    			return "none";
    		}
    	
    	    var validateType = obj.attr("ng-validate");
    	    var isValidate = "pass";

    	    switch (validateType) {
    		case 'idcard':
    			if(IdentityCodeValid(obj.val())){
    				isValidate = "pass";
    			}else{
    				isValidate = "error";
    			}
    			break;
    		case 'idcardrepeat':
    			IdentityCodeRepeatValid(obj)
    				isValidate = "error";
    			break;
    		case 'validateCode':
    			validateCode(obj)
    				isValidate = "none";
    			break;
    		case 'cname':
    			if(chinaDocValidate(obj.val())){
    				isValidate = "pass";
    			}else{
    				isValidate = "error";
    			}
    			break;
    		case 'validatePost':
    			if(checkPost(obj.val())){
    				isValidate = "pass";
    			}else{
    				isValidate = "error";
    			}
    			break;
    		case 'email':
    			if(emailCheck(obj.val())){
    				isValidate = "pass";
    			}else{
    				isValidate = "error";
    			}
    			break;
    		case 'password':
    			if(passwordCheck(obj)){
    				isValidate = "pass";
    			}else{
    				isValidate = "error";
    			}
    			break;
    		case 'tel':
    			if(checkTel(obj.val())){
    				isValidate = "pass";
    			}else{
    				isValidate = "error";
    			}
    			break;
    		case 'phone':
    			if(checkPhone(obj.val())){
    				isValidate = "pass";
    			}else{
    				isValidate = "error";
    			}
    			break;

    		default:
    			isValidate="none";
    		}
    	    return isValidate;
    }
    
	
	var iniValidate = function($scope){
		$('#'+$scope.controllerName+' input[type="text"]').popover({trigger:"focus"});
		
		$('#'+$scope.controllerName+' input[not-null="yes"]').parent().append('<span style="color:#f00;top:6px;font-family:sans-serif;font-size: 24px;" class="glyphicon glyphicon-asterisk form-control-feedback"></span>');
		$('#'+$scope.controllerName+' select[not-null="yes"]').parent().append('<span style="color:#f00;top:6px;font-family:sans-serif;font-size: 24px;" class="glyphicon glyphicon-asterisk form-control-feedback"></span>');
    	
		$('#'+$scope.controllerName+' input[type="text"]').blur(function(){
			var validate = validating($(this));
			//console.log(validate);
			if("pass" == validate){
				$(this).parent().children('span').addClass("hide");
				$(this).parent().removeClass("has-error");
				$(this).parent().addClass("has-success");
				$(this).parent().append('<span class="glyphicon glyphicon-ok form-control-feedback"></span>');
			}else if("error" == validate){
				$(this).parent().children('span').addClass("hide");
				$(this).parent().removeClass("has-success");
				$(this).parent().addClass("has-error");
				$(this).parent().append('<span class="glyphicon glyphicon-remove form-control-feedback"></span>');
			}else if($(this).val() != null && $(this).val() != ""){
				$(this).parent().children('span').addClass("hide");
				$(this).parent().removeClass("has-error");
				$(this).parent().addClass("has-success");
				$(this).parent().append('<span class="glyphicon glyphicon-ok form-control-feedback"></span>');
			}else if("none" == validate){
				$(this).parent().children('span').addClass("hide");
				$(this).parent().removeClass("has-success");
				$(this).parent().removeClass("has-error");
			}
		});
		
		$('#'+$scope.controllerName+' input[type="password"]').blur(function(){
			var validate = validating($(this));
			//console.log(validate);
			if("pass" == validate){
				$(this).parent().children('span').addClass("hide");
				$(this).parent().removeClass("has-error");
				$(this).parent().addClass("has-success");
				$(this).parent().append('<span class="glyphicon glyphicon-ok form-control-feedback"></span>');
			}else if("error" == validate){
				$(this).parent().children('span').addClass("hide");
				$(this).parent().removeClass("has-success");
				$(this).parent().addClass("has-error");
				$(this).parent().append('<span class="glyphicon glyphicon-remove form-control-feedback"></span>');
			}else if($(this).val() != null && $(this).val() != ""){
				$(this).parent().children('span').addClass("hide");
				$(this).parent().removeClass("has-error");
				$(this).parent().addClass("has-success");
				$(this).parent().append('<span class="glyphicon glyphicon-ok form-control-feedback"></span>');
			}else if("none" == validate){
				$(this).parent().children('span').addClass("hide");
				$(this).parent().removeClass("has-success");
				$(this).parent().removeClass("has-error");
			}
		});
		
		$('#'+$scope.controllerName+' select').blur(function(){
			var validate = validating($(this));
			if("pass" == validate){
				$(this).parent().children('span').addClass("hide");
				$(this).parent().removeClass("has-error");
				$(this).parent().addClass("has-success");
				$(this).parent().append('<span class="glyphicon glyphicon-ok form-control-feedback"></span>');
			}else if("error" == validate){
				$(this).parent().children('span').addClass("hide");
				$(this).parent().removeClass("has-success");
				$(this).parent().addClass("has-error");
				$(this).parent().append('<span class="glyphicon glyphicon-remove form-control-feedback"></span>');
			}else if($(this).val() != null && $(this).val() != ""){
				$(this).parent().children('span').addClass("hide");
				$(this).parent().removeClass("has-error");
				$(this).parent().addClass("has-success");
				$(this).parent().append('<span class="glyphicon glyphicon-ok form-control-feedback"></span>');
			}
		});
		
		$scope.validateForm = function(){
			var isPass = true;
			var obj = null;
			$('#'+$scope.controllerName+' input[type="text"]').each(function(){ 
				var validate = validating($(this));
				if("error" == validate){
					isPass = false;
					obj = $(this);
					$(this).parent().children('span').addClass("hide");
					$(this).parent().removeClass("has-success");
					$(this).parent().addClass("has-error");
					$(this).parent().append('<span class="glyphicon glyphicon-remove form-control-feedback"></span>');
					
				}
        	});
			
			$('#'+$scope.controllerName+' input[type="password"]').each(function(){ 
				var validate = validating($(this));
				if("error" == validate){
					isPass = false;
					obj = $(this);
					$(this).parent().children('span').addClass("hide");
					$(this).parent().removeClass("has-success");
					$(this).parent().addClass("has-error");
					$(this).parent().append('<span class="glyphicon glyphicon-remove form-control-feedback"></span>');
					
				}
        	});
			
			$('#'+$scope.controllerName+' select[not-null="yes"]').each(function(){ 
				if($(this).val() == null || $(this).val() == ""){
					isPass = false;
					obj = $(this);
					$(this).parent().children('span').addClass("hide");
					$(this).parent().removeClass("has-success");
					$(this).parent().addClass("has-error");
					$(this).parent().append('<span class="glyphicon glyphicon-remove form-control-feedback"></span>');
				}
        	});
			if(null != obj){
				obj.focus();
			}
			
			
			return isPass;
		}
	}
	
    return{  
    	iniValidate:iniValidate,
    	checkPhone:checkPhone
    }; 
       
})();
