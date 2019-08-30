package nil.ed.onlineexam.service;

import nil.ed.onlineexam.entity.Permission;
import nil.ed.onlineexam.mapper.PermissionMapper;
import nil.ed.onlineexam.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    public List<Permission> listPermissionOfRole(Integer roleId){
        return permissionMapper.listPermissionsOfRole(roleId);
    }
}
