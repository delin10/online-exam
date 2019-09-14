package nil.ed.onlineexam.service;

import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.Permission;

import java.util.List;

/**
 * 资源管理
 */
public interface IPermissionService {
    /**
     * 根据id获取资源信息
     * @param id 资源id
     * @return 资源信息
     */
    Response<Permission> getPermissionById(Integer id);

    /**
     * 添加资源
     * @param permission 资源
     * @param uid 创建者id
     * @return 添加结果信息
     */
    Response<Void> addPermission(Permission permission, Integer uid);

    /**
     * 根据id删除资源
     * @param id 资源id
     * @return 删除结果信息
     */
    Response<Void> deletePermission(Integer id);

    /**
     * 获取当前用户可访问的资源
     * @param uid 用户id
     * @return 资源列表
     */
    Response<List<Permission>> listPermissionsOfCurrentUser(Integer uid);

    /**
     * 显示当前的资源列表
     * @return
     */
    Response<List<Permission>> listAllPermissions();



    /**
     * 获取角色可访问的资源
     * @param roleId 角色id
     * @return 资源列表
     */
    Response<List<Permission>> listPermissionsOfRole(Integer roleId);
}
