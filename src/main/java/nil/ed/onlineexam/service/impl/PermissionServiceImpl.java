package nil.ed.onlineexam.service.impl;

import nil.ed.onlineexam.common.NormalResponseBuilder;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.ResponseCodeEnum;
import nil.ed.onlineexam.entity.Permission;
import nil.ed.onlineexam.mapper.PermissionMapper;
import nil.ed.onlineexam.service.IPermissionService;
import nil.ed.onlineexam.service.support.impl.SimpleSelectOneHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("permissionService")
public class PermissionServiceImpl implements IPermissionService {
    private static Integer ADMIN = 1;
    @Resource
    private PermissionMapper permissionMapper;


    @Override
    public Response<Permission> getPermissionById(Integer id) {
        return null;
    }

    @Override
    public Response<Void> addPermission(Permission permission, Integer uid) {
        checkPermissionRequired(permission);

        permission.setCreator(uid);
        permissionMapper.insert(permission);
        return new NormalResponseBuilder<Void>()
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .build();
    }

    @Override
    public Response<Void> deletePermission(Integer id) {
        return null;
    }

    @Override
    public Response<List<Permission>> listPermissionsOfCurrentUser(Integer uid) {
        return new SimpleSelectOneHelper<List<Permission>>()
                .operate(() -> permissionMapper.listPermissionsOfUser(uid));
    }

    @Override
    public Response<List<Permission>> listAllPermissions() {
        return new SimpleSelectOneHelper<List<Permission>>()
                .operate(() -> permissionMapper.listAllResources());
    }

    @Override
    public Response<List<Permission>> listPermissionsOfRole(Integer roleId) {
        return new SimpleSelectOneHelper<List<Permission>>()
                .operate(() -> {
                    if (roleId.equals(ADMIN)){
                        return permissionMapper.listAllResources();
                    }
                    List<Permission> permissions = permissionMapper.listPermissionsOfRole(roleId);

                    if (permissions.isEmpty() || permissions.get(0) == null){
                        return Collections.emptyList();
                    }

                    return permissions;
                });
    }


    private void checkPermissionRequired(Permission permission){
        if (permission.getUri() == null){
            throw new IllegalArgumentException("uri为空");
        }

        if (permission.getName() == null){
            throw new IllegalArgumentException("名称为空");
        }
    }
}
