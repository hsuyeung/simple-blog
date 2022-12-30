package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.cache.RbacCache;
import com.hsuyeung.blog.constant.enums.LogicSwitchEnum;
import com.hsuyeung.blog.exception.BizException;
import com.hsuyeung.blog.mapper.RoleMapper;
import com.hsuyeung.blog.model.dto.role.CreateRoleRequestDTO;
import com.hsuyeung.blog.model.dto.role.UpdateRoleRequestDTO;
import com.hsuyeung.blog.model.entity.RoleEntity;
import com.hsuyeung.blog.model.entity.RolePermissionEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.permission.EnabledPermissionVO;
import com.hsuyeung.blog.model.vo.role.EnabledRoleVO;
import com.hsuyeung.blog.model.vo.role.RoleInfoVO;
import com.hsuyeung.blog.service.*;
import com.hsuyeung.blog.util.AssertUtil;
import com.hsuyeung.blog.util.BizAssertUtil;
import com.hsuyeung.blog.util.ConvertUtil;
import com.hsuyeung.blog.util.DateUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.hsuyeung.blog.constant.DateFormatConstants.FORMAT_YEAR_TO_SECOND;
import static com.hsuyeung.blog.constant.RegexConstants.ROLE_CODE_REGEX;
import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.REDIS_USER_PERMISSION_KEY;
import static com.hsuyeung.blog.constant.enums.LogicSwitchEnum.OFF;
import static com.hsuyeung.blog.constant.enums.LogicSwitchEnum.ON;

