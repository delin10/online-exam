package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper {
    int insert(Permission record);

    int insertSelective(Permission record);

    List<Permission> listAllResources();

    Permission getResourceById(@Param("id") Integer id);

    int deleteResourceById(@Param("id") Integer id);

    int updateResource(@Param("permission") Permission permission);

    List<Permission> listPermissionsOfRole(@Param("roleId") Integer roleId);
}