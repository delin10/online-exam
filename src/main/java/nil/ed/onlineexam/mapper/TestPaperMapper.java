package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.TestPaper;
import nil.ed.onlineexam.vo.TestPaperVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestPaperMapper {
    int insert(TestPaper record);

    List<TestPaper> listTestPapers();

    TestPaperVO getTestPaperById(@Param("id") Integer id);

    Integer deleteTestPaperById(@Param("id" )Integer id);

    Integer updateTestPaper(@Param("testPaper") TestPaper testPaper);

    Integer updatePublishedStatus(@Param("status") byte status, @Param("id") Integer pid, @Param("uid") Integer uid);
}