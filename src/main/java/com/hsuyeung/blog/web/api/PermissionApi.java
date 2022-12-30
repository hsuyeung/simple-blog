package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.dto.permission.CreatePermissionRequestDTO;
import com.hsuyeung.blog.model.dto.permission.UpdatePermissionRequestDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.permission.EnabledPermissionVO;
import com.hsuyeung.blog.model.vo.permission.PermissionInfoVO;
import com.hsuyeung.blog.service.IPermissionService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
public class PermissionApi implements IBaseWebResponse {
    @Resource
    private IPermissionService permissionService;

    /**
     * 创建一个权限
     *
     * @param createPermissionRequestDTO 创建权限请求参数
     * @return 创建是否成功
     */
    @ApiOperation("创建一个权限")
    @PutMapping
    public WebResponse<Boolean> createPermission(
            @ApiParam("创建权限请求参数") @RequestBody CreatePermissionRequestDTO createPermissionRequestDTO) {
        return ok(permissionService.createPermission(createPermissionRequestDTO));
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
     * @param updatePermissionRequestDTO 更新权限请求参数
     * @return 更新是否成功
     */
    @ApiOperation("更新权限")
    @PostMapping
    public WebResponse<Boolean> updatePermission(
            @ApiParam("更新权限请求参数") @RequestBody UpdatePermissionRequestDTO updatePermissionRequestDTO) {
        return ok(permissionService.updatePermission(updatePermissionRequestDTO));
    }

    /**
     * 锁定一个权限
     *
     * @param pid 权限 id
     */
    @ApiOperation("锁定一个权限")
    @PostMapping("/lock/{pid}")
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
    @PostMapping("/unlock/{pid}")
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
     * @param path           权限路径，右模糊
     * @param method         请求方法类型，全匹配，不区分大小写
     * @param permissionDesc 权限描述，全模糊
     * @param enabled        是否可用，全匹配
     * @param pageNum        页码
     * @param pageSize       每页数量
     * @return 权限分页列表
     */
    @ApiOperation("分页查询权限列表")
    @GetMapping("/page")
    public WebResponse<PageVO<PermissionInfoVO>> getPermissionPage(
            @ApiParam("权限路径") @RequestParam(value = "path", required = false) String path,
            @ApiParam("请求方法类型") @RequestParam(value = "method", required = false) String method,
            @ApiParam("权限描述") @RequestParam(value = "permissionDesc", required = false) String permissionDesc,
            @ApiParam("是否可用") @RequestParam(value = "enabled", required = false) Boolean enabled,
            @ApiParam("页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam("每页数量") @RequestParam("pageSize") Integer pageSize) {
        return ok(permissionService.getPermissionPage(path, method, permissionDesc, enabled, pageNum, pageSize));
    }
}
