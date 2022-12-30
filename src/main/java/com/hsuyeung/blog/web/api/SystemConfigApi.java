package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.dto.systemconfig.UpdateSystemConfigRequestDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.systemconfig.SystemConfigInfoVO;
import com.hsuyeung.blog.service.ISystemConfigService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author hsuyeung
 * @date 2022/07/06
 */
@Api(tags = "系统配置相关接口")
@RestController
@RequestMapping("/api/system/config")
public class SystemConfigApi implements IBaseWebResponse {
    @Resource
    private ISystemConfigService systemConfigService;

    /**
     * 更新一个系统配置
     *
     * @param updateSystemConfigRequestDTO 更新系统配置请求参数
     */
    @ApiOperation("更新一个系统配置")
    @PostMapping
    public WebResponse<Void> updateSystemConfig(
            @ApiParam("更新系统配置请求参数") @RequestBody UpdateSystemConfigRequestDTO updateSystemConfigRequestDTO) {
        systemConfigService.updateSystemConfig(updateSystemConfigRequestDTO);
        return ok();
    }

    /**
     * 分页查询系统配置列表
     *
     * @param key      系统配置 key，全模糊
     * @param group    系统配置分组，精确匹配
     * @param desc     系统配置描述，全模糊
     * @param enabled  是否可用，全匹配
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 系统配置分页列表
     */
    @ApiOperation("分页查询系统配置列表")
    @GetMapping("/page")
    public WebResponse<PageVO<SystemConfigInfoVO>> getSystemConfigPage(
            @ApiParam("系统配置 key") @RequestParam(value = "key", required = false) String key,
            @ApiParam("系统配置分组") @RequestParam(value = "group", required = false) String group,
            @ApiParam("系统配置描述") @RequestParam(value = "desc", required = false) String desc,
            @ApiParam("是否可用") @RequestParam(value = "enabled", required = false) Boolean enabled,
            @ApiParam("页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam("每页数量") @RequestParam("pageSize") Integer pageSize) {
        return ok(systemConfigService.getSystemConfigPage(key, group, desc, enabled, pageNum, pageSize));
    }

    /**
     * 刷新指定的系统配置缓存
     *
     * @param confId 系统配置 id
     */
    @ApiOperation("刷新指定的系统配置缓存")
    @PostMapping("/refresh/cache/{confId}")
    public WebResponse<Void> refreshOneCache(@ApiParam("配置 id") @PathVariable("confId") Long confId) {
        systemConfigService.refreshOneCache(confId);
        return ok();
    }

    /**
     * 刷新所有的系统配置缓存
     */
    @ApiOperation("刷新所有的系统配置缓存")
    @PostMapping("/refresh/cache")
    public WebResponse<Void> refreshAllCache() {
        systemConfigService.refreshAllCache();
        return ok();
    }

}
