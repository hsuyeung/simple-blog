package com.hsuyeung.blog.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsuyeung.blog.exception.SystemInternalException;
import com.hsuyeung.blog.model.entity.SystemConfigEntity;
import com.hsuyeung.blog.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.hsuyeung.blog.constant.SystemConfigConstants.SYSTEM_CONFIG_KEY_FORMAT;

/**
 * 系统配置缓存
 *
 * @author hsuyeung
 * @date 2022/06/06
 */
@Component
@RequiredArgsConstructor
public class SystemConfigCache {
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;

    /**
     * 从 redis 中获取指定分组的单个系统配置
     *
     * @param group 分组名
     * @param field 配置的 key
     * @return 配置的 key 对应的配置实体类
     */
    public SystemConfigEntity getSystemConfigValueCache(String group, Object field) {
        String key = SYSTEM_CONFIG_KEY_FORMAT.replace("{group}", group);
        String value = (String) redisUtil.hGet(key, field);
        try {
            return value == null ? null : objectMapper.readValue(value, SystemConfigEntity.class);
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("反序列化系统配置对象失败", e);
        }
    }

    /**
     * 缓存单个系统配置
     *
     * @param group              分组
     * @param key                配置 key
     * @param systemConfigEntity 缓存的对象
     */
    public void cacheSystemConfig(String group, String key, SystemConfigEntity systemConfigEntity) {
        try {
            redisUtil.hPut(SYSTEM_CONFIG_KEY_FORMAT.replace("{group}", group), key, objectMapper.writeValueAsString(systemConfigEntity));
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("序列化系统配置对象失败", e);
        }
    }


    public <T> T getPageCustomConfigCache(String key, Class<T> clazz) {
        String value = (String) redisUtil.get(key);
        try {
            return value == null ? null : objectMapper.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            throw new SystemInternalException(String.format("反序列化 %s 对象失败", clazz.getSimpleName()), e);
        }
    }

    public <T> void cachePageCustomConfigCache(String key, T data) {
        try {
            redisUtil.set(key, objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new SystemInternalException(String.format("反序列化 %s 对象失败", data.getClass().getSimpleName()), e);
        }
    }
}
