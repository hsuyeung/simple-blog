package com.hsuyeung.blog.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsuyeung.blog.exception.SystemInternalException;
import com.hsuyeung.blog.model.vo.permission.PermissionVO;
import com.hsuyeung.blog.service.IPermissionService;
import com.hsuyeung.blog.service.IRolePermissionService;
import com.hsuyeung.blog.service.IUserRoleService;
import com.hsuyeung.blog.service.IUserService;
import com.hsuyeung.blog.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * RBAC 相关的缓存
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class RbacCache {
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;
    private final IUserRoleService userRoleService;
    private final IRolePermissionService rolePermissionService;
    private final IPermissionService permissionService;
    private final IUserService userService;

    public Set<PermissionVO> getUserPermissions(String key, Long uid) {
        String value = (String) redisUtil.hGet(key, String.valueOf(uid));
        try {
            return value == null ? null : objectMapper.readValue(value, new TypeReference<Set<PermissionVO>>() {
            });
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("反序列化 PermissionDTO 对象失败", e);
        }
    }

    public void cacheUserPermissions(String key, Long uid, Set<PermissionVO> permissions) {
        try {
            redisUtil.hPut(key, String.valueOf(uid), objectMapper.writeValueAsString(permissions));
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("序列化 PermissionDTO 对象失败", e);
        }
    }

    public boolean deleteCache(String key, Long uid) {
        redisUtil.hDelete(key, String.valueOf(uid));
        return true;
    }

    /**
     * 刷新所有用户的权限缓存
     *
     * @param key 缓存的 key
     */
    public void refreshCache(String key) {
        Set<Long> enabledUids = userService.getUids(true);
        enabledUids.forEach(uid -> deleteCache(key, uid));
        Map<Long, Set<Long>> userRoleMap = userRoleService.getUidRidsMap(enabledUids);
        userRoleMap.forEach((uid, rids) -> {
            Set<Long> pids = rolePermissionService.getPermissionIds(rids, true);
            if (!CollectionUtils.isEmpty(pids)) {
                cacheUserPermissions(key, uid, permissionService.getPermissions(pids, true));
            }
        });
    }
}
