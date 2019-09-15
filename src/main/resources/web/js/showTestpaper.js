var paperTemplateStr = "<div>\n" +
    "            <h3>{{ d.name }}</h3>\n" +
    "            <p>分值：{{ d.score }}</p>\n" +
    "            <p>开始时间：{{ dateToTime(d.startTime/1000) }}</p>\n" +
    "            <p>结束时间：{{ dateToTime(d.endTime/1000) }}</p>\n" +
    "            <p>考试时长：{{ d.testDuration }}分钟</p>\n" +
    "        </div>\n" +
    "        <HR style=\"FILTER:alpha(opacity=100,finishopacity=0,style=3)\" width=\"90%\"color=#987cb9 SIZE=3>\n" +
    "        <div>\n" +
    "            <form class=\"layui-form\" lay-filter=\"testPaper\" id=\"testPaper\">\n" +
    "                {{#  layui.each(d.questions, function(index, item){ }}\n" +
    "                 <div class=\"layui-form-item\">\n" +
    "                     <div>{{ item.firstSeq }}-{{ item.secSeq }}. {{ item.content }}({{ item.score }})</div>\n" +
    "                     {{#  if(item.type == 0){ }}\n" +
    "                        <div>\n" +
    "                        {{#  layui.each(JSON.parse(item.options), function(index, option){ }}\n" +
    "                            <input type=\"radio\" name=\"qid{{ item.id }}\" value=\"{{ index }}\" title=\"{{ option }}\" class=\"layui-input\" required/>\n" +
    "                        {{# }); }}\n" +
    "                         </div>\n" +
    "                    {{#  } else { }}\n" +
    "                        <br />\n" +
    "                        <div>\n" +
    "                            <textarea name=\"qid{{ item.id }}\" placeholder=\"请输入内容\" class=\"layui-textarea\"></textarea>\n" +
    "                        </div>\n" +
    "                    {{#  } }}\n" +
    "                 </div>\n" +
    "                {{#  }); }}\n" +
    "                <div class=\"layui-form-item\">\n" +
    "                    <div class=\"layui-input-block\">\n" +
    "                        <button class=\"layui-btn\" lay-submit lay-filter=\"submit-answer\">提交答案</button>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "            </form>\n" +
    "        </div>";

layui.use(['jquery','laytpl', "form", "table"], function(){
    var laytpl = layui.laytpl, $ = layui.jquery, form = layui.form, table = layui.table;

    //你也可以采用下述同步写法，将 render 方法的回调函数剔除，可直接返回渲染好的字符
    var paperTemplate =  laytpl(paperTemplateStr);

    let url = window.location.href;
    let pid = getQueryString("pid");
    if (!pid) {
        return;
    }

    $.get(host + "/testPaper/get/"+pid, function (res) {
        paperTemplate.render(res.data, function (html) {
            $("#paper-container").append(html)
            /*
            如果表单空间是动态生成的，必须调用这个函数，否则不渲染
             */
            form.on("submit(testPaper)", function (data) {
                let obj = getObjFromForm(layui.jquery,"testPaper");
                let postObj = [];
                for (let property in obj){
                    postObj.push({
                        qid: property.substr(3),
                        answer: obj[property]
                    });
                }

                postAndAlertMessage("/exam/testPaper/submitAnswer?pid="+pid, postObj);
                return false;
            });

            form.render()
        })
    })
    //////////////////////////////////////////////////////addTestPaper

});

function submit(){

}
