package nil.ed.onlineexam.delagate;

import nil.ed.onlineexam.service.impl.CourseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CourseServiceDelegate {
    @Resource
    private CourseServiceImpl courseService;

    public boolean isValid(Integer cid){
        return courseService.isValid(cid);
    }
}
