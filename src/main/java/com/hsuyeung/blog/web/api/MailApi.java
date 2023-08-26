package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.mail.MailSearchDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.mail.MailInfoVO;
import com.hsuyeung.blog.service.IMailService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hsuyeung
 * @date 2022/07/06
 */
@Api(tags = "邮件相关接口")
@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailApi implements IBaseWebResponse {
    private final IMailService mailService;

    /**
     * 分页查询邮件列表
     *
     * @param pageSearchParam 邮件分页搜索条件
     * @return 邮件分页列表
     */
    @ApiOperation("分页查询邮件列表")
    @PostMapping("/actions/page")
    public WebResponse<PageVO<MailInfoVO>> getMailPage(
            @ApiParam("发件人") @RequestBody PageSearchDTO<MailSearchDTO> pageSearchParam) {
        return ok(mailService.getMailPage(pageSearchParam));
    }
}
