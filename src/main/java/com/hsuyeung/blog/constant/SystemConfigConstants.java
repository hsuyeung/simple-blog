package com.hsuyeung.blog.constant;

import com.hsuyeung.blog.util.AssertUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * 系统配置相关常量
 *
 * @author hsuyeung
 * @date 2022/05/17
 */
public final class SystemConfigConstants {
    public static final String SYSTEM_CONFIG_KEY_FORMAT = "blog:systemConfig:{group}";
    public static final String GROUP_REDIS = "redis";
    public static final String GROUP_ARTICLE = "article";
    public static final String GROUP_CATEGORY = "category";
    public static final String GROUP_SYSTEM = "system";
    public static final String GROUP_MAIL = "mail";
    public static final String GROUP_CUSTOM = "custom";
    public static final String GROUP_USER = "user";
    public static final String GROUP_ADMIN = "admin";

    /**
     * 用户分组下的配置 key 名字常量
     */
    @Getter
    @AllArgsConstructor
    @ToString
    public enum SystemConfigEnum {
        /**
         * 首页最新文章列表展示数量
         */
        ARTICLE_HOME_ARTICLE_LIST_SIZE(GROUP_ARTICLE, "homeArticleListSize", "15"),

        /**
         * 首页最新文章列表数据缓存 key
         */
        REDIS_HOME_ARTICLE_LIST_KEY(GROUP_REDIS, "homeArticleListKey", "blog:article:homeArticleList"),
        /**
         * 文章详情缓存 key
         */
        REDIS_ARTICLE_DETAIL_KEY(GROUP_REDIS, "articleDetailKey", "blog:article:articleDetail"),
        /**
         * 文章归档的数据缓存 key
         */
        REDIS_ARTICLE_ARCHIVE_KEY(GROUP_REDIS, "articleArchiveKey", "blog:article:archive"),
        /**
         * 根据 ip 对接口进行限流的数据缓存 key
         */
        REDIS_API_RATE_LIMIT_BY_IP_KEY(GROUP_REDIS, "apiRateLimitByIpKey", "blog:apiRateLimit:ip"),
        /**
         * 根据请求的用户 id 对接口进行限流的数据缓存 key
         */
        REDIS_API_RATE_LIMIT_BY_UID_KEY(GROUP_REDIS, "apiRateLimitByUidKey", "blog:apiRateLimit:uid"),
        /**
         * 指定文章的评论列表缓存 key
         */
        REDIS_COMMENT_LIST_KEY(GROUP_REDIS, "commentListKey", "blog:comment:commentList"),
        /**
         * 友链分组列表数据缓存 key
         */
        REDIS_FRIEND_LINK_GROUP_LIST_KEY(GROUP_REDIS, "friendLinkGroupKey", "blog:friendLink:friendLinkGroup"),
        /**
         * 首页自定义配置缓存 key
         */
        REDIS_HOME_CUSTOM_CONFIG_KEY(GROUP_REDIS, "homeCustomConfigKey", "blog:customConfig:home"),
        /**
         * 归档自定义配置缓存 key
         */
        REDIS_ARCHIVE_CUSTOM_CONFIG_KEY(GROUP_REDIS, "archiveCustomConfigKey", "blog:customConfig:archive"),
        /**
         * 关于页面自定义配置缓存 key
         */
        REDIS_ABOUT_CUSTOM_CONFIG_KEY(GROUP_REDIS, "aboutCustomConfigKey", "blog:customConfig:about"),
        /**
         * 页面公共自定义配置缓存 key
         */
        REDIS_COMMON_CUSTOM_CONFIG_KEY(GROUP_REDIS, "commonCustomConfigKey", "blog:customConfig:common"),
        /**
         * 友链页面自定义配置缓存 key
         */
        REDIS_FRIEND_LINK_CUSTOM_CONFIG_KEY(GROUP_REDIS, "friendLinkCustomConfigKey", "blog:customConfig:friendLink"),
        /**
         * 用户权限集合缓存 key
         */
        REDIS_USER_PERMISSION_KEY(GROUP_REDIS, "userPermissionKey", "blog:rbac:userPermission"),
        /**
         * 用户 token 缓存 key
         */
        REDIS_USER_TOKEN_KEY(GROUP_REDIS, "userTokenKey", "blog:token:user"),
        /**
         * 用户登录失败次数的缓存 key
         */
        REDIS_LOGIN_FAILED_NUM_KEY(GROUP_REDIS, "loginFailedNumKey", "blog:login:failedNum"),


