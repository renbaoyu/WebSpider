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

	// 绑定时间选择控件
	$("input[type=datetime]").click(function(){
		if(laydate){
			laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})
		}else{
			alert("使用datetime类型的数据时，需要引入laydate插件");
		}
	});

	// 设置表格的皮肤
	$("table").addClass("table table-striped");
})