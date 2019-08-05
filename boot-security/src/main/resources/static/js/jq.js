$.ajaxSetup({
	cache:false,
	headers:{
		"token":localStorage.getItem("token")
	},
	error:function(xhr,textStatus,errorThrown){
		var msg=xhr.responseText;
		var response=JSON.parse(msg);
		var code=response.code;
		var message=response.message;
		if(code==400){
			layer.msg(message)
		}
		if(code=401){
			//token过期或者没有token
			localStorage.removeItem("token");
			location.href="login.html"
		}
		if(code==403){
			layer.msg("没有该权限");
		}
		if(code=500){
			layer.msg("系统错误");
		}
	}
})