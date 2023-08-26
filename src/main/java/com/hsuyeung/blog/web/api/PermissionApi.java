package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.permission.CreatePermissionDTO;
import com.hsuyeung.blog.model.dto.permission.PermissionSearchDTO;
import com.hsuyeung.blog.model.dto.permission.UpdatePermissionDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.permission.EnabledPermissionVO;
import com.hsuyeung.blog.model.vo.permission.PermissionInfoVO;
import com.hsuyeung.blog.service.IPermissionService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限相关接口
 *
 * @author hsuyeung
 * @date 2022/06/29
 */
@Api(tags = "权限相关接口")
@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionApi implements IBaseWebResponse {
    private final IPermissionService permissionService;

    /**
     * 创建一个权限
     *
     * @param createPermission 创建权限请求参数
     * @return 创建是否成功
     */
    @ApiOperation("创建一个权限")
    @PostMapping
    public WebResponse<Boolean> createPermission(
            @ApiParam("创建权限请求参数") @RequestBody CreatePermissionDTO createPermission) {
        return ok(permissionService.createPermission(createPermission));
    }

    /**
     * 删除一个权限
     *
     * @param pid 权限 id
     */
    @ApiOperation("删除一个权限")
    @DeleteMapping("/{pid}")
    public WebResponse<Void> deletePermission(@ApiParam("权限 id") @PathVariable("pid") Long pid) {
        permissionService.deletePermission(pid);
        return ok();
    }

    /**
     * 更新权限
     *
     * @param updatePermission 更新权限请求参数
     * @return 更新是否成功
     */
    @ApiOperation("更新权限")
    @PutMapping
    public WebResponse<Boolean> updatePermission(
            @ApiParam("更新权限请求参数") @RequestBody UpdatePermissionDTO updatePermission) {
        return ok(permissionService.updatePermission(updatePermission));
    }

    /**
     * 锁定一个权限
     *
     * @param pid 权限 id
     */
    @ApiOperation("锁定一个权限")
    @PutMapping("/actions/lock/{pid}")
    public WebResponse<Void> lockPermission(@ApiParam("权限 id") @PathVariable("pid") Long pid) {
        permissionService.lockPermission(pid);
        return ok();
    }

    /**
     * 解锁一个权限
     *
     * @param pid 权限 id
     */
    @ApiOperation("解锁一个权限")
    @PutMapping("/actions/unlock/{pid}")
    public WebResponse<Void> unlockPermission(@ApiParam("权限 id") @PathVariable("pid") Long pid) {
        permissionService.unlockPermission(pid);
        return ok();
    }

    /**
     * 获取所有可用状态的权限信息
     *
     * @return 可用状态的权限列表
     */
    @ApiOperation("获取所有可用状态的权限信息")
    @GetMapping("/all/enabled")
    public WebResponse<List<EnabledPermissionVO>> getAllEnabledPermission() {
        return ok(permissionService.getAllEnabledPermission());
    }

    /**
     * 分页查询权限列表
     *
     * @param pageSearchParam 权限分页搜索条件
     * @return 权限分页列表
     */
    @ApiOperation("分页查询权限列表")
    @PostMapping("/actions/page")
    public WebResponse<PageVO<PermissionInfoVO>> getPermissionPage(
            @ApiParam("权限分页搜索条件") @RequestBody PageSearchDTO<PermissionSearchDTO> pageSearchParam) {
        return ok(permissionService.getPermissionPage(pageSearchParam));
    }
}
