package nil.ed.onlineexam.controller;

import com.alibaba.fastjson.JSONObject;
import nil.ed.onlineexam.common.CommonVO;
import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.service.ITestPaperService;
import nil.ed.onlineexam.vo.TestPaperWithQuestionsVO;
import nil.ed.onlineexam.vo.UserTestVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    public Response<PageResult<UserTestVO>> listCanJoinOrHaveJoinedTests(@RequestAttribute("user") UserDetails user){
        return paperService.listCanJoinOrHaveJoinedTests(Integer.valueOf(user.getUsername()));
    }
}
