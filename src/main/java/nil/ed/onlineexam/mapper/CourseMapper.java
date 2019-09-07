package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.Course;
import nil.ed.onlineexam.entity.JoinedCourse;
import nil.ed.onlineexam.vo.CourseVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseMapper {
    void insert(@Param("course") Course course);

    List<Course> listCourses();

    Course getCourseById(@Param("id") Integer id);

    Integer deleteCourseById(@Param("id" )Integer id);

    Integer updateCourse(@Param("course") Course course);

    Integer joinCourse(@Param("joinedCourse") JoinedCourse joinedCourse);

    List<CourseVO> listJoinedCourses(@Param("uid") Integer uid, @Param("pageStart") Integer pageStart, @Param("pageSize") Integer pageSize);

    Integer countJoinedCourses(@Param("uid") Integer uid);
}
