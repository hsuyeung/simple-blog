package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.dto.user.CreateUserRequestDTO;
import com.hsuyeung.blog.model.dto.user.UpdateUserRequestDTO;
import com.hsuyeung.blog.model.dto.user.UserLoginRequestDTO;
import com.hsuyeung.blog.model.entity.UserEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.role.EnabledRoleVO;
import com.hsuyeung.blog.model.vo.user.UserInfoVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
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
     * @param userLoginRequestDTO 登录参数
     * @param ipAddr              ip 地址
     * @return 生成的 token
     * @throws UnsupportedEncodingException 密码加密失败
     * @throws NoSuchAlgorithmException     密码加密失败
     */
    String login(@Valid UserLoginRequestDTO userLoginRequestDTO, String ipAddr)
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
     * @param createUserRequestDTO 创建用户参数
     * @return 创建成功返回 true。否则返回 false
     * @throws UnsupportedEncodingException 密码加密失败
     * @throws NoSuchAlgorithmException     密码加密失败
     */
    boolean createUser(@Valid CreateUserRequestDTO createUserRequestDTO)
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
    void lockUser(Long uid);

    /**
     * 根据 id 解锁一个用户
     *
     * @param uid 用户 id
     */
    void unlockUser(Long uid);

    /**
     * 更新用户信息
     *
     * @param updateUserRequestDTO 更新用户信息参数
     * @return 更新成功返回 true，否则返回 false
     * @throws UnsupportedEncodingException 密码加密失败
     * @throws NoSuchAlgorithmException     密码加密失败
     */
    boolean updateUser(@Valid UpdateUserRequestDTO updateUserRequestDTO)
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
     * @param username 用户名，全模糊
     * @param nickname 昵称，全模糊
     * @param enabled  是否可用，全匹配
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 用户分页列表
     */
    PageVO<UserInfoVO> getUserPage(String username, String nickname, Boolean enabled,
                                   Integer pageNum, Integer pageSize);

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
    void assignUserRole(Long uid, Collection<Long> rids);

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
