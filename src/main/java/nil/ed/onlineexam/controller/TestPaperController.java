package nil.ed.onlineexam.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import nil.ed.onlineexam.common.CommonVO;
import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.SubmittedAnswer;
import nil.ed.onlineexam.service.ITestPaperService;
import nil.ed.onlineexam.vo.ScoreStatisticVO;
import nil.ed.onlineexam.vo.TestPaperWithQuestionWithSubmittedAnswerVO;
import nil.ed.onlineexam.vo.TestPaperWithQuestionsVO;
import nil.ed.onlineexam.vo.UserTestVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/exam/testPaper")
public class TestPaperController {
    @Resource
    private ITestPaperService paperService;

    @GetMapping(value = "/get/{id}")
    public Response<TestPaperWithQuestionsVO> getTestPaper(@PathVariable("id") Integer id, Model model){
        return paperService.getTestPaper(id);
    }

    @GetMapping(value = "/generate/random")
    public Response<CommonVO> generateTestPaperRandomly(@RequestParam(value = "optionNum", required = false, defaultValue = "2") Integer optionNum,
                                                        @RequestParam(value = "subjectiveNum", required = false, defaultValue = "2") Integer subjectiveNum){
        return paperService.generateTestPaperRandomly(optionNum, subjectiveNum);
    }

    @PostMapping(value="/add")
    public Response<Void> addTestPaper(@RequestBody JSONObject testPaper, @RequestAttribute("user") UserDetails user){
        return paperService.addTestPaper(testPaper, Integer.valueOf(user.getUsername()));
    }

    @PostMapping(value="/joined/{tid}")
    public Response<Void> addTestPaper(@PathVariable("tid") Integer tid,
                                       @RequestAttribute("user") UserDetails user){
        return paperService.joinTest(tid, Integer.valueOf(user.getUsername()));
    }

    @GetMapping(value="/list")
    public Response<PageResult<UserTestVO>> listCanJoinOrHaveJoinedTests(@RequestParam(value = "uid", required = false) Integer uid,
                                                                         @RequestParam(value = "cid", required = false) Integer cid,
                                                                         @RequestAttribute("user") UserDetails user){
        return paperService.listCanJoinOrHaveJoinedTests(Optional.ofNullable(uid).orElse(Integer.valueOf(user.getUsername())), cid);
    }

    @PostMapping(value = "/submitAnswer")
    public Response<Void> submitAnswer(@RequestBody List<SubmittedAnswer> submittedAnswerList,
                                       @RequestParam("pid") Integer pid,
                                       @RequestAttribute("user") UserDetails user){
        return paperService.submitAnswer(submittedAnswerList, pid, Integer.valueOf(user.getUsername()));
    }

    @GetMapping(value = "/get/ofStudent")
    public Response<TestPaperWithQuestionWithSubmittedAnswerVO> getTestPaperWithQuestionWithSubmittedAnswerVO(@RequestParam("pid") Integer pid,
                                                                                                              @RequestParam("uid") Integer uid){
        return paperService.getTestPaperWithQuestionWithSubmittedAnswerVO(pid, uid);
    }

    @PostMapping(value = "/mark")
    public Response<Short> markPaper(@RequestBody JSONArray jsonArray,
                                     @RequestParam("uid") Integer uid,
                                     @RequestParam("pid") Integer pid,
                                     @RequestAttribute("user") UserDetails user){
        return paperService.markTestPaper(pid, uid, Integer.valueOf(user.getUsername()), jsonArray);
    }

    @GetMapping(value = "/showScore/{pid}")
    public  Response<ScoreStatisticVO> showScore(@PathVariable("pid") Integer pid,
                                                 @RequestAttribute("user") UserDetails user){
        return paperService.showScore(pid, Integer.valueOf(user.getUsername()));
    }
}
