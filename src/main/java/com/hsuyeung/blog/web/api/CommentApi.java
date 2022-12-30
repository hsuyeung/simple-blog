package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.annotation.ApiRateLimit;
import com.hsuyeung.blog.model.dto.comment.SubmitCommentRequestDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.comment.CommentInfoVO;
import com.hsuyeung.blog.service.ICommentService;
import com.hsuyeung.blog.util.IpUtil;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

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
public class CommentApi implements IBaseWebResponse {
    @Resource
    private ICommentService commentService;

    /**
     * 用户提交评论
     *
     * @param submitCommentRequestDTO 提交评论请求参数
     * @param request                 请求
     * @return 评论 id
     */
    @ApiRateLimit(value = 60, strategy = IP, message = "{rest} 秒后可再次评论")
    @ApiOperation("用户提交评论")
    @PutMapping
    public WebResponse<Long> submitComment(
            @ApiParam("评论参数") @RequestBody SubmitCommentRequestDTO submitCommentRequestDTO,
            HttpServletRequest request) throws URISyntaxException, IOException {
        return ok(commentService.submitComment(submitCommentRequestDTO, IpUtil.getIpAddr(request)));
    }

    /**
     * 分页查询评论列表
     *
     * @param nickname       昵称，全模糊
     * @param email          邮箱地址，全模糊
     * @param website        网站，全模糊
     * @param parentNickname 父级评论人的昵称，全模糊
     * @param replyNickname  回复的评论人的昵称，全模糊
     * @param articleId      文章 id，精准匹配
     * @param ip             ip 地址，全模糊
     * @param notification   是否接收邮件提醒，精准匹配
     * @param startTimestamp 开始时间戳，大于等于
     * @param endTimestamp   结束时间戳，小于等于
     * @param pageNum        页码
     * @param pageSize       每页数量
     * @return 评论分页列表
     */
    @ApiOperation("分页查询评论列表")
    @GetMapping("/page")
    public WebResponse<PageVO<CommentInfoVO>> getCommentPage(
            @ApiParam("评论人昵称") @RequestParam(value = "nickname", required = false) String nickname,
            @ApiParam("评论人邮箱") @RequestParam(value = "email", required = false) String email,
            @ApiParam("评论人网址") @RequestParam(value = "website", required = false) String website,
            @ApiParam("父级评论人昵称") @RequestParam(value = "parentNickname", required = false) String parentNickname,
            @ApiParam("回复评论人昵称") @RequestParam(value = "replyNickname", required = false) String replyNickname,
            @ApiParam("文章 id") @RequestParam(value = "articleId", required = false) Integer articleId,
            @ApiParam("ip") @RequestParam(value = "ip", required = false) String ip,
            @ApiParam("是否接收邮件提醒") @RequestParam(value = "notification", required = false) Integer notification,
            @ApiParam("开始时间戳") @RequestParam(value = "startTimestamp", required = false) Long startTimestamp,
            @ApiParam("结束时间戳") @RequestParam(value = "endTimestamp", required = false) Long endTimestamp,
            @ApiParam("页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam("每页数量") @RequestParam("pageSize") Integer pageSize) {
        return ok(commentService.getCommentPage(nickname, email, website, parentNickname, replyNickname, articleId, ip,
                notification, startTimestamp, endTimestamp, pageNum, pageSize));
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
