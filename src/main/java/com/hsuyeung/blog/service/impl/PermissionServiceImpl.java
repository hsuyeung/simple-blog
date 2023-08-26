package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.cache.RbacCache;
import com.hsuyeung.blog.exception.BizException;
import com.hsuyeung.blog.mapper.PermissionMapper;
import com.hsuyeung.blog.model.dto.PageDTO;
import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.permission.CreatePermissionDTO;
import com.hsuyeung.blog.model.dto.permission.PermissionSearchDTO;
import com.hsuyeung.blog.model.dto.permission.UpdatePermissionDTO;
import com.hsuyeung.blog.model.entity.PermissionEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.permission.EnabledPermissionVO;
import com.hsuyeung.blog.model.vo.permission.PermissionInfoVO;
import com.hsuyeung.blog.model.vo.permission.PermissionVO;
import com.hsuyeung.blog.service.IPermissionService;
import com.hsuyeung.blog.service.IRolePermissionService;
import com.hsuyeung.blog.service.ISystemConfigService;
import com.hsuyeung.blog.service.IUserService;
import com.hsuyeung.blog.util.BizAssertUtil;
import com.hsuyeung.blog.util.ConvertUtil;
import com.hsuyeung.blog.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.hsuyeung.blog.constant.DateFormatConstants.FORMAT_YEAR_TO_SECOND;
import static com.hsuyeung.blog.constant.RegexConstants.HTTP_METHOD_REGEX;
import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.REDIS_USER_PERMISSION_KEY;
import static com.hsuyeung.blog.constant.enums.LogicSwitchEnum.OFF;
import static com.hsuyeung.blog.constant.enums.LogicSwitchEnum.ON;

/**
 * 权限服务实现类
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Service("permissionService")
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity> implements IPermissionService {
    private final IRolePermissionService rolePermissionService;
    private final RbacCache rbacCache;
    private final ISystemConfigService systemConfigService;
    private final IUserService userService;

    @Override
    public Set<PermissionVO> getPermissions(Collection<Long> pids, boolean onlyQueryEnabled) {
        List<PermissionEntity> permissionEntityList = lambdaQuery()
                .select(PermissionEntity::getPath, PermissionEntity::getMethod, PermissionEntity::getPermissionDesc)
                .in(PermissionEntity::getId, pids)
                .eq(onlyQueryEnabled, PermissionEntity::getEnabled, ON)
                .list();
        if (CollectionUtils.isEmpty(permissionEntityList)) {
            return Collections.emptySet();
        }
        return new HashSet<>(ConvertUtil.convertList(permissionEntityList, PermissionVO.class));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createPermission(CreatePermissionDTO createPermission) {
        String method = createPermission.getMethod().trim();
        BizAssertUtil.isTrue(method.matches(HTTP_METHOD_REGEX), "method 目前仅支持 GET、PUT、POST、DELETE，不区分大小写");
        try {
            return save(PermissionEntity.builder()
                    .path(createPermission.getPath().trim())
                    .method(method.toUpperCase())
                    .permissionDesc(createPermission.getPermissionDesc().trim())
                    .enabled(Objects.equals(createPermission.getEnabled(), true) ? ON : OFF)
                    .build());
        } catch (DuplicateKeyException e) {
            throw new BizException("权限重复", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePermission(Long pid) {
        // 删除角色-权限关系
        rolePermissionService.deleteByPid(pid);
        // 删除权限
        removeById(pid);
        // 刷新用户权限
        rbacCache.refreshCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePermission(UpdatePermissionDTO updatePermission) {
        String method = updatePermission.getMethod().trim();
        BizAssertUtil.isTrue(method.matches(HTTP_METHOD_REGEX), "method 目前仅支持 GET、PUT、POST、DELETE，不区分大小写");
        updatePermission.setMethod(method.toUpperCase());
        PermissionEntity entity = ConvertUtil.convert(updatePermission, PermissionEntity.class);
        entity.setEnabled(updatePermission.getEnabled() ? ON : OFF);
        boolean res;
        try {
            res = updateById(entity);
        } catch (DuplicateKeyException e) {
            throw new BizException("权限重复", e);
        }
        // 刷新用户权限
        rbacCache.refreshCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class));
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void lockPermission(Long pid) {
        lambdaUpdate()
                .set(PermissionEntity::getEnabled, OFF)
                .eq(PermissionEntity::getId, pid)
                .update(new PermissionEntity());
        // 刷新用户权限
        rbacCache.refreshCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unlockPermission(Long pid) {
        lambdaUpdate()
                .set(PermissionEntity::getEnabled, ON)
                .eq(PermissionEntity::getId, pid)
                .update(new PermissionEntity());
        // 刷新用户权限
        rbacCache.refreshCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class));
    }

    @Override
    public List<EnabledPermissionVO> getAllEnabledPermission() {
        return ConvertUtil.convertList(
                lambdaQuery()
                        .select(PermissionEntity::getId, PermissionEntity::getPermissionDesc)
                        .eq(PermissionEntity::getEnabled, ON)
                        .list(),
                EnabledPermissionVO.class);
    }

    @Override
    public List<EnabledPermissionVO> getPermissionInfo(Collection<Long> pids, boolean onlyQueryEnabled) {
        return ConvertUtil.convertList(
                lambdaQuery()
                        .select(PermissionEntity::getId, PermissionEntity::getPermissionDesc)
                        .eq(onlyQueryEnabled, PermissionEntity::getEnabled, ON)
                        .in(PermissionEntity::getId, pids).list(),
                EnabledPermissionVO.class);
    }

    @Override
    public PageVO<PermissionInfoVO> getPermissionPage(PageSearchDTO<PermissionSearchDTO> pageSearchParam) {
        PermissionSearchDTO searchParam = pageSearchParam.getSearchParam();
        String method = null;
        String path = null;
        String permissionDesc = null;
        Boolean enabled = null;

        if (Objects.nonNull(searchParam)) {
            method = searchParam.getMethod();
            if (StringUtils.hasLength(method)) {
                method = method.toUpperCase();
            }
            path = searchParam.getPath();
            permissionDesc = searchParam.getPermissionDesc();
            enabled = searchParam.getEnabled();
        }

        PageDTO pageParam = pageSearchParam.getPageParam();
        Page<PermissionEntity> entityPage = lambdaQuery()
                .select(PermissionEntity::getId, PermissionEntity::getPath, PermissionEntity::getMethod,
                        PermissionEntity::getPermissionDesc, PermissionEntity::getEnabled,
                        PermissionEntity::getCreateTime, PermissionEntity::getCreateBy,
                        PermissionEntity::getUpdateTime, PermissionEntity::getUpdateBy)
                .like(StringUtils.hasLength(path), PermissionEntity::getPath, path)
                .eq(StringUtils.hasLength(method), PermissionEntity::getMethod, method)
                .like(StringUtils.hasLength(permissionDesc), PermissionEntity::getPermissionDesc, permissionDesc)
                .eq(Objects.nonNull(enabled), PermissionEntity::getEnabled, Objects.equals(enabled, true) ? ON : OFF)
                .orderByDesc(PermissionEntity::getUpdateTime)
                .page(new Page<>(pageParam.getPageNum(), pageParam.getPageSize()));
        List<PermissionEntity> entityList = entityPage.getRecords();
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageVO<>(0L, Collections.emptyList());
        }
        List<PermissionInfoVO> voList = entityList.stream().map(entity -> {
            PermissionInfoVO vo = ConvertUtil.convert(entity, PermissionInfoVO.class);
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
}
