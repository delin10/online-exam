package nil.ed.onlineexam.security;

import lombok.extern.slf4j.Slf4j;
import nil.ed.onlineexam.aop.annotation.MethodInvokeLog;
import nil.ed.onlineexam.entity.Permission;
import nil.ed.onlineexam.mapper.PermissionMapper;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用于判断是否在权限表中
 */
@Slf4j
public class InvocationSecurityMetadataSourceServiceImpl implements SecurityMetadataSource {
    private static AntPathMatcher antPathMatcher = new AntPathMatcher();
    private static final Object LOCK = new Object();
    @Resource
    private PermissionMapper permissionMapper;
    private volatile Map<String,Collection<ConfigAttribute>> configAttributeList;


    @MethodInvokeLog
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        /*
        返回null不会执行decision逻辑
         */
        loadDefinePermissions();

        FilterInvocation invocation = (FilterInvocation) object;
        log.info("==> params: {}", invocation);
        String uri = invocation.getHttpRequest().getRequestURI();
        return configAttributeList.entrySet()
                .stream()
                .filter(entry -> antPathMatcher.match(entry.getKey(), uri))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(Collections.emptyList());
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    private void loadDefinePermissions(){
        if (configAttributeList == null) {
            synchronized(LOCK){
                if (configAttributeList == null) {
                    List<Permission> permissionList = permissionMapper.listAllResources();
                    configAttributeList = new HashMap<>(permissionList.size() + 1, 1);
                    for (Permission permission : permissionList){
                        configAttributeList.put(permission.getUri(),Collections.singleton(new SecurityConfig(permission.getUri())));
                    }
                }
            }
        }
    }
}
