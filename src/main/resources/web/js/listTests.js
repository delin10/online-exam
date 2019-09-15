let opMapper = {
   "showScore": data => {
      layui.jquery.get({
         url: "/exam/testPaper/showScore/"+data.id,
         dataType: "json",
         statusCode: {
            200: res => {
               if (res.code == 0){
                  let statistic = res.data;
                  if (statistic.totalScore < 0){
                     alert("未批改");
                     return;
                  }
                  alert("选择题分数:"+statistic.optionScore+"\n主观题分数:"+statistic.subjectiveScore+"\n总分:"+statistic.totalScore);
               } else {
                  alert(res.message);
               }
            }
         }
      });
   },
   "noOp": data=>{},
   "joinTest": data => {
      joinTest(data.id);
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
      field: "joinTime",
      title: "参加时间",
      templet: joinStatusMapper
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

function operationEventIdMapper(d) {
   console.log("operationMapper ==> ", d);
   let currentTime = new Date().getTime();
   if (d.joinTime && d.joinTime > 0){
      return "showScore";
   }

   if (d.startTime > currentTime || d.endTime < currentTime){
      return "noOp";
   }

   return "joinTest";
}

function operationTitleMapper(d) {
   console.log("operationMapper ==> ", d);
   let currentTime = new Date().getTime();
   if (d.joinTime && d.joinTime > 0){
      return "查看成绩";
   }

   if (d.startTime > currentTime || d.endTime < currentTime){
      return "无操作";
   }

   return "参加考试";
}

function joinTest(tid){
   postAndAlertMessage("/exam/testPaper/join/"+tid, data=undefined, res => {
      responseProcessor.alertMessage(res);
      layui.table.render();
   });
}

layui.use(["table"], () => {
   let table = layui.table;

   table.render({
      elem: "#testsOfUser",
      url: "/exam/testPaper/list",
      cols: testCols,
      parseData: basePageMapper
   })

   table.on("tool(testsOfUser)", obj=>{
      let layEvent = obj.event, data = obj.data;
      console.log(obj);
      console.log(opMapper)
      if (opMapper[layEvent]){
         opMapper[layEvent](data);
      }
   })
});