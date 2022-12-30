package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.dto.permission.CreatePermissionRequestDTO;
import com.hsuyeung.blog.model.dto.permission.UpdatePermissionRequestDTO;
import com.hsuyeung.blog.model.entity.PermissionEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.permission.EnabledPermissionVO;
import com.hsuyeung.blog.model.vo.permission.PermissionInfoVO;
import com.hsuyeung.blog.model.vo.permission.PermissionVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 权限服务接口
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Validated
public interface IPermissionService extends IService<PermissionEntity> {
    /**
     * 根据权限 id 查询权限
     *
     * @param pids             权限 id 集合
     * @param onlyQueryEnabled 是否只查询开启状态的数据
     * @return 权限集合
     */
    Set<PermissionVO> getPermissions(Collection<Long> pids, boolean onlyQueryEnabled);

    /**
     * 创建一个权限
     *
     * @param createPermissionRequestDTO 创建权限请求参数
     * @return 创建成功返回 true，否则返回 false
     */
    boolean createPermission(@Valid CreatePermissionRequestDTO createPermissionRequestDTO);

    /**
     * 删除一个权限
     *
     * @param pid 权限 id
     */
    void deletePermission(Long pid);

    /**
     * 更新权限
     *
     * @param updatePermissionRequestDTO 更新权限请求参数
     * @return 更新成功返回 true，否则返回 false
     */
    boolean updatePermission(@Valid UpdatePermissionRequestDTO updatePermissionRequestDTO);

    /**
     * 锁定一个权限
     *
     * @param pid 权限 id
     */
    void lockPermission(Long pid);

    /**
     * 解锁一个权限
     *
     * @param pid 权限 id
     */
    void unlockPermission(Long pid);

    /**
     * 获取所有可用状态的权限信息
     *
     * @return 可用状态的权限列表
     */
    List<EnabledPermissionVO> getAllEnabledPermission();

    /**
     * 获取指定权限 id 的详细信息
     *
     * @param pids             权限 id 集合
     * @param onlyQueryEnabled 是否只查询启用状态的
     * @return 指定权限 id 的详细信息
     */
    List<EnabledPermissionVO> getPermissionInfo(Collection<Long> pids, boolean onlyQueryEnabled);

    /**
     * 分页查询权限列表
     *
     * @param path           权限路径，右模糊
     * @param method         请求方法类型，全匹配
     * @param permissionDesc 权限描述，全模糊
     * @param enabled        是否可用，全匹配
     * @param pageNum        页码
     * @param pageSize       每页数量
     * @return 权限分页列表
     */
    PageVO<PermissionInfoVO> getPermissionPage(String path, String method, String permissionDesc, Boolean enabled,
                                               Integer pageNum, Integer pageSize);
}
