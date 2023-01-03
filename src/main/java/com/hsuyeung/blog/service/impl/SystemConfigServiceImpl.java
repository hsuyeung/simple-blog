package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.cache.SystemConfigCache;
import com.hsuyeung.blog.constant.SystemConfigConstants;
import com.hsuyeung.blog.mapper.SystemConfigMapper;
import com.hsuyeung.blog.model.dto.systemconfig.UpdateSystemConfigRequestDTO;
import com.hsuyeung.blog.model.entity.SystemConfigEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.customconfig.*;
import com.hsuyeung.blog.model.vo.systemconfig.SystemConfigInfoVO;
import com.hsuyeung.blog.service.ISystemConfigService;
import com.hsuyeung.blog.service.IUserService;
import com.hsuyeung.blog.util.AssertUtil;
import com.hsuyeung.blog.util.BizAssertUtil;
import com.hsuyeung.blog.util.ConvertUtil;
import com.hsuyeung.blog.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hsuyeung.blog.constant.DateFormatConstants.FORMAT_YEAR_TO_SECOND;
import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.*;
import static com.hsuyeung.blog.constant.enums.LogicSwitchEnum.OFF;
import static com.hsuyeung.blog.constant.enums.LogicSwitchEnum.ON;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author hsuyeung
 * @since 2022/06/05
 */
