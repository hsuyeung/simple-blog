package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.mail.MailInfoVO;
import com.hsuyeung.blog.service.IMailService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hsuyeung
 * @date 2022/07/06
 */
@Api(tags = "邮件相关接口")
@RestController
@RequestMapping("/api/mail")
public class MailApi implements IBaseWebResponse {
    @Resource
    private IMailService mailService;

    /**
     * 分页查询邮件列表
     *
     * @param from     发件人，全模糊
     * @param to       收件人，全模糊
     * @param subject  主题，全模糊
     * @param cc       抄送人，全模糊
     * @param bcc      密送人，全模糊
     * @param status   状态，精确匹配
     * @param type     类型，精确匹配
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 邮件分页列表
     */
    @ApiOperation("分页查询邮件列表")
    @GetMapping("/page")
    public WebResponse<PageVO<MailInfoVO>> getMailPage(
            @ApiParam("发件人") @RequestParam(value = "from", required = false) String from,
            @ApiParam("收件人") @RequestParam(value = "to", required = false) String to,
            @ApiParam("主题") @RequestParam(value = "subject", required = false) String subject,
            @ApiParam("抄送") @RequestParam(value = "cc", required = false) String cc,
            @ApiParam("密送") @RequestParam(value = "bcc", required = false) String bcc,
            @ApiParam("状态") @RequestParam(value = "status", required = false) Integer status,
            @ApiParam("类型") @RequestParam(value = "type", required = false) Integer type,
            @ApiParam("开始时间戳") @RequestParam(value = "startTimestamp", required = false) Long startTimestamp,
            @ApiParam("结束时间戳") @RequestParam(value = "endTimestamp", required = false) Long endTimestamp,
            @ApiParam("页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam("每页数量") @RequestParam("pageSize") Integer pageSize) {
        return ok(mailService.getMailPage(from, to, subject, cc, bcc, status, type, startTimestamp, endTimestamp, pageNum, pageSize));
    }
}
