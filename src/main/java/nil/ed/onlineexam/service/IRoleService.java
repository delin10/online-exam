package nil.ed.onlineexam.service;

import nil.ed.onlineexam.common.PageResult;
import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * 角色管理
 */
public interface IRoleService {
    /**
     * 添加角色
     * @param role 角色
     * @param creator 创建者
     * @return 是否添加成功
     */
    Response<Void> addRole(Role role, Integer creator);

    /**
     * 删除角色
     * @param roleId 角色id
     * @return 是否删除成功
     */
    Response<Void> deleteRole(Integer roleId);

    /**
     * 根据id获取角色信息
     * @param Id 角色id
     * @return 角色信息
     */
    Response<Role> getRoleById(Integer Id);

    /**
     * 分页查询角色
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    Response<PageResult<Role>> listRolesByPage(Integer pageNo, Integer pageSize);

    /**
     * 修改角色权限
     * @param roleId 角色id
     * @param resources 权限
     * @param operator 操作人
     * @return 结果信息
     */
    Response<Void> authResources(Integer roleId, Set<Integer> resources, Integer operator);
}
