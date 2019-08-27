package nil.ed.book.mapper;

import nil.ed.book.entity.Role;
import nil.ed.book.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    List<User> listUsers();

    User getUserById(@Param("id") Integer id);

    int deleteUserById(@Param("id") Integer id);

    int updateUser(@Param("user") User user);
}