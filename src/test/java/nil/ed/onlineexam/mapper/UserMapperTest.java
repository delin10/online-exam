package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.AbstractServiceTest;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class UserMapperTest extends AbstractServiceTest {
    @Resource
    private UserMapper userMapper;

    @Test
    public void listUserVOs() {
        printAsJsonString(userMapper.listUserVOs(0, Integer.MAX_VALUE));
    }
}