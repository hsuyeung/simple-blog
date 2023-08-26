package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.annotation.ApiRateLimit;
import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.comment.CommentSearchDTO;
import com.hsuyeung.blog.model.dto.comment.SubmitCommentDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.comment.CommentInfoVO;
import com.hsuyeung.blog.service.ICommentService;
import com.hsuyeung.blog.util.IpUtil;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.hsuyeung.blog.constant.enums.ApiRateLimitStrategyEnum.IP;

/**
 * 评论接口
 *
 * @author hsuyeung
 * @date 2022/06/16
 */
@Api(tags = "评论相关接口")
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentApi implements IBaseWebResponse {
    private final ICommentService commentService;

    /**
     * 用户提交评论
     *
     * @param submitComment 提交评论请求参数
     * @param request       请求
     * @return 评论 id
     */
    @ApiRateLimit(value = 60, strategy = IP, message = "{rest} 秒后可再次评论")
    @ApiOperation("用户提交评论")
    @PostMapping("/actions/submit")
    public WebResponse<Long> submitComment(
            @ApiParam("评论参数") @RequestBody SubmitCommentDTO submitComment,
            HttpServletRequest request) {
        return ok(commentService.submitComment(submitComment, IpUtil.getIpAddr(request)));
    }

    /**
     * 分页查询评论列表
     *
     * @param pageSearchParam 评论分页搜素条件
     * @return 评论分页列表
     */
    @ApiOperation("分页查询评论列表")
    @PostMapping("/actions/page")
    public WebResponse<PageVO<CommentInfoVO>> getCommentPage(
            @ApiParam("评论分页搜索条件") @RequestBody PageSearchDTO<CommentSearchDTO> pageSearchParam) {
        return ok(commentService.getCommentPage(pageSearchParam));
    }

    /**
     * 删除指定的评论
     *
     * @param id 评论 id
     */
    @ApiOperation("删除指定的评论")
    @DeleteMapping("/{id}")
    public WebResponse<Void> deleteComment(@ApiParam("评论 id") @PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return ok();
    }
}
