package nil.ed.book.mapper;

import nil.ed.book.AbstractServiceTest;
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
