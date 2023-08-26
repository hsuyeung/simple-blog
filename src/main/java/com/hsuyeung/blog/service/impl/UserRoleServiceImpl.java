package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.mapper.UserRoleMapper;
import com.hsuyeung.blog.model.entity.UserRoleEntity;
import com.hsuyeung.blog.service.IUserRoleService;
import com.hsuyeung.blog.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户-角色服务实现类
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Service("userRoleService")
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements IUserRoleService {
    private final IUserService userService;

    @Override
    public Set<Long> getRoleIds(Long uid, boolean onlyQueryEnabled) {
        if (onlyQueryEnabled && !userService.isExist(uid, true)) {
            return Collections.emptySet();
        }
        return lambdaQuery()
                .select(UserRoleEntity::getRid)
                .eq(UserRoleEntity::getUid, uid)
                .list()
                .stream()
                .map(UserRoleEntity::getRid)
                .collect(Collectors.toSet());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByUid(Long uid) {
        return lambdaUpdate()
                .eq(UserRoleEntity::getUid, uid)
                .remove();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByRid(Long rid) {
        return lambdaUpdate()
                .eq(UserRoleEntity::getRid, rid)
                .remove();
    }

    @Override
    public Map<Long, Set<Long>> getUidRidsMap(Set<Long> uids) {
        return lambdaQuery()
                .select(UserRoleEntity::getUid, UserRoleEntity::getRid)
                .in(UserRoleEntity::getUid, uids)
                .list()
                .stream()
                .collect(Collectors.groupingBy(UserRoleEntity::getUid,
                        Collectors.mapping(UserRoleEntity::getRid, Collectors.toSet())));
    }
}