        /**
         * 默认分类的路由
         */
        CATEGORY_DEFAULT_CATEGORY_ROUTE(GROUP_CATEGORY, "defaultCategoryRoute", "other"),
        /**
         * 默认分类的名字
         */
        CATEGORY_DEFAULT_CATEGORY_NAME(GROUP_CATEGORY, "defaultCategoryName", "未分类"),


        /**
         * 接口请求被限流时的提示语
         */
        SYSTEM_API_RATE_LIMIT_EXCEPTION_TIP(GROUP_SYSTEM, "apiRateLimitExceptionTip", "请求太快啦~"),
        /**
         * gravatar 头像请求的 url
         */
        SYSTEM_GRAVATAR_REQUEST_URL(GROUP_SYSTEM, "gravatarRequestUrl", "https://gravatar.loli.net/avatar/{hash}?d={d}"),
        /**
         * gravatar 头像未请求到时展示的默认头像类型参数
         */
        SYSTEM_GRAVATAR_DEFAULT_AVATAR_PARAM(GROUP_SYSTEM, "gravatarDefaultAvatarParam", "monsterid"),
        /**
         * qq 号换取头像 k 值 的url
         */
        SYSTEM_QQ_NUM_EXCHANGE_K_URL(GROUP_SYSTEM, "qqNumExchangeKUrl", "https://ptlogin2.qq.com/getface?&imgtype=1&uin={qq}"),
        /**
         * qq 头像请求的 url
         */
        SYSTEM_QQ_AVATAR_REQUEST_URL(GROUP_SYSTEM, "qqAvatarRequestUrl", "https://q1.qlogo.cn/g?b=qq&k={k}&s=160"),
        /**
         * 博客首页 url
         */
        SYSTEM_BLOG_HOME_URL(GROUP_SYSTEM, "blogHomeUrl", "https://www.hsuyeung.com"),
        /**
         * 浏览器静态资源版本，用于当静态资源更新后通知客户端更新缓存
         */
        SYSTEM_BROWSER_STATIC_RESOURCE_VERSION(GROUP_SYSTEM, "browserStaticResourceVersion", "1.0.0"),
        /**
         * 发送失败的邮件最大重试次数
         */
        SYSTEM_MAIL_RETRY_MAX_NUM(GROUP_SYSTEM, "mailRetryMaxNum", "3"),

        /**
         * 评论被回复的邮件主题
         */
        MAIL_COMMENT_BE_REPLIED_MAIL_SUBJECT(GROUP_MAIL, "commentBeRepliedMailSubject", "评论被回复通知"),
        /**
         * 邮件模板 header 图片
         */
        MAIL_HEADER_IMG(GROUP_MAIL, "mailHeaderImg", "https://cdn.jsdelivr.net/gh/LIlGG/halo-theme-sakura@1.3.3/source/images/other/head.jpg"),
        /**
         * 邮件模板 footer 图片
         */
        MAIL_FOOTER_IMG(GROUP_MAIL, "mailFooterImg", "https://cdn.jsdelivr.net/gh/LIlGG/halo-theme-sakura@1.3.3/source/images/other/hr.png"),

