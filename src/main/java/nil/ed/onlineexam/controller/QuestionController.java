package nil.ed.onlineexam.controller;

import nil.ed.onlineexam.aop.annotation.MethodInvokeLog;
import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.Question;
import nil.ed.onlineexam.service.IQuestionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/exam/question")
public class QuestionController {
    @Resource
    @Qualifier("questionService")
    private IQuestionService questionService;

    @MethodInvokeLog
    @GetMapping("/list")
    public Response<PageResult<Question>> listQuestions(@RequestParam(value = "type",required = false) Integer type){
        return questionService.listQuestions(0, 20, type);
    }
}
