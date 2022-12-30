package com.hsuyeung.blog.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsuyeung.blog.exception.SystemInternalException;
import com.hsuyeung.blog.model.vo.friendlink.FriendLinkVO;
import com.hsuyeung.blog.util.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 友链缓存
 *
 * @author hsuyeung
 * @date 2022/06/23
 */
@Component
public class FriendLinkCache {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ObjectMapper objectMapper;


    public FriendLinkVO getFriendLinkVO(String key) {
        String value = (String) redisUtil.get(key);
        try {
            return value == null ? null : objectMapper.readValue(value, FriendLinkVO.class);
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("反序列化 FriendLinkVO 对象失败", e);
        }
    }

    public void cacheFriendLinkVO(String key, FriendLinkVO friendLinkVO) {
        try {
            redisUtil.set(key, objectMapper.writeValueAsString(friendLinkVO));
        } catch (JsonProcessingException e) {
            throw new SystemInternalException("序列化 FriendLinkVO 对象失败", e);
        }
    }
}
