package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.constant.enums.ContentTypeEnum;
import com.hsuyeung.blog.mapper.ContentMapper;
import com.hsuyeung.blog.model.entity.ContentEntity;
import com.hsuyeung.blog.service.IContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.hsuyeung.blog.constant.enums.ContentTypeEnum.HTML;

/**
 * <p>
 * 文章内容表 服务实现类
 * </p>
 *
 * @author hsuyeung
 * @since 2022/06/05
 */
@Slf4j
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, ContentEntity> implements IContentService {

    @Override
    public String getContent(Long contentId, ContentTypeEnum contentType) {
        boolean isHtml = Objects.equals(contentType, HTML);
        SFunction<ContentEntity, ?> contentColumn = isHtml
                ? ContentEntity::getHtmlContent
                : ContentEntity::getMdContent;
        ContentEntity contentEntity = lambdaQuery()
                .select(contentColumn)
                .eq(ContentEntity::getId, contentId)
                .one();
        if (Objects.isNull(contentEntity)) {
            log.error(String.format("id 为 %d 的内容不存在", contentId));
            return "";
        }
        return isHtml ? contentEntity.getHtmlContent() : contentEntity.getMdContent();
    }
}
