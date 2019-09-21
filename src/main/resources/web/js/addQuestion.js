layui.use(["table", "layer", "form"], ()=>{
    renderQuestionTbl();
});

function renderQuestionTbl(){
    let table = layui.table;

    table.render({
        elem: "#questionTbl",
        url: "/exam/question/list",
        parseData: basePageMapper,
        cols: questionColsMapper,
        toolbar: "#questionTblToolbar"
    });

    table.on("toolbar(questionTbl)", obj=>{
       let event = obj.event, data = obj.data;
       console.log(obj);
       if (event === "addSubjectiveQuestion"){
            openAddSubjectiveQuestionForm();
       }else if (event === "addOptionQuestion"){
            openAddOptionalQuestionForm();
       }
    });
}

function openAddSubjectiveQuestionForm(){
    layui.layer.open({
        title: "添加主观题",
        type: 1,
        content: layui.jquery("#addSubjectiveQuestionFormScript")[0].innerHTML,
        btn: ["提交","取消"],
        yes: (index, layero) => {
            let obj = getObjFromForm(layui.jquery, "addSubjectiveQuestionForm");
            obj.type = 1;
            postAndAlertMessage("/exam/question/add", obj, res =>{
                responseProcessor.alertMessage(res);
                renderQuestionTbl();
            });
            layui.layer.close(layui.layer.index);
        }
    });
}



function openAddOptionalQuestionForm(){
    let $ = layui.jquery,index = 1,optionMapper = ['A','B','C','D','E'];
    layui.layer.open({
        title: "添加选择题",
        type: 1,
        content: layui.jquery("#addOptionQuestionFormScript")[0].innerHTML,
        btn: ["提交","取消","添加选项"],
        yes: (num) => {
            let obj = getObjFromForm(layui.jquery, "addOptionQuestionForm");
            let optionArr = [];
            for (let i = 0; i < index; ++i){
                let name = `option${i}`;
                optionArr.push(obj[name]);
                console.log(optionArr);
                delete obj[name];
            }
            let isValid = false;
            for (let i in optionMapper){
                if (obj.answer === optionMapper[i]){
                    obj.answer = i;
                    isValid = true;
                    break;
                }
            }

            if (!isValid){
                alert("答案选项不存在");
                return false;
            }

            obj.options = JSON.stringify(optionArr);
            obj.type = 0;
            console.log(obj);
            postAndAlertMessage("/exam/question/add", obj, res => {
                responseProcessor.alertMessage(res);
                renderQuestionTbl();
            });
            layui.layer.close(layui.layer.index);
        },
        btn3: (num, layero) =>{
            if (index === 5){
                alert("不能超过5个选项");
                return false;
            }

            $("#addOptionQuestionForm").append(
                    `<div class="layui-form-item">
                      <label class="layui-form-label">${optionMapper[index]}</label>
                      <div class="layui-input-inline">
                        <input type="text" name="option${index}" required placeholder="请输入选项" autocomplete="off" class="layui-input">
                      </div>
                    </div>`);
            ++index;
            return false;
        }
    });
}

function mapType(d){
    return d.type === 0 ? "选择题" : "主观题";
}

let questionColsMapper = [[
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
    }
]];