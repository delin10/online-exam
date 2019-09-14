let opUid = undefined;

layui.use(["table","layer"], () => {
    let cid = getQueryString("cid");

    layui.table.render({
        elem: "#studentTbl",
        url: "/exam/course/list/students?cid="+cid,
        parseData: res => {
            return {
                "code": res.code, //解析接口状态
                "msg": res.message, //解析提示文本
                "count": res.data.data[0].students.length, //解析数据长度
                "data": res.data.data[0].students //解析数据列表
            }
        },
        cols: [[
            {
                field: "id",
                title: "id"
            },{
                field: "nickName",
                title: "昵称"
            },{
                field: "joinTime",
                title: "参加时间"
            },{
                title: "操作",
                toolbar: "#studentTblOpBtn"
            }
        ]]
    });

    layui.table.on("tool(studentTbl)", obj => {
        let event = obj.event, uid = obj.data.id;
        opUid = uid;

        if (event === "showJoinedTest"){
            /**
             * 弹出层表格
             */
            layui.layer.open({
                type: 1,
                title: "考试列表",
                content: "<table id=\"joinedTestTbl\" lay-filter=\"joinedTestTbl\"></table>",
                success: (index, layero) => {
                    layui.table.render({
                        elem: "#joinedTestTbl",
                        url: "/exam/testPaper/list?uid="+uid,
                        cols: testCols,
                        parseData: basePageMapper
                    });
                }
            });
        }
    })
});

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
        field: "joinTime",
        title: "参加时间",
        templet: joinStatusMapper
    },{
        title: "状态",
        templet: testStatusMapper
    },{
        title: "成绩",
        templet: d => d.totalScore < 0 ?  "未批改" : d.totalScore
    },{
        title: "操作",
        toolbar: "#joinedTestTblBar"
    }
]];

function joinedTestTblBarMapper(d) {
    if (d.totalScore < 0){
        return {
            opId: "markPaper",
            opTitle: "批改试卷"
        }
    }

    return {
        opId: "noOp",
        opTitle: "无操作"
    };
}

