package nil.ed.onlineexam.controller.view;

import nil.ed.onlineexam.service.ITestPaperService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/exam/testPaper/view")
public class TestPaperViewController {
    @Resource
    private ITestPaperService testPaperService;

    @GetMapping(value = "/get/{id}", produces = MediaType.ALL_VALUE)
    public String greeting(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("paper", testPaperService.getTestPaper(id));
        return "test/showTestPaper";
    }
}
