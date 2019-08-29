package nil.ed.onlineexam.security;

import lombok.extern.slf4j.Slf4j;
import nil.ed.onlineexam.entity.Permission;
import nil.ed.onlineexam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * 决策逻辑
 * @author lidelin
 * @since 2019-08-29
 */
@Slf4j
public class AccessDecisionManagerImpl implements AccessDecisionManager {
    private AntPathMatcher pathMatcher;
    @Autowired
    private RoleService roleService;

    {
        pathMatcher = new AntPathMatcher();
    }

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        if (CollectionUtils.isEmpty(collection)){
            return;
        }

        FilterInvocation request = (FilterInvocation) o;
        String uri = request.getHttpRequest().getRequestURI();
        log.info("===> uri = {}", uri);
        Collection<? extends GrantedAuthority> grantedAuthorityList = authentication.getAuthorities();

        log.info("===> grantedAuthorityList = {}", grantedAuthorityList);

        GrantedAuthority authority = grantedAuthorityList.iterator().next();
        Integer roleId = -1;
        if (authority.getAuthority().equals("ROLE_ANONYMOUS")){

        }else {
            roleId = Integer.valueOf(authority.getAuthority());
        }
        // 获取并且映射权限列表
        List<Permission> permissionList = roleService.listPermissionOfRole(roleId);
        ConfigAttribute configAttribute = collection.iterator().next();

        permissionList.stream().map(Permission::getUri)
                .filter(configAttribute.getAttribute()::equals)
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("access denied"));
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
