package nil.ed.onlineexam.service;

import nil.ed.onlineexam.mapper.RoleMapper;
import nil.ed.onlineexam.mapper.UserMapper;
import nil.ed.onlineexam.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;

    public UserVO getUserInfo(Integer id){
        return null;
    }

}
