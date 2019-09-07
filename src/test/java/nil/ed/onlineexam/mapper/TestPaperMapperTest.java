package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.AbstractServiceTest;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class TestPaperMapperTest extends AbstractServiceTest {
    @Resource
    private TestPaperMapper testPaperMapper;

    @Test
    public void listPublishedTestPapersOf() {
        printAsJsonString(testPaperMapper.listPublishedTestPapersOf(2,10, 0,20));
    }
}