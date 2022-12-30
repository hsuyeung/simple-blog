package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.service.IUserTokenService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户 token 相关接口
 *
 * @author hsuyeung
 * @date 2022/07/01
 */
@Api(tags = "用户 token 相关接口")
@RestController
@RequestMapping("/api/user/token")
public class UserTokenApi implements IBaseWebResponse {
    @Resource
    private IUserTokenService userTokenService;

    @ApiOperation("校验 token 是否有效")
    @GetMapping("/validation")
    public WebResponse<Boolean> checkUserTokenValidation(@RequestParam String token) {
        return ok(!userTokenService.isExpired(token));
    }
}
