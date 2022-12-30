package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.mapper.RolePermissionMapper;
import com.hsuyeung.blog.model.entity.RolePermissionEntity;
import com.hsuyeung.blog.service.IRolePermissionService;
import com.hsuyeung.blog.service.IRoleService;
import com.hsuyeung.blog.util.AssertUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity> implements IRolePermissionService {
    @Resource
    @Lazy
    private IRoleService roleService;

    @Override
    public Set<Long> getPermissionIds(Collection<Long> rids, boolean onlyQueryEnabled) {
        AssertUtil.notEmpty(rids, "rids 不能为空");
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
        AssertUtil.notNull(rid, "rid 不能为空");
        return lambdaUpdate()
                .eq(RolePermissionEntity::getRid, rid)
                .remove();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByPid(Long pid) {
        AssertUtil.notNull(pid, "pid 不能为空");
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
