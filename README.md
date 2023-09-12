# simple-blog

基于 SpringBoot、Thymeleaf、原生 HTML + CSS + JS 的一款前后端不分离的博客。

## 在线体验地址

- 博客页面：[Hsu Yeung 的博客](https://www.hsuyeung.com/)
- 管理页面：暂未开放在线体验，可参照下文的`部署步骤`本地部署后访问 localhost:{端口号}/admin/home
  体验。初始帐号/密码：`admin`/`user@12345`

## 介绍

博客整体 UI 设计是直接 F12 参考的 [春水煎茶](https://writings.sh/) 这位大佬的博客折腾出来的。

### 博客前台已实现的功能/页面：

1. 首页（最新 N 篇文章）、归档（按照月份归档）页、友链页、关于页、文章详情页
2. 评论功能，不需要登录，支持根据邮箱自动获取 ~~QQ 头像（优先）和~~
   （失效原因见：[v1.4.0 发布说明](https://github.com/hsuyeung/simple-blog/releases/tag/v1.4.0)） Gravatar 头像，支持
   Markdown
   语法以及预览，Markdown
   渲染使用的 [marked](https://github.com/markedjs/marked)
3. 文章图片懒加载，参考文章 [图片懒加载](https://juejin.cn/post/6844904066418491406#heading-3) 实现
4. XSS 净化：[DOMPurify](https://github.com/cure53/DOMPurify)
5. 代码高亮：[Highlight.js](https://github.com/highlightjs/highlight.js)，本博客下载的版本只支持部分语言，需要更多的语言高亮可以去官方下载后替换项目中的
   js 文件即可
6. 图片缩放：[medium-zoom](https://github.com/francoischalifour/medium-zoom)
7. 弹窗消息提醒：[message_js](https://github.com/nyy-2017/message_js)
8. 图标使用 [IconMoon](https://icomoon.io/)
9. 评论后提醒博客管理员，评论被回复后根据被评论人的选择决定是否发送邮件提醒被评论人
10. 支持通过 iframe 嵌入 B 站视频、网易云音乐。具体使用方法如下：

```text
在新增/编辑文章或者发布评论时，加入如下代码：

!bv{{iframe 链接地址}}，例如：

!bv{{<iframe src="//player.bilibili.com/player.html?aid=904641287&bvid=BV1pP4y1i7Xh&cid=949961021&page=1"
scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"></iframe>}}

最终会被 marked 解析渲染为

<div class='bilibili-aspect-ratio'>
  <iframe src="//player.bilibili.com/player.html?aid=904641287&bvid=BV1pP4y1i7Xh&cid=949961021&page=1"
          scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"></iframe>
</div>

// iframe 有个问题就是会自动播放视频，体验不是很好
// 解决办法也很简单，本博客已实现 src 属性的（图片、视频、音频等都可以哦）懒加载功能（功能 3），可以通过懒加载的形式，等 iframe
区域出现在屏幕上时才加载具体资源
// 具体做法是只需要将 iframe 的 src 属性修改为 data-src 即可
!bv{{<iframe data-src="//player.bilibili.com/player.html?aid=904641287&bvid=BV1pP4y1i7Xh&cid=949961021&page=1"
scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"></iframe>}}
```

详情可参考文章 [网站支持展示 B 站 iframe 视频](https://www.hsuyeung.com/article/support-bilibili-iframe)

11. 文章、评论支持 live photo 图片的展示，具体使用方法如下：

```text
使用如下 marked 扩展语法：

![图片说明](图片链接)(视频链接)

最终将会渲染成以下 HTML 代码：

<figure>
    <div class='livePhotoContainer'
        data-live-photo
        data-effect-type='live'
        data-playback-style='full'
        data-proactively-loads-video='true'
        data-photo-src='图片链接'
        data-video-src='视频链接'></div>
    <figcaption>图片说明</figcaption>
</figure>
```

该项功能由于实现原理的原因，有诸多限制条件，详情可参考文章 [网站支持 Live Photo 图片展示](https://www.hsuyeung.com/article/support-live-photo)

### 博客后台已实现功能

由于自己初衷只是为了搭建一个简单的博客自己使用，毕竟博客的重点在于自己写博客，而不是博客功能有多丰富。
一开始甚至都没想要做管理后台，想着直接操作数据库和 redis
就可以管理了，但后来觉得不太方便就决定还是搞一个简单地管理后台，因此后台管理页面做得比较糊弄，没有注释（但是看名字也基本都知道是干嘛的），直接采用了一个页面来展示、处理所有数据，体验不是很好，勉强算能用。如果各位有愿意的话可以把后台管理页面用前端框架重写，API
接口都是有的。

1. 用户名/密码登录
2. RBAC 权限控制，权限划分到每个请求接口，为了简单直接使用两个拦截器来处理用户 Token 和权限校验
3. 使用注解和 AOP 实现简单的接口请求速率控制，支持 IP 和 用户 id 两种控制方式
4. 文章、评论、系统配置、友链、用户、角色、权限管理
5. 支持上传文件至服务器本地并返回可访问链接（懒得用第三方的 OSS 以后迁移麻烦，所以就自己搞了个简单的文件上传、读取功能）

### 项目结构

```text
├─main
│  ├─java
│  │  └─com
│  │      └─hsuyeung
│  │          └─blog
│  │              ├─annotation            # 自定义注解
│  │              ├─aop                   # 自定义切面
│  │              ├─cache                 # 缓存相关
│  │              │  └─lfu                # 基于内存大小而不是缓存数据个数的 LFU 缓存算法实现
│  │              ├─config                # 各种配置类
│  │              │  └─properties         # yml 文件中的自定义配置
│  │              ├─constant              # 各种常量
│  │              │  └─enums              # 枚举
│  │              ├─exception             # 自定义异常以及全局异常处理类
│  │              ├─filter                # 过滤器
│  │              ├─interceptor           # 拦截器
│  │              ├─mapper                # mapper 类
│  │              ├─model                 # 各种数据对象
│  │              │  ├─dto                # 接口入参、DAO 层和 Service 层相互传递的数据对象
│  │              │  │  ├─article         # 博客文章相关 DTO
│  │              │  │  ├─comment         # 博客评论相关 DTO
│  │              │  │  ├─friendlink      # 博客友链相关 DTO
│  │              │  │  ├─mail            # 博客邮件相关 DTO
│  │              │  │  ├─permission      # 权限相关 DTO
│  │              │  │  ├─role            # 角色相关 DTO
│  │              │  │  ├─systemconfig    # 系统内置的一些配置项相关 DTO
│  │              │  │  └─user            # 用户相关 DTO
│  │              │  ├─entity             # 数据库表对应的实体类
│  │              │  └─vo                 # 后端向页面传递的需要渲染的数据对象或第三方返回的数据对象
│  │              │      ├─article        # 文章相关 VO
│  │              │      ├─comment        # 评论相关 VO
│  │              │      ├─customconfig   # 各个页面的自定义配置相关 VO
│  │              │      ├─file           # 文件相关 VO
│  │              │      ├─friendlink     # 友链相关 VO
│  │              │      ├─httpclient     # http client 请求相关 VO
│  │              │      ├─mail           # 邮件相关 VO
│  │              │      ├─permission     # 权限相关 VO
│  │              │      ├─role           # 角色相关 VO
│  │              │      ├─systemconfig   # 系统内置的一些配置项相关 VO
│  │              │      └─user           # 角色相关 VO
│  │              │─rss                   # RSS 订阅文件相关实现
│  │              ├─schedule              # 定时任务
│  │              ├─service               # Service 接口
│  │              │  └─impl               # Service 接口实现
│  │              ├─util                  # 一些自定义的工具类
│  │              └─web                   # web 相关
│  │                  ├─api               # API 接口
│  │                  ├─controller        # 页面跳转的控制器
│  │                  └─core              # 一些 web 相关的核心类
│  └─resources
│      ├─mapper                           # 编写的 sql 语句
│      ├─sql                              # 项目初始化需要执行的 sql 文件
│      ├─static
│      │  ├─css                           # 项目需要使用到的样式文件
│      │  │  └─admin                      # 后台页面相关的样式文件
│      │  ├─fonts                         # iconmoon 字体以及图标相关文件
│      │  ├─img                           # 图片资源
│      │  ├─js                            # js 文件
│      │  │  └─admin                      # 管理后台的 js 文件
│      │  └─plugin                        # 项目用到的一些外部代码（插件）
│      │      ├─dompurify                 # xss 净化
│      │      ├─highlight                 # 代码高亮
│      │      ├─lazyload                  # 图片懒加载
│      │      ├─livephotoskit             # live photo 图片展示
│      │      ├─marked                    # markdown 渲染
│      │      ├─medium-zoom               # 图片点击缩放
│      │      └─message                   # 消息弹窗
│      └─templates
│          ├─admin                        # 管理后台相关页面文件
│          ├─component                    # 页面组件
│          │  └─admin                     # 管理后台组件
│          │      ├─article               # 文章管理相关组件
│          │      ├─comment               # 评论管理相关组件
│          │      ├─file                  # 文件管理相关组件
│          │      ├─friend_link           # 友链管理相关组件
│          │      ├─mail                  # 邮件管理相关组件
│          │      ├─permission            # 权限管理相关组件
│          │      ├─role                  # 角色管理相关组件
│          │      ├─system_config         # 系统配置管理相关组件
│          │      └─user                  # 用户管理相关组件
│          ├─error                        # 404 以及 500 错误页面定义
│          └─mail                         # 邮件模板文件定义
└─test                                    # 单元测试相关
    └─java
        └─com
            └─hsuyeung
                └─blog

```

## 部署步骤

该部署步骤只介绍让项目正常跑起来必须要做的事情，可改可不改以及页面、功能修改请参考下文的 `修改建议`

1. 补充 `application.yml` 文件中注释中带 **TODO** 标识的配置项
2. `application-dev.yml` 为开发环境配置，`application-prod.yml` 为生产环境配置，根据自己的情况补充其中带 **TODO** 标识的配置项
3. 修改 `com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum` 类中带 **TODO** 标识的配置
4. 将 src/main/resources/sql/05_init_system_config.sql 中的 `记得配置加密密钥`
   修改为 `com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.USER_TOKEN_SECRET` 的值；`记得配置邮箱地址`
   修改为 `com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.ADMIN_EMAIL_ADDRESS` 的值
5. 创建数据库，名字为 `blog`，如果不叫这个名字就需要修改 `application-dev.yml` 和 `application-prod.yml` 中的 jdbc
   连接配置。数据库字符集推荐 `utf8mb4`
6. 按照**编号顺序**将 `src/main/resources/sql/` 文件夹下的 sql 全部在刚才新建的数据库中执行
7. 开启 `redis-server` 服务，服务器上的话还需要配置 redis 的访问密码和 `application-prod.yml` 中配置的密码保持一致
8. 启动项目，访问 `localhost:8083` 即可访问博客首页，`localhost:8083/admin/home` 即可访问博客后台管理页面

## 修改建议

1. 配置文件大部分都有注释说明，可以根据自己的需要进行修改
2. 博客 css 文件均压缩处理了，所以如果自己修改了 css 请记得同步修改 `*.min.css` 文件，否则页面上不会生效哦
3. 博客虽然很多东西做成了配置，但是最开始做的时候只想的自己个人用没想过开源出来，所以有些地方就还是直接写死了，所以很多名字、联系方式什么的需要各位自己全局搜索替换一下或者自己添加系统配置
4. 博客中使用到的静态图片资源名字都是在页面上写死了的，所以如果要替换图片请记得修改代码中引用的资源名称

## 说明

本博客的初衷是为了简单，所以不考虑做成前后端分离以及支持 OSS 上传图片啥的，因为懒得维护多个项目以及第三方数据。

欢迎各位体验该博客并提出宝贵的改进意见。
