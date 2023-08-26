package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.cache.RbacCache;
import com.hsuyeung.blog.constant.enums.LogicSwitchEnum;
import com.hsuyeung.blog.exception.BizException;
import com.hsuyeung.blog.mapper.UserMapper;
import com.hsuyeung.blog.model.dto.PageDTO;
import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.user.CreateUserDTO;
import com.hsuyeung.blog.model.dto.user.UpdateUserDTO;
import com.hsuyeung.blog.model.dto.user.UserLoginDTO;
import com.hsuyeung.blog.model.dto.user.UserSearchDTO;
import com.hsuyeung.blog.model.entity.UserEntity;
import com.hsuyeung.blog.model.entity.UserRoleEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.role.EnabledRoleVO;
import com.hsuyeung.blog.model.vo.user.UserInfoVO;
import com.hsuyeung.blog.service.*;
import com.hsuyeung.blog.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.hsuyeung.blog.constant.DateFormatConstants.FORMAT_YEAR_TO_SECOND;
import static com.hsuyeung.blog.constant.RegexConstants.*;
import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.*;
import static com.hsuyeung.blog.constant.enums.LogicSwitchEnum.OFF;
import static com.hsuyeung.blog.constant.enums.LogicSwitchEnum.ON;

/**
 * 用户服务实现类
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {
    private final IUserTokenService userTokenService;
    private final ISystemConfigService systemConfigService;
    private final IUserRoleService userRoleService;
    private final RbacCache rbacCache;
    private final RedisUtil redisUtil;
    private final IRoleService roleService;

    @Override
    public boolean isExist(Long uid, boolean onlyQueryEnabled) {
        return lambdaQuery().select(UserEntity::getId).eq(UserEntity::getId, uid).eq(onlyQueryEnabled, UserEntity::getEnabled, ON).one() != null;
    }

    @Override
    public String login(UserLoginDTO userLogin, String ipAddr) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String key = systemConfigService.getConfigValue(REDIS_LOGIN_FAILED_NUM_KEY, String.class);
        Integer loginFailedMaxNum = systemConfigService.getConfigValue(USER_LOGIN_FAILED_MAX_NUM, Integer.class);
        Integer errNum = (Integer) redisUtil.hGet(key, ipAddr);
        BizAssertUtil.isTrue(Objects.isNull(errNum) || errNum < loginFailedMaxNum, "该 IP 已被禁止登录");

        String username = userLogin.getUsername().trim();
        String password = userLogin.getPassword().trim();
        BizAssertUtil.isTrue(username.matches(USERNAME_REGEX), "非法用户名");
        BizAssertUtil.isTrue(password.matches(PASSWORD_REGEX), "非法密码");
        UserEntity user = lambdaQuery().select(UserEntity::getId).eq(UserEntity::getUsername, username).eq(UserEntity::getPassword, MD5Util.md5Hex(password)).one();
        if (Objects.isNull(user)) {
            synchronized (this) {
                errNum = Math.toIntExact(redisUtil.hIncrBy(key, ipAddr, 1L));
                BizAssertUtil.isTrue(errNum < loginFailedMaxNum, "失败次数已达告警阈值，请求 ip 将被记录并被永久禁止登录，如有问题请联系网站管理员");
                throw new BizException(String.format("用户名或密码错误，剩余尝试次数：%d", loginFailedMaxNum - errNum));
            }
        }
        // 登录成功则将该 ip 地址的限制记录清空
        redisUtil.hDelete(key, ipAddr);
        return userTokenService.generateUserToken(user.getId(), LocalDateTime.now().plusSeconds(systemConfigService.getConfigValue(USER_TOKEN_EXPIRE_TIME, Long.class)));
    }

    @Override
    public boolean logout(Long uid) {
        return userTokenService.deleteUserToken(uid);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createUser(CreateUserDTO createUser) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String username = createUser.getUsername().trim();
        String nickname = createUser.getNickname().trim();
        String password = createUser.getPassword().trim();
        String reconfirmPassword = createUser.getReconfirmPassword().trim();
        BizAssertUtil.isTrue(username.matches(USERNAME_REGEX), "非法用户名");
        BizAssertUtil.isTrue(username.matches(NICKNAME_REGEX), "非法昵称");
        BizAssertUtil.isTrue(password.matches(PASSWORD_REGEX), "非法密码");
        BizAssertUtil.isTrue(Objects.equals(password, reconfirmPassword), "两次输入密码不一致");

        try {
            return save(UserEntity.builder().username(username).nickname(nickname).password(MD5Util.md5Hex(password)).enabled(Objects.equals(createUser.getEnabled(), true) ? ON : OFF).build());
        } catch (DuplicateKeyException e) {
            throw new BizException("用户重复", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUser(Long uid) {
        BizAssertUtil.isTrue(isExist(uid, false), "用户不存在");
        // 删除用户的角色
        userRoleService.deleteByUid(uid);
        // 删除用户
        removeById(uid);
        // 删除用户 token
        userTokenService.deleteUserToken(uid);
        // 删除用户权限缓存
        rbacCache.deleteCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class), uid);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void lockUser(Long uid) {
        lambdaUpdate().set(UserEntity::getEnabled, OFF).eq(UserEntity::getId, uid).update(new UserEntity());
        // 删除用户的权限缓存
        rbacCache.deleteCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class), uid);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unlockUser(Long uid) {
        lambdaUpdate().set(UserEntity::getEnabled, ON).eq(UserEntity::getId, uid).update(new UserEntity());
        // 刷新用户权限
        rbacCache.refreshCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(UpdateUserDTO updateUser) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        boolean isNeedDelToken = false;
        Long id = updateUser.getId();
        UserEntity userEntity = lambdaQuery().select(UserEntity::getId, UserEntity::getUsername, UserEntity::getNickname, UserEntity::getPassword, UserEntity::getEnabled).eq(UserEntity::getId, id).one();
        BizAssertUtil.notNull(userEntity, "用户不存在");
        // 如果更新了帐号状态且是由可用->不可用，则需要删除用户的权限和 token
        LogicSwitchEnum enabled = updateUser.getEnabled() ? ON : OFF;
        if (!Objects.equals(enabled, userEntity.getEnabled())) {
            if (Objects.equals(enabled, OFF)) {
                isNeedDelToken = true;
                // 删除用户权限缓存
                rbacCache.deleteCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class), id);
            }
            userEntity.setEnabled(enabled);
        }
        String username = updateUser.getUsername().trim();
        // 如果更新了用户名或者密码则要将改用户的 token 失效掉，然后让用户重新登录
        if (!Objects.equals(username, userEntity.getUsername())) {
            BizAssertUtil.isTrue(username.matches(USERNAME_REGEX), "非法用户名");
            userEntity.setUsername(username);
            isNeedDelToken = true;
        }
        String oldPassword = updateUser.getOldPassword();
        String newPassword = updateUser.getNewPassword();
        String reconfirmNewPassword = updateUser.getReconfirmNewPassword();
        if (StringUtils.hasLength(newPassword)) {
            BizAssertUtil.hasLength(oldPassword, "原密码不能为空");
            BizAssertUtil.isTrue(Objects.equals(MD5Util.md5Hex(oldPassword), userEntity.getPassword()), "原密码错误");
            BizAssertUtil.isTrue(newPassword.matches(PASSWORD_REGEX), "新密码格式非法");
            BizAssertUtil.isTrue(!Objects.equals(oldPassword, newPassword), "新密码不能和旧密码一样");
            BizAssertUtil.isTrue(Objects.equals(newPassword, reconfirmNewPassword), "新密码两次输入不一致");
            userEntity.setPassword(MD5Util.md5Hex(newPassword.trim()));
            isNeedDelToken = true;
        }
        String nickname = updateUser.getNickname().trim();
        if (!Objects.equals(nickname, userEntity.getNickname())) {
            userEntity.setNickname(nickname);
        }
        boolean isSuccess;
        try {
            isSuccess = updateById(userEntity);
        } catch (DuplicateKeyException e) {
            throw new BizException("用户重复", e);
        }
        if (isSuccess && isNeedDelToken) {
            isSuccess = userTokenService.deleteUserToken(id);
        }
        return isSuccess;
    }

    @Override
    public Set<Long> getUids(boolean onlyQueryEnabled) {
        return lambdaQuery().select(UserEntity::getId).eq(!onlyQueryEnabled, UserEntity::getEnabled, ON).list().stream().map(UserEntity::getId).collect(Collectors.toSet());
    }

    @Override
    public PageVO<UserInfoVO> getUserPage(PageSearchDTO<UserSearchDTO> pageSearchParam) {
        UserSearchDTO searchParam = pageSearchParam.getSearchParam();
        String username = null;
        String nickname = null;
        Boolean enabled = null;

        if (Objects.nonNull(searchParam)) {
            username = searchParam.getUsername();
            nickname = searchParam.getNickname();
            enabled = searchParam.getEnabled();
        }

        PageDTO pageParam = pageSearchParam.getPageParam();
        Page<UserEntity> entityPage = lambdaQuery()
                .select(UserEntity::getId, UserEntity::getUsername, UserEntity::getNickname, UserEntity::getEnabled,
                        UserEntity::getCreateTime, UserEntity::getCreateBy, UserEntity::getUpdateTime, UserEntity::getUpdateBy)
                .like(StringUtils.hasLength(username), UserEntity::getUsername, username)
                .like(StringUtils.hasLength(nickname), UserEntity::getNickname, nickname)
                .eq(Objects.nonNull(enabled), UserEntity::getEnabled, Objects.equals(enabled, true) ? ON : OFF)
                .orderByDesc(UserEntity::getUpdateTime)
                .page(new Page<>(pageParam.getPageNum(), pageParam.getPageSize()));
        List<UserEntity> entityList = entityPage.getRecords();
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageVO<>(0L, Collections.emptyList());
        }
        List<UserInfoVO> voList = entityList.stream().map(entity -> {
            UserInfoVO vo = ConvertUtil.convert(entity, UserInfoVO.class);
            vo.setEnabled(Objects.equals(entity.getEnabled(), ON));
            vo.setCreateTime(DateUtil.formatLocalDateTime(entity.getCreateTime(), FORMAT_YEAR_TO_SECOND));
            vo.setUpdateTime(DateUtil.formatLocalDateTime(entity.getUpdateTime(), FORMAT_YEAR_TO_SECOND));
            String createBy = entity.getCreateBy();
            String updateBy = entity.getUpdateBy();
            vo.setCreateBy(getUsernameById(StringUtils.hasLength(createBy) ? Long.parseLong(createBy) : null));
            vo.setUpdateBy(getUsernameById(StringUtils.hasLength(updateBy) ? Long.parseLong(updateBy) : null));
            return vo;
        }).collect(Collectors.toList());
        return new PageVO<>(entityPage.getTotal(), voList);
    }

    @Override
    public List<EnabledRoleVO> getUserAllEnabledRoles(Long uid) {
        AssertUtil.isTrue(isExist(uid, false), "用户不存在");
        Set<Long> roleIds = userRoleService.getRoleIds(uid, true);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        return roleService.getRoleInfo(roleIds, true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assignUserRole(Long uid, Collection<Long> rids) {
        // 删除用户的所有角色
        userRoleService.deleteByUid(uid);
        // 保存新的角色
        userRoleService.saveBatch(rids.stream().map(rid -> new UserRoleEntity(uid, rid)).collect(Collectors.toList()));
        // 刷新用户的权限
        rbacCache.refreshCache(systemConfigService.getConfigValue(REDIS_USER_PERMISSION_KEY, String.class));
    }


    @Override
    public String getUsernameById(Long uid) {
        if (Objects.isNull(uid)) {
            return "";
        }
        UserEntity userEntity = lambdaQuery()
                .select(UserEntity::getUsername)
                .eq(UserEntity::getId, uid)
                .one();
        if (Objects.isNull(userEntity)) {
            return "";
        }
        return userEntity.getUsername();
    }

    @Override
    public String getNickname(Long uid) {
        UserEntity user = lambdaQuery().select(UserEntity::getNickname).eq(UserEntity::getId, uid).one();
        BizAssertUtil.notNull(user, "用户不存在");
        return user.getNickname();
    }
}
