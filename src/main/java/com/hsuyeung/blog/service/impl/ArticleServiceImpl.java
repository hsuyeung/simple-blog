package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.cache.ArticleCache;
import com.hsuyeung.blog.constant.StringConstants;
import com.hsuyeung.blog.constant.SystemConfigConstants;
import com.hsuyeung.blog.constant.enums.ContentTypeEnum;
import com.hsuyeung.blog.exception.BizException;
import com.hsuyeung.blog.exception.NotFoundException;
import com.hsuyeung.blog.exception.SystemInternalException;
import com.hsuyeung.blog.mapper.ArticleMapper;
import com.hsuyeung.blog.model.dto.article.AddArticleRequestDTO;
import com.hsuyeung.blog.model.dto.article.UpdateArticleRequestDTO;
import com.hsuyeung.blog.model.entity.ArticleEntity;
import com.hsuyeung.blog.model.entity.ContentEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.article.*;
import com.hsuyeung.blog.rss.Item;
import com.hsuyeung.blog.rss.RSS;
import com.hsuyeung.blog.rss.Writer;
import com.hsuyeung.blog.service.*;
import com.hsuyeung.blog.util.AssertUtil;
import com.hsuyeung.blog.util.BizAssertUtil;
import com.hsuyeung.blog.util.ConvertUtil;
import com.hsuyeung.blog.util.DateUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.hsuyeung.blog.constant.DateFormatConstants.*;
import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.*;
import static com.hsuyeung.blog.constant.enums.ContentTypeEnum.HTML;
import static com.hsuyeung.blog.constant.enums.PinEnum.PIN;
import static com.hsuyeung.blog.constant.enums.PinEnum.UN_PIN;

