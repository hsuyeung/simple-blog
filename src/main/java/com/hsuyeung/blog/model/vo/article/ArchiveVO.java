package com.hsuyeung.blog.model.vo.article;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 归档信息
 *
 * @author hsuyeung
 * @date 2022/06/07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveVO implements Serializable {
    private static final long serialVersionUID = -6868588806411146073L;

    @ApiModelProperty("总数")
    private Integer totalSize;

    @ApiModelProperty("归档结点列表")
    private List<ArchiveNode> archiveNodeList;
}
