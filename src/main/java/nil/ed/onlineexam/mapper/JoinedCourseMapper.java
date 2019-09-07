package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.JoinedCourse;
import org.apache.ibatis.annotations.Param;

public interface JoinedCourseMapper {
    int insert(JoinedCourse record);

    int insertSelective(JoinedCourse record);
}