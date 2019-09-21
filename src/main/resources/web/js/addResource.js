layui.use(["table","layer","form"], () => {
    renderResourceTbl();
});

function renderResourceTbl(){
    layui.table.render({
        elem: "#resourceTbl",
        cols: resourceColMapper,
        parseData: baseMapper,
        url: "/exam/resource/list/all",
        toolbar: "#resourceTblToolbar"
    });
    layui.table.on("toolbar(resourceTbl)", e => {
        console.log(e);
        if(e.event === "addResource"){
            openAddResourceForm();
        }
    });
}

function openAddResourceForm(){
    layui.layer.open({
        title: "添加资源",
        type: 1,
        content: layui.jquery("#addResourceFormScript")[0].innerHTML,
        btn: ["提交","取消"],
        yes: (index, layero) => {
            let obj = getObjFromForm(layui.jquery, "addResourceForm");
            postAndAlertMessage("/exam/resource/add", obj, res => {
                responseProcessor.alertMessage(res);
                renderResourceTbl();
            });
            layui.layer.close(layui.layer.index);
        }
    });
}

let resourceColMapper = [[
    {
        field: "id",
        title: "id"
    },{
        field: "name",
        title: "资源名称"
    },{
        field: "uri",
        title: "uri"
    },{
        field: "createTime",
        title: "创建时间"
    },{
        field: "creator",
        title: "创建者"
    }
]];