package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.TestPaper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestPaperMapper {
    int insert(TestPaper record);

    List<TestPaper> listTestPapers();

    TestPaper getTestPaperById(@Param("id") Integer id);

    Integer deleteTestPaperById(@Param("id" )Integer id);

    Integer updateTestPaper(@Param("testPaper") TestPaper testPaper);
}