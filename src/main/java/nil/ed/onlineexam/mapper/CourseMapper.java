package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.Course;
import nil.ed.onlineexam.entity.JoinedCourse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseMapper {
    void insert(@Param("course") Course course);

    List<Course> listCourses();

    Course getCourseById(@Param("id") Integer id);

    Integer deleteCourseById(@Param("id" )Integer id);

    Integer updateCourse(@Param("course") Course course);

    Integer joinCourse(@Param("joinedCourse") JoinedCourse joinedCourse);
}
