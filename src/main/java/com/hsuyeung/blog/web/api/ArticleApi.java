package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.dto.article.AddArticleRequestDTO;
import com.hsuyeung.blog.model.dto.article.UpdateArticleRequestDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.article.ArticleInfoVO;
import com.hsuyeung.blog.model.vo.article.ArticleTitleInfoVO;
import com.hsuyeung.blog.service.IArticleService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.hsuyeung.blog.constant.enums.ContentTypeEnum.MARKDOWN;

/**
 * @author hsuyeung
 * @date 2022/07/07
 */
@Api(tags = "文章相关接口")
@RestController
@RequestMapping("/api/article")
public class ArticleApi implements IBaseWebResponse {
    @Resource
    private IArticleService articleService;

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
     * @param title          标题，全模糊
     * @param author         作者，全模糊
     * @param keywords       关键词，全模糊
     * @param desc           描述，全模糊
     * @param startTimestamp 开始时间戳，大于等于
     * @param endTimestamp   结束时间戳，小于等于
     * @param pageNum        页码
     * @param pageSize       每页数量
     * @return 文章分页列表
     */
    @ApiOperation("查询文章分页列表")
    @GetMapping("/page")
    public WebResponse<PageVO<ArticleInfoVO>> getArticlePage(
            @ApiParam("标题") @RequestParam(value = "title", required = false) String title,
            @ApiParam("作者") @RequestParam(value = "author", required = false) String author,
            @ApiParam("关键词") @RequestParam(value = "keywords", required = false) String keywords,
            @ApiParam("描述") @RequestParam(value = "desc", required = false) String desc,
            @ApiParam("是否置顶") @RequestParam(value = "pin", required = false) Boolean pin,
            @ApiParam("开始时间戳") @RequestParam(value = "startTimestamp", required = false) Long startTimestamp,
            @ApiParam("结束时间戳") @RequestParam(value = "endTimestamp", required = false) Long endTimestamp,
            @ApiParam("页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam("每页数量") @RequestParam("pageSize") Integer pageSize) {
        return ok(articleService.getArticlePage(title, author, keywords, desc, pin, pageNum, startTimestamp, endTimestamp, pageSize));
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
     * @param addArticleRequestDTO 新增文章请求参数
     */
    @ApiOperation("新增一篇文章")
    @PutMapping
    public WebResponse<Void> addArticle(
            @ApiParam("新增文章请求参数") @RequestBody AddArticleRequestDTO addArticleRequestDTO) {
        articleService.addArticle(addArticleRequestDTO);
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
     * @param updateArticleRequestDTO 更新文章请求参数
     */
    @ApiOperation("更新文章")
    @PostMapping
    public WebResponse<Void> updateArticle(
            @ApiParam("更新文章信息请求参数") @RequestBody UpdateArticleRequestDTO updateArticleRequestDTO) {
        articleService.updateArticle(updateArticleRequestDTO);
        return ok();
    }
}
