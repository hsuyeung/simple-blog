package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.cache.FriendLinkCache;
import com.hsuyeung.blog.mapper.FriendLinkMapper;
import com.hsuyeung.blog.model.dto.friendlink.AddFriendLinkDTO;
import com.hsuyeung.blog.model.dto.friendlink.UpdateFriendLinkDTO;
import com.hsuyeung.blog.model.entity.FriendLinkEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.friendlink.FriendLinkGroupVO;
import com.hsuyeung.blog.model.vo.friendlink.FriendLinkInfoVO;
import com.hsuyeung.blog.model.vo.friendlink.FriendLinkItemVO;
import com.hsuyeung.blog.model.vo.friendlink.FriendLinkVO;
import com.hsuyeung.blog.service.IFriendLinkService;
import com.hsuyeung.blog.service.ISystemConfigService;
import com.hsuyeung.blog.service.IUserService;
import com.hsuyeung.blog.util.AssertUtil;
import com.hsuyeung.blog.util.BizAssertUtil;
import com.hsuyeung.blog.util.ConvertUtil;
import com.hsuyeung.blog.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.hsuyeung.blog.constant.DateFormatConstants.FORMAT_YEAR_TO_SECOND;
import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.REDIS_FRIEND_LINK_GROUP_LIST_KEY;

/**
 * @author hsuyeung
 * @date 2022/06/22
 */
