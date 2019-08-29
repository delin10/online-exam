package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.TestPaper;

public interface TestPaperMapper {
    int insert(TestPaper record);

    int insertSelective(TestPaper record);
}