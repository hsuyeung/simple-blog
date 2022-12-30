package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.constant.enums.ContentTypeEnum;
import com.hsuyeung.blog.model.entity.ContentEntity;

/**
 * <p>
 * 文章内容表 服务类
 * </p>
 *
 * @author hsuyeung
 * @since 2022/06/05
 */
public interface IContentService extends IService<ContentEntity> {

    /**
     * 根据内容 id 和内容类型获取内容
     *
     * @param contentId   内容 id
     * @param contentType {@link ContentTypeEnum}
     * @return 内容
     */
    String getContent(Long contentId, ContentTypeEnum contentType);
}