@Service("friendLinkService")
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLinkEntity> implements IFriendLinkService {
    @Resource
    private FriendLinkCache friendLinkCache;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private IUserService userService;

    @Override
    public FriendLinkVO getFriendLinkData() {
        String key = systemConfigService.getConfigValue(REDIS_FRIEND_LINK_GROUP_LIST_KEY, String.class);
        FriendLinkVO friendLinkVO = friendLinkCache.getFriendLinkVO(key);
        if (Objects.nonNull(friendLinkVO)) {
            return friendLinkVO;
        }
        friendLinkVO = this.getFriendLinkVOFromDB();
        if (friendLinkVO == null) {
            return null;
        }
        friendLinkCache.cacheFriendLinkVO(key, friendLinkVO);

        return friendLinkVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addFriendLink(AddFriendLinkDTO addFriendLinkDTO) {
        AssertUtil.notNull(addFriendLinkDTO, "addFriendLinkDTO 不能为空");
        save(ConvertUtil.convert(addFriendLinkDTO, FriendLinkEntity.class));
        refreshFriendLinkCache();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteFriendLink(Long id) {
        AssertUtil.notNull(id, "id 不能为空");
        lambdaUpdate().eq(FriendLinkEntity::getId, id).remove();
        refreshFriendLinkCache();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFriendLink(UpdateFriendLinkDTO updateFriendLinkDTO) {
        AssertUtil.notNull(updateFriendLinkDTO, "updateFriendLinkDTO 不能为空");
        Long id = updateFriendLinkDTO.getId();
        FriendLinkEntity friendLinkEntity = lambdaQuery()
                .select(FriendLinkEntity::getId, FriendLinkEntity::getLinkName, FriendLinkEntity::getLinkUrl,
                        FriendLinkEntity::getLinkAvatar, FriendLinkEntity::getLinkDesc, FriendLinkEntity::getLinkGroup)
                .eq(FriendLinkEntity::getId, id)
                .one();
        BizAssertUtil.notNull(friendLinkEntity, String.format("id 为 %d 的友链不存在", id));
        if (Objects.isNull(updateFriendLinkDTO.getLinkDesc())) {
            updateFriendLinkDTO.setLinkDesc("");
        }
        BeanUtils.copyProperties(updateFriendLinkDTO, friendLinkEntity);
        updateById(friendLinkEntity);
        refreshFriendLinkCache();
    }

    @Override
    public PageVO<FriendLinkInfoVO> getFriendLinkPage(String linkName, String linkUrl, String linkDesc, String linkGroup,
                                                      Integer pageNum, Integer pageSize) {
        Page<FriendLinkEntity> entityPage = lambdaQuery()
                .select(FriendLinkEntity::getId, FriendLinkEntity::getLinkUrl, FriendLinkEntity::getLinkName,
                        FriendLinkEntity::getLinkAvatar, FriendLinkEntity::getLinkDesc, FriendLinkEntity::getLinkGroup,
                        FriendLinkEntity::getCreateTime, FriendLinkEntity::getCreateBy,
                        FriendLinkEntity::getUpdateTime, FriendLinkEntity::getUpdateBy)
                .like(StringUtils.hasLength(linkName), FriendLinkEntity::getLinkName, linkName)
                .like(StringUtils.hasLength(linkUrl), FriendLinkEntity::getLinkUrl, linkUrl)
                .like(StringUtils.hasLength(linkDesc), FriendLinkEntity::getLinkDesc, linkDesc)
                .eq(StringUtils.hasLength(linkGroup), FriendLinkEntity::getLinkGroup, linkGroup)
                .orderByDesc(FriendLinkEntity::getUpdateTime)
                .page(new Page<>(pageNum, pageSize));
        List<FriendLinkEntity> entityList = entityPage.getRecords();
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageVO<>(0L, Collections.emptyList());
        }
        List<FriendLinkInfoVO> voList = entityList.stream().map(entity -> {
            FriendLinkInfoVO vo = ConvertUtil.convert(entity, FriendLinkInfoVO.class);
            vo.setCreateTime(DateUtil.formatLocalDateTime(entity.getCreateTime(), FORMAT_YEAR_TO_SECOND));
            vo.setUpdateTime(DateUtil.formatLocalDateTime(entity.getUpdateTime(), FORMAT_YEAR_TO_SECOND));
            String createBy = entity.getCreateBy();
            String updateBy = entity.getUpdateBy();
            vo.setCreateBy(userService.getUsernameById(StringUtils.hasLength(createBy) ? Long.parseLong(createBy) : null));
            vo.setUpdateBy(userService.getUsernameById(StringUtils.hasLength(updateBy) ? Long.parseLong(updateBy) : null));
            return vo;
        }).collect(Collectors.toList());
        return new PageVO<>(entityPage.getTotal(), voList);
    }

    @Override
    public void refreshFriendLinkCache() {
        friendLinkCache.cacheFriendLinkVO(systemConfigService.getConfigValue(REDIS_FRIEND_LINK_GROUP_LIST_KEY, String.class), this.getFriendLinkVOFromDB());
    }

    // --------------------------------------------- PRIVATE METHOD ---------------------------------------------

    /**
     * 从数据库获取博客前台友链信息
     *
     * @return {@link FriendLinkVO}
     */
    private FriendLinkVO getFriendLinkVOFromDB() {
        FriendLinkVO friendLinkVO;
        List<FriendLinkEntity> friendLinkEntityList = lambdaQuery()
                .select(FriendLinkEntity::getLinkName, FriendLinkEntity::getLinkUrl, FriendLinkEntity::getLinkAvatar,
                        FriendLinkEntity::getLinkDesc, FriendLinkEntity::getLinkGroup)
                .list();
        if (CollectionUtils.isEmpty(friendLinkEntityList)) {
            return null;
        }
        friendLinkVO = new FriendLinkVO();
        List<FriendLinkGroupVO> friendLinkGroupList = new ArrayList<>(8);
        friendLinkVO.setTotalSize((long) friendLinkEntityList.size());
        Map<String, List<FriendLinkEntity>> groupByLinkGroup = friendLinkEntityList.stream().collect(Collectors.groupingBy(FriendLinkEntity::getLinkGroup));
        for (Map.Entry<String, List<FriendLinkEntity>> entry : groupByLinkGroup.entrySet()) {
            friendLinkGroupList.add(new FriendLinkGroupVO(entry.getKey(), ConvertUtil.convertList(entry.getValue(), FriendLinkItemVO.class)));
        }
        friendLinkVO.setFriendLinkGroupList(friendLinkGroupList);
        return friendLinkVO;
    }
}
