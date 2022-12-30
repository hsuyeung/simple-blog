package com.hsuyeung.blog.web.controller;

import com.hsuyeung.blog.model.vo.article.ArticleDetailVO;
import com.hsuyeung.blog.service.IArticleService;
import com.hsuyeung.blog.service.ICommentService;
import com.hsuyeung.blog.service.IFriendLinkService;
import com.hsuyeung.blog.service.ISystemConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.SYSTEM_BROWSER_STATIC_RESOURCE_VERSION;

/**
 * 博客页面路由
 *
 * @author hsuyeng
 * @date 2022/06/04
 */
@Controller
public class BlogPageController {
    @Resource
    private IArticleService articleService;
    @Resource
    private ICommentService commentService;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private IFriendLinkService friendLinkService;

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("articleList", articleService.getHomeArticleList());
        mv.addObject("homeCustomConf", systemConfigService.getHomeCustomConfig());
        mv.addObject("commonCustomConf", systemConfigService.getCommonCustomConfig());
        mv.addObject("v", systemConfigService.getConfigValue(SYSTEM_BROWSER_STATIC_RESOURCE_VERSION, String.class));
        mv.addObject("currentPage", "index");
        return mv;
    }

    @RequestMapping("/article/{articleRoute}")
    public ModelAndView articleDetail(@PathVariable("articleRoute") String articleRoute) {
        ModelAndView mv = new ModelAndView("article");
        ArticleDetailVO articleDetail = articleService.getArticleDetail(articleRoute);
        mv.addObject("article", articleDetail);
        mv.addObject("commentData", commentService.getCommentList(articleDetail.getId()));
        mv.addObject("commonCustomConf", systemConfigService.getCommonCustomConfig());
        mv.addObject("v", systemConfigService.getConfigValue(SYSTEM_BROWSER_STATIC_RESOURCE_VERSION, String.class));
        return mv;
    }

    @RequestMapping("/archive")
    public ModelAndView archive() {
        ModelAndView mv = new ModelAndView("archive");
        mv.addObject("archive", articleService.getArchiveList());
        mv.addObject("archiveCustomConf", systemConfigService.getArchiveCustomConfig());
        mv.addObject("commonCustomConf", systemConfigService.getCommonCustomConfig());
        mv.addObject("v", systemConfigService.getConfigValue(SYSTEM_BROWSER_STATIC_RESOURCE_VERSION, String.class));
        mv.addObject("currentPage", "archive");
        return mv;
    }

    @RequestMapping("/about")
    public ModelAndView about() {
        ModelAndView mv = new ModelAndView("about");
        mv.addObject("commentData", commentService.getCommentList(0L));
        mv.addObject("aboutCustomConf", systemConfigService.getAboutCustomConfig());
        mv.addObject("commonCustomConf", systemConfigService.getCommonCustomConfig());
        mv.addObject("v", systemConfigService.getConfigValue(SYSTEM_BROWSER_STATIC_RESOURCE_VERSION, String.class));
        mv.addObject("currentPage", "about");
        return mv;
    }

    @RequestMapping("/friend/link")
    public ModelAndView friendLink() {
        ModelAndView mv = new ModelAndView("friend_link");
        mv.addObject("friendLinkData", friendLinkService.getFriendLinkData());
        mv.addObject("friendLinkCustomConf", systemConfigService.getFriendLinkCustomConfig());
        mv.addObject("commonCustomConf", systemConfigService.getCommonCustomConfig());
        mv.addObject("v", systemConfigService.getConfigValue(SYSTEM_BROWSER_STATIC_RESOURCE_VERSION, String.class));
        mv.addObject("currentPage", "friend_link");
        return mv;
    }
}
