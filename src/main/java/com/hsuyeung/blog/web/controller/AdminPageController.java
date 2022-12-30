package com.hsuyeung.blog.web.controller;

import com.hsuyeung.blog.service.IMailService;
import com.hsuyeung.blog.service.ISystemConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.SYSTEM_BROWSER_STATIC_RESOURCE_VERSION;

/**
 * 管理后台页面控制器
 *
 * @author hsuyeung
 * @date 2022/06/29
 */
@Controller
@RequestMapping("/admin")
public class AdminPageController {
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private IMailService mailService;

    @RequestMapping("/home")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("admin/home");
        mv.addObject("v", systemConfigService.getConfigValue(SYSTEM_BROWSER_STATIC_RESOURCE_VERSION, String.class));
        return mv;
    }

    @RequestMapping("/preview/mail/text/{mailId}")
    public ModelAndView previewMailText(@PathVariable("mailId") Long mailId) {
        ModelAndView mv = new ModelAndView("admin/mail_preview");
        mv.addObject("text", mailService.getMailText(mailId));
        return mv;
    }
}
