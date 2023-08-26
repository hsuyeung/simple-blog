package com.hsuyeung.blog.model.dto.mail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 邮件分页搜索条件
 *
 * @author hsuyeung
 * @since 2023/8/26
 */
@Data
@ApiModel(description = "邮件分页搜索条件")
public class MailSearchDTO implements Serializable {
    private static final long serialVersionUID = 5751405303584710345L;

    @ApiParam("发件人")
    @Size(max = 255, message = "发件人不能超过 255 个字符")
    private String from;

    @ApiParam("收件人")
    @Size(max = 2550, message = "收件人不能超过 2550 个字符")
    private String to;

    @ApiParam("主题")
    @Size(max = 255, message = "主题不能超过 255 个字符")
    private String subject;

    @ApiParam("抄送")
    @Size(max = 2550, message = "抄送不能超过 2550 个字符")
    private String cc;

    @ApiParam("密送")
    @Size(max = 2550, message = "密送不能超过 2550 个字符")
    private String bcc;

    @ApiParam("状态")
    private Integer status;

    @ApiParam("类型")
    private Integer type;

    @ApiParam("开始时间戳")
    private Long startTimestamp;

    @ApiParam("结束时间戳")
    private Long endTimestamp;

}