        /**
         * 博客首页 title 内容
         */
        CUSTOM_BLOG_HOME_TITLE(GROUP_CUSTOM, "blogHomeTitle", "Hsu Yeung 的个人博客"),
        /**
         * 博客首页描述
         */
        CUSTOM_BLOG_HOME_DESC(GROUP_CUSTOM, "blogHomeDesc", "Hsu Yeung 的个人博客。"),
        /**
         * 博客首页关键词
         */
        CUSTOM_BLOG_HOME_KEYWORDS(GROUP_CUSTOM, "blogHomeKeywords", "hsuyeung,Hsu Yeung,blog"),
        /**
         * 博客首页 banner 图片
         */
        CUSTOM_BLOG_HOME_BANNER_IMG(GROUP_CUSTOM, "blogHomeBannerImg", "/img/home_banner.jpg"),
        /**
         * 博客 header 的一句话文本
         */
        CUSTOM_HEADER_TEXT(GROUP_CUSTOM, "headerText", "前天看到了小兔，昨天是小鹿，今天是你。"),
        /**
         * 博客 about 区域 footer 的一句话文本
         */
        CUSTOM_ABOUT_FOOTER_TEXT(GROUP_CUSTOM, "aboutFooterText", "前天看到了小兔，昨天是小鹿，今天是你。"),
        /**
         * 博客 footer 的 copyright 信息
         */
        CUSTOM_FOOTER_COPYRIGHT(GROUP_CUSTOM, "footerCopyright", "© 2020 ~ 2022"),
        /**
         * 博客 footer 的 about 文本
         */
        CUSTOM_FOOTER_ABOUT_TEXT(GROUP_CUSTOM, "footerAboutText", "关于本站"),
        /**
         * 备案号
         */
        CUSTOM_BEI_AN_NUM(GROUP_CUSTOM, "beianNum", "蜀ICP备2022013775号-1"),
        /**
         * 博客归档页面描述
         */
        CUSTOM_BLOG_ARCHIVE_DESC(GROUP_CUSTOM, "blogArchiveDesc", "HsuYeung 的文章归档。"),
        /**
         * 博客归档页面关键词
         */
        CUSTOM_BLOG_ARCHIVE_KEYWORDS(GROUP_CUSTOM, "blogArchiveKeywords", "hsuyeung,Hsu Yeung,blog,archive"),
        /**
         * 博客归档页面 banner 图片
         */
        CUSTOM_BLOG_ARCHIVE_BANNER_IMG(GROUP_CUSTOM, "blogArchiveBannerImg", "/img/archive_banner.png"),
        /**
         * 博客关于页面描述
         */
        CUSTOM_BLOG_ABOUT_DESC(GROUP_CUSTOM, "blogAboutDesc", "关于 Hsu Yeung。"),
        /**
         * 博客关于页面关键词
         */
        CUSTOM_BLOG_ABOUT_KEYWORDS(GROUP_CUSTOM, "blogAboutKeywords", "hsuyeung,Hsu Yeung,blog,about"),
        /**
         * 博客关于页面 banner 图片
         */
        CUSTOM_BLOG_ABOUT_BANNER_IMG(GROUP_CUSTOM, "blogAboutBannerImg", "/img/about_banner.png"),
        /**
         * 博客头像
         */
        CUSTOM_BLOG_AVATAR(GROUP_CUSTOM, "avatar", "/img/avatar.jpg"),
        /**
         * 友链页面的描述
         */
        CUSTOM_FRIEND_LINK_DESC(GROUP_CUSTOM, "friendLinkDesc", "Hsu Yeung 的友链。"),
        /**
         * 友链页面关键词
         */
        CUSTOM_FRIEND_LINK_KEYWORDS(GROUP_CUSTOM, "friendLinkKeywords", "hsuyeung,Hsu Yeung,blog,friendLink"),
        /**
         * 友链页面 banner 图
         */
        CUSTOM_FRIEND_LINK_BANNER_IMG(GROUP_CUSTOM, "friendLinkBannerImg", "/img/friend_link_banner.png"),


        /**
         * 用户 token 加密密钥
         * TODO: 配置 token 加密密钥
         */
        USER_TOKEN_SECRET(GROUP_USER, "userTokenSecret", "记得配置我"),
        /**
         * 用户 token 过期时间，单位：秒
         */
        USER_TOKEN_EXPIRE_TIME(GROUP_USER, "tokenExpireTime", "604800"),
        /**
         * 用户登录失败尝试最大次数
         */
        USER_LOGIN_FAILED_MAX_NUM(GROUP_USER, "loginFailedMaxNum", "3"),

        /**
         * 网站管理员的邮箱地址，接收网站回复通知
         * TODO: 修改自己的邮箱地址
         */
        ADMIN_EMAIL_ADDRESS(GROUP_ADMIN, "adminEmailAddress", "记得配置接收通知的邮箱地址"),
        /**
         * 网站管理员名字
         */
        ADMIN_NAME(GROUP_ADMIN, "adminName", "Hsu Yeung"),
        ;

        private final String group;
        private final String keyName;
        private final String defaultValue;


        public static String getDefaultValue(String group, String keyName) {
            AssertUtil.hasLength(group, "group 不能为空");
            AssertUtil.hasLength(keyName, "keyName 不能为空");
            for (SystemConfigEnum configEnum : SystemConfigEnum.values()) {
                if (Objects.equals(configEnum.getGroup(), group) && Objects.equals(configEnum.getKeyName(), keyName)) {
                    return configEnum.getDefaultValue();
                }
            }
            return "";
        }
    }

    private SystemConfigConstants() {
    }
}
