package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.mapper.RolePermissionMapper;
import com.hsuyeung.blog.model.entity.RolePermissionEntity;
import com.hsuyeung.blog.service.IRolePermissionService;
import com.hsuyeung.blog.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色-权限服务实现类
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Service("rolePermissionService")
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity> implements IRolePermissionService {
    private final IRoleService roleService;

    @Override
    public Set<Long> getPermissionIds(Collection<Long> rids, boolean onlyQueryEnabled) {
        if (onlyQueryEnabled) {
            rids = roleService.filterNotEnabledRid(rids);
        }
        if (CollectionUtils.isEmpty(rids)) {
            return Collections.emptySet();
        }
        return lambdaQuery()
                .select(RolePermissionEntity::getPid)
                .in(RolePermissionEntity::getRid, rids)
                .list()
                .stream()
                .map(RolePermissionEntity::getPid)
                .collect(Collectors.toSet());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByRid(Long rid) {
        return lambdaUpdate()
                .eq(RolePermissionEntity::getRid, rid)
                .remove();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByPid(Long pid) {
        return lambdaUpdate()
                .eq(RolePermissionEntity::getPid, pid)
                .remove();
    }

    @Override
    public Set<Long> getRoleIds(Long pid) {
        return lambdaQuery()
                .select(RolePermissionEntity::getRid)
                .eq(RolePermissionEntity::getPid, pid)
                .list()
                .stream()
                .map(RolePermissionEntity::getRid)
                .collect(Collectors.toSet());
    }
}
