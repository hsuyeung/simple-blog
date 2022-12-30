package com.hsuyeung.blog.model.vo.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 评论者信息实体类
 *
 * @author hsuyeung
 * @date 2022/06/13
 */
@Data
public class CommentUserVO implements Serializable {
    private static final long serialVersionUID = 220875395425392864L;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("网站")
    private String website;
}
