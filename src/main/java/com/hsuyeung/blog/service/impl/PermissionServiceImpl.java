package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.cache.RbacCache;
import com.hsuyeung.blog.exception.BizException;
import com.hsuyeung.blog.mapper.PermissionMapper;
import com.hsuyeung.blog.model.dto.permission.CreatePermissionRequestDTO;
import com.hsuyeung.blog.model.dto.permission.UpdatePermissionRequestDTO;
import com.hsuyeung.blog.model.entity.PermissionEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.permission.EnabledPermissionVO;
import com.hsuyeung.blog.model.vo.permission.PermissionInfoVO;
import com.hsuyeung.blog.model.vo.permission.PermissionVO;
import com.hsuyeung.blog.service.IPermissionService;
import com.hsuyeung.blog.service.IRolePermissionService;
import com.hsuyeung.blog.service.ISystemConfigService;
import com.hsuyeung.blog.service.IUserService;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity> implements IPermissionService {
    @Resource
    private IRolePermissionService rolePermissionService;
    @Resource
    private RbacCache rbacCache;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    @Lazy
    private IUserService userService;

    @Override
    public Set<PermissionVO> getPermissions(Collection<Long> pids, boolean onlyQueryEnabled) {
        AssertUtil.notNull(pids, "pids 不能为空");
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
    public boolean createPermission(CreatePermissionRequestDTO createPermissionRequestDTO) {
        AssertUtil.notNull(createPermissionRequestDTO, "createPermissionRequestDTO 不能为空");
        String method = createPermissionRequestDTO.getMethod().trim();
        BizAssertUtil.isTrue(method.matches(HTTP_METHOD_REGEX), "method 目前仅支持 GET、PUT、POST、DELETE，不区分大小写");
        try {
            return save(PermissionEntity.builder()
                    .path(createPermissionRequestDTO.getPath().trim())
                    .method(method.toUpperCase())
                    .permissionDesc(createPermissionRequestDTO.getPermissionDesc().trim())
                    .enabled(Objects.equals(createPermissionRequestDTO.getEnabled(), true) ? ON : OFF)
                    .build());
        } catch (DuplicateKeyException e) {
            throw new BizException("权限重复", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePermission(Long pid) {
        AssertUtil.notNull(pid, "pid 不能为空");
        // 删除角色-权限关系
        rolePermissionService.deleteByPid(pid);
        // 删除权限
        removeById(pid);
        // 刷新用户权限
        rbacCache.refreshCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePermission(UpdatePermissionRequestDTO updatePermissionRequestDTO) {
        AssertUtil.notNull(updatePermissionRequestDTO, "updatePermissionRequestDTO 不能为空");
        String method = updatePermissionRequestDTO.getMethod().trim();
        BizAssertUtil.isTrue(method.matches(HTTP_METHOD_REGEX), "method 目前仅支持 GET、PUT、POST、DELETE，不区分大小写");
        updatePermissionRequestDTO.setMethod(method.toUpperCase());
        PermissionEntity entity = ConvertUtil.convert(updatePermissionRequestDTO, PermissionEntity.class);
        entity.setEnabled(updatePermissionRequestDTO.getEnabled() ? ON : OFF);
        try {
            return updateById(entity);
        } catch (DuplicateKeyException e) {
            throw new BizException("权限重复", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void lockPermission(Long pid) {
        AssertUtil.notNull(pid, "pid 不能为空");
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
        AssertUtil.notNull(pid, "pid 不能为空");
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
        AssertUtil.notNull(pids, "pids 不能为空");
        return ConvertUtil.convertList(
                lambdaQuery()
                        .select(PermissionEntity::getId, PermissionEntity::getPermissionDesc)
                        .eq(onlyQueryEnabled, PermissionEntity::getEnabled, ON)
                        .in(PermissionEntity::getId, pids).list(),
                EnabledPermissionVO.class);
    }

    @Override
    public PageVO<PermissionInfoVO> getPermissionPage(String path, String method, String permissionDesc, Boolean enabled,
                                                      Integer pageNum, Integer pageSize) {
        if (StringUtils.hasLength(method)) {
            method = method.toUpperCase();
        }
        Page<PermissionEntity> entityPage = lambdaQuery()
                .select(PermissionEntity::getId, PermissionEntity::getPath, PermissionEntity::getMethod,
                        PermissionEntity::getPermissionDesc, PermissionEntity::getEnabled,
                        PermissionEntity::getCreateTime, PermissionEntity::getCreateBy,
                        PermissionEntity::getUpdateTime, PermissionEntity::getUpdateBy)
                .likeRight(StringUtils.hasLength(path), PermissionEntity::getPath, path)
                .eq(StringUtils.hasLength(method), PermissionEntity::getMethod, method)
                .like(StringUtils.hasLength(permissionDesc), PermissionEntity::getPermissionDesc, permissionDesc)
                .eq(Objects.nonNull(enabled), PermissionEntity::getEnabled, Objects.equals(enabled, true) ? ON : OFF)
                .orderByDesc(PermissionEntity::getUpdateTime)
                .page(new Page<>(pageNum, pageSize));
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