/**
 * 角色服务实现类
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements IRoleService {
    @Resource
    private IUserRoleService userRoleService;
    @Resource
    private IRolePermissionService rolePermissionService;
    @Resource
    private RbacCache rbacCache;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    @Lazy
    private IUserService userService;
    @Resource
    private IPermissionService permissionService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createRole(CreateRoleRequestDTO createRoleRequestDTO) {
        AssertUtil.notNull(createRoleRequestDTO, "createRoleRequestDTO 不能为空");
        RoleEntity roleEntity = ConvertUtil.convert(createRoleRequestDTO, RoleEntity.class);
        roleEntity.setEnabled(Objects.equals(createRoleRequestDTO.getEnabled(), true) ? ON : OFF);
        try {
            return save(roleEntity);
        } catch (DuplicateKeyException e) {
            throw new BizException("角色重复", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRole(Long rid) {
        // 删除用户-角色关系
        userRoleService.deleteByRid(rid);
        // 删除角色-权限关系
        rolePermissionService.deleteByRid(rid);
        // 删除角色
        removeById(rid);
        // 刷新用户权限
        rbacCache.refreshCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRole(UpdateRoleRequestDTO updateRoleRequestDTO) {
        AssertUtil.notNull(updateRoleRequestDTO, "updateRoleRequestDTO 不能为空");
        Long id = updateRoleRequestDTO.getId();
        RoleEntity roleEntity = lambdaQuery()
                .select(RoleEntity::getId, RoleEntity::getRoleCode, RoleEntity::getRoleDesc, RoleEntity::getEnabled)
                .eq(RoleEntity::getId, id)
                .one();
        BizAssertUtil.notNull(roleEntity, "角色不存在");
        String roleCode = updateRoleRequestDTO.getRoleCode().trim();
        BizAssertUtil.isTrue(roleCode.matches(ROLE_CODE_REGEX), "非法角色编码");
        roleEntity.setRoleCode(roleCode);
        String roleDesc = updateRoleRequestDTO.getRoleDesc();
        if (Objects.isNull(roleDesc)) {
            roleDesc = "";
        } else {
            roleDesc = roleDesc.trim();
        }
        roleEntity.setRoleDesc(roleDesc.trim());
        // 如果更新了角色状态，则需要刷新用户权限
        boolean isNeedRefreshPermission = false;
        LogicSwitchEnum enabled = updateRoleRequestDTO.getEnabled() ? ON : OFF;
        if (!Objects.equals(enabled, roleEntity.getEnabled())) {
            isNeedRefreshPermission = true;
            roleEntity.setEnabled(enabled);
        }
        try {
            updateById(roleEntity);
        } catch (DuplicateKeyException e) {
            throw new BizException("角色重复", e);
        }
        if (isNeedRefreshPermission) {
            // 刷新用户权限缓存
            rbacCache.refreshCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void lockRole(Long rid) {
        AssertUtil.notNull(rid, "rid 不能为空");
        lambdaUpdate().set(RoleEntity::getEnabled, OFF).eq(RoleEntity::getId, rid).update(new RoleEntity());
        // 刷新用户权限
        rbacCache.refreshCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unlockRole(Long rid) {
        AssertUtil.notNull(rid, "rid 不能为空");
        lambdaUpdate().set(RoleEntity::getEnabled, ON).eq(RoleEntity::getId, rid).update(new RoleEntity());
        // 刷新用户权限
        rbacCache.refreshCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class));
    }

    @Override
    public List<EnabledRoleVO> getAllEnabledRoles() {
        return ConvertUtil.convertList(lambdaQuery().select(RoleEntity::getId, RoleEntity::getRoleCode, RoleEntity::getRoleDesc).eq(RoleEntity::getEnabled, ON).list(), EnabledRoleVO.class);
    }

    @Override
    public List<EnabledRoleVO> getRoleInfo(Collection<Long> rids, boolean onlyQueryEnabled) {
        AssertUtil.notNull(rids, "rids 不能为空");
        return ConvertUtil.convertList(lambdaQuery().select(RoleEntity::getId, RoleEntity::getRoleCode, RoleEntity::getRoleDesc).eq(onlyQueryEnabled, RoleEntity::getEnabled, ON).in(RoleEntity::getId, rids).list(), EnabledRoleVO.class);
    }

    @Override
    public PageVO<RoleInfoVO> getRolePage(String roleCode, Boolean enabled, Integer pageNum, Integer pageSize) {
        Page<RoleEntity> entityPage = lambdaQuery()
                .select(RoleEntity::getId, RoleEntity::getRoleCode, RoleEntity::getRoleDesc, RoleEntity::getEnabled,
                        RoleEntity::getCreateTime, RoleEntity::getCreateBy, RoleEntity::getUpdateTime, RoleEntity::getUpdateBy)
                .like(StringUtils.hasLength(roleCode), RoleEntity::getRoleCode, roleCode)
                .eq(Objects.nonNull(enabled), RoleEntity::getEnabled, Objects.equals(enabled, true) ? ON : OFF)
                .orderByDesc(RoleEntity::getUpdateTime)
                .page(new Page<>(pageNum, pageSize));
        List<RoleEntity> entityList = entityPage.getRecords();
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageVO<>(0L, Collections.emptyList());
        }
        List<RoleInfoVO> voList = entityList.stream().map(entity -> {
            RoleInfoVO vo = ConvertUtil.convert(entity, RoleInfoVO.class);
            vo.setEnabled(Objects.equals(entity.getEnabled(), ON));
            vo.setCreateTime(DateUtil.formatLocalDateTime(entity.getCreateTime(), FORMAT_YEAR_TO_SECOND));
            vo.setUpdateTime(DateUtil.formatLocalDateTime(entity.getUpdateTime(), FORMAT_YEAR_TO_SECOND));
            String createBy = entity.getCreateBy();
            String updateBy = entity.getUpdateBy();
            vo.setCreateBy(userService.getUsernameById(StringUtils.hasLength(createBy) ? Long.parseLong(createBy) : null));
            vo.setUpdateBy(userService.getUsernameById(StringUtils.hasLength(updateBy) ? Long.parseLong(updateBy) : null));
            return vo;
        }).collect(Collectors.toList());
        return new PageVO<>(entityPage.getTotal(), voList);
    }

    @Override
    public List<Long> filterNotEnabledRid(Collection<Long> rids) {
        AssertUtil.notNull(rids, "rids 不能为空");
        return lambdaQuery()
                .select(RoleEntity::getId)
                .in(RoleEntity::getId, rids)
                .eq(RoleEntity::getEnabled, ON)
                .list()
                .stream()
                .map(RoleEntity::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnabledPermissionVO> getRoleAllEnabledPermissions(Long rid) {
        AssertUtil.notNull(rid, "rid 不能为空");
        AssertUtil.isTrue(isExist(rid, false), "角色不存在");
        Set<Long> pids = rolePermissionService.getPermissionIds(Collections.singleton(rid), true);
        if (CollectionUtils.isEmpty(pids)) {
            return Collections.emptyList();
        }
        return permissionService.getPermissionInfo(pids, true);
    }

    @Override
    public boolean isExist(Long rid, boolean onlyQueryEnabled) {
        AssertUtil.notNull(rid, "rid 不能为空");
        return lambdaQuery().select(RoleEntity::getId).eq(RoleEntity::getId, rid).eq(onlyQueryEnabled, RoleEntity::getEnabled, ON).one() != null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assignRolePermission(Long rid, Collection<Long> pids) {
        AssertUtil.notNull(rid, "rid 不能为空");
        AssertUtil.notNull(pids, "pids 不能为空");
        // 删除角色的所有权限
        rolePermissionService.deleteByRid(rid);
        // 保存新的权限
        rolePermissionService.saveBatch(pids.stream().map(pid -> new RolePermissionEntity(rid, pid)).collect(Collectors.toList()));
        // 刷新用户的权限
        rbacCache.refreshCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class));
    }
}
