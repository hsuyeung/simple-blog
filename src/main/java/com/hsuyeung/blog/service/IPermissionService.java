package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.permission.CreatePermissionDTO;
import com.hsuyeung.blog.model.dto.permission.PermissionSearchDTO;
import com.hsuyeung.blog.model.dto.permission.UpdatePermissionDTO;
import com.hsuyeung.blog.model.entity.PermissionEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.permission.EnabledPermissionVO;
import com.hsuyeung.blog.model.vo.permission.PermissionInfoVO;
import com.hsuyeung.blog.model.vo.permission.PermissionVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    Set<PermissionVO> getPermissions(@NotEmpty(message = "pids 不能为 空") Collection<Long> pids, boolean onlyQueryEnabled);

    /**
     * 创建一个权限
     *
     * @param createPermission 创建权限请求参数
     * @return 创建成功返回 true，否则返回 false
     */
    boolean createPermission(@NotNull(message = "createPermission 不能为 null") @Valid CreatePermissionDTO createPermission);

    /**
     * 删除一个权限
     *
     * @param pid 权限 id
     */
    void deletePermission(@NotNull(message = "pid 不能为 null") Long pid);

    /**
     * 更新权限
     *
     * @param updatePermission 更新权限请求参数
     * @return 更新成功返回 true，否则返回 false
     */
    boolean updatePermission(@NotNull(message = "updatePermission 不能为 null") @Valid UpdatePermissionDTO updatePermission);

    /**
     * 锁定一个权限
     *
     * @param pid 权限 id
     */
    void lockPermission(@NotNull(message = "pid 不能为 null") Long pid);

    /**
     * 解锁一个权限
     *
     * @param pid 权限 id
     */
    void unlockPermission(@NotNull(message = "pid 不能为 null") Long pid);

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
    List<EnabledPermissionVO> getPermissionInfo(@NotEmpty(message = "pids 不能为空") Collection<Long> pids, boolean onlyQueryEnabled);

    /**
     * 分页查询权限列表
     *
     * @param pageSearchParam 权限分页搜索条件
     * @return 权限分页列表
     */
    PageVO<PermissionInfoVO> getPermissionPage(@NotNull(message = "pageSearchParam 不能为 null") @Valid
                                               PageSearchDTO<PermissionSearchDTO> pageSearchParam);
}
