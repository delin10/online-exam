package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.JoinedCourse;
import nil.ed.onlineexam.entity.JoinedTest;
import org.apache.ibatis.annotations.Param;

public interface JoinedTestMapper {
    int insert(JoinedTest joinedTest);

    int updateScore(@Param("joinedTest") JoinedTest joinedTest);
}