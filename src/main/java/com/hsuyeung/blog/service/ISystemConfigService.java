package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.constant.SystemConfigConstants;
import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.systemconfig.SystemConfigSearchDTO;
import com.hsuyeung.blog.model.dto.systemconfig.UpdateSystemConfigDTO;
import com.hsuyeung.blog.model.entity.SystemConfigEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.customconfig.*;
import com.hsuyeung.blog.model.vo.systemconfig.SystemConfigInfoVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 系统配置表 服务类
 * </p>
 *
 * @author hsuyeung
 * @since 2022/06/05
 */
@Validated
public interface ISystemConfigService extends IService<SystemConfigEntity> {

    /**
     * 查询指定分组下的单个配置的值，强制类型转换为指定的类型返回，如果该配置不存在，则使用其默认值
     *
     * @param systemConfigEnum {@link SystemConfigConstants.SystemConfigEnum}
     * @param valueType        值的类型
     * @param <T>              值的类型
     * @return 值
     */
    <T> T getConfigValue(@NotNull(message = "systemConfigEnum 不能为 null") SystemConfigConstants.SystemConfigEnum systemConfigEnum,
                         @NotNull(message = "valueType 不能为 null") Class<T> valueType);

    /**
     * 获取首页的自定义配置
     *
     * @return k-配置名称，v-配置值
     */
    HomeCustomConfigVO getHomeCustomConfig();

    /**
     * 获取文章归档页的自定义配置
     *
     * @return k-配置名称，v-配置值
     */
    ArchiveCustomConfigVO getArchiveCustomConfig();

    /**
     * 获取关于页的自定义配置
     *
     * @return k-配置名称，v-配置值
     */
    AboutCustomConfigVO getAboutCustomConfig();

    /**
     * 获取公共配置
     *
     * @return k-配置名称，v-配置值
     */
    CommonCustomConfigVO getCommonCustomConfig();

    /**
     * 获取友链页面的自定义配置
     *
     * @return k-配置名称，v-配置值
     */
    FriendLinkCustomConfigVO getFriendLinkCustomConfig();

    /**
     * 分页查询系统配置列表
     *
     * @param pageSearchParam 系统配置分页搜索条件
     * @return 系统配置分页列表
     */
    PageVO<SystemConfigInfoVO> getSystemConfigPage(@NotNull(message = "pageSearchParam 不能为 null") @Valid
                                                   PageSearchDTO<SystemConfigSearchDTO> pageSearchParam);

    /**
     * 更新一个系统配置
     *
     * @param updateSystemConfig 更新系统配置请求参数
     */
    void updateSystemConfig(@NotNull(message = "updateSystemConfig 不能为 null") @Valid UpdateSystemConfigDTO updateSystemConfig);

    /**
     * 刷新指定的系统配置缓存
     *
     * @param confId 系统配置 id
     */
    void refreshOneCache(Long confId);

    /**
     * 刷新所有的系统配置缓存
     */
    void refreshAllCache();
}
