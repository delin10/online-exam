package nil.ed.onlineexam.service.impl;

import com.alibaba.fastjson.JSON;
import nil.ed.onlineexam.aop.annotation.MethodInvokeLog;
import nil.ed.onlineexam.common.NormalResponseBuilder;
import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.common.ResponseCodeEnum;
import nil.ed.onlineexam.entity.Permission;
import nil.ed.onlineexam.entity.Role;
import nil.ed.onlineexam.mapper.PermissionMapper;
import nil.ed.onlineexam.mapper.RoleMapper;
import nil.ed.onlineexam.service.IRoleService;
import nil.ed.onlineexam.service.support.DeleterHelper;
import nil.ed.onlineexam.service.support.SelectOneHelper;
import nil.ed.onlineexam.service.support.UpdaterHelper;
import nil.ed.onlineexam.service.support.impl.SimpleSelectOneHelper;
import nil.ed.onlineexam.service.support.impl.SimpleSelectPageHelper;
import nil.ed.onlineexam.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executor;

@Service("roleService")
public class RoleServiceImpl implements IRoleService {
    private static Integer ADMIN = 1;
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    private DeleterHelper deleterHelper;

    private UpdaterHelper updaterHelper;

    private SelectOneHelper<Role> selectOneHelper = new SimpleSelectOneHelper<>();

    private Executor executor;

    @Autowired
    @Qualifier("simpleDeleteHelper")
    public void setDeleterHelper(DeleterHelper deleterHelper) {
        this.deleterHelper = deleterHelper;
    }


    @Autowired
    @Qualifier("simpleUpdateHelper")
    public void setUpdaterHelper(UpdaterHelper updaterHelper) {
        this.updaterHelper = updaterHelper;
    }

    @Autowired
    @Qualifier("commonExecutor")
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    @MethodInvokeLog
    public Response<Void> addRole(Role role, Integer creator) {
        setCreateFields(role, creator);

        roleMapper.insert(role);

        return new NormalResponseBuilder<Void>()
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .build();
    }

    @Override
    @MethodInvokeLog
    public Response<Void> deleteRole(Integer roleId) {
        return deleterHelper.operate(() -> roleMapper.deleteRoleById(roleId));
    }

    @Override
    @MethodInvokeLog
    public Response<Role> getRoleById(Integer id) {
        return selectOneHelper.operate(() -> roleMapper.getRoleById(id));
    }

    @Override
    @MethodInvokeLog
    public Response<PageResult<Role>> listRolesByPage(Integer pageNo, Integer pageSize) {
        return new SimpleSelectPageHelper<Role>(executor)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setCounter(() -> roleMapper.countRoles())
                .operate(() -> roleMapper.listRoles(PageUtils.calPageStart(pageNo, pageSize), pageSize));
    }

    @Override
    @MethodInvokeLog
    @Transactional(rollbackFor = Exception.class)
    public Response<Void> authResources(Integer roleId, Set<Integer> resources, Integer creator) {
        if (resources == null || resources.isEmpty()) {
            return new NormalResponseBuilder<Void>()
                    .setCodeEnum(ResponseCodeEnum.SUCCESS)
                    .build();
        }

        if (Objects.isNull(permissionMapper.checkIdList(resources))){
            return new NormalResponseBuilder<Void>()
                    .setCodeEnum(ResponseCodeEnum.NOT_FOUND)
                    .build();
        }

        if (roleId.equals(ADMIN)){
            return new NormalResponseBuilder<Void>()
                    .setCode(ResponseCodeEnum.FAILED.getCode())
                    .setMessage("无法修改系统管理员的权限")
                    .build();
        }

        /**
         * 返回带null的非空集合！！！！！！！！！！！！！！！！！！！！
         */
        List<Permission> permissionsOfRole = permissionMapper.listPermissionsOfRole(roleId);
        if (permissionsOfRole.isEmpty() || permissionsOfRole.get(0) == null){
            permissionsOfRole = new LinkedList<>();
        }
        List<Integer> deletedList = new LinkedList<>();
        System.out.println(JSON.toJSONString(permissionsOfRole));

        permissionsOfRole.parallelStream()
                .map(Permission::getId)
                 .forEach(r -> {
                    if (resources.contains(r)){
                        resources.remove(r);
                    }else{
                        deletedList.add(r);
                    }
                });

        deletedList.parallelStream().forEach(rid -> roleMapper.deleteResourceOfRole(roleId, rid));

        resources.parallelStream().forEach(rid -> roleMapper.addResourceOfRole(roleId, rid, creator));

        return new NormalResponseBuilder<Void>()
                .setCodeEnum(ResponseCodeEnum.SUCCESS)
                .build();
    }

    private void setCreateFields(Role role, Integer creator){
        role.setCreateTime(Instant.now().toEpochMilli());
        role.setCreator(creator);
        role.setUpdateTime(role.getCreateTime());
    }

    private void setUpdateFields(Role role, Integer updater){
        role.setUpdateTime(Instant.now().toEpochMilli());
    }
}
