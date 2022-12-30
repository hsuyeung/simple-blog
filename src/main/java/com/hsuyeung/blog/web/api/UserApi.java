package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.annotation.ApiRateLimit;
import com.hsuyeung.blog.model.dto.user.CreateUserRequestDTO;
import com.hsuyeung.blog.model.dto.user.UpdateUserRequestDTO;
import com.hsuyeung.blog.model.dto.user.UserLoginRequestDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.role.EnabledRoleVO;
import com.hsuyeung.blog.model.vo.user.UserInfoVO;
import com.hsuyeung.blog.service.IUserService;
import com.hsuyeung.blog.service.IUserTokenService;
import com.hsuyeung.blog.util.IpUtil;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hsuyeung.blog.constant.enums.ApiRateLimitStrategyEnum.IP;

/**
 * 用户相关接口
 *
 * @author hsuyeung
 * @date 2022/06/29
 */
@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/api/user")
public class UserApi implements IBaseWebResponse {
    @Resource
    private IUserService userService;
    @Resource
    private IUserTokenService userTokenService;

    /**
     * 登录（暂时限制一个 IP 在 1 分钟只等调用一次登录接口）
     *
     * @param userLoginRequestDTO 登录参数
     * @return 登录成功生成的 token
     */
    @ApiRateLimit(value = 60, strategy = IP, message = "请在 {rest} 秒后重试")
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public WebResponse<String> login(
            @ApiParam("用户登录参数") @RequestBody UserLoginRequestDTO userLoginRequestDTO,
            HttpServletRequest request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return ok(userService.login(userLoginRequestDTO, IpUtil.getIpAddr(request)));
    }

    /**
     * 用户退出登录
     *
     * @param request {@link HttpServletRequest}
     */
    @ApiOperation("用户退出登录")
    @PostMapping("/logout")
    public WebResponse<Boolean> logout(HttpServletRequest request) {
        return ok(userService.logout(userTokenService.getUserIdFromRequestHeader(request)));
    }

    /**
     * 创建用户
     *
     * @param createUserRequestDTO 创建用户参数
     * @return 创建是否成功
     */
    @ApiOperation("创建一个用户")
    @PutMapping
    public WebResponse<Boolean> createUser(
            @ApiParam("创建用户请求参数") @RequestBody CreateUserRequestDTO createUserRequestDTO)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return ok(userService.createUser(createUserRequestDTO));
    }

    /**
     * 删除一个用户
     *
     * @param uid 用户 id
     */
    @ApiOperation("删除一个用户")
    @DeleteMapping("/{uid}")
    public WebResponse<Void> deleteUser(@ApiParam("用户 id") @PathVariable("uid") Long uid) {
        userService.deleteUser(uid);
        return ok();
    }

    /**
     * 锁定一个用户
     *
     * @param uid 用户 id
     */
    @ApiOperation("锁定一个用户")
    @PostMapping("/lock/{uid}")
    public WebResponse<Void> lockUser(@ApiParam("用户 id") @PathVariable("uid") Long uid) {
        userService.lockUser(uid);
        return ok();
    }

    /**
     * 解锁一个用户
     *
     * @param uid 用户 id
     */
    @ApiOperation("解锁一个用户")
    @PostMapping("/unlock/{uid}")
    public WebResponse<Void> unlockUser(@ApiParam("用户 id") @PathVariable("uid") Long uid) {
        userService.unlockUser(uid);
        return ok();
    }

    /**
     * 更新用户信息
     *
     * @param updateUserRequestDTO 更新用户信息参数
     * @return 更新是否成功
     */
    @ApiOperation("更新用户信息")
    @PostMapping
    public WebResponse<Boolean> updateUser(
            @ApiParam("更新用户请求参数") @RequestBody UpdateUserRequestDTO updateUserRequestDTO)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return ok(userService.updateUser(updateUserRequestDTO));
    }

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
    @ApiOperation("分页查询用户列表")
    @GetMapping("/page")
    public WebResponse<PageVO<UserInfoVO>> getUserPage(
            @ApiParam("用户名") @RequestParam(value = "username", required = false) String username,
            @ApiParam("昵称") @RequestParam(value = "nickname", required = false) String nickname,
            @ApiParam("是否可用") @RequestParam(value = "enabled", required = false) Boolean enabled,
            @ApiParam("页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam("每页数量") @RequestParam("pageSize") Integer pageSize) {
        return ok(userService.getUserPage(username, nickname, enabled, pageNum, pageSize));
    }

    /**
     * 获取用户拥有的所有启用状态的角色列表
     *
     * @param uid 用户 id
     * @return 该用户拥有的所有启用状态的角色列表
     */
    @ApiOperation("获取用户拥有的所有启用状态的角色列表")
    @GetMapping("/{uid}/all/enabled/role")
    public WebResponse<List<EnabledRoleVO>> getUserAllEnabledRoles(@ApiParam("用户 id") @PathVariable("uid") Long uid) {
        return ok(userService.getUserAllEnabledRoles(uid));
    }

    /**
     * 给指定用户授予指定角色
     *
     * @param uid  用户 id
     * @param rids 角色 id，多个用半角逗号隔开
     */
    @ApiOperation("给指定用户授予指定角色")
    @PutMapping("/{uid}/role")
    public WebResponse<Void> assignUserRole(
            @ApiParam("用户 id") @PathVariable("uid") Long uid,
            @ApiParam("角色 id，多个用半角逗号分隔") @RequestParam("rids") String rids) {
        Set<Long> ridSet = Arrays.stream(rids.split(",")).filter(StringUtils::hasLength).map(Long::valueOf).collect(Collectors.toSet());
        userService.assignUserRole(uid, ridSet);
        return ok();
    }

    /**
     * 根据 token 获取用户昵称
     *
     * @param request 请求，获取 token
     * @return 用户昵称
     */
    @ApiOperation("根据 token 获取用户昵称")
    @GetMapping("/nickname")
    public WebResponse<String> getUserNickname(HttpServletRequest request) {
        return ok(userService.getNickname(userTokenService.getUserIdFromRequestHeader(request)));
    }
}
