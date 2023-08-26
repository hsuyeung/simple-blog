package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.article.AddArticleDTO;
import com.hsuyeung.blog.model.dto.article.ArticleSearchDTO;
import com.hsuyeung.blog.model.dto.article.UpdateArticleDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.article.ArticleInfoVO;
import com.hsuyeung.blog.model.vo.article.ArticleTitleInfoVO;
import com.hsuyeung.blog.service.IArticleService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hsuyeung.blog.constant.enums.ContentTypeEnum.MARKDOWN;

/**
 * @author hsuyeung
 * @date 2022/07/07
 */
@Api(tags = "文章相关接口")
@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleApi implements IBaseWebResponse {
    private final IArticleService articleService;

    /**
     * 查询文章标题列表
     *
     * @return 文章标题列表
     */
    @ApiOperation("查询文章标题列表")
    @GetMapping("/title/list")
    public WebResponse<List<ArticleTitleInfoVO>> getArticleTitleList() {
        return ok(articleService.getArticleTitleList());
    }

    /**
     * 查询文章分页列表
     *
     * @param pageSearchParam 文章分页搜索条件
     * @return 文章分页列表
     */
    @ApiOperation("查询文章分页列表")
    @PostMapping("/actions/page")
    public WebResponse<PageVO<ArticleInfoVO>> getArticlePage(
            @ApiParam(value = "文章分页搜索条件", required = true) @RequestBody
            PageSearchDTO<ArticleSearchDTO> pageSearchParam) {
        return ok(articleService.getArticlePage(pageSearchParam));
    }

    /**
     * 删除指定文章
     *
     * @param id 文章 id
     */
    @ApiOperation("删除指定文章")
    @DeleteMapping("/{id}")
    public WebResponse<Void> deleteArticle(@ApiParam("文章 id") @PathVariable("id") Long id) {
        articleService.deleteArticle(id);
        return ok();
    }

    /**
     * 新增一篇文章
     *
     * @param addArticle 新增文章请求参数
     */
    @ApiOperation("新增一篇文章")
    @PostMapping
    public WebResponse<Void> addArticle(
            @ApiParam("新增文章请求参数") @RequestBody AddArticleDTO addArticle) {
        articleService.addArticle(addArticle);
        return ok();
    }

    /**
     * 根据文章 id 获取文章的 markdown 格式的内容
     *
     * @param aid 文章 id
     * @return 文章的 markdown 格式的内容
     */
    @ApiOperation("根据文章 id 获取文章的 markdown 格式的内容")
    @GetMapping("/{aid}/md/content")
    public WebResponse<String> getArticleMdContent(@ApiParam("文章 id") @PathVariable("aid") Long aid) {
        return ok(articleService.getArticleContent(aid, MARKDOWN));
    }

    /**
     * 更新文章
     *
     * @param updateArticle 更新文章请求参数
     */
    @ApiOperation("更新文章")
    @PutMapping
    public WebResponse<Void> updateArticle(
            @ApiParam("更新文章信息请求参数") @RequestBody UpdateArticleDTO updateArticle) {
        articleService.updateArticle(updateArticle);
        return ok();
    }
}