@Slf4j
@Service("systemConfigService")
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfigEntity> implements ISystemConfigService {
    @Resource
    private SystemConfigCache systemConfigCache;
    @Resource
    @Lazy
    private IUserService userService;

    @Override
    public <T> T getConfigValue(SystemConfigConstants.SystemConfigEnum systemConfigEnum, Class<T> valueType) {
        AssertUtil.notNull(systemConfigEnum, "systemConfigEnum 不能为空");
        String group = systemConfigEnum.getGroup();
        String key = systemConfigEnum.getKeyName();
        SystemConfigEntity systemConfigEntity = systemConfigCache.getSystemConfigValueCache(group, key);
        if (Objects.isNull(systemConfigEntity)) {
            systemConfigEntity = lambdaQuery()
                    .select(SystemConfigEntity::getConfKey, SystemConfigEntity::getConfGroup, SystemConfigEntity::getConfValue)
                    .eq(StringUtils.hasLength(group), SystemConfigEntity::getConfGroup, group)
                    .eq(SystemConfigEntity::getEnabled, ON)
                    .eq(Objects.nonNull(key), SystemConfigEntity::getConfKey, key)
                    .one();
            if (Objects.isNull(systemConfigEntity)) {
                log.error("配置 {}.{} 不存在，使用系统内置默认值：{}", systemConfigEnum.getGroup(), systemConfigEnum.getKeyName(), systemConfigEnum.getDefaultValue());
                return ConvertUtil.convertType(systemConfigEnum.getDefaultValue(), valueType);
            }
            systemConfigCache.cacheSystemConfig(group, key, systemConfigEntity);
        }
        return ConvertUtil.convertType(systemConfigEntity.getConfValue(), valueType);
    }

    @Override
    public HomeCustomConfigVO getHomeCustomConfig() {
        String key = getConfigValue(REDIS_HOME_CUSTOM_CONFIG_KEY, String.class);
        HomeCustomConfigVO homeCustomConfigVO = systemConfigCache.getPageCustomConfigCache(key, HomeCustomConfigVO.class);
        if (Objects.nonNull(homeCustomConfigVO)) {
            return homeCustomConfigVO;
        }
        return this.getAndCacheHomeCustomConfig(key);
    }

    @Override
    public ArchiveCustomConfigVO getArchiveCustomConfig() {
        String key = getConfigValue(REDIS_ARCHIVE_CUSTOM_CONFIG_KEY, String.class);
        ArchiveCustomConfigVO archiveCustomConfigVO = systemConfigCache.getPageCustomConfigCache(key, ArchiveCustomConfigVO.class);
        if (Objects.nonNull(archiveCustomConfigVO)) {
            return archiveCustomConfigVO;
        }
        return this.getAndCacheArchiveCustomConfig(key);
    }

    @Override
    public AboutCustomConfigVO getAboutCustomConfig() {
        String key = getConfigValue(REDIS_ABOUT_CUSTOM_CONFIG_KEY, String.class);
        AboutCustomConfigVO aboutCustomConfigVO = systemConfigCache.getPageCustomConfigCache(key, AboutCustomConfigVO.class);
        if (Objects.nonNull(aboutCustomConfigVO)) {
            return aboutCustomConfigVO;
        }
        return this.getAndCacheAboutCustomConfig(key);
    }


    @Override
    public CommonCustomConfigVO getCommonCustomConfig() {
        String key = getConfigValue(REDIS_COMMON_CUSTOM_CONFIG_KEY, String.class);
        CommonCustomConfigVO commonCustomConfigVO = systemConfigCache.getPageCustomConfigCache(key, CommonCustomConfigVO.class);
        if (Objects.nonNull(commonCustomConfigVO)) {
            return commonCustomConfigVO;
        }
        return this.getAndCacheCommonCustomConfig(key);
    }

    @Override
    public FriendLinkCustomConfigVO getFriendLinkCustomConfig() {
        String key = getConfigValue(REDIS_FRIEND_LINK_CUSTOM_CONFIG_KEY, String.class);
        FriendLinkCustomConfigVO friendLinkCustomConfigVO = systemConfigCache.getPageCustomConfigCache(key, FriendLinkCustomConfigVO.class);
        if (Objects.nonNull(friendLinkCustomConfigVO)) {
            return friendLinkCustomConfigVO;
        }
        return this.getAndCacheFriendLinkCustomConfig(key);
    }

    @Override
    public PageVO<SystemConfigInfoVO> getSystemConfigPage(String key, String group, String desc, Boolean enabled, Integer pageNum, Integer pageSize) {
        Page<SystemConfigEntity> entityPage = lambdaQuery()
                .select(SystemConfigEntity::getId, SystemConfigEntity::getConfKey, SystemConfigEntity::getConfValue,
                        SystemConfigEntity::getConfGroup, SystemConfigEntity::getDescription, SystemConfigEntity::getEnabled,
                        SystemConfigEntity::getCreateTime, SystemConfigEntity::getCreateBy,
                        SystemConfigEntity::getUpdateTime, SystemConfigEntity::getUpdateBy)
                .like(StringUtils.hasLength(key), SystemConfigEntity::getConfKey, key)
                .eq(StringUtils.hasLength(group), SystemConfigEntity::getConfGroup, group)
                .like(StringUtils.hasLength(desc), SystemConfigEntity::getDescription, desc)
                .eq(Objects.nonNull(enabled), SystemConfigEntity::getEnabled, Objects.equals(enabled, true) ? ON : OFF)
                .orderByDesc(SystemConfigEntity::getUpdateTime)
                .page(new Page<>(pageNum, pageSize));
        List<SystemConfigEntity> entityList = entityPage.getRecords();
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageVO<>(0L, Collections.emptyList());
        }
        List<SystemConfigInfoVO> voList = entityList.stream().map(entity -> {
            SystemConfigInfoVO vo = ConvertUtil.convert(entity, SystemConfigInfoVO.class);
            vo.setConfDefaultValue(SystemConfigConstants.SystemConfigEnum.getDefaultValue(entity.getConfGroup(), entity.getConfKey()));
            vo.setEnabled(Objects.equals(entity.getEnabled(), ON));
            vo.setCreateTime(DateUtil.formatLocalDateTime(entity.getCreateTime(), FORMAT_YEAR_TO_SECOND));
            vo.setUpdateTime(DateUtil.formatLocalDateTime(entity.getUpdateTime(), FORMAT_YEAR_TO_SECOND));
            String createBy = entity.getCreateBy();
            String updateBy = entity.getUpdateBy();
            vo.setCreateBy(userService.getUsernameById(StringUtils.hasLength(createBy) ? Long.parseLong(createBy) : null));
            vo.setUpdateBy(userService.getUsernameById(StringUtils.hasLength(updateBy) ? Long.parseLong(updateBy) : null));
            return vo;
        }).collect(Collectors.toList());
        return new PageVO<>(entityPage.getTotal(), voList);
    }

    @Override
    public void updateSystemConfig(UpdateSystemConfigRequestDTO updateSystemConfigRequestDTO) {
        AssertUtil.notNull(updateSystemConfigRequestDTO, "updateSystemConfigRequestDTO 不能为空");
        Long id = updateSystemConfigRequestDTO.getId();
        SystemConfigEntity systemConfigEntity = lambdaQuery()
                .eq(SystemConfigEntity::getId, id)
                .one();
        BizAssertUtil.notNull(systemConfigEntity, "配置不存在");
        systemConfigEntity.setConfValue(updateSystemConfigRequestDTO.getConfValue().trim());
        systemConfigEntity.setEnabled(updateSystemConfigRequestDTO.getEnabled() ? ON : OFF);
        updateById(systemConfigEntity);
        refreshOneCache(id);
        refreshAllCustomConfig();
    }

    @Override
    public void refreshOneCache(Long confId) {
        AssertUtil.notNull(confId, "confId 不能为空");
        SystemConfigEntity systemConfigEntity = lambdaQuery()
                .select(SystemConfigEntity::getConfKey, SystemConfigEntity::getConfGroup, SystemConfigEntity::getConfValue)
                .eq(SystemConfigEntity::getId, confId)
                .one();
        BizAssertUtil.notNull(systemConfigEntity, "配置不存在");
        systemConfigCache.cacheSystemConfig(systemConfigEntity.getConfGroup(), systemConfigEntity.getConfKey(), systemConfigEntity);
    }

    @Override
    public void refreshAllCache() {
        List<SystemConfigEntity> entityList = lambdaQuery()
                .select(SystemConfigEntity::getConfKey, SystemConfigEntity::getConfGroup, SystemConfigEntity::getConfValue)
                .eq(SystemConfigEntity::getEnabled, ON)
                .list();
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }
        entityList.forEach(entity -> systemConfigCache.cacheSystemConfig(entity.getConfGroup(), entity.getConfKey(), entity));
        refreshAllCustomConfig();
    }

    // --------------------------------------------- PRIVATE METHOD REGION ---------------------------------------------

    /**
     * 获取并缓存首页的自定义配置
     *
     * @param key 缓存的 key
     * @return 缓存值
     */
    private HomeCustomConfigVO getAndCacheHomeCustomConfig(String key) {
        HomeCustomConfigVO homeCustomConfigVO = HomeCustomConfigVO.builder()
                .blogHomeTitle(getConfigValue(CUSTOM_BLOG_HOME_TITLE, String.class))
                .blogHomeDesc(getConfigValue(CUSTOM_BLOG_HOME_DESC, String.class))
                .blogHomeKeywords(getConfigValue(CUSTOM_BLOG_HOME_KEYWORDS, String.class))
                .blogHomeBannerImg(getConfigValue(CUSTOM_BLOG_HOME_BANNER_IMG, String.class))
                .build();
        systemConfigCache.cachePageCustomConfigCache(key, homeCustomConfigVO);
        return homeCustomConfigVO;
    }

    /**
     * 获取并缓存归档页面的自定义配置
     *
     * @param key 缓存的 key
     * @return 缓存值
     */
    private ArchiveCustomConfigVO getAndCacheArchiveCustomConfig(String key) {
        ArchiveCustomConfigVO archiveCustomConfigVO = ArchiveCustomConfigVO.builder()
                .blogArchiveDesc(getConfigValue(CUSTOM_BLOG_ARCHIVE_DESC, String.class))
                .blogArchiveKeywords(getConfigValue(CUSTOM_BLOG_ARCHIVE_KEYWORDS, String.class))
                .blogArchiveBannerImg(getConfigValue(CUSTOM_BLOG_ARCHIVE_BANNER_IMG, String.class))
                .build();
        systemConfigCache.cachePageCustomConfigCache(key, archiveCustomConfigVO);
        return archiveCustomConfigVO;
    }

    /**
     * 获取并缓存关于页面的自定义配置
     *
     * @param key 缓存的 key
     * @return 缓存值
     */
    private AboutCustomConfigVO getAndCacheAboutCustomConfig(String key) {
        AboutCustomConfigVO aboutCustomConfigVO = AboutCustomConfigVO.builder()
                .blogAboutDesc(getConfigValue(CUSTOM_BLOG_ABOUT_DESC, String.class))
                .blogAboutKeywords(getConfigValue(CUSTOM_BLOG_ABOUT_KEYWORDS, String.class))
                .blogAboutBannerImg(getConfigValue(CUSTOM_BLOG_ABOUT_BANNER_IMG, String.class))
                .build();
        systemConfigCache.cachePageCustomConfigCache(key, aboutCustomConfigVO);
        return aboutCustomConfigVO;
    }

    /**
     * 获取并缓存所有页面公共的自定义配置
     *
     * @param key 缓存的 key
     * @return 缓存值
     */
    private CommonCustomConfigVO getAndCacheCommonCustomConfig(String key) {
        CommonCustomConfigVO commonCustomConfigVO = CommonCustomConfigVO.builder()
                .headerText(getConfigValue(CUSTOM_HEADER_TEXT, String.class))
                .aboutFooterText(getConfigValue(CUSTOM_ABOUT_FOOTER_TEXT, String.class))
                .footerCopyright(getConfigValue(CUSTOM_FOOTER_COPYRIGHT, String.class))
                .footerAboutText(getConfigValue(CUSTOM_FOOTER_ABOUT_TEXT, String.class))
                .beianNum(getConfigValue(CUSTOM_BEI_AN_NUM, String.class))
                .avatar(getConfigValue(CUSTOM_BLOG_AVATAR, String.class))
                .build();
        systemConfigCache.cachePageCustomConfigCache(key, commonCustomConfigVO);
        return commonCustomConfigVO;
    }

    /**
     * 获取并缓存友链页面的自定义配置
     *
     * @param key 缓存的 key
     * @return 缓存值
     */
    private FriendLinkCustomConfigVO getAndCacheFriendLinkCustomConfig(String key) {
        FriendLinkCustomConfigVO friendLinkCustomConfigVO = FriendLinkCustomConfigVO.builder()
                .friendLinkDesc(getConfigValue(CUSTOM_FRIEND_LINK_DESC, String.class))
                .friendLinkKeywords(getConfigValue(CUSTOM_FRIEND_LINK_KEYWORDS, String.class))
                .friendLinkBannerImg(getConfigValue(CUSTOM_FRIEND_LINK_BANNER_IMG, String.class))
                .build();
        systemConfigCache.cachePageCustomConfigCache(key, friendLinkCustomConfigVO);
        return friendLinkCustomConfigVO;
    }

    /**
     * 刷新所有的页面自定义配置
     */
    private void refreshAllCustomConfig() {
        this.getAndCacheHomeCustomConfig(getConfigValue(REDIS_HOME_CUSTOM_CONFIG_KEY, String.class));
        this.getAndCacheArchiveCustomConfig(getConfigValue(REDIS_ARCHIVE_CUSTOM_CONFIG_KEY, String.class));
        this.getAndCacheAboutCustomConfig(getConfigValue(REDIS_ABOUT_CUSTOM_CONFIG_KEY, String.class));
        this.getAndCacheCommonCustomConfig(getConfigValue(REDIS_COMMON_CUSTOM_CONFIG_KEY, String.class));
        this.getAndCacheFriendLinkCustomConfig(getConfigValue(REDIS_FRIEND_LINK_CUSTOM_CONFIG_KEY, String.class));
    }
}
