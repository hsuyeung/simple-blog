package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.dto.comment.SubmitCommentRequestDTO;
import com.hsuyeung.blog.model.entity.CommentEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.comment.CommentInfoVO;
import com.hsuyeung.blog.model.vo.comment.CommentVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * <p>
 * 文章评论服务类
 * </p>
 *
 * @author hsuyeung
 * @since 2022/06/05
 */
@Validated
public interface ICommentService extends IService<CommentEntity> {

    /**
     * 获取评论列表
     *
     * @param articleId 文章 id
     * @return 当文章 id 为 0 或 null 时表示获取的是关于页面的留言列表，大于 0 时为指定文章的评论列表，否则出错
     */
    CommentVO getCommentList(Long articleId);

    /**
     * 提交评论
     *
     * @param submitCommentRequestDTO 请求参数
     * @param remoteAddr              评论者的 ip 地址
     * @return 新增的评论的 id
     */
    Long submitComment(@Valid SubmitCommentRequestDTO submitCommentRequestDTO, String remoteAddr) throws URISyntaxException, IOException;

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
    PageVO<CommentInfoVO> getCommentPage(String nickname, String email, String website, String parentNickname,
                                         String replyNickname, Integer articleId, String ip, Integer notification,
                                         Long startTimestamp, Long endTimestamp, Integer pageNum, Integer pageSize);

    /**
     * 删除指定的评论
     *
     * @param id 评论 id
     */
    void deleteComment(Long id);

    /**
     * 统计文章的评论数
     *
     * @param aid 文章 id
     * @return 评论数
     */
    Long countByArticleId(Long aid);
}
