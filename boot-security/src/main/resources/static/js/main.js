
//初始化左侧菜单
initMenu();
function initMenu(){
	$.ajax({
		url:"/permissions/current",
		type:"get",
		success:function(data){
			//拼接左侧菜单栏
			var menu=$("#menu");
			$.each(data,function(index,item){
				var li=$("<li>").addClass("layui-nav-item layui-nav-itemed");
                var a=$("<a>").attr("href","javascript:;").attr("lay-id",item.id).attr("data-url",item.href);
              	var i= $("<i>").addClass("fa "+item.css).attr("aria-hidden","true");       //addClass    "fa    "
                var cite=$("<cite>").text(item.name);
                a.append(i).append(cite);
				li.append(a);
				li.appendTo(menu);
				setChild(li,item.child);
			})
		}
	})
}
//添加下级菜单
function setChild(li,child){
	if(child!=null){
		//li里面塞子菜单
		$.each(child,function(index2,item2){
			var dl=$("<dl>").addClass("layui-nav-child");
			var dd=$("<dd>").addClass("layui-this").appendTo(dl);
			var a=$("<a>").attr("href","javascript:;").attr("data-url",item2.href).attr("lay-id",item2.id);
			var i=$("<i>").addClass("fa "+item2.css);
			i.appendTo(a);
			var cite=$("<cite>").text(item2.name);
			cite.appendTo(a)
			dd.append(a);
			li.append(dl)
			setChild(dd,item2.child);
		})
	}
}




//加载element模块
var active;

layui.use(['layer', 'element'], function() {
    var $ = layui.jquery,
        layer = layui.layer;
    var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
    element.on('nav(demo)', function(elem){
        //layer.msg(elem.text());
    });

    //触发事件  HOME的tab选项
    active = {
        tabAdd: function(obj){
            var lay_id = $(this).attr("lay-id");
            var title = $(this).html() + '<i class="layui-icon" data-id="' + lay_id + '"></i>';
            //新增一个Tab项
            element.tabAdd('admin-tab', {
                title: title,
                content: '<iframe src="' + $(this).attr('data-url') + '"></iframe>',
                id: lay_id
            });
            element.tabChange("admin-tab", lay_id);
        },
        tabDelete: function(lay_id){
            element.tabDelete("admin-tab", lay_id);
        },
        tabChange: function(lay_id){
            element.tabChange('admin-tab', lay_id);
        }
    };
    //添加tab
    $(document).on('click','a',function(){
        if(!$(this)[0].hasAttribute('data-url') || $(this).attr('data-url')===''){
            return;
        }
        var tabs = $(".layui-tab-title").children();
        var lay_id = $(this).attr("lay-id");

        for(var i = 0; i < tabs.length; i++) {
            if($(tabs).eq(i).attr("lay-id") == lay_id) {
                active.tabChange(lay_id);
                return;
            }
        }
        active["tabAdd"].call(this);
        resize();
    });

    //iframe自适应
    function resize(){
        var $content = $('.admin-nav-card .layui-tab-content');
        $content.height($(this).height() - 147);
        $content.find('iframe').each(function() {
            $(this).height($content.height());
        });
    }
    $(window).on('resize', function() {
        var $content = $('.admin-nav-card .layui-tab-content');
        $content.height($(this).height() - 147);
        $content.find('iframe').each(function() {
            $(this).height($content.height());
        });
    }).resize();

    //toggle左侧菜单
    $('.admin-side-toggle').on('click', function() {
        var sideWidth = $('#admin-side').width();
        if(sideWidth === 200) {
            $('#admin-body').animate({
                left: '0'
            });
            $('#admin-footer').animate({
                left: '0'
            });
            $('#admin-side').animate({
                width: '0'
            });
        } else {
            $('#admin-body').animate({
                left: '200px'
            });
            $('#admin-footer').animate({
                left: '200px'
            });
            $('#admin-side').animate({
                width: '200px'
            });
        }
    });

    //手机设备的简单适配
    var treeMobile = $('.site-tree-mobile'),
        shadeMobile = $('.site-mobile-shade');
    treeMobile.on('click', function () {
        $('body').addClass('site-mobile');
    });
    shadeMobile.on('click', function () {
        $('body').removeClass('site-mobile');
    });
});