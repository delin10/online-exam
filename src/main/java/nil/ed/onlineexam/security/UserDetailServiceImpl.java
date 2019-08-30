package nil.ed.onlineexam.security;

import nil.ed.onlineexam.aop.annotation.MethodInvokeLog;
import nil.ed.onlineexam.entity.Permission;
import nil.ed.onlineexam.entity.User;
import nil.ed.onlineexam.mapper.PermissionMapper;
import nil.ed.onlineexam.mapper.RoleMapper;
import nil.ed.onlineexam.mapper.UserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户权限获取服务
 * @author lidelin
 * @since 2019-08-29
 */
public class UserDetailServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private RoleMapper roleMapper;

    @MethodInvokeLog
    @Override
    public UserDetails loadUserByUsername(String idStr) throws UsernameNotFoundException {
        try {
            // 获取用户信息
            Integer id = Integer.valueOf(idStr);
            User user = userMapper.getUserById(id);

            if (user != null){
                // 获取并且映射权限列表
                List<Permission> permissionLS = permissionMapper.listPermissionsOfRole(user.getRole());

                return new org.springframework.security.core.userdetails.User(String.valueOf(user.getId()),
                        user.getPasswordMd5(), Collections.singletonList(new SimpleGrantedAuthority(String.valueOf(user.getRole()))));
            }else{
                throw new UsernameNotFoundException(MessageFormat.format("No such user whose uid = {0}", idStr));
            }
        }catch (Exception e){
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

}
