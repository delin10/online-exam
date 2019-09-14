package nil.ed.onlineexam.vo;

import lombok.Data;
import nil.ed.onlineexam.entity.Course;

import java.util.List;

@Data
public class CourseWithStudentsVO extends Course {
    private List<UserOfCourseVO> students;
}
