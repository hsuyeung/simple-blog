package com.hsuyeung.blog.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.constant.enums.ContentTypeEnum;
import com.hsuyeung.blog.model.dto.article.AddArticleRequestDTO;
import com.hsuyeung.blog.model.dto.article.UpdateArticleRequestDTO;
import com.hsuyeung.blog.model.entity.ArticleEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.article.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author hsuyeung
 * @since 2022/06/05
 */
@Validated
public interface IArticleService extends IService<ArticleEntity> {

    /**
     * 获取博客首页的文章列表
     *
     * @return {@link List<HomeArticleVO>}
     */
    List<HomeArticleVO> getHomeArticleList();

    /**
     * 根据文章路由获取博客文章详情
     *
     * @param articleRoute 文章路由
     * @return {@link ArticleDetailVO}
     */
    ArticleDetailVO getArticleDetail(String articleRoute);

    /**
     * 获取归档页面数据列表
     *
     * @return 归档数据
     */
    ArchiveVO getArchiveList();

    /**
     * 判断指定 id 的文章是否存在
     *
     * @param articleId 文章 id
     * @return 存在返回 true，否则返回 false
     */
    boolean isExists(Long articleId);

    /**
     * 根据文章 id 查询文章的路由
     *
     * @param articleId 文章 id
     * @return {@link ArticleRouteAndTitleVO}
     */
    ArticleRouteAndTitleVO getArticleRouteAndTitle(Long articleId);

    /**
     * 查询文章标题列表
     *
     * @return 文章标题列表
     */
    List<ArticleTitleInfoVO> getArticleTitleList();

    /**
     * 根据文章 id 批量查询文章路由和标题信息
     *
     * @param ids 文章 id 集合
     * @return key-文章 id，value-文章路由
     */
    Map<Long, ArticleRouteAndTitleVO> getArticleRouteAndTitle(Collection<Long> ids);

    /**
     * 查询文章分页列表
     *
     * @param title          标题，全模糊
     * @param author         作者，全模糊
     * @param keywords       关键词，全模糊
     * @param desc           描述，全模糊
     * @param pin            是否置顶，精确搜索
     * @param pageNum        页码
     * @param startTimestamp 开始时间戳，大于等于
     * @param endTimestamp   结束时间戳，小于等于
     * @param pageSize       每页数量
     * @return 文章分页列表
     */
    PageVO<ArticleInfoVO> getArticlePage(String title, String author, String keywords, String desc, Boolean pin,
                                         Integer pageNum, Long startTimestamp, Long endTimestamp, Integer pageSize);

    /**
     * 删除指定文章
     *
     * @param id 文章 id
     */
    void deleteArticle(Long id);

    /**
     * 新增一篇文章
     *
     * @param addArticleRequestDTO 新增文章请求参数
     */
    void addArticle(@Valid AddArticleRequestDTO addArticleRequestDTO);

    /**
     * 根据文章 id 获取文章的指定格式的内容
     *
     * @param aid         文章 id
     * @param contentType 文章类型，{@link ContentTypeEnum}
     * @return 文章内容
     */
    String getArticleContent(Long aid, ContentTypeEnum contentType);

    /**
     * 更新文章
     *
     * @param updateArticleRequestDTO 更新文章请求参数
     */
    void updateArticle(@Valid UpdateArticleRequestDTO updateArticleRequestDTO);
}
