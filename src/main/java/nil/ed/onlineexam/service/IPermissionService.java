package nil.ed.onlineexam.service;

import nil.ed.onlineexam.common.Response;
import nil.ed.onlineexam.entity.Permission;

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
     * @return 添加结果信息
     */
    Response<Void> addPermission(Permission permission);

    /**
     * 根据id删除资源
     * @param id 资源id
     * @return 删除结果信息
     */
    Response<Void> deletePermission(Integer id);
}
