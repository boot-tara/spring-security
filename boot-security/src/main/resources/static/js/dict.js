
function initSelect(type){
	console.log(type)
	$.ajax({
		url:"/dicts/"+type,
		type:"get",
		success:function(data){
			console.log(data);
			//填充下拉选
		    var select=	$("#status");
		    select.empty();
		    $("<option>").text("全部").val(-1).appendTo(select)
		    $.each(data,function(index,item){
		    	$("<option>").text(item.val).val(item.k).appendTo(select);
			})
		}
	})
}