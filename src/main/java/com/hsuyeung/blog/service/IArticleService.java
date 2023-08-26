package com.hsuyeung.blog.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.constant.enums.ContentTypeEnum;
import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.article.AddArticleDTO;
import com.hsuyeung.blog.model.dto.article.ArticleSearchDTO;
import com.hsuyeung.blog.model.dto.article.UpdateArticleDTO;
import com.hsuyeung.blog.model.entity.ArticleEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.article.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    ArticleDetailVO getArticleDetail(@NotBlank(message = "articleRoute 不能为空") String articleRoute);

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
    ArticleRouteAndTitleVO getArticleRouteAndTitle(@NotNull(message = "articleId 不能为 null") Long articleId);

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
    Map<Long, ArticleRouteAndTitleVO> getArticleRouteAndTitle(@NotEmpty(message = "ids 不能为空") Collection<Long> ids);

    /**
     * 查询文章分页列表
     *
     * @param pageSearchParam 文章分页搜索条件
     * @return 文章分页列表
     */
    PageVO<ArticleInfoVO> getArticlePage(@NotNull(message = "pageSearchParam 不能为 null") @Valid
                                         PageSearchDTO<ArticleSearchDTO> pageSearchParam);

    /**
     * 删除指定文章
     *
     * @param id 文章 id
     */
    void deleteArticle(Long id);

    /**
     * 新增一篇文章
     *
     * @param addArticle 新增文章请求参数
     */
    void addArticle(@NotNull(message = "addArticle 不能为 null") @Valid AddArticleDTO addArticle);

    /**
     * 根据文章 id 获取文章的指定格式的内容
     *
     * @param aid         文章 id
     * @param contentType 文章类型，{@link ContentTypeEnum}
     * @return 文章内容
     */
    String getArticleContent(@NotNull(message = "aid 不能为 null") Long aid,
                             @NotBlank(message = "contentType 不能为 null") ContentTypeEnum contentType);

    /**
     * 更新文章
     *
     * @param updateArticle 更新文章请求参数
     */
    void updateArticle(@NotNull(message = "updateArticle 不能为 null") @Valid UpdateArticleDTO updateArticle);
}
