//添加菜单---一级菜单下拉选
    initParentMenuSelect();
    function initParentMenuSelect(){
    	alert(2)
        $.ajax({
            url:"/permissions/parents",
            type:"get",
            success:function(data){
                var parentId=$("#parentId");
                $("<option>").text("root").val(0).appendTo(parentId);
                $.each(data,function (index,item) {
                    var opt=$("<option>").text(item.name).val(item.id);
                    opt.appendTo(parentId);
                })
            }
        })
    }

function getSettting() {
    var setting = {
        check : {
            enable : true,
            chkboxType : {   //勾选取消影响父子节点
                "Y" : "ps",
                "N" : "ps"
            }
        },
        async : {
            enable : true,
        },
        data : {
            simpleData : {
                enable : true,
                idKey : "id",
                pIdKey : "pId",
                rootPId : 0
            }
        },
        callback : {
            onCheck : zTreeOnCheck   //取消勾选出发毁掉函数
        }
    };

    return setting;
}

//所有权限的json数组
function getMenuTree() {
    var root={
        id: 0,
        name: "root",
        open: true,
    };

    $.ajax({
        url:"/permissions/all",
        type:"get",
        async : false,
        success: function(data){
            var length = data.length;
            var children = [];
            for (var i = 0; i < length; i++) {
                var d = data[i];
                var node = createNode(d);
                children[i] = node;
            }

            root.children = children;
        }
    });
    return root;
}

function createNode(d) {
    var id = d.id;
    var pId = d['parentId'];
    var name = d.name;
    var child = d['child'];

    var node = {
        open : true,
        id : id,
        name : name,
        pId : pId,
    };
    //有子节点
    if (child != null) {
        var length = child.length;
        if (length > 0) {
            var children = [];
            for (var i = 0; i < length; i++) {
                children[i] = createNode(child[i]);
            }

            node.children = children; //节点乒接
        }

    }
    return node;
}

    //加载权限树
    function  initMenuDatas(id){
        //1获取角色id对应的权限集合
        $.ajax({
            url:"/permissions/role/"+id,
            type:"get",
            success:function(data){
                console.log(data);
            //将所得到的权限集合放入到ids中
                var ids=[];
                for(var i=0;i<data.length;i++){
                    ids.push(data[i].id);
                }

                initMenuCheck(ids);
            }
        })
    }

function initMenuCheck(ids) {

    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    var length = ids.length;
    if(length > 0){       //一级菜单选中  root
        var node = treeObj.getNodeByParam("id", 0, null);//获取节点
        treeObj.checkNode(node, true, false);	//勾选
    }

    for(var i=0; i<length; i++){  // 根据ID选中
        var node = treeObj.getNodeByParam("id", ids[i], null);  //null表示不在某个父节点下搜索
        treeObj.checkNode(node, true, false);  //不联动
    }

}

//获取选中的节点的ids
function getCheckedMenuIds(){
    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    //获取选中的节点
    var nodes=treeObj.getCheckedNodes(true);
    var ids=[];
    for(var i=0;i<nodes.length;i++){
        ids.push(nodes[i]['id']);
    }
    return ids;
}

function zTreeOnCheck(event, treeId, treeNode) {
//	console.log(treeNode.id + ", " + treeNode.name + "," + treeNode.checked
//			+ "," + treeNode.pId);
//	console.log(JSON.stringify(treeNode));
}
