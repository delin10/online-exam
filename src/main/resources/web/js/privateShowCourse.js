layui.use(["table","layer","laydate"], ()=>{
    let table = layui.table;

    renderCoursesTbl();
});

let courseColsMapper = [[
    {
        field: "id",
        title: "id"
    },{
        field: "name",
        title: "课程名称"
    },{
        field: "teacherName",
        title: "老师"
    },{
        field: "teacher",
        title: "老师id"
    },{
        field: "createTime",
        title: "课程创建时间"
    },{
        field: "startTime",
        title: "开始时间"
    },{
        field: "endTime",
        title: "截止时间"
    },{
        field: "updateTime",
        title: "更新时间"
    },{
        field: "joinTime",
        title: "加入时间",
        templet: d => d.joinTime && d.joinTime > 0 ? d.joinTime : "--"
    },{
        title: "课程状态",
        templet: d => courseStatusMapper(d)["statusTitle"]
    },{
        title: "操作",
        toolbar: "#opToolBar"
    }
]];

function courseStatusMapper(d){
    let currentTime = new Date().getTime();

    if (d.startTime > currentTime){
        return {
            statusTitle: "未开始",
        };
    }

    if (d.joinTime && d.joinTime > 0){
        return  {
            statusTitle: "进行中",
        };
    }

    if (d.endTime < currentTime){
        return {
            statusTitle: "已截止",
        };
    }

    return  {
        statusTitle: "进行中",
    };
}

function renderCoursesTbl(){
    layui.table.render({
        elem: "#courseTbl",
        url: "/exam/course/list/own",
        cols: courseColsMapper,
        parseData: basePageMapper,
        toolbar: "#courseTblToolbar"
    });

    layui.table.on("tool(courseTbl)", obj=>{
        let event = obj.event,
            data = obj.data,
            operation = opMapper[event];
        if (operation){
            operation(data);
        }
    });

    layui.table.on("toolbar(courseTbl)", e => {
        console.log(e);
        if(e.event === "addCourse"){
            openAddCourseForm();
        }
    });
}

function openAddCourseForm(){
    layui.layer.open({
        title: "添加资源",
        type: 1,
        content: layui.jquery("#addCourseFormScript")[0].innerHTML,
        btn: ["提交","取消"],
        success: (layro,index) =>{
          renderLayDate("#startTime");
          renderLayDate("#endTime");
        },
        yes: (index, layero) => {
            let obj = getObjFromForm(layui.jquery, "addCourseForm");
            obj.startTime = new Date(obj.startTime).getTime();
            obj.endTime = new Date(obj.endTime).getTime();
            postAndAlertMessage("/exam/course/add", obj, res =>{
                responseProcessor.alertMessage(res);
                renderCourseTbl();
            });
            layui.layer.close(layui.layer.index);
        }
    });
}
