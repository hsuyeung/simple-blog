package com.hsuyeung.blog.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsuyeung.blog.exception.SystemInternalException;
import com.hsuyeung.blog.model.entity.CommentEntity;
import com.hsuyeung.blog.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author hsuyeung
 * @date 2022/06/18
 */
@Component
@RequiredArgsConstructor
public class CommentCache {
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;


    public List<CommentEntity> getCommentList(String key, Long articleId) {
        String value = (String) redisUtil.hGet(key, String.valueOf(articleId));
        try {
            return value == null ? null : objectMapper.readValue(value, new TypeReference<List<CommentEntity>>() {
            });
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("反序列化 CommentEntity 对象失败", e);
        }
    }

    public void cacheCommentList(String key, Long articleId, List<CommentEntity> commentEntityList) {
        try {
            redisUtil.hPut(key, String.valueOf(articleId), objectMapper.writeValueAsString(commentEntityList));
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("序列化 CommentEntity 对象失败", e);
        }
    }
}
