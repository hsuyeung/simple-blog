package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.systemconfig.SystemConfigSearchDTO;
import com.hsuyeung.blog.model.dto.systemconfig.UpdateSystemConfigDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.systemconfig.SystemConfigInfoVO;
import com.hsuyeung.blog.service.ISystemConfigService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author hsuyeung
 * @date 2022/07/06
 */
@Api(tags = "系统配置相关接口")
@RestController
@RequestMapping("/api/system/config")
@RequiredArgsConstructor
public class SystemConfigApi implements IBaseWebResponse {
    private final ISystemConfigService systemConfigService;

    /**
     * 更新一个系统配置
     *
     * @param updateSystemConfig 更新系统配置请求参数
     */
    @ApiOperation("更新一个系统配置")
    @PutMapping
    public WebResponse<Void> updateSystemConfig(
            @ApiParam("更新系统配置请求参数") @RequestBody UpdateSystemConfigDTO updateSystemConfig) {
        systemConfigService.updateSystemConfig(updateSystemConfig);
        return ok();
    }

    /**
     * 分页查询系统配置列表
     *
     * @param pageSearchParam 系统配置分页搜索条件
     * @return 系统配置分页列表
     */
    @ApiOperation("分页查询系统配置列表")
    @PostMapping("/actions/page")
    public WebResponse<PageVO<SystemConfigInfoVO>> getSystemConfigPage(
            @ApiParam("系统配置分页搜索条件") @RequestBody PageSearchDTO<SystemConfigSearchDTO> pageSearchParam) {
        return ok(systemConfigService.getSystemConfigPage(pageSearchParam));
    }

    /**
     * 刷新指定的系统配置缓存
     *
     * @param confId 系统配置 id
     */
    @ApiOperation("刷新指定的系统配置缓存")
    @PutMapping("/actions/refresh/cache/{confId}")
    public WebResponse<Void> refreshOneCache(@ApiParam("配置 id") @PathVariable("confId") Long confId) {
        systemConfigService.refreshOneCache(confId);
        return ok();
    }

    /**
     * 刷新所有的系统配置缓存
     */
    @ApiOperation("刷新所有的系统配置缓存")
    @PutMapping("/actions/refresh/cache")
    public WebResponse<Void> refreshAllCache() {
        systemConfigService.refreshAllCache();
        return ok();
    }

}
