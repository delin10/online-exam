layui.use(["table"], ()=>{
    let table = layui.table;

    renderCoursesTbl();

    table.on("tool(showCourses)", obj=>{
        let event = obj.event,
            data = obj.data,
            operation = opMapper[event];
        if (operation){
            operation(data);
        }
    })
})

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
            opId: "noOp",
            opTitle: "无操作"
        };
    }

    if (d.joinTime && d.joinTime > 0){
        return  {
            statusTitle: "进行中",
            opId: "noOp",
            opTitle: "已参加"
        };
    }

    if (d.endTime < currentTime){
        return {
            statusTitle: "已截止",
            opId: "noOp",
            opTitle: "无操作"
        };
    }

    return  {
        statusTitle: "进行中",
        opId: "joinCourse",
        opTitle: "加入课程"
    };
}

function renderCoursesTbl(){
    layui.table.render({
        elem: "#showCourses",
        url: "/exam/course/list",
        cols: courseColsMapper,
        parseData: basePageMapper
    })
}

let opMapper = {
    joinCourse: data => {
        postAndAlertMessage("/exam/course/join/"+data.id,
            undefined,
         res => {
                responseProcessor.alertMessage(res);
                renderCoursesTbl();
            });
    }
}
