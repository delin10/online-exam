layui.use(["table","layer", "form","laydate","element"], function(){
    var table = layui.table, $ = layui.jquery, laydate = layui.laydate
    table.render({
        elem: "#opt-q-tbl",
        url: "/exam/question/list?type=0",
        cols: qColMapper,
        parseData: function (res) {
            return {
                "code": res.code, //解析接口状态
                "msg": res.message, //解析提示文本
                "count": res.data.total, //解析数据长度
                "data": res.data.data //解析数据列表
            }
        }
    })

    table.render({
        elem: "#sub-q-tbl",
        url: "/exam/question/list?type=1",
        cols: qColMapper,
        parseData: function (res) {
            return {
                "code": res.code, //解析接口状态
                "msg": res.message, //解析提示文本
                "count": res.data.total, //解析数据长度
                "data": res.data.data //解析数据列表
            }
        }
    })
    layui.layer.open({
        type: 1,
        title: "test"
    })
    layui.form.render()
    console.log(renderLayDate)
    renderLayDate("#startTime","datetime")
    renderLayDate("#endTime","datetime")


    listen("optTbl")
    listen("subTbl")
    listen("sub-q-tbl")
    listen("opt-q-tbl")
})
function generateTestPaperRandomly(){
    var table = layui.table, $ = layui.jquery
    $.get("/exam/testPaper/generate/random",function (res) {
        var data = res.data
        if (res.code == 0){
            table.render({
                id: "optTbl",
                elem: "#opt-tbl",
                height: 300,
                title: "选择题",
                data: data.options,
                cols: optionColMapper
            })

            table.render({
                id: "subTbl",
                elem: "#sub-tbl",
                height: 300,
                title: "主观题",
                data: data.subjectives,
                cols:subjectiveColMapper
            })

        }else{
            alert(data.message);
        }
    })
}

function mapType(d){
    return d.type === 0 ? "选择题" : "主观题";
}

function listen(layFilter){
    console.log(layFilter)
    layui.table.on("tool("+layFilter+")", function(obj){
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        console.log()
        if(layEvent === 'add'){ //删除
            layer.confirm('真的删除行么', function(index){
                obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                layer.close(index);
                //向服务端发送删除指令
            });
        } else if(layEvent === 'replace'){ //编辑
            var row = undefined;

            if (layFilter === 'sub-q-tbl' && selectedSub){
                row = selectedSub
            }else if (layFilter === 'opt-q-tbl' && selectedOpt){
                row = selectedOpt
            }else{
                alert("无可替换的行")
                return
            }
            row.update(obj.data)
        }
    })

    layui.table.on('row('+layFilter+')', function(obj){
        select(layFilter, obj)
        //标注选中样式
        obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
    });
}

function select(layFilter,row){
    if (layFilter === "subTbl"){
        selectedSub = row
    }else if (layFilter === "optTbl") {
        selectedOpt = row
    }
}

function submitTestPaper(){
    var $=layui.jquery, form = layui.form
    var cache = layui.table.cache;
    var subTbl = cache.subTbl, optTbl = cache.optTbl;
    var totalScore = $("#total-score").val()
    var paperInfoObj = getObjFromForm($, "paperInfo")
    paperInfoObj["startTime"] = new Date(paperInfoObj["startTime"]).getTime()
    paperInfoObj["endTime"] = new Date(paperInfoObj["endTime"]).getTime()
    paperInfoObj.options = []
    paperInfoObj.subjectives = []
    for (var i in subTbl){
        if (!subTbl[i]['score']){
            alert("分数不能为空")
            return
        }else{
            paperInfoObj.subjectives.push({qid: subTbl[i]['id'], score: subTbl[i]['score']})
        }
    }

    for (var i in optTbl){
        if (!optTbl[i]['score']){
            alert("分数不能为空")
            return
        }else{
            paperInfoObj.options.push({qid: optTbl[i]['id'], score: optTbl[i]['score']})
        }
    }
    $.post({
        url: "/exam/testPaper/add",
        data: JSON.stringify(paperInfoObj),
        contentType: "application/json;charset=UTF-8",
        dataType: "application/json",
        statusCode: {
            200: function(res) {
                console.log(res)
            },
            500: function (res) {
                alert(res.message)
            }
        }
    })
    console.log(paperInfoObj)
}

var selectedOpt = undefined
var selectedSub = undefined

var optionColMapper = [[
    {
        field: "id",
        title: "id",
        unresize: true
    },{
        field: "type",
        title: "类型",
        templet: mapType,
        unresize: true

    },{
        field: "content",
        title: "题目内容",
        unresize: true

    },{
        field: "options",
        title: "选项",
        unresize: true

    },{
        field: "answer",
        title: "答案",
        unresize: true
    },{
        field: "score",
        title: "分数",
        edit: "text",
        value: "10"
    },{
        title: "操作",
        fixed: 'right',
        width:150,
        align:'center',
        toolbar: '#op-bar'
    }
]]

var subjectiveColMapper = [[
    {
        field: "id",
        title: "id"
    },{
        field: "type",
        title: "类型",
        templet: mapType,
    },{
        field: "content",
        title: "题目内容"
    },{
        field: "options",
        title: "选项"
    },{
        field: "answer",
        title: "答案"
    },{
        field: "score",
        title: "分数",
        edit: "text",
        value: "10"
    },{
        title: "操作",
        fixed: 'right',
        width:150,
        align:'center',
        toolbar: '#op-bar'
    }
]]

var qColMapper = [[
    {
        field: "id",
        title: "id"
    },{
        field: "type",
        title: "类型",
        templet: mapType,
    },{
        field: "content",
        title: "题目内容"
    },{
        field: "options",
        title: "选项"
    },{
        field: "answer",
        title: "答案"
    },{
        title: "操作",
        fixed: 'right',
        width:150,
        align:'center',
        toolbar: '#op-bar'
    }
]]