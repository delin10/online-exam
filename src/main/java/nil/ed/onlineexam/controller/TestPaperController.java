package nil.ed.onlineexam.controller;

import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.service.ITestPaperService;
import nil.ed.onlineexam.vo.TestPaperVO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/exam/testPaper")
public class TestPaperController {
    @Resource
    private ITestPaperService paperService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Response<TestPaperVO> getTestPaper(@PathVariable("id") Integer id, Model model){
        return paperService.getTestPaper(id);
    }
}
