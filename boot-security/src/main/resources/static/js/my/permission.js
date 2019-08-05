//分析用户权限  控制按钮是否存在
checkPermission();
function checkPermission(){
    $.ajax({
        url:"/permissions/owns",
        type:"get",
        success:function(data){
            alert(1)
			$.each($("[permission]"),function(index,item){
                var per=$(item).attr("permission")
                if($.inArray(per,data)<0){
                    $(item).hide();
                }
			})

        }
    })
}