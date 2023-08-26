package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.user.CreateUserDTO;
import com.hsuyeung.blog.model.dto.user.UpdateUserDTO;
import com.hsuyeung.blog.model.dto.user.UserLoginDTO;
import com.hsuyeung.blog.model.dto.user.UserSearchDTO;
import com.hsuyeung.blog.model.entity.UserEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.role.EnabledRoleVO;
import com.hsuyeung.blog.model.vo.user.UserInfoVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 用户服务接口
 *
 * @author hsuyeung
 * @date 2022/06/28
 */
@Validated
public interface IUserService extends IService<UserEntity> {
    /**
     * 判断指定 id 的用户是否存在
     *
     * @param uid              用户 id
     * @param onlyQueryEnabled 是否只查询开启状态的数据
     * @return 存在返回 true，否则返回 false
     */
    boolean isExist(Long uid, boolean onlyQueryEnabled);

    /**
     * 用户登录
     *
     * @param userLogin 登录参数
     * @param ipAddr    ip 地址
     * @return 生成的 token
     * @throws UnsupportedEncodingException 密码加密失败
     * @throws NoSuchAlgorithmException     密码加密失败
     */
    String login(@NotNull(message = "userLogin 不能为 null") @Valid UserLoginDTO userLogin, String ipAddr)
            throws UnsupportedEncodingException, NoSuchAlgorithmException;

    /**
     * 用户退出登录
     *
     * @param uid 用户 id
     * @return 退出成功返回 true，否则返回 false
     */
    boolean logout(Long uid);

    /**
     * 创建一个用户
     *
     * @param createUser 创建用户参数
     * @return 创建成功返回 true。否则返回 false
     * @throws UnsupportedEncodingException 密码加密失败
     * @throws NoSuchAlgorithmException     密码加密失败
     */
    boolean createUser(@NotNull(message = "createUser 不能为 null") @Valid CreateUserDTO createUser)
            throws UnsupportedEncodingException, NoSuchAlgorithmException;

    /**
     * 根据 id 删除一个用户
     *
     * @param uid 用户 id
     */
    void deleteUser(Long uid);

    /**
     * 根据 id 锁定一个用户
     *
     * @param uid uid
     */
    void lockUser(@NotNull(message = "uid 不能为 null") Long uid);

    /**
     * 根据 id 解锁一个用户
     *
     * @param uid 用户 id
     */
    void unlockUser(@NotNull(message = "uid 不能为 null") Long uid);

    /**
     * 更新用户信息
     *
     * @param updateUser 更新用户信息参数
     * @return 更新成功返回 true，否则返回 false
     * @throws UnsupportedEncodingException 密码加密失败
     * @throws NoSuchAlgorithmException     密码加密失败
     */
    boolean updateUser(@NotNull(message = "updateUser 不能为 null") @Valid UpdateUserDTO updateUser)
            throws UnsupportedEncodingException, NoSuchAlgorithmException;

    /**
     * 查询用户 id 集合
     *
     * @param onlyQueryEnabled 是否只查询开启状态的数据
     * @return 用户 id 集合
     */
    Set<Long> getUids(boolean onlyQueryEnabled);

    /**
     * 分页查询用户列表
     *
     * @param pageSearchParam 用户分页搜索条件
     * @return 用户分页列表
     */
    PageVO<UserInfoVO> getUserPage(@NotNull(message = "pageSeatchParam 不能为 null") @Valid
                                   PageSearchDTO<UserSearchDTO> pageSearchParam);

    /**
     * 获取用户拥有的所有启用状态的角色列表
     *
     * @param uid 用户 id
     * @return 该用户拥有的所有启用状态的角色列表
     */
    List<EnabledRoleVO> getUserAllEnabledRoles(Long uid);

    /**
     * 给指定用户授予指定角色
     *
     * @param uid  用户 id
     * @param rids 角色 id 集合
     */
    void assignUserRole(@NotNull(message = "uid 不能为 null") Long uid,
                        @NotEmpty(message = "rids 不能为空") Collection<Long> rids);

    /**
     * 根据用户 id 获取用户名
     *
     * @param uid 用户 id
     * @return 用户名，不存在则返回空字符串
     */
    String getUsernameById(Long uid);

    /**
     * 根据用户 id 获取用户昵称
     *
     * @param uid 用户 id
     * @return 用户昵称
     */
    String getNickname(Long uid);
}
