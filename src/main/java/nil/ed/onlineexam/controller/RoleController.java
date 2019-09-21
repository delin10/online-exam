package nil.ed.onlineexam.controller;

import com.alibaba.fastjson.JSONObject;
import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.Role;
import nil.ed.onlineexam.service.IRoleService;
import nil.ed.onlineexam.vo.AuthRoleResourceVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/exam/role")
public class RoleController {
    @Resource
    @Qualifier("roleService")
    private IRoleService roleService;

    @GetMapping(value = "/list")
    public Response<PageResult<Role>> listRoles(@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = "" + Integer.MAX_VALUE) Integer pageSize){
        return roleService.listRolesByPage(pageNo, pageSize);
    }

    @PostMapping(value = "/auth")
    public Response<Void> authResources(@RequestBody AuthRoleResourceVO authRoleResourceVO,
                                        @RequestAttribute("user") UserDetails user){
        return roleService.authResources(authRoleResourceVO.getRoleId(),
                authRoleResourceVO.getResources(),
                Integer.valueOf(user.getUsername()));
    }

    @PostMapping(value = "/add")
    public Response<Void> addRole(@RequestBody Role role,
                                  @RequestAttribute("user") UserDetails user){
        return roleService.addRole(role, Integer.valueOf(user.getUsername()));
    }
}
