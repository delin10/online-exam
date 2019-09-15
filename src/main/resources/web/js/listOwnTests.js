let opMapper = {
    "publish": data => {
        postAndAlertMessage("/exam/testPaper/publish/"+data.id,"", res => {
            responseProcessor.alertMessage(res);
            renderTestsOfOwnTbl();
        });
    },
    "noOp": data => {

    }
};

var testCols = [[
    {
        field: "id",
        title: "id"
    },{
        field: "cid",
        title: "课程id"
    },{
        field: "courseName",
        title: "课程名称"
    },{
        field: "name",
        title: "考试名称"
    },{
        field: "startTime",
        title: "开始时间"
    },{
        field: "endTime",
        title: "截止时间"
    },{
        field: "testDuration",
        title: "考试时长"
    },{
        field: "score",
        title: "分数"
    },{
        title: "状态",
        templet: testStatusMapper
    },{
        title: "操作",
        toolbar: "#op-btn"
    }
]];

function testStatusMapper(d) {
    console.log(d);
    let testStatusStr = "正在进行",currentTime = new Date().getTime();

    if (d.startTime > currentTime) {
        testStatusStr = "未开始";
    } else if (d.endTime < currentTime) {
        testStatusStr = "已截止";
    }
    return testStatusStr;
}

function joinStatusMapper(d) {
    console.log(d);

    return d.joinTime && d.joinTime > 0 ? d.joinTime : "--";
}

function operationMapper(d) {
    console.log("operationMapper ==> ", d);
    let currentTime = new Date().getTime();

    if (d.endTime < currentTime){
        return {
            opId: "noOp",
            opTitle: "已过期"
        };
    }

    if (d.published === 1){
        return {
            opId: "noOp",
            opTitle: "已发布"
        };
    }

    return {
        opId: "publish",
        opTitle: "发布"
    };
}

function renderTestsOfOwnTbl(){
    let table = layui.table;

    table.render({
        elem: "#testsOfOwnTbl",
        url: "/exam/testPaper/list/ofOwn",
        cols: testCols,
        parseData: basePageMapper
    })

    table.on("tool(testsOfOwnTbl)", obj=>{
        let layEvent = obj.event, data = obj.data;
        console.log(obj);
        console.log(opMapper)
        if (opMapper[layEvent]){
            opMapper[layEvent](data);
        }
    })
}

layui.use(["table"], () => {
    renderTestsOfOwnTbl();
});