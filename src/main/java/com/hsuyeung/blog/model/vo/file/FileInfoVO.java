package com.hsuyeung.blog.model.vo.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页列表文件信息
 *
 * @author hsuyeung
 * @date 2022/07/07
 */
@ApiModel(description = "分页列表文件信息")
@Data
public class FileInfoVO implements Serializable {
    private static final long serialVersionUID = -2075427838065008705L;

    @ApiModelProperty("文件访问全路径")
    private String url;

    @ApiModelProperty("上传时间")
    private String createTime;

    @ApiModelProperty("上传人")
    private String createBy;
}
