layui.use(["layer","transfer","table"], () => {
    let $ = layui.jquery, trans = layui.transfer;
    renderRoleTbl();
});

let renderRoleTbl = () => {
    let $ = layui.jquery, layer = layui.layer;
    layui.table.render({
        elem: "#roleTbl",
        url: "/exam/role/list",
        cols: roleColMapper,
        parseData: basePageMapper
    });

    layui.table.on("tool(roleTbl)", obj=>{
        let event = obj.event,
            data = obj.data;
        if (event === "setResource"){
            if (data.id == 1){
                layer.msg("无法修改管理员权限");
                return;
            }

            layer.open({
                type: 1,
                title: "管理资源",
                shade: 0.8,
                area: ["500px", "500px"],
                btn: ["提交","取消"],
                yes: (index, layero) =>{
                    let postData ={
                        "resources": layui.transfer.getData("resourceSelector").map(obj => obj.value),
                        "roleId": data.id
                    };

                    postAndAlertMessage("/exam/role/auth", postData);
                },
                content: $("#resourceSelectorScript")[0].innerHTML,
                success: obj => {
                    console.log(obj);

                    let right = [], resources = [];
                    $.get({
                        url: "/exam/resource/list/all",
                        success: res => {
                            res.data.forEach(r => {
                                resources.push({
                                    "value": r.id,
                                    "title": r.name,
                                    "disabled": "",
                                    "checked": ""
                                })
                            })

                            $.get({
                                url: "/exam/resource/list/ofRole/"+data.id,
                                success: res => {
                                    res.data.forEach(r => {
                                        right.push(r.id);
                                    })

                                    layui.transfer.render({
                                        elem: "#resourceSelector",
                                        data:resources,
                                        value: right,
                                        id: "resourceSelector" //定义索引
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }
    })
};

let roleColMapper = [[
    {
        "title": "id",
        "field": "id"
    },{
        "title": "角色名称",
        "field": "name"
    },{
        "title": "角色描述",
        "field": "desc"
    },{
        "title": "创建时间",
        "field": "createTime"
    },{
        "title": "上次更新时间",
        "field": "lastUpdateTime"
    },{
        "title": "创建人",
        "field": "creator"
    },{
        "title": "操作",
        "toolbar": "#opToolBar"
    }
]];
