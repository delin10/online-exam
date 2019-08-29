package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.AbstractServiceTest;
import org.junit.Test;

import javax.annotation.Resource;

public class RoleMapperTest extends AbstractServiceTest {
    @Resource
    private RoleMapper mapper;

    @Test
    public void testListRoles() {
        printAsJsonString(mapper.listRoles());
    }
}
