(function() {
	require.config({
		baseUrl : './',
		urlArgs: "v=7",
		paths : {
			jquery : 'assets/js/jquery/1.11.3/jquery.min',
			jqueryUi : 'assets/js/jqueryUi/js/jquery-ui-1.9.2.custom.min',
			jqueryUiZh : 'assets/js/jqueryUi/jqueryUi_ZH',
			jqueryForm : 'assets/js/jqueryForm/jquery.form',
			
			bootstrap : 'assets/js/bootstrap/3.3.4/js/bootstrap.min',
			bootstrapswitch : 'assets/js/bootstrap-switch',
			
			jztree : 'assets/js/zTree/js/jquery.ztree.all-3.5.min',
			smartMenu : 'assets/js/jquerySmartMenu/js/jquery-smartMenu-min',
			jqueryUpload : 'assets/js/jqueryUpload/jquery.upload',
			
			umeditorConfig : 'assets/js/umeditor/umeditor.config',
			umeditorMin : 'assets/js/umeditor/umeditor',
			umeditorLang : 'assets/js/umeditor/lang/zh-cn/zh-cn',
			
			jwplayer:"js/jwplayer/jwplayer",
	        flexpaper:"js/FlexPaper/flexpaper_flash",
	        
	        highchartsexport:'assets/js/highcharts/code/exporting',
			highcharts: 'assets/js/highcharts/code/highcharts',
			highchartsGridLight: 'assets/js/highcharts/themes/grid-light',
			highchartsMore: 'assets/js/highcharts/code/highcharts-more',
			highcharts3D: 'assets/js/highcharts/highcharts-3d',
			
			date: 'assets/js/bootstrap-daterangepicker/date',
			daterangepicker: 'assets/js/bootstrap-daterangepicker/daterangepicker',
			bootstrapDatetimepicker : 'assets/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker',
			bootstrapDatetimepickerZhCn : 'assets/js/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN',
			
			pageController: 'assets/js/page',
			validateController: 'assets/js/validate',
			

			swfObject: 'assets/js/jquery.uploadify-v2.1.0/swfobject',
			jqueryUploadify: 'assets/js/jquery.uploadify-v2.1.0/jquery.uploadify.v2.1.0.min',
			uploadConntrolle: 'assets/js/upload'
		},
		shim : {
			highchartsGridLight : {
				deps : [ 'highcharts'],
				exports : 'highchartsGridLight'
			},
			highchartsexport : {
				deps : [ 'highchartsGridLight'],
				exports : 'highchartsexport'
			},
			highchartsMore : {
				deps : [ 'highcharts','highchartsexport','highchartsGridLight'],
				exports : 'highchartsMore'
			},
			bootstrap : {
				deps : [ 'jqueryForm' ],
				exports : 'bootstrap'
			},
			bootstrapswitch : {
				deps : [ 'bootstrap' ],
				exports : 'bootstrapswitch'
			},
			daterangepicker : {
				deps : [ 'date' ],
				exports : 'daterangepicker'
			},
			umeditorMin : {
				deps : [ 'umeditorConfig' ],
				exports : 'umeditorMin'
			},
			jqueryUiZh : {
				deps : [ 'jqueryUi'],
				exports : 'jqueryUiZh'
			},
			bootstrapDatetimepicker : {
				deps : [ 'jqueryUi'],
				exports : 'bootstrapDatetimepicker'
			},
			bootstrapDatetimepickerZhCn: {
				deps : [ 'bootstrapDatetimepicker'],
				exports : 'bootstrapDatetimepickerZhCn'
			},
			umeditorLang : {
				deps : [ 'umeditorMin' ],
				exports : 'umeditorLang'
			}
		}
		
	});
	

	require(['bootstrap','pageController','validateController'], function() {
		if(!window.console){
			console = (function(){
				var instance = null;
				function Constructor(){
					this.div = document.createElement("console");
					this.div.id = "console";
					this.div.style.cssText = "filter:alpha(opacity=80);position:absolute;top:100px;right:0px;width:30%;border:1px solid #ccc;background:#eee;display:none";
					document.body.appendChild(this.div);
					//this.div = document.getElementById("console");
				}
				Constructor.prototype = {
					log : function(str){
						var p = document.createElement("p");
						p.innerHTML = "LOG: "+str;
						this.div.appendChild(p);
					},
					debug : function(str){
						var p = document.createElement("p");
						p.innerHTML = "DEBUG: "+str;
						p.style.color = "blue";
						this.div.appendChild(p);
					},
					error : function(str){
						var p = document.createElement("p");
						p.innerHTML = "ERROR: "+str;
						p.style.color = "red";
						this.div.appendChild(p);
					}
				}
				function getInstance(){
					if(instance == null){
						instance =  new Constructor();
					}
					return instance;
				}
				return getInstance();
			})()
		}
	});
	
})(this);