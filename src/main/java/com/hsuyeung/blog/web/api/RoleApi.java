package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.dto.role.CreateRoleRequestDTO;
import com.hsuyeung.blog.model.dto.role.UpdateRoleRequestDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.permission.EnabledPermissionVO;
import com.hsuyeung.blog.model.vo.role.EnabledRoleVO;
import com.hsuyeung.blog.model.vo.role.RoleInfoVO;
import com.hsuyeung.blog.service.IRoleService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色相关接口
 *
 * @author hsuyeung
 * @date 2022/06/29
 */
@Api(tags = "角色相关接口")
@RestController
@RequestMapping("/api/role")
public class RoleApi implements IBaseWebResponse {
    @Resource
    private IRoleService roleService;

    /**
     * 创建一个角色
     *
     * @param createRoleRequestDTO 创建角色请求参数
     * @return 创建是否成功
     */
    @ApiOperation("创建一个角色")
    @PutMapping
    public WebResponse<Boolean> createRole(
            @ApiParam("创建角色请求参数") @RequestBody CreateRoleRequestDTO createRoleRequestDTO) {
        return ok(roleService.createRole(createRoleRequestDTO));
    }

    /**
     * 删除一个角色
     *
     * @param rid 角色 id
     */
    @ApiOperation("删除一个角色")
    @DeleteMapping("/{rid}")
    public WebResponse<Boolean> deleteRole(@ApiParam("角色 id") @PathVariable("rid") Long rid) {
        roleService.deleteRole(rid);
        return ok();
    }

    /**
     * 更新角色
     *
     * @param updateRoleRequestDTO 更新角色参数
     * @return 更新是否成功
     */
    @ApiOperation("更新角色")
    @PostMapping
    public WebResponse<Void> updateRole(
            @ApiParam("更新角色请求参数") @RequestBody UpdateRoleRequestDTO updateRoleRequestDTO) {
        roleService.updateRole(updateRoleRequestDTO);
        return ok();
    }

    /**
     * 锁定一个角色
     *
     * @param rid 角色 id
     */
    @ApiOperation("锁定一个角色")
    @PostMapping("/lock/{rid}")
    public WebResponse<Void> lockRole(@ApiParam("角色 id") @PathVariable("rid") Long rid) {
        roleService.lockRole(rid);
        return ok();
    }

    /**
     * 解锁一个角色
     *
     * @param rid 角色 id
     */
    @ApiOperation("解锁一个角色")
    @PostMapping("/unlock/{rid}")
    public WebResponse<Void> unlockRole(@ApiParam("角色 id") @PathVariable("rid") Long rid) {
        roleService.unlockRole(rid);
        return ok();
    }

    /**
     * 查询所有启用状态的角色
     *
     * @return 所有启用状态的角色
     */
    @ApiOperation("查询所有启用状态的角色")
    @GetMapping("/all/enabled")
    public WebResponse<List<EnabledRoleVO>> getAllEnabledRoles() {
        return ok(roleService.getAllEnabledRoles());
    }

    /**
     * 分页查询角色列表
     *
     * @param roleCode 角色编码，全模糊
     * @param enabled  是否可用，全匹配
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 角色分页列表
     */
    @ApiOperation("分页查询角色列表")
    @GetMapping("/page")
    public WebResponse<PageVO<RoleInfoVO>> getRolePage(
            @ApiParam("角色编码") @RequestParam(value = "roleCode", required = false) String roleCode,
            @ApiParam("是否可用") @RequestParam(value = "enabled", required = false) Boolean enabled,
            @ApiParam("页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam("每页数量") @RequestParam("pageSize") Integer pageSize) {
        return ok(roleService.getRolePage(roleCode, enabled, pageNum, pageSize));
    }

    /**
     * 获取角色拥有的所有启用状态的权限列表
     *
     * @param rid 角色 id
     * @return 该角色拥有的所有启用状态的权限列表
     */
    @ApiOperation("获取角色拥有的所有启用状态的权限列表")
    @GetMapping("/{rid}/all/enabled/permission")
    public WebResponse<List<EnabledPermissionVO>> getRoleAllEnabledPermissions(@ApiParam("角色 id") @PathVariable("rid") Long rid) {
        return ok(roleService.getRoleAllEnabledPermissions(rid));
    }

    /**
     * 给指定角色授予指定权限
     *
     * @param rid  角色 id
     * @param pids 权限 id，多个用半角逗号隔开
     */
    @ApiOperation("给指定角色授予指定权限")
    @PutMapping("/{rid}/permission")
    public WebResponse<Void> assignRolePermission(
            @ApiParam("角色 id") @PathVariable("rid") Long rid,
            @ApiParam("权限 id，多个用半角逗号分隔") @RequestParam("pids") String pids) {
        Set<Long> pidSet = Arrays.stream(pids.split(",")).filter(StringUtils::hasLength).map(Long::valueOf).collect(Collectors.toSet());
        roleService.assignRolePermission(rid, pidSet);
        return ok();
    }
}
