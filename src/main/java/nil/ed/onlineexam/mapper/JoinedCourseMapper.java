package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.JoinedCourse;

public interface JoinedCourseMapper {
    int insert(JoinedCourse record);

    int insertSelective(JoinedCourse record);
}