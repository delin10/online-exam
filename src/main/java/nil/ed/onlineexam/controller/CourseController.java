package nil.ed.onlineexam.controller;

import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.Course;
import nil.ed.onlineexam.service.ICourseService;
import nil.ed.onlineexam.vo.BaseTestPaperVO;
import nil.ed.onlineexam.vo.CourseVO;
import nil.ed.onlineexam.vo.CourseWithStudentsVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/exam/course")
public class CourseController {
    @Resource
    private ICourseService courseService;

    @PostMapping("/join/{cid}")
    public Response<Void> joinCourse(@PathVariable("cid") Integer cid,
                                     @RequestAttribute("user")UserDetails user){
        return courseService.joinCourse(Integer.valueOf(user.getUsername()),cid);
    }

    @GetMapping("/list/joined")
    public Response<PageResult<CourseVO>> listJoinedCourse(@RequestAttribute("user") UserDetails user){
        return courseService.listJoinedCourses(Integer.valueOf(user.getUsername()));
    }

    @GetMapping(value="/joined/test/list/{cid}")
    public Response<PageResult<BaseTestPaperVO>> addTestPaper(@PathVariable("cid") Integer cid,
                                                  @RequestAttribute("user") UserDetails user){
        return courseService.listPublishedTestPapersOf(cid, Integer.valueOf(user.getUsername()));
    }

    @GetMapping(value = "/list")
    public Response<PageResult<CourseVO>> listCourses(){
        return courseService.listCourses();
    }

    @GetMapping(value = "/list/own")
    public Response<PageResult<CourseVO>> listCourses(@RequestAttribute("user") UserDetails user){
        return courseService.listCoursesOfTeacher(Integer.valueOf(user.getUsername()));
    }

    @GetMapping(value = "/list/students")
    public Response<PageResult<CourseWithStudentsVO>> listStudents(@RequestParam(value = "cid", required = false) Integer cid,
                                                                   @RequestAttribute("user") UserDetails user){
        return courseService.listCourseWithStudentsVOs(cid, Integer.valueOf(user.getUsername()));
    }

    @PostMapping(value = "/add")
    public Response<Void> addCourse(@RequestBody Course course,
                                    @RequestAttribute("userId") Integer currentUserId){
        return courseService.addCourse(course, currentUserId);
    }
}
