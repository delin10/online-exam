package nil.ed.onlineexam.mapper;

import nil.ed.onlineexam.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    List<User> listUsers();

    User getUserById(@Param("id") Integer id);

    Integer deleteUserById(@Param("id" )Integer id);

    Integer updateUser(@Param("user") User user);
}