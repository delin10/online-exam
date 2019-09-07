package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.TestPaper;
import nil.ed.onlineexam.vo.BaseTestPaperVO;
import nil.ed.onlineexam.vo.TestPaperWithQuestionsVO;
import nil.ed.onlineexam.vo.UserTestVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestPaperMapper {
    int insert(TestPaper record);

    List<TestPaper> listTestPapers();

    TestPaperWithQuestionsVO getTestPaperById(@Param("id") Integer id);

    Integer deleteTestPaperById(@Param("id" )Integer id);

    Integer updateTestPaper(@Param("testPaper") TestPaper testPaper);

    Integer updatePublishedStatus(@Param("status") byte status, @Param("id") Integer pid, @Param("uid") Integer uid);

    List<BaseTestPaperVO> listPublishedTestPapersOf(@Param("uid") Integer uid, @Param("cid") Integer cid, @Param("pageStart") Integer pageStart, @Param("pageSize") Integer pageSize);

    Integer countPublishedTestPapersOf(@Param("uid") Integer uid, @Param("cid") Integer cid);

    TestPaper getCanJoinedTestOrNULL(@Param("uid") Integer uid, @Param("tid") Integer tid);

    List<UserTestVO> listUserTestsOf(@Param("uid") Integer uid, @Param("cid") Integer cid, @Param("pageStart") Integer pageStart, @Param("pageSize") Integer pageSize);

    Integer countUserTestsOf(@Param("uid") Integer uid, @Param("cid") Integer cid);

}