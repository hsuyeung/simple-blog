package com.hsuyeung.blog.model.vo.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 评论时间信息实体类
 *
 * @author hsuyeung
 * @date 2022/06/13
 */
@Data
public class CommentTimeVO implements Serializable {
    private static final long serialVersionUID = -6741531752802668513L;

    @ApiModelProperty("格式化的时间")
    private String formattedTime;

    @ApiModelProperty("未格式化的时间")
    private String standardTime;

    @ApiModelProperty("美化后的时间")
    private String beautifyTime;
}
