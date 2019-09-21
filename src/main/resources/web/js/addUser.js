layui.use(["table","layer","form"], () => {
    renderUserTbl();
});

function renderUserTbl(){
    layui.table.render({
        elem: "#userTbl",
        cols: userColMapper,
        parseData: basePageMapper,
        url: "/exam/user/list/all",
        toolbar: "#userTblToolbar"
    });
    layui.table.on("toolbar(userTbl)", e => {
        console.log(e);
        if(e.event === "addUser"){
            openAddUserForm();
        }
    });

    layui.table.on("tool(userTbl)", userTblObj => {
        console.log(userTblObj);
        let event = userTblObj.event;
        if (event === "updateRole"){
            /**
             * 弹出层表格
             */
            layui.layer.open({
                type: 1,
                title: "修改角色",
                content: "<table id=\"updateRoleTbl\" lay-filter=\"updateRoleTbl\"></table>",
                success: (index, layero) => {
                    layui.table.render({
                        elem: "#updateRoleTbl",
                        url: "/exam/role/list",
                        cols: roleColMapper,
                        parseData: basePageMapper
                    });

                    layui.table.on("tool(updateRoleTbl)", updateRoleObj => {
                        let event = updateRoleObj.event;

                        if (event === "updateRole"){
                            postAndAlertMessage("/exam/user/auth?userId="+userTblObj.data.id+"&roleId="+updateRoleObj.data.id, "", res => {
                                responseProcessor.alertMessage(res);
                                layui.layer.close(layui.layer.index);
                                renderUserTbl();
                            });
                        }
                    });
                }
            });
        }
    });
}

function openAddUserForm(){
    layui.layer.open({
        title: "添加资源",
        type: 1,
        content: layui.jquery("#addUserFormScript")[0].innerHTML,
        btn: ["提交","取消"],
        yes: (index, layero) => {
            let obj = getObjFromForm(layui.jquery, "addUserForm");
            postAndAlertMessage("/exam/user/add", obj, res =>{
                responseProcessor.alertMessage(res);
                renderUserTbl();
            });
            layui.layer.close(layui.layer.index);
        }
    });
}

let userColMapper = [[
    {
        field: "id",
        title: "id"
    },{
        field: "nickName",
        title: "昵称"
    },{
        title: "角色名称",
        templet: d => {
            return d.role.name;
        }
    },{
        title: "角色描述",
        templet: d => {
            return d.role.desc;
        }
    },{
        title: "操作",
        toolbar: "#userTblOpToolBar"
    }
]];

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
        title: "操作",
        toolbar: "#updateRoleTblOpToolBar"
    }
]];


let updateRoleTblConfig = {

};