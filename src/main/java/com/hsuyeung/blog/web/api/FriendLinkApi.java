package com.hsuyeung.blog.web.api;

import com.hsuyeung.blog.model.dto.friendlink.AddFriendLinkDTO;
import com.hsuyeung.blog.model.dto.friendlink.UpdateFriendLinkDTO;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.friendlink.FriendLinkInfoVO;
import com.hsuyeung.blog.service.IFriendLinkService;
import com.hsuyeung.blog.web.core.IBaseWebResponse;
import com.hsuyeung.blog.web.core.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 友链相关接口
 *
 * @author hsuyeung
 * @date 2022/06/22
 */
@Api(tags = "友链相关接口")
@RestController
@RequestMapping("/api/friend/link")
public class FriendLinkApi implements IBaseWebResponse {
    @Resource
    private IFriendLinkService friendLinkService;

    @ApiOperation("增加一个友链")
    @PutMapping
    public WebResponse<Void> addFriendLink(
            @ApiParam("新增友链请求参数") @RequestBody AddFriendLinkDTO addFriendLinkDTO) {
        friendLinkService.addFriendLink(addFriendLinkDTO);
        return ok();
    }

    @ApiOperation("删除一个友链")
    @DeleteMapping("/{id}")
    public WebResponse<Void> deleteFriendLink(@ApiParam("友链 id") @PathVariable("id") Long id) {
        friendLinkService.deleteFriendLink(id);
        return ok();
    }

    @ApiOperation("更新友链")
    @PostMapping
    public WebResponse<Void> updateFriendLink(
            @ApiParam("更新友链请求参数") @RequestBody UpdateFriendLinkDTO updateFriendLinkDTO) {
        friendLinkService.updateFriendLink(updateFriendLinkDTO);
        return ok();
    }

    /**
     * 分页查询友链列表
     *
     * @param linkName  友链名字，全模糊
     * @param linkUrl   友链链接，全模糊
     * @param linkDesc  友链描述，全模糊
     * @param linkGroup 友链分组，精确匹配
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 友链分页列表
     */
    @ApiOperation("分页查询友链列表")
    @GetMapping("/page")
    public WebResponse<PageVO<FriendLinkInfoVO>> getFriendLinkPage(
            @ApiParam("友链名字") @RequestParam(value = "linkName", required = false) String linkName,
            @ApiParam("友链链接") @RequestParam(value = "linkUrl", required = false) String linkUrl,
            @ApiParam("友链描述") @RequestParam(value = "linkDesc", required = false) String linkDesc,
            @ApiParam("友链分组") @RequestParam(value = "linkGroup", required = false) String linkGroup,
            @ApiParam("页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam("每页数量") @RequestParam("pageSize") Integer pageSize) {
        return ok(friendLinkService.getFriendLinkPage(linkName, linkUrl, linkDesc, linkGroup, pageNum, pageSize));
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
