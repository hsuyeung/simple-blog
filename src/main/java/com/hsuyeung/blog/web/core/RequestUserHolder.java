package com.hsuyeung.blog.web.core;

/**
 * 全局保存当前操作用户信息
 *
 * @author hsuyeung
 * @date 2022/07/10
 */
public final class RequestUserHolder {
    /**
     * 保存用户 id的 ThreadLocal
     */
    private static final ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

    /**
     * 添加当前操作人 id
     */
    public static void addCurrentUid(Long uid) {
        userThreadLocal.set(uid);
    }

    /**
     * 获取当前操作人的 id
     *
     * @return 当前操作人的 id
     */
    public static Long getCurrentUid() {
        return userThreadLocal.get();
    }


    /**
     * 防止内存泄漏
     */
    public static void remove() {
        userThreadLocal.remove();
    }


    private RequestUserHolder() {
    }
}
