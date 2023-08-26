package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.dto.PageSearchDTO;
import com.hsuyeung.blog.model.dto.friendlink.AddFriendLinkDTO;
import com.hsuyeung.blog.model.dto.friendlink.FriendLinkSearchDTO;
import com.hsuyeung.blog.model.dto.friendlink.UpdateFriendLinkDTO;
import com.hsuyeung.blog.model.entity.FriendLinkEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.friendlink.FriendLinkInfoVO;
import com.hsuyeung.blog.model.vo.friendlink.FriendLinkVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author hsuyeung
 * @date 2022/06/22
 */
@Validated
public interface IFriendLinkService extends IService<FriendLinkEntity> {

    /**
     * 获取友链列表并且按照 group 分组
     *
     * @return {@link FriendLinkVO}
     */
    FriendLinkVO getFriendLinkData();

    /**
     * 添加一个友链
     *
     * @param addFriendLink {@link AddFriendLinkDTO}
     */
    void addFriendLink(@NotNull(message = "addFriendLink 不能为 null") @Valid AddFriendLinkDTO addFriendLink);

    /**
     * 根据 id 删除一个友链
     *
     * @param id 友链 id
     */
    void deleteFriendLink(Long id);

    /**
     * 更新友链
     *
     * @param updateFriendLink {@link UpdateFriendLinkDTO}
     */
    void updateFriendLink(@NotNull(message = "updateFriendLink 不能为 null") @Valid UpdateFriendLinkDTO updateFriendLink);

    /**
     * 分页查询友链列表
     *
     * @param pageSearchParam 友链分页搜索条件
     * @return 友链分页列表
     */
    PageVO<FriendLinkInfoVO> getFriendLinkPage(@NotNull(message = "pageSearchParam 不能为 null") @Valid
                                               PageSearchDTO<FriendLinkSearchDTO> pageSearchParam);

    /**
     * 刷新友链缓存
     */
    void refreshFriendLinkCache();
}
