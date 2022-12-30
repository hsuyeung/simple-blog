package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.dto.role.CreateRoleRequestDTO;
import com.hsuyeung.blog.model.dto.role.UpdateRoleRequestDTO;
import com.hsuyeung.blog.model.entity.RoleEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.permission.EnabledPermissionVO;
import com.hsuyeung.blog.model.vo.role.EnabledRoleVO;
import com.hsuyeung.blog.model.vo.role.RoleInfoVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
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
     * @param createRoleRequestDTO 创建角色请求参数
     * @return 创建成功返回 true，否则返回 false
     */
    boolean createRole(@Valid CreateRoleRequestDTO createRoleRequestDTO);

    /**
     * 删除角色
     *
     * @param rid 角色 id
     */
    void deleteRole(Long rid);

    /**
     * 更新角色
     *
     * @param updateRoleRequestDTO 更新角色请求参数
     */
    void updateRole(@Valid UpdateRoleRequestDTO updateRoleRequestDTO);

    /**
     * 锁定一个角色
     *
     * @param rid 角色 id
     */
    void lockRole(Long rid);

    /**
     * 解锁一个用户
     *
     * @param rid 用户 id
     */
    void unlockRole(Long rid);

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
    List<EnabledRoleVO> getRoleInfo(Collection<Long> roleIds, boolean onlyQueryEnabled);

    /**
     * 分页查询角色列表
     *
     * @param roleCode 角色编码，全模糊
     * @param enabled  是否可用，全匹配
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 角色分页列表
     */
    PageVO<RoleInfoVO> getRolePage(String roleCode, Boolean enabled, Integer pageNum, Integer pageSize);

    /**
     * 将不可用的角色 id 过滤掉
     *
     * @param rids 过滤前的角色 id 集合
     * @return 存在且可用的角色 id
     */
    List<Long> filterNotEnabledRid(Collection<Long> rids);

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
    boolean isExist(Long rid, boolean onlyQueryEnabled);

    /**
     * 给指定角色授予指定权限
     *
     * @param rid  角色 id
     * @param pids 权限 id 集合
     */
    void assignRolePermission(Long rid, Collection<Long> pids);
}
