package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.entity.RolePermissionEntity;

import java.util.Collection;
import java.util.Set;

/**
 * 角色-权限服务接口
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
public interface IRolePermissionService extends IService<RolePermissionEntity> {
    /**
     * 获取指定角色 id 的权限 id 集合
     *
     * @param rids             角色 id 集合
     * @param onlyQueryEnabled 是否只查询可用的角色 id 关联的权限 id 集合
     * @return 权限 id 集合
     */
    Set<Long> getPermissionIds(Collection<Long> rids, boolean onlyQueryEnabled);

    /**
     * 根据角色 id 删除权限
     *
     * @param rid 角色 id
     * @return 删除成功返回 true，否则返回 false
     */
    boolean deleteByRid(Long rid);

    /**
     * 根据权限 id 删除
     *
     * @param pid 权限 id
     * @return 删除成功返回 true，否则返回 false1
     */
    boolean deleteByPid(Long pid);

    /**
     * 获取拥有指定权限的所有角色 id
     *
     * @param pid 权限 id
     * @return 角色 id 集合
     */
    Set<Long> getRoleIds(Long pid);
}
