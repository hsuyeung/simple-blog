package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.friendlink.AddFriendLinkDTO;
import com.hsuyeung.blog.model.dto.friendlink.FriendLinkSearchDTO;
import com.hsuyeung.blog.model.dto.friendlink.UpdateFriendLinkDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.friendlink.FriendLinkInfoVO;
import com.hsuyeung.blog.service.IFriendLinkService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 友链相关接口
 *
 * @author hsuyeung
 * @date 2022/06/22
 */
@Api(tags = "友链相关接口")
@RestController
@RequestMapping("/api/friend/link")
@RequiredArgsConstructor
public class FriendLinkApi implements IBaseWebResponse {
    private final IFriendLinkService friendLinkService;

    @ApiOperation("增加一个友链")
    @PostMapping
    public WebResponse<Void> addFriendLink(
            @ApiParam("新增友链请求参数") @RequestBody AddFriendLinkDTO addFriendLink) {
        friendLinkService.addFriendLink(addFriendLink);
        return ok();
    }

    @ApiOperation("删除一个友链")
    @DeleteMapping("/{id}")
    public WebResponse<Void> deleteFriendLink(@ApiParam("友链 id") @PathVariable("id") Long id) {
        friendLinkService.deleteFriendLink(id);
        return ok();
    }

    @ApiOperation("更新友链")
    @PutMapping
    public WebResponse<Void> updateFriendLink(
            @ApiParam("更新友链请求参数") @RequestBody UpdateFriendLinkDTO updateFriendLink) {
        friendLinkService.updateFriendLink(updateFriendLink);
        return ok();
    }

    /**
     * 分页查询友链列表
     *
     * @param pageSearchParam 友链分页搜索条件
     * @return 友链分页列表
     */
    @ApiOperation("分页查询友链列表")
    @PostMapping("/actions/page")
    public WebResponse<PageVO<FriendLinkInfoVO>> getFriendLinkPage(
            @ApiParam("友链分页搜索条件") @RequestBody PageSearchDTO<FriendLinkSearchDTO> pageSearchParam) {
        return ok(friendLinkService.getFriendLinkPage(pageSearchParam));
    }

    /**
     * 刷新友链缓存
     */
    @ApiOperation("刷新友链缓存")
    @PostMapping("/refresh/cache")
    public WebResponse<Void> refreshFriendLinkCache() {
        friendLinkService.refreshFriendLinkCache();
        return ok();
    }

}
