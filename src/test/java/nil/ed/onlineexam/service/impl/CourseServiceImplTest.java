package nil.ed.onlineexam.service.impl;

import nil.ed.onlineexam.AbstractServiceTest;
import nil.ed.onlineexam.service.ICourseService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class CourseServiceImplTest extends AbstractServiceTest {
    @Resource
    private ICourseService courseService;

    @Test
    public void listPublishedTestPapersOf() {
        printAsJsonString(courseService.listPublishedTestPapersOf(10, 2));
    }
}