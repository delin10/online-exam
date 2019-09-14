let uid = getQueryString("uid"), pid = getQueryString("pid");
layui.use(["table"], ()=>{
    layui.table.render({
        elem: "#paperTbl",
        url: "/exam/testPaper/get/ofStudent?uid="+uid+"&pid="+pid,
        parseData: res => {
            return {
                "code": res.code, //解析接口状态
                "msg": res.message, //解析提示文本
                "count": res.data.questions.length, //解析数据长度
                "data": res.data.questions //解析数据列表
            }
        },
        cols: qColsMapper,
        done: function(res, curr, count){
            console.log(res);
        }
    });

    layui.table.on("edit(paperTbl)", function(obj){
        if (obj.data.type === 0){
            return;
        }
        console.log(obj);
        console.log(obj.value); //得到修改后的值
        console.log(obj.field); //当前编辑的字段名
        console.log(obj.data); //所在行的所有相关数据
    });
});

function mapType(d){
    return d.type === 0 ? "选择题" : "主观题";
}

let qColsMapper = [[
    {
        field: "id",
        title: "id",
    },{
        field: "type",
        title: "类型",
        templet: mapType,
        
    },{
        field: "content",
        title: "题目内容",
    },{
        field: "options",
        title: "选项",
    },{
        field: "answer",
        title: "答案",
    },{
        field: "score",
        title: "分数",
    },{
        field: "submittedAnswer",
        title: "提交的答案"
    },{
        field: "gainScore",
        title: "成绩"
    },{
        title: "打分",
        templet: d => {
            let disable = d.type === 0;
            return "<input id='input"+d.id+ "'type=\"text\" name=\"score\" placeholder=\"请输入分数\" autocomplete=\"off\" class=\"layui-input\""+ (disable ? "disabled value='"+d.gainScore+"'" : d.gainScore)+" >"
        }
    }
]];

function submitMark() {
    let data = layui.table.cache["paperTbl"], $ = layui.jquery;
    let postData = [];
    data.forEach(e => {
       if (e.type === 0){
           return;
       }

       let obj = {
            qid: e.id,
            score: $("#input"+e.id).val()
       };
       postData.push(obj);
    });

    for (let i in postData){
        if (postData[i].score.search(/[0-9]+/) < 0){
            alert("分数必须为数字");
            return;
        }
    }

    postAndAlertMessage("/exam/testPaper/mark?pid="+pid+"&uid="+uid, postData);
}