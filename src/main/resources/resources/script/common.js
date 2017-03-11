// 页面初始化
$(function(){
	// 绑定提交按钮动作
	$("#submit").click(function(){
		$("#submit").parents("form").submit();
	});
	
	// 只读的checkbox依然可选择，做不可选择处理
	$("input:checkbox[readonly]").click(function(){
		return false;
	});
})