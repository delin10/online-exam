package nil.ed.onlineexam.controller;

import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.User;
import nil.ed.onlineexam.service.IUserService;
import nil.ed.onlineexam.vo.UserVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/exam/user")
public class UserController {
    @Resource
    @Qualifier("userService")
    private IUserService userService;

    @PostMapping("/add")
    public Response<UserVO> addUser(@RequestBody User user){
        return userService.register(user);
    }

    @PostMapping("/auth")
    public Response<Void> authRoleToUser(@RequestParam("userId") Integer userId,
                                         @RequestParam("roleId") Integer roleId,
                                         @RequestAttribute("user")UserDetails user){
        return userService.changeRoleOfUser(userId, roleId, Integer.valueOf(user.getUsername()));
    }

    @GetMapping("/list/all")
    public Response<PageResult<UserVO>> listUsers(){
        return userService.listUsers();
    }
}
