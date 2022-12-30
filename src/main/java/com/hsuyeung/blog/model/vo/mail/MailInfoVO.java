package com.hsuyeung.blog.model.vo.mail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页列表邮件信息
 *
 * @author hsuyeung
 * @date 2022/07/06
 */
@ApiModel(description = "分页列表邮件信息")
@Data
public class MailInfoVO implements Serializable {
    private static final long serialVersionUID = -5548040036708028953L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("发件人")
    private String mFrom;

    @ApiModelProperty("收件人")
    private String mTo;

    @ApiModelProperty("主题")
    private String mSubject;

    @ApiModelProperty("内容预览地址")
    private String textPreviewUrl;

    @ApiModelProperty("抄送")
    private String cc;

    @ApiModelProperty("密送")
    private String bcc;

    @ApiModelProperty("状态")
    private String mStatus;

    @ApiModelProperty("错误信息")
    private String errorMsg;

    @ApiModelProperty("邮件类型")
    private String type;

    @ApiModelProperty("发送时间")
    private String sendTime;

    @ApiModelProperty("重试次数")
    private Integer retryNum;
}
