//定义a.js 模块
console.log("加载module.main的Js");
define(function (p) {
	console.log("module.main");
	console.log(p);
    var a = "这是bbbbbbbbbb!!!!!!!!"
    // return 返回a
    return a
})