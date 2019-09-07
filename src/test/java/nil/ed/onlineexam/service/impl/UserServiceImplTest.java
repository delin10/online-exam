package nil.ed.onlineexam.service.impl;

import nil.ed.onlineexam.AbstractServiceTest;
import nil.ed.onlineexam.service.IUserService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class UserServiceImplTest extends AbstractServiceTest {
    @Resource
    private IUserService userService;

    @Test
    public void getUser() {
        printAsJsonString(userService.getUser(2));
    }
}