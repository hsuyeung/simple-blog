package com.hsuyeung.blog.service.impl;

import com.hsuyeung.blog.cache.RbacCache;
import com.hsuyeung.blog.model.vo.permission.PermissionVO;
import com.hsuyeung.blog.service.*;
import com.hsuyeung.blog.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.REDIS_USER_PERMISSION_KEY;
import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.USER_TOKEN_SECRET;

/**
 * RBAC 认证服务实现类
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Slf4j
@Service("rbacAuthorityService")
@RequiredArgsConstructor
public class RbacAuthorityServiceImpl implements IRbacAuthorityService {
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private final RbacCache rbacCache;
    private final IUserService userService;
    private final IUserRoleService userRoleService;
    private final IRolePermissionService rolePermissionService;
    private final IPermissionService permissionService;
    private final ISystemConfigService systemConfigService;
    private final IUserTokenService userTokenService;

    @Override
    public boolean hasPermission(HttpServletRequest request) {
        Long uid = userTokenService.getUserIdFromRequestHeader(request);
        return this.checkRequest(request, uid);
    }

    @Override
    public boolean hasPermission(HttpServletRequest request, String token) {
        Long uid = JwtUtil.getClaim(token, "id", Long.class, systemConfigService.getConfigValue(USER_TOKEN_SECRET, String.class));
        return this.checkRequest(request, uid);
    }

    // --------------------------------------------- PRIVATE METHOD ---------------------------------------------

    /**
     * 检查指定 id 的用户是否拥有当前请求的访问权限
     *
     * @param request 请求
     * @param uid     用户 id
     * @return 有权限返回 true。否则返回 false
     */
    private boolean checkRequest(HttpServletRequest request, Long uid) {
        if (uid <= 0L || !userService.isExist(uid, true)) {
            log.error(String.format("id 为 %d 的用户不存在或已被锁定", uid));
            return false;
        }
        String key = systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class);
        Set<PermissionVO> permissions = rbacCache.getUserPermissions(key, uid);
        if (CollectionUtils.isEmpty(permissions)) {
            Set<Long> rids = userRoleService.getRoleIds(uid, true);
            if (CollectionUtils.isEmpty(rids)) {
                return false;
            }
            Set<Long> pids = rolePermissionService.getPermissionIds(rids, true);
            if (CollectionUtils.isEmpty(pids)) {
                return false;
            }
            permissions = permissionService.getPermissions(pids, true);
            if (CollectionUtils.isEmpty(permissions)) {
                return false;
            }
            rbacCache.cacheUserPermissions(key, uid, permissions);
        }
        return permissions.stream().anyMatch(permission ->
                ANT_PATH_MATCHER.match(permission.getPath(), request.getRequestURI())
                        && request.getMethod().equalsIgnoreCase(permission.getMethod()));
    }
}
