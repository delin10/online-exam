package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.Role;
import nil.ed.onlineexam.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    int insert(Role record);

    int insertSelective(Role record);


    List<Role> listRoles(Integer pageNo, Integer pageSize);

    Integer countRoles();

    Role getRoleById(@Param("id") Integer id);

    Integer deleteRoleById(@Param("id" )Integer id);

    Integer updateRole(@Param("role") Role role);
}