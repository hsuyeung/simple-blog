package com.hsuyeung.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hsuyeung.blog.model.dto.friendlink.AddFriendLinkDTO;
import com.hsuyeung.blog.model.dto.friendlink.UpdateFriendLinkDTO;
import com.hsuyeung.blog.model.entity.FriendLinkEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.friendlink.FriendLinkInfoVO;
import com.hsuyeung.blog.model.vo.friendlink.FriendLinkVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

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
     * @param addFriendLinkDTO {@link AddFriendLinkDTO}
     */
    void addFriendLink(@Valid AddFriendLinkDTO addFriendLinkDTO);

    /**
     * 根据 id 删除一个友链
     *
     * @param id 友链 id
     */
    void deleteFriendLink(Long id);

    /**
     * 更新友链
     *
     * @param updateFriendLinkDTO {@link UpdateFriendLinkDTO}
     */
    void updateFriendLink(@Valid UpdateFriendLinkDTO updateFriendLinkDTO);

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
    PageVO<FriendLinkInfoVO> getFriendLinkPage(String linkName, String linkUrl, String linkDesc, String linkGroup,
                                               Integer pageNum, Integer pageSize);

    /**
     * 刷新友链缓存
     */
    void refreshFriendLinkCache();
}
