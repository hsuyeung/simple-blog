package com.hsuyeung.blog.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

import java.util.Map;


/**
 * Jwt 工具类，生成 JWT 和认证
 *
 * @author hsuyeung
 * @date 2020/10/16
 */
public final class JwtUtil {

    /**
     * 获取 token 中的所有字段值
     *
     * @param token  token
     * @param secret 加密密钥
     * @return 所有字段的值
     */
    public static Map<String, Claim> getClaims(String token, String secret) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        return verifier.verify(token).getClaims();
    }

    /**
     * 从 token 中获取指定字段的值
     *
     * @param token  token
     * @param key    字段的名字
     * @param clazz  字段值的类型
     * @param secret 加密密钥
     * @param <T>    字段值的类型
     * @return 指定字段的值
     */
    public static <T> T getClaim(String token, String key, Class<T> clazz, String secret) {
        Map<String, Claim> claimMap = getClaims(token, secret);
        return claimMap.get(key).as(clazz);
    }

    private JwtUtil() {
    }
}
