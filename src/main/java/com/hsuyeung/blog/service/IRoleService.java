package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.role.CreateRoleDTO;
import com.hsuyeung.blog.model.dto.role.RoleSearchDTO;
import com.hsuyeung.blog.model.dto.role.UpdateRoleDTO;
import com.hsuyeung.blog.model.entity.RoleEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.permission.EnabledPermissionVO;
import com.hsuyeung.blog.model.vo.role.EnabledRoleVO;
import com.hsuyeung.blog.model.vo.role.RoleInfoVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * 角色服务接口
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Validated
public interface IRoleService extends IService<RoleEntity> {

    /**
     * 创建一个角色
     *
     * @param createRole 创建角色请求参数
     * @return 创建成功返回 true，否则返回 false
     */
    boolean createRole(@NotNull(message = "createRole 不能为 null") @Valid CreateRoleDTO createRole);

    /**
     * 删除角色
     *
     * @param rid 角色 id
     */
    void deleteRole(Long rid);

    /**
     * 更新角色
     *
     * @param updateRole 更新角色请求参数
     */
    void updateRole(@NotNull(message = "updateRole 不能为 null") @Valid UpdateRoleDTO updateRole);

    /**
     * 锁定一个角色
     *
     * @param rid 角色 id
     */
    void lockRole(@NotNull(message = "rid 不能为 null") Long rid);

    /**
     * 解锁一个用户
     *
     * @param rid 用户 id
     */
    void unlockRole(@NotNull(message = "rid 不能为 null") Long rid);

    /**
     * 查询所有启用状态的角色
     *
     * @return 所有启用状态的角色
     */
    List<EnabledRoleVO> getAllEnabledRoles();

    /**
     * 获取指定角色 id 的详细信息
     *
     * @param roleIds          角色 id 集合
     * @param onlyQueryEnabled 是否只查询启用状态的
     * @return 指定角色 id 的详细信息
     */
    List<EnabledRoleVO> getRoleInfo(@NotEmpty(message = "rids 不能为空") Collection<Long> roleIds, boolean onlyQueryEnabled);

    /**
     * 分页查询角色列表
     *
     * @param pageSearchParam 角色分页搜索条件
     * @return 角色分页列表
     */
    PageVO<RoleInfoVO> getRolePage(@NotNull(message = "pageSearchParam 不能为 null") @Valid
                                   PageSearchDTO<RoleSearchDTO> pageSearchParam);

    /**
     * 将不可用的角色 id 过滤掉
     *
     * @param rids 过滤前的角色 id 集合
     * @return 存在且可用的角色 id
     */
    List<Long> filterNotEnabledRid(@NotEmpty(message = "rids 不能为空") Collection<Long> rids);

    /**
     * 获取角色拥有的所有启用状态的权限列表
     *
     * @param rid 角色 id
     * @return 该角色拥有的所有启用状态的权限列表
     */
    List<EnabledPermissionVO> getRoleAllEnabledPermissions(Long rid);

    /**
     * 判断指定 id 的角色是否存在
     *
     * @param rid              角色 id
     * @param onlyQueryEnabled 是否只查询开启状态的数据
     * @return 存在返回 true，否则返回 false
     */
    boolean isExist(@NotNull(message = "rid 不能为 null") Long rid, boolean onlyQueryEnabled);

    /**
     * 给指定角色授予指定权限
     *
     * @param rid  角色 id
     * @param pids 权限 id 集合
     */
    void assignRolePermission(@NotNull(message = "rid 不能为 null") Long rid,
                              @NotEmpty(message = "pids 不能为空") Collection<Long> pids);
}
