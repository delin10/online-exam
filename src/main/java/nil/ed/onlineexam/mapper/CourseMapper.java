package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.Course;
import nil.ed.onlineexam.entity.JoinedCourse;
import nil.ed.onlineexam.vo.CourseVO;
import nil.ed.onlineexam.vo.CourseWithStudentsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface CourseMapper {
    void insert(@Param("course") Course course);

    List<CourseVO> listCourses(@Param("currentUser") Integer currentUser,
                               @Param("pageStart") Integer pageStart,
                               @Param("pageSize") Integer pageSize);

    Integer countCourses();

    Integer countCoursesIfUser(@Param("currentUser") Integer currentUser);

    Course getCourseById(@Param("id") Integer id);

    Integer deleteCourseById(@Param("id" )Integer id);

    Integer updateCourse(@Param("course") Course course);

    Integer joinCourse(@Param("joinedCourse") JoinedCourse joinedCourse);

    List<CourseVO> listJoinedCourses(@Param("uid") Integer uid, @Param("pageStart") Integer pageStart, @Param("pageSize") Integer pageSize);

    Integer countJoinedCourses(@Param("uid") Integer uid);

    /**
     * count 使用countCourses
     * @param cid 课程id
     * @param currentUser 用户id
     * @param pageStart 分页起始
     * @param pageSize 分页大小
     * @return 结果
     */
    List<CourseWithStudentsVO> listCourseWithStudents(@Param("cid") Integer cid,
                                                      @Param("currentUser") Integer currentUser,
                                                      @Param("pageStart") Integer pageStart,
                                                      @Param("pageSize") Integer pageSize);

}
