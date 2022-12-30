package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.entity.UserRoleEntity;

import java.util.Map;
import java.util.Set;

/**
 * 用户-角色服务接口
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
public interface IUserRoleService extends IService<UserRoleEntity> {
    /**
     * 查询指定用户 id 的角色 id 集合
     *
     * @param uid              用户 id
     * @param onlyQueryEnabled 是否只查询有效的用户 id 关联的角色 id 集合
     * @return 用户所拥有的角色 id 集合
     */
    Set<Long> getRoleIds(Long uid, boolean onlyQueryEnabled);

    /**
     * 根据用户 id 删除角色
     *
     * @param uid 用户 id
     * @return 删除成功返回 true，否则返回 false
     */
    boolean deleteByUid(Long uid);

    /**
     * 根据角色 id 删除用户
     *
     * @param rid 角色 id
     * @return 删除成功返回 true，否则返回 false
     */
    boolean deleteByRid(Long rid);

    /**
     * 获取指定用户拥有的角色集合
     *
     * @param uids 用户 id 集合
     * @return key-用户 id，value-角色 id 集合
     */
    Map<Long, Set<Long>> getUidRidsMap(Set<Long> uids);
}
