package com.hsuyeung.blog.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsuyeung.blog.exception.SystemInternalException;
import com.hsuyeung.blog.model.vo.article.ArchiveVO;
import com.hsuyeung.blog.model.vo.article.ArticleDetailVO;
import com.hsuyeung.blog.model.vo.article.HomeArticleVO;
import com.hsuyeung.blog.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 文章缓存
 *
 * @author hsuyeung
 * @date 2022/06/06
 */
@Component
@RequiredArgsConstructor
public class ArticleCache {
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;

    public List<HomeArticleVO> getHomeArticleList(String key) {
        String value = (String) redisUtil.get(key);
        try {
            return value == null ? null : objectMapper.readValue(value, new TypeReference<List<HomeArticleVO>>() {
            });
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("反序列化 HomeArticleVO 对象失败", e);
        }
    }

    public void cacheHomeArticleList(String key, List<HomeArticleVO> homeArticleList) {
        try {
            redisUtil.set(key, objectMapper.writeValueAsString(homeArticleList));
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("序列化 HomeArticleVO 对象失败", e);
        }
    }

    public ArticleDetailVO getArticleDetail(String key, String articleRoute) {
        String value = (String) redisUtil.hGet(key, articleRoute);
        try {
            return value == null ? null : objectMapper.readValue(value, ArticleDetailVO.class);
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("反序列化 ArticleDetailVO 对象失败", e);
        }
    }

    public void cacheArticleDetail(String key, String articleRoute, ArticleDetailVO articleDetail) {
        try {
            redisUtil.hPut(key, articleRoute, objectMapper.writeValueAsString(articleDetail));
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("序列化 ArticleDetailVO 对象失败", e);
        }
    }

    public ArchiveVO getArchive(String key) {
        String value = (String) redisUtil.get(key);
        try {
            return value == null ? null : objectMapper.readValue(value, ArchiveVO.class);
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("反序列化 ArchiveVO 对象失败", e);
        }
    }

    public void cacheArchive(String key, ArchiveVO archiveVO) {
        try {
            redisUtil.set(key, objectMapper.writeValueAsString(archiveVO));
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("序列化 ArchiveVO 对象失败", e);
        }
    }

    public void deleteCache(String key) {
        redisUtil.delete(key);
    }

    public void deleteCache(String key, String field) {
        redisUtil.hDelete(key, field);
    }
}
