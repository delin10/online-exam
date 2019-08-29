package nil.ed.onlineexam.controller;

import nil.ed.onlineexam.aop.annotation.MethodInvokeLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam")
public class TestController {

    @MethodInvokeLog
    @GetMapping(value = "/api/{id}")
    public String test(@PathVariable("id") String id){
        return "test"+id;
    }
}
