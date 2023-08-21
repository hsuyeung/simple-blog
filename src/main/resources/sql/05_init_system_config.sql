INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('homeArticleListSize', '15', 'article', '首页最新文章列表展示数量', 1, '1', '2022-06-06 10:56:28', '1',
        '2022-06-06 10:56:28', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('homeArticleListKey', 'blog:article:homeArticleList', 'redis', '首页最新文章列表数据缓存 key', 1, '1',
        '2022-06-06 10:57:09', '1', '2022-06-06 11:03:01', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('articleDetailKey', 'blog:article:articleDetail', 'redis', '文章详情缓存 key', 1, '1', '2022-06-06 16:14:02',
        '1',
        '2022-06-06 16:14:02', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('articleArchiveKey', 'blog:article:archive', 'redis', '文章归档的数据缓存 key', 1, '1', '2022-06-08 15:26:14',
        '1',
        '2022-06-20 10:38:36', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('apiRateLimitByIpKey', 'blog:apiRateLimit:ip', 'redis', '根据 ip 对接口进行限流的数据缓存 key', 1, '1',
        '2022-06-17 15:24:44',
        '1', '2022-06-17 15:24:44', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('apiRateLimitExceptionTip', '请求太快啦~', 'system', '接口请求被限流时的提示语', 1, '1', '2022-06-17 16:13:40',
        '1',
        '2022-06-17 16:13:40', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('gravatarRequestUrl', 'https://gravatar.loli.net/avatar/{hash}?d={d}', 'system', 'gravatar 头像请求的 url', 1,
        '1',
        '2022-06-18 11:58:01', '1', '2022-06-18 11:58:01', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('gravatarDefaultAvatarParam', 'monsterid', 'system', 'gravatar 头像未请求到时展示的默认头像类型参数', 1, '1',
        '2022-06-18 11:58:32', '1', '2022-06-18 11:58:39', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('qqAvatarRequestUrl', 'https://q1.qlogo.cn/g?b=qq&k={k}&s=160', 'system', 'qq 头像请求的 url', 1, '1',
        '2022-06-18 11:59:02', '1', '2022-06-21 15:23:24', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('commentListKey', 'blog:comment:commentList', 'redis', '指定文章的评论列表缓存 key', 1, '1',
        '2022-06-18 19:47:27', '1',
        '2022-06-18 19:59:07', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('commentBeRepliedMailSubject', '评论被回复通知', 'mail', '评论被回复的邮件主题', 1, '1', '2022-06-18 23:38:04',
        '1',
        '2022-06-18 23:38:04', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('blogHomeUrl', 'https://www.hsuyeung.com', 'system', '博客首页 url', 1, '1', '2022-06-19 00:09:42', '1',
        '2022-06-19 00:09:42', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('blogHomeTitle', 'Hsu Yeung 的个人博客', 'custom', '博客首页 title 内容', 1, '1', '2022-06-20 14:58:21', '1',
        '2022-06-20 14:58:21', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('blogHomeDesc', 'Hsu Yeung 的个人博客', 'custom', '博客首页描述', 1, '1', '2022-06-20 14:58:45', '1',
        '2022-06-20 14:58:45', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('blogHomeKeywords', 'hsuyeung,Hsu Yeung,blog', 'custom', '博客首页关键词', 1, '1', '2022-06-20 14:59:05', '1',
        '2022-06-20 15:40:01', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('headerText', '前天看到了小兔，昨天是小鹿，今天是你。', 'custom', '博客 header 的一句话文本', 1, '1',
        '2022-06-20 15:01:06', '1',
        '2022-06-20 15:01:06', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('aboutFooterText', '前天看到了小兔，昨天是小鹿，今天是你。', 'custom', '博客 about 区域 footer 的一句话文本', 1,
        '1', '2022-06-20 15:01:29',
        '1', '2022-06-20 15:01:29', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('footerCopyright', '© 2020 ~ 2022', 'custom', '博客 footer 的 copyright 信息', 1, '1', '2022-06-20 15:01:49',
        '1',
        '2022-06-20 15:01:49', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('footerAboutText', '关于本站', 'custom', '博客 footer 的 about 文本', 1, '1', '2022-06-20 15:02:09', '1',
        '2022-06-20 15:02:09', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('beianNum', '蜀ICP备2022013775号-1', 'custom', '备案号', 1, '1', '2022-06-20 15:02:27', '1',
        '2022-06-20 15:02:27',
        0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('blogArchiveDesc', 'HsuYeung 的文章归档', 'custom', '博客归档页面描述', 1, '1', '2022-06-20 15:40:21', '1',
        '2022-06-20 15:40:21', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('blogArchiveKeywords', 'hsuyeung,Hsu Yeung,blog,archive', 'custom', '博客归档页面关键词', 1, '1',
        '2022-06-20 15:40:39',
        '1', '2022-06-20 15:40:39', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('blogAboutDesc', '关于 Hsu Yeung', 'custom', '博客关于页面描述', 1, '1', '2022-06-20 16:48:17', '1',
        '2022-06-20 16:48:17', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('blogAboutKeywords', 'hsuyeung,Hsu Yeung,blog,about', 'custom', '博客关于页面关键词', 1, '1',
        '2022-06-20 16:48:39', '1',
        '2022-06-20 16:48:39', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('blogHomeBannerImg', '/img/home_banner.jpg', 'custom', '博客首页 banner 图片', 1, '1', '2022-06-20 16:53:08',
        '1',
        '2022-06-24 21:45:53', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('blogAboutBannerImg', '/img/about_banner.png', 'custom', '博客关于页面 banner 图片', 1, '1',
        '2022-06-20 16:53:36', '1',
        '2022-06-25 12:11:57', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('avatar', '/img/avatar.jpg', 'custom', '博客头像', 1, '1', '2022-06-20 17:12:36', '1', '2022-06-20 17:12:36',
        0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('qqNumExchangeKUrl', 'https://ptlogin2.qq.com/getface?&imgtype=1&uin={qq}', 'system',
        'qq 号换取头像 k 值 的url', 1,
        '1', '2022-06-21 15:26:06', '1', '2022-06-21 15:26:06', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('mailHeaderImg', 'https://cdn.jsdelivr.net/gh/LIlGG/halo-theme-sakura@1.3.3/source/images/other/head.jpg',
        'mail', '邮件模板 header 图片', 1, '1', '2022-06-21 19:32:17', '1', '2022-06-21 19:32:17', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('mailFooterImg', 'https://cdn.jsdelivr.net/gh/LIlGG/halo-theme-sakura@1.3.3/source/images/other/hr.png', 'mail',
        '邮件模板 footer 图片', 1, '1', '2022-06-21 19:33:02', '1', '2022-06-21 19:33:17', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('browserStaticResourceVersion', '1.0.0', 'system', '浏览器静态资源版本，用于当静态资源更新后通知客户端更新缓存',
        1, '1',
        '2022-06-22 10:25:03', '1', '2022-06-22 10:25:03', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('blogArchiveBannerImg', '/img/archive_banner.png', 'custom', '博客归档页面 banner 图片', 1, '1',
        '2022-06-23 10:03:17',
        '1', '2022-06-25 12:00:37', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('friendLinkGroupKey', 'blog:friendLink:friendLinkGroup', 'redis', '友链分组列表数据缓存 key', 1, '1',
        '2022-06-23 11:22:54', '1', '2022-06-23 11:22:54', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('friendLinkBannerImg', '/img/friend_link_banner.png', 'custom', '友链页面 banner 图', 1, '1',
        '2022-06-23 14:30:18',
        '1', '2022-06-25 11:53:28', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('friendLinkDesc', 'Hsu Yeung 的友链', 'custom', '友链页面的描述', 1, '1', '2022-06-23 14:30:39', '1',
        '2022-06-23 14:30:39', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('friendLinkKeywords', 'hsuyeung,Hsu Yeung,blog,friendLink', 'custom', '友链页面关键词', 1, '1',
        '2022-06-23 14:30:59',
        '1', '2022-06-23 14:30:59', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('homeCustomConfigKey', 'blog:customConfig:home', 'redis', '首页自定义配置缓存 key', 1, '1',
        '2022-06-23 18:10:50', '1',
        '2022-06-24 21:26:55', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('archiveCustomConfigKey', 'blog:customConfig:archive', 'redis', '归档自定义配置缓存 key', 1, '1',
        '2022-06-23 18:11:12',
        '1', '2022-06-24 21:26:57', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('aboutCustomConfigKey', 'blog:customConfig:about', 'redis', '关于页面自定义配置缓存 key', 1, '1',
        '2022-06-24 10:38:06',
        '1', '2022-06-24 21:26:59', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('commonCustomConfigKey', 'blog:customConfig:common', 'redis', '页面公共自定义配置缓存 key', 1, '1',
        '2022-06-24 10:38:26',
        '1', '2022-06-24 21:27:00', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('friendLinkCustomConfigKey', 'blog:customConfig:friendLink', 'redis', '友链页面自定义配置缓存 key', 1, '1',
        '2022-06-24 10:38:48', '1', '2022-06-24 21:27:01', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('userTokenSecret', '记得配置加密密钥', 'user', '用户 token 加密密钥', 1, '1', '2022-06-29 13:57:16', '1',
        '2022-06-29 13:57:16', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('tokenExpireTime', '604800', 'user', '用户 token 过期时间，单位：秒', 1, '1', '2022-06-29 13:57:33', '1',
        '2022-06-29 13:57:33', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('userPermissionKey', 'blog:rbac:userPermission', 'redis', '用户权限集合缓存 key', 1, '1', '2022-06-29 13:58:08',
        '1',
        '2022-06-29 13:58:08', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('userTokenKey', 'blog:token:user', 'redis', '用户 token 缓存 key', 1, '1', '2022-06-29 13:58:25', '1',
        '2022-06-29 13:58:25', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('loginFailedNumKey', 'blog:login:failedNum', 'redis', '用户登录失败次数的缓存 key', 1, '1',
        '2022-06-30 15:31:28', '1',
        '2022-06-30 15:31:28', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`, `create_by`,
                               `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('loginFailedMaxNum', '3', 'user', '用户登录失败尝试最大次数', 1, '1', '2022-06-30 15:33:19', '1',
        '2022-07-06 16:31:47', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`,
                               `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('apiRateLimitByUidKey', 'blog:apiRateLimit:uid', 'redis', '根据请求的用户 id 对接口进行限流的数据缓存 key', 1,
        '1',
        '2022-07-11 19:09:28', '1', '2022-07-11 19:09:28', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`,
                               `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('adminEmailAddress', '记得配置邮箱地址', 'admin', '网站管理员的邮箱地址，接收网站回复通知', 1, '1',
        '2022-07-14 09:56:08', '1', '2022-07-14 09:56:14', 0);
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`,
                               `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('adminName', 'Hsu Yeung', 'admin', '网站管理员名字', 1, '1', '2022-07-14 10:33:00', '1', '2022-07-14 10:33:06',
        0);