/**
 * <p>
 * 博客文章表 服务实现类
 * </p>
 *
 * @author hsuyeung
 * @since 2022/06/05
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleEntity> implements IArticleService {
    @Resource
    private ArticleCache articleCache;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private IContentService contentService;
    @Resource
    private IUserService userService;
    @Resource
    @Lazy
    private ICommentService commentService;

    @Override
    public List<HomeArticleVO> getHomeArticleList() {
        String key = systemConfigService.getConfigValue(REDIS_HOME_ARTICLE_LIST_KEY, String.class);
        List<HomeArticleVO> homeArticleList = articleCache.getHomeArticleList(key);
        if (!CollectionUtils.isEmpty(homeArticleList)) {
            return homeArticleList;
        }
        Integer articleSize = systemConfigService.getConfigValue(ARTICLE_HOME_ARTICLE_LIST_SIZE, Integer.class);
        List<ArticleEntity> entityList = lambdaQuery()
                .select(ArticleEntity::getTitle, ArticleEntity::getRoute,
                        ArticleEntity::getCreateTime, ArticleEntity::getPin)
                .orderByDesc(Arrays.asList(ArticleEntity::getPin, ArticleEntity::getCreateTime))
                .last(" LIMIT " + articleSize)
                .list();
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        homeArticleList = entityList.stream().map(entity -> {
            HomeArticleVO vo = ConvertUtil.convert(entity, HomeArticleVO.class);
            vo.setCreateTime(DateUtil.formatLocalDateTime(entity.getCreateTime(), FORMAT_YEAR_TO_DAY_SPLIT_BY_FORWARD_SLASH));
            vo.setPin(Objects.equals(entity.getPin(), PIN));
            return vo;
        }).collect(Collectors.toList());
        articleCache.cacheHomeArticleList(key, homeArticleList);
        return homeArticleList;
    }

    @Override
    public ArticleDetailVO getArticleDetail(String articleRoute) {
        AssertUtil.hasLength(articleRoute, "articleRoute 不能为空");
        String key = systemConfigService.getConfigValue(REDIS_ARTICLE_DETAIL_KEY, String.class);
        ArticleDetailVO articleDetail = articleCache.getArticleDetail(key, articleRoute);
        if (Objects.nonNull(articleDetail)) {
            return articleDetail;
        }
        ArticleEntity articleEntity = lambdaQuery()
                .select(ArticleEntity::getId, ArticleEntity::getTitle, ArticleEntity::getAuthor,
                        ArticleEntity::getKeywords, ArticleEntity::getDescription, ArticleEntity::getContentId,
                        ArticleEntity::getCreateTime)
                .eq(ArticleEntity::getRoute, articleRoute)
                .one();
        if (Objects.isNull(articleEntity)) {
            throw new NotFoundException(String.format("文章 %s 不存在", articleRoute));
        }
        articleDetail = ConvertUtil.convert(articleEntity, ArticleDetailVO.class);
        articleDetail.setCreateTime(DateUtil.formatLocalDateTime(articleEntity.getCreateTime(), FORMAT_YEAR_TO_DAY_SPLIT_BY_CN));
        articleDetail.setContent(contentService.getContent(articleEntity.getContentId(), HTML));

        articleCache.cacheArticleDetail(key, articleRoute, articleDetail);

        return articleDetail;
    }

    @Override
    public ArchiveVO getArchiveList() {
        String key = systemConfigService.getConfigValue(REDIS_ARTICLE_ARCHIVE_KEY, String.class);
        ArchiveVO archiveVO = articleCache.getArchive(key);
        if (Objects.nonNull(archiveVO)) {
            return archiveVO;
        }
        List<ArticleEntity> allArticleList = lambdaQuery()
                .select(ArticleEntity::getRoute, ArticleEntity::getTitle, ArticleEntity::getCreateTime)
                .orderByDesc(Arrays.asList(ArticleEntity::getPin, ArticleEntity::getCreateTime))
                .list();
        if (CollectionUtils.isEmpty(allArticleList)) {
            return null;
        }
        archiveVO = this.getArchiveByMonth(allArticleList);
        articleCache.cacheArchive(key, archiveVO);
        return archiveVO;
    }

    @Override
    public boolean isExists(Long articleId) {
        return lambdaQuery().select(ArticleEntity::getId).eq(ArticleEntity::getId, articleId) != null;
    }

    @Override
    public ArticleRouteAndTitleVO getArticleRouteAndTitle(Long articleId) {
        AssertUtil.notNull(articleId, "articleId 不能为空");
        ArticleEntity articleEntity = lambdaQuery().select(ArticleEntity::getRoute, ArticleEntity::getTitle).eq(ArticleEntity::getId, articleId).one();
        AssertUtil.notNull(articleEntity, String.format("id 为 %d 的文章不存在", articleId));
        return ConvertUtil.convert(articleEntity, ArticleRouteAndTitleVO.class);
    }

    @Override
    public List<ArticleTitleInfoVO> getArticleTitleList() {
        return ConvertUtil.convertList(
                lambdaQuery()
                        .select(ArticleEntity::getId, ArticleEntity::getTitle)
                        .list(),
                ArticleTitleInfoVO.class
        );
    }

    @Override
    public Map<Long, ArticleRouteAndTitleVO> getArticleRouteAndTitle(Collection<Long> ids) {
        AssertUtil.notNull(ids, "ids 不能为空");
        List<ArticleEntity> entityList = lambdaQuery()
                .select(ArticleEntity::getId, ArticleEntity::getRoute, ArticleEntity::getTitle)
                .in(ArticleEntity::getId, ids)
                .list();
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyMap();
        }
        Map<Long, ArticleRouteAndTitleVO> map = new HashMap<>(entityList.size());
        entityList.forEach(entity -> map.put(entity.getId(), new ArticleRouteAndTitleVO(entity.getTitle(), entity.getRoute())));
        return map;
    }

    @Override
    public PageVO<ArticleInfoVO> getArticlePage(String title, String author, String keywords, String desc, Boolean pin,
                                                Integer pageNum, Long startTimestamp, Long endTimestamp, Integer pageSize) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (Objects.nonNull(startTimestamp)) {
            startTime = DateUtil.fromLongToJava8LocalDate(startTimestamp);
        }
        if (Objects.nonNull(endTimestamp)) {
            endTime = DateUtil.fromLongToJava8LocalDate(endTimestamp);
        }
        Page<ArticleEntity> entityPage = lambdaQuery()
                .select(ArticleEntity::getId, ArticleEntity::getTitle, ArticleEntity::getAuthor, ArticleEntity::getKeywords,
                        ArticleEntity::getPin, ArticleEntity::getRoute, ArticleEntity::getDescription,
                        ArticleEntity::getCreateTime, ArticleEntity::getCreateBy, ArticleEntity::getUpdateTime,
                        ArticleEntity::getUpdateBy)
                .like(StringUtils.hasLength(title), ArticleEntity::getTitle, title)
                .like(StringUtils.hasLength(author), ArticleEntity::getAuthor, author)
                .like(StringUtils.hasLength(keywords), ArticleEntity::getKeywords, keywords)
                .like(StringUtils.hasLength(desc), ArticleEntity::getDescription, desc)
                .eq(Objects.nonNull(pin), ArticleEntity::getPin, Objects.equals(pin, true) ? PIN : UN_PIN)
                .ge(Objects.nonNull(startTime), ArticleEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), ArticleEntity::getCreateTime, endTime)
                .orderByDesc(ArticleEntity::getUpdateTime)
                .page(new Page<>(pageNum, pageSize));
        List<ArticleEntity> entityList = entityPage.getRecords();
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageVO<>(0L, Collections.emptyList());
        }
        List<ArticleInfoVO> voList = entityList.stream().map(entity -> {
            ArticleInfoVO vo = ConvertUtil.convert(entity, ArticleInfoVO.class);
            vo.setPin(Objects.equals(entity.getPin(), PIN));
            vo.setUrl(String.format("%s/article/%s",
                    systemConfigService.getConfigValue(SYSTEM_BLOG_HOME_URL, String.class),
                    entity.getRoute()));
            vo.setCommentNum(commentService.countByArticleId(entity.getId()));
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteArticle(Long id) {
        ArticleEntity article = lambdaQuery().select(ArticleEntity::getRoute).eq(ArticleEntity::getId, id).one();
        if (Objects.isNull(article)) {
            return;
        }
        lambdaUpdate().eq(ArticleEntity::getId, id).remove();
        // 刷新缓存
        this.refreshHomeArticleListCache();
        this.refreshArchiveCache();
        articleCache.deleteCache(systemConfigService.getConfigValue(REDIS_ARTICLE_DETAIL_KEY, String.class), article.getRoute());
        // 刷新 RSS
        this.refreshRSS();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addArticle(AddArticleRequestDTO addArticleRequestDTO) {
        AssertUtil.notNull(addArticleRequestDTO, "addArticleRequestDTO 不能为空");
        ContentEntity contentEntity = ContentEntity.builder()
                .mdContent(addArticleRequestDTO.getMdContent())
                .htmlContent(addArticleRequestDTO.getHtmlContent())
                .build();
        contentService.save(contentEntity);
        ArticleEntity articleEntity = ConvertUtil.convert(addArticleRequestDTO, ArticleEntity.class)
                .setContentId(contentEntity.getId())
                .setPin(Objects.equals(addArticleRequestDTO.getPin(), true) ? PIN : UN_PIN);
        try {
            save(articleEntity);
        } catch (DuplicateKeyException e) {
            throw new BizException("文章重复", e);
        }
        // 刷新缓存
        this.refreshHomeArticleListCache();
        this.refreshArchiveCache();
        // 刷新 RSS
        this.refreshRSS();
    }

    @Override
    public String getArticleContent(Long aid, ContentTypeEnum contentType) {
        AssertUtil.notNull(aid, "aid 不能为空");
        AssertUtil.notNull(contentType, "contentType 不能为空");
        ArticleEntity article = lambdaQuery().select(ArticleEntity::getContentId).eq(ArticleEntity::getId, aid).one();
        BizAssertUtil.notNull(article, "文章不存在");
        return contentService.getContent(article.getContentId(), contentType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArticle(UpdateArticleRequestDTO updateArticleRequestDTO) {
        AssertUtil.notNull(updateArticleRequestDTO, "updateArticleRequestDTO 不能为空");
        ArticleEntity articleEntity = lambdaQuery().eq(ArticleEntity::getId, updateArticleRequestDTO.getId()).one();
        BizAssertUtil.notNull(articleEntity, "文章不存在");
        try {
            // 更新文章
            updateById(ConvertUtil.convert(updateArticleRequestDTO, ArticleEntity.class)
                    .setPin(Objects.equals(updateArticleRequestDTO.getPin(), true) ? PIN : UN_PIN));
            // 更新文章内容
            contentService.updateById(ContentEntity.builder()
                    .id(articleEntity.getContentId())
                    .mdContent(updateArticleRequestDTO.getMdContent())
                    .htmlContent(updateArticleRequestDTO.getHtmlContent())
                    .build());
        } catch (DuplicateKeyException e) {
            throw new BizException("文章重复", e);
        }
        // 刷新文章列表缓存
        this.refreshHomeArticleListCache();
        // 刷新文章归档缓存
        this.refreshArchiveCache();
        // 刷新文章详情缓存
        this.refreshArticleDetailCache(articleEntity.getRoute(), updateArticleRequestDTO.getRoute());
        // 刷新 RSS
        this.refreshRSS();
    }

    // --------------------------------------------- PRIVATE METHOD ---------------------------------------------

    /**
     * 刷新文章详情缓存
     *
     * @param oldRoute 修改前的文章路由
     * @param newRoute 修改后的文章路由
     */
    private void refreshArticleDetailCache(String oldRoute, String newRoute) {
        articleCache.deleteCache(systemConfigService.getConfigValue(REDIS_ARTICLE_DETAIL_KEY, String.class), oldRoute);
        getArticleDetail(newRoute);
    }

    /**
     * 刷新归档页面文章数据
     */
    private void refreshArchiveCache() {
        articleCache.deleteCache(systemConfigService.getConfigValue(REDIS_ARTICLE_ARCHIVE_KEY, String.class));
        getArchiveList();
    }

    /**
     * 刷新博客首页文章列表缓存
     */
    private void refreshHomeArticleListCache() {
        articleCache.deleteCache(systemConfigService.getConfigValue(REDIS_HOME_ARTICLE_LIST_KEY, String.class));
        getHomeArticleList();
    }

    /**
     * 根据月份归档
     *
     * @return 归档信息
     */
    private ArchiveVO getArchiveByMonth(List<ArticleEntity> allArticleList) {
        List<ArchiveNode> archiveNodeList = new ArrayList<>();
        Map<String, List<ArticleEntity>> articleTimeMap = allArticleList.stream()
                .collect(Collectors.groupingBy(entity ->
                        DateUtil.formatLocalDateTime(entity.getCreateTime(), FORMAT_YEAR_TO_MONTH_SPLIT_BY_CN)));
        articleTimeMap.forEach((time, articleList) -> {
            ArchiveNode archiveNode = new ArchiveNode();
            archiveNode.setTime(time);
            List<ArchiveArticleVO> archiveArticleList = articleList.stream()
                    .map(entity -> ConvertUtil.convert(entity, ArchiveArticleVO.class))
                    .collect(Collectors.toList());
            archiveNode.setArticleList(archiveArticleList);
            archiveNodeList.add(archiveNode);
        });
        archiveNodeList.sort(Comparator.comparing(ArchiveNode::getTime).reversed());
        return new ArchiveVO(allArticleList.size(), archiveNodeList);
    }

    /**
     * 刷新 RSS 文件
     */
    private void refreshRSS() {
        try {
            List<ArticleEntity> articleList = lambdaQuery()
                    .orderByDesc(ArticleEntity::getCreateTime)
                    .list();
            if (CollectionUtils.isEmpty(articleList)) {
                return;
            }
            String blogHomeUrl = systemConfigService.getConfigValue(SYSTEM_BLOG_HOME_URL, String.class);
            List<Item> itemList = articleList.stream()
                    .map(article -> {
                        String articleUrl = String.format("%s/article/%s", blogHomeUrl, article.getRoute());
                        return Item.builder()
                                .title(article.getTitle())
                                .link(articleUrl)
                                .description(article.getDescription())
                                .comments(String.format("%s#comment-container", articleUrl))
                                .guid(articleUrl)
                                .pubDate(DateUtil.formatLocalDateTimeToRFC822String(article.getCreateTime(), Locale.ENGLISH))
                                .source(Item.Source.builder()
                                        .url(blogHomeUrl)
                                        .value("Hsu Yeung 的博客")
                                        .build())
                                .build();
                    })
                    .collect(Collectors.toList());
            LocalDateTime now = LocalDateTime.now();
            String rfc822 = DateUtil.formatLocalDateTimeToRFC822String(now, Locale.ENGLISH);
            RSS rss = RSS.builder()
                    .title("Hsu Yeung 的博客")
                    .link(blogHomeUrl)
                    .description(systemConfigService.getConfigValue(SystemConfigConstants.SystemConfigEnum.CUSTOM_HEADER_TEXT, String.class))
                    .language("zh")
                    .copyright(String.format("© 2020 ~ %s", now.getYear()))
                    .pubDate(rfc822)
                    .lastBuildDate(rfc822)
                    .category("blog")
                    .generator("java")
                    .docs("https://www.rssboard.org/rss-specification")
                    .itemList(itemList)
                    .build();
            new Writer(rss, StringConstants.RSS_FILE_PATH).write();
        } catch (Exception e) {
            throw new SystemInternalException("创建 RSS 文件失败", e);
        }
    }
}
