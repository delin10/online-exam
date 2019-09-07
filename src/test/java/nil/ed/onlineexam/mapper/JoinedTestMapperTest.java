package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.AbstractServiceTest;
import nil.ed.onlineexam.entity.JoinedTest;
import org.junit.Test;

import javax.annotation.Resource;

import java.time.Instant;

import static org.junit.Assert.*;

public class JoinedTestMapperTest extends AbstractServiceTest {
    @Resource
    private JoinedTestMapper joinedTestMapper;

    @Test
    public void insert() {
        JoinedTest joinedTest = new JoinedTest();
        joinedTest.setScore((short)-1);
        joinedTest.setCreateTime(Instant.now().toEpochMilli());
        joinedTest.setUpdateTime(Instant.now().toEpochMilli());
        joinedTest.setUid(2);
        joinedTest.setTid(2);
        joinedTestMapper.insert(joinedTest);

    }

    @Test
    public void updateScore() {
        JoinedTest joinedTest = new JoinedTest();
    }
}