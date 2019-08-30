package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.AbstractServiceTest;
import nil.ed.onlineexam.common.enumm.CourseStatusEnum;
import nil.ed.onlineexam.entity.Course;
import org.junit.Test;

import javax.annotation.Resource;

import java.time.Instant;

import static org.junit.Assert.*;

public class CourseMapperTest extends AbstractServiceTest {
    @Resource
    private CourseMapper mapper;

    @Test
    public void insert() {
        Course course = new Course();
        course.setName("test");
        course.setStatus(CourseStatusEnum.READY.getCode());
        course.setTeacher(2);
        course.setCreateTime(Instant.now().toEpochMilli());
        course.setUpdateTime(Instant.now().toEpochMilli());
        course.setStartTime(Instant.now().toEpochMilli());
        course.setEndTime(Instant.now().toEpochMilli()+1000);
        mapper.insert(course);
        mapper.insert(course);
        mapper.insert(course);
        mapper.insert(course);
        mapper.insert(course);
        mapper.insert(course);
        mapper.insert(course);
    }

    @Test
    public void listCourses() {
        printAsJsonString(mapper.listCourses());
    }

    @Test
    public void getCourseById() {
        printAsJsonString(mapper.getCourseById(1));
        printAsJsonString(mapper.getCourseById(2));
    }

    @Test
    public void deleteCourseById() {
        //System.out.println(mapper.deleteCourseById(1));
    }

    @Test
    public void updateCourse() {
        Course course = new Course();
        course.setId(1);
        course.setName("修改");
        mapper.updateCourse(course);
    }
}