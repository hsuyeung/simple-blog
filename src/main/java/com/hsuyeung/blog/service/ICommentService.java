package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.comment.CommentSearchDTO;
import com.hsuyeung.blog.model.dto.comment.SubmitCommentDTO;
import com.hsuyeung.blog.model.entity.CommentEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.comment.CommentInfoVO;
import com.hsuyeung.blog.model.vo.comment.CommentVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
     * @param submitComment 请求参数
     * @param remoteAddr    评论者的 ip 地址
     * @return 新增的评论的 id
     */
    Long submitComment(@NotNull(message = "submitComment 不能为 null") @Valid SubmitCommentDTO submitComment,
                       String remoteAddr);

    /**
     * 分页查询评论列表
     *
     * @param pageSearchParam 评论分页搜索条件
     * @return 评论分页列表
     */
    PageVO<CommentInfoVO> getCommentPage(@NotNull(message = "pageSearchParam 不能为 null") @Valid
                                         PageSearchDTO<CommentSearchDTO> pageSearchParam);

    /**
     * 删除指定的评论
     *
     * @param id 评论 id
     */
    void deleteComment(@NotNull(message = "id 不能为 null") Long id);

    /**
     * 统计文章的评论数
     *
     * @param aid 文章 id
     * @return 评论数
     */
    Long countByArticleId(Long aid);
}
