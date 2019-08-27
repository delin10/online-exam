package nil.ed.book.mapper;

import nil.ed.book.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int insert(Role record);

    int insertSelective(Role record);

    List<Role> listRoles();

    Role getRoleById(@Param("id") Integer id);

    int deleteRoleById(@Param("id") Integer id);

    int updateRole(@Param("role") Role role);

}