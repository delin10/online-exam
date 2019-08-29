package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.TestPaperContentItem;

public interface TestPaperContentItemMapper {
    int insert(TestPaperContentItem record);

    int insertSelective(TestPaperContentItem record);
}