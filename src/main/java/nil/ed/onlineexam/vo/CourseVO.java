package nil.ed.onlineexam.vo;

import lombok.Data;
import nil.ed.onlineexam.entity.Course;

@Data
public class CourseVO extends Course {
    private String teacherName;
}
