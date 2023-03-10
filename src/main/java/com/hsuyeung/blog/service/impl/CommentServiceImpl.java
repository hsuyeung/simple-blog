package com.hsuyeung.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsuyeung.blog.cache.CommentCache;
import com.hsuyeung.blog.constant.enums.CommentNotificationSwitchEnum;
import com.hsuyeung.blog.exception.SystemInternalException;
import com.hsuyeung.blog.mapper.CommentMapper;
import com.hsuyeung.blog.model.dto.comment.SubmitCommentRequestDTO;
import com.hsuyeung.blog.model.dto.mail.SendMailDTO;
import com.hsuyeung.blog.model.entity.CommentEntity;
import com.hsuyeung.blog.model.vo.PageVO;
import com.hsuyeung.blog.model.vo.article.ArticleRouteAndTitleVO;
import com.hsuyeung.blog.model.vo.comment.*;
import com.hsuyeung.blog.model.vo.httpclient.HttpClientResult;
import com.hsuyeung.blog.service.IArticleService;
import com.hsuyeung.blog.service.ICommentService;
import com.hsuyeung.blog.service.IMailService;
import com.hsuyeung.blog.service.ISystemConfigService;
import com.hsuyeung.blog.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.hsuyeung.blog.constant.DateFormatConstants.FORMAT_YEAR_TO_SECOND;
import static com.hsuyeung.blog.constant.MailTemplateConstants.MAIL_BE_COMMENTED_TEMPLATE;
import static com.hsuyeung.blog.constant.MailTemplateConstants.MAIL_COMMENT_BE_REPLIED_TEMPLATE;
import static com.hsuyeung.blog.constant.RegexConstants.*;
import static com.hsuyeung.blog.constant.SystemConfigConstants.SystemConfigEnum.*;
import static com.hsuyeung.blog.constant.enums.CommentNotificationSwitchEnum.OFF;
import static com.hsuyeung.blog.constant.enums.CommentNotificationSwitchEnum.ON;
import static com.hsuyeung.blog.constant.enums.MailTypeEnum.BE_COMMENTED;
import static com.hsuyeung.blog.constant.enums.MailTypeEnum.COMMENT_BE_REPLIED;

/**
 * <p>
 * ???????????????????????????
 * </p>
 *
 * @author hsuyeung
 * @since 2022/06/05
 */
@Slf4j
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements ICommentService {
    @Value("${spring.mail.username}")
    private String from;
    @Resource
    private IArticleService articleService;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private CommentCache commentCache;
    @Resource
    private IMailService mailService;
    @Resource
    private HttpClientUtil httpClientUtil;

    @Override
    public CommentVO getCommentList(Long articleId) {
        articleId = Objects.isNull(articleId) ? 0L : articleId;

        // ?????????????????????????????????
        String key = systemConfigService.getConfigValue(REDIS_COMMENT_LIST_KEY, String.class);
        List<CommentEntity> entityList = commentCache.getCommentList(key, articleId);
        if (CollectionUtils.isEmpty(entityList)) {
            entityList = this.getCommentListFromDB(articleId);
            if (CollectionUtils.isEmpty(entityList)) {
                return new CommentVO(0L, Collections.emptyList());
            }
            commentCache.cacheCommentList(key, articleId, entityList);
        }
        List<CommentItemVO> commentItemList = new ArrayList<>(8);
        // ?????????????????????
        for (CommentEntity commentEntity : entityList) {
            if (Objects.equals(commentEntity.getParentId(), 0L) || Objects.equals(commentEntity.getReplyId(), 0L)) {
                CommentItemVO commentItem = this.buildCommentItem(commentEntity, null);
                // ???????????????????????????????????????
                commentItem.setChildComments(this.findChildCommentList(entityList, commentEntity.getId()));
                commentItemList.add(commentItem);
            }
        }
        // ??????????????????????????????
        Collections.reverse(commentItemList);
        return new CommentVO((long) entityList.size(), commentItemList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long submitComment(SubmitCommentRequestDTO submitCommentRequestDTO, String ipAddr) throws URISyntaxException, IOException {
        AssertUtil.notNull(submitCommentRequestDTO, "submitCommentRequestDTO ????????????");
        Long articleId = submitCommentRequestDTO.getArticleId();
        String nickname = submitCommentRequestDTO.getNickname();
        BizAssertUtil.isTrue(nickname.matches(NICKNAME_REGEX), "???????????????");
        BizAssertUtil.isTrue(articleService.isExists(articleId), "????????????????????????");
        // ????????????
        String email = submitCommentRequestDTO.getEmail();
        AssertUtil.isTrue(email.matches(EMAIL_REGEX), "??????????????????????????????");
        String website = submitCommentRequestDTO.getWebsite();
        if (!StringUtils.hasLength(website)) {
            website = "";
        } else {
            website = CommonUtil.removeTag(website);
            if (!website.matches(WEBSITE_PREFIX_REGEX)) {
                website = "http://" + website;
            }
        }
        Long parentCommentId = submitCommentRequestDTO.getParentCommentId();
        Long replyCommentId = submitCommentRequestDTO.getReplyCommentId();
        CommentEntity commentEntity = CommentEntity.builder()
                .nickname(nickname)
                .notification(Boolean.TRUE.equals(submitCommentRequestDTO.getIsNotificationChecked()) ? ON : OFF)
                .parentId(parentCommentId)
                .replyId(replyCommentId)
                .content(submitCommentRequestDTO.getContent())
                .email(email)
                .website(website)
                .articleId(articleId)
                .ip(ipAddr)
                .avatar(this.generateAvatar(email))
                .createBy(nickname)
                .updateBy(nickname)
                .build();
        save(commentEntity);
        // ????????????
        commentCache.cacheCommentList(
                systemConfigService.getConfigValue(REDIS_COMMENT_LIST_KEY, String.class),
                articleId,
                this.getCommentListFromDB(articleId));
        if (!(Objects.equals(parentCommentId, 0L) || Objects.equals(replyCommentId, 0L))) {
            // ????????????????????????????????????????????????????????????????????????????????????????????????
            this.sendReplyNotificationEmail(commentEntity);
        }
        // ?????????????????????????????????????????????????????????????????????
        this.sendReplyNotificationEmailToAdmin(commentEntity);
        return commentEntity.getId();
    }

    @Override
    public PageVO<CommentInfoVO> getCommentPage(String nickname, String email, String website, String parentNickname,
                                                String replyNickname, Integer articleId, String ip, Integer notification,
                                                Long startTimestamp, Long endTimestamp, Integer pageNum, Integer pageSize) {
        CommentNotificationSwitchEnum commentNotificationSwitchEnum = CommentNotificationSwitchEnum.getByCode(notification);
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (Objects.nonNull(startTimestamp)) {
            startTime = DateUtil.fromLongToJava8LocalDate(startTimestamp);
        }
        if (Objects.nonNull(endTimestamp)) {
            endTime = DateUtil.fromLongToJava8LocalDate(endTimestamp);
        }
        List<Long> parentIdList = null;
        if (StringUtils.hasLength(parentNickname)) {
            parentIdList = this.getIdsByNickname(parentNickname);
        }
        List<Long> replyIdList = null;
        if (StringUtils.hasLength(replyNickname)) {
            replyIdList = this.getIdsByNickname(replyNickname);
        }
        Page<CommentEntity> entityPage = lambdaQuery()
                .select(CommentEntity::getId, CommentEntity::getNickname, CommentEntity::getAvatar, CommentEntity::getEmail,
                        CommentEntity::getWebsite, CommentEntity::getContent, CommentEntity::getParentId, CommentEntity::getReplyId,
                        CommentEntity::getArticleId, CommentEntity::getIp, CommentEntity::getNotification, CommentEntity::getCreateTime)
                .like(StringUtils.hasLength(nickname), CommentEntity::getNickname, nickname)
                .like(StringUtils.hasLength(email), CommentEntity::getEmail, email)
                .like(StringUtils.hasLength(website), CommentEntity::getWebsite, website)
                .in(!CollectionUtils.isEmpty(parentIdList), CommentEntity::getParentId, parentIdList)
                .in(!CollectionUtils.isEmpty(replyIdList), CommentEntity::getReplyId, replyIdList)
                .eq(Objects.nonNull(articleId), CommentEntity::getArticleId, articleId)
                .like(StringUtils.hasLength(ip), CommentEntity::getIp, ip)
                .eq(Objects.nonNull(commentNotificationSwitchEnum), CommentEntity::getNotification, commentNotificationSwitchEnum)
                .ge(Objects.nonNull(startTime), CommentEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), CommentEntity::getCreateTime, endTime)
                .orderByDesc(CommentEntity::getCreateTime)
                .page(new Page<>(pageNum, pageSize));
        List<CommentEntity> entityList = entityPage.getRecords();
        if (CollectionUtils.isEmpty(entityList)) {
            return new PageVO<>(0L, Collections.emptyList());
        }
        Map<Long, ArticleRouteAndTitleVO> articleRouteAndTitleMap = articleService.getArticleRouteAndTitle(entityList.stream()
                .map(CommentEntity::getArticleId)
                .collect(Collectors.toList()));
        String blogHomeUrl = systemConfigService.getConfigValue(SYSTEM_BLOG_HOME_URL, String.class);
        List<CommentInfoVO> voList = entityList.stream().map(entity -> {
            CommentInfoVO vo = ConvertUtil.convert(entity, CommentInfoVO.class);
            boolean isAboutPage = Objects.equals(entity.getArticleId(), 0L);
            ArticleRouteAndTitleVO articleRouteAndTitleVO = articleRouteAndTitleMap.getOrDefault(entity.getArticleId(), new ArticleRouteAndTitleVO());
            String articleUrl;
            if (isAboutPage) {
                articleUrl = String.format("%s/about", blogHomeUrl);
            } else {
                articleUrl = String.format("%s/article/%s", blogHomeUrl, articleRouteAndTitleVO.getRoute());
            }
            vo.setContentPreviewUrl(articleUrl + "#comment-" + entity.getId());
            vo.setParentNickname(this.getNicknameById(entity.getParentId()));
            vo.setReplyNickname(this.getNicknameById(entity.getReplyId()));
            vo.setArticleTitle(isAboutPage ? "?????????" : articleRouteAndTitleVO.getTitle());
            vo.setArticleUrl(articleUrl);
            vo.setNotification(Objects.equals(entity.getNotification(), ON));
            vo.setCreateTime(DateUtil.formatLocalDateTime(entity.getCreateTime(), FORMAT_YEAR_TO_SECOND));
            return vo;
        }).collect(Collectors.toList());
        return new PageVO<>(entityPage.getTotal(), voList);
    }

    @Override
    public void deleteComment(Long id) {
        AssertUtil.notNull(id, "id ????????????");
        List<Long> articleIdList = lambdaQuery()
                .select(CommentEntity::getArticleId)
                .eq(CommentEntity::getId, id)
                .or()
                .eq(CommentEntity::getParentId, id)
                .or()
                .eq(CommentEntity::getReplyId, id)
                .list()
                .stream()
                .map(CommentEntity::getArticleId).distinct().collect(Collectors.toList());
        lambdaUpdate()
                .eq(CommentEntity::getId, id)
                .or()
                .eq(CommentEntity::getParentId, id)
                .or()
                .eq(CommentEntity::getReplyId, id)
                .remove();
        // ????????????
        articleIdList.forEach(articleId ->
                commentCache.cacheCommentList(
                        systemConfigService.getConfigValue(REDIS_COMMENT_LIST_KEY, String.class),
                        articleId,
                        this.getCommentListFromDB(articleId)));
    }

    @Override
    public Long countByArticleId(Long aid) {
        AssertUtil.notNull(aid, "aid ????????????");
        return lambdaQuery().eq(CommentEntity::getArticleId, aid).count();
    }

    // --------------------------------------------- PRIVATE METHOD ---------------------------------------------

    private void sendReplyNotificationEmailToAdmin(CommentEntity commentEntity) {
        Long commentId = commentEntity.getId();
        Long articleId = commentEntity.getArticleId();
        boolean isAboutPageComment = Objects.equals(articleId, 0L);
        ArticleRouteAndTitleVO articleRouteAndTitle = null;
        if (!isAboutPageComment) {
            articleRouteAndTitle = articleService.getArticleRouteAndTitle(articleId);
        }
        String commentEntityContent = commentEntity.getContent();
        String blogHomeUrl = systemConfigService.getConfigValue(SYSTEM_BLOG_HOME_URL, String.class);
        SendMailDTO sendMailDTO = SendMailDTO.builder()
                .mFrom(from)
                .mTo(systemConfigService.getConfigValue(ADMIN_EMAIL_ADDRESS, String.class))
                .mSubject("???????????????????????????????????????~")
                .text(MAIL_BE_COMMENTED_TEMPLATE
                        .replace("{{emailHeaderImg}}", systemConfigService.getConfigValue(MAIL_HEADER_IMG, String.class))
                        .replace("{{adminName}}", systemConfigService.getConfigValue(ADMIN_NAME, String.class))
                        .replace("{{commentAuthor}}", commentEntity.getNickname())
                        .replace("{{articleUrl}}", isAboutPageComment
                                ? String.format("%s/about", blogHomeUrl)
                                : String.format("%s/article/%s", blogHomeUrl, articleRouteAndTitle.getRoute()))
                        .replace("{{articleTitle}}", isAboutPageComment ? "?????????" : articleRouteAndTitle.getTitle())
                        .replace("{{commentContent}}", CommonUtil.removeTag(String.format("%s%s",
                                commentEntityContent.substring(0, Math.min(50, commentEntityContent.length())),
                                commentEntityContent.length() > 50 ? " ..." : "")))
                        .replace("{{emailFooterImg}}", systemConfigService.getConfigValue(MAIL_FOOTER_IMG, String.class))
                        .replace("{{commentUrl}}", isAboutPageComment
                                ? String.format("%s/about#comment-%d", blogHomeUrl, commentId)
                                : String.format("%s/article/%s#comment-%d", blogHomeUrl, articleRouteAndTitle.getRoute(), commentId)))
                .type(BE_COMMENTED)
                .sendTime(LocalDateTime.now())
                .build();
        mailService.sendMimeMail(sendMailDTO);
    }

    /**
     * ???????????? id ?????????????????????
     *
     * @param id ?????? id
     * @return ??????
     */
    private String getNicknameById(Long id) {
        AssertUtil.notNull(id, "id ????????????");
        CommentEntity comment = lambdaQuery().select(CommentEntity::getNickname).eq(CommentEntity::getId, id).one();
        return Objects.isNull(comment) ? "" : comment.getNickname();
    }

    /**
     * ????????????????????????????????? id ??????
     *
     * @param nickname ??????
     * @return ??????????????? id ??????
     */
    private List<Long> getIdsByNickname(String nickname) {
        AssertUtil.notNull(nickname, "nickname ????????????");
        return lambdaQuery()
                .select(CommentEntity::getId)
                .like(CommentEntity::getNickname, nickname)
                .list()
                .stream()
                .map(CommentEntity::getId)
                .collect(Collectors.toList());
    }

    /**
     * ????????????????????????????????????
     *
     * @param commentEntity ????????????
     */
    private void sendReplyNotificationEmail(CommentEntity commentEntity) {
        Long commentId = commentEntity.getId();
        Long replyCommentId = commentEntity.getReplyId();
        CommentEntity replyComment = lambdaQuery()
                .select(CommentEntity::getId, CommentEntity::getNickname, CommentEntity::getNotification,
                        CommentEntity::getArticleId, CommentEntity::getContent, CommentEntity::getEmail)
                .eq(CommentEntity::getId, replyCommentId)
                .one();
        if (Objects.isNull(replyComment)) {
            log.error("???????????????id={}?????????????????????id={}???????????????????????????????????????", commentId, replyCommentId);
            return;
        }
        if (Objects.equals(replyComment.getNotification(), OFF)) {
            return;
        }
        String blogHomeUrl = systemConfigService.getConfigValue(SYSTEM_BLOG_HOME_URL, String.class);
        boolean isAboutPageComment = Objects.equals(replyComment.getArticleId(), 0L);
        ArticleRouteAndTitleVO articleRouteAndTitle = null;
        if (!isAboutPageComment) {
            articleRouteAndTitle = articleService.getArticleRouteAndTitle(replyComment.getArticleId());
        }
        String replyCommentContent = replyComment.getContent();
        String commentEntityContent = commentEntity.getContent();
        SendMailDTO sendMailDTO = SendMailDTO.builder()
                .mFrom(from)
                // replyComment ?????????????????????
                .mTo(replyComment.getEmail())
                .mSubject(systemConfigService.getConfigValue(MAIL_COMMENT_BE_REPLIED_MAIL_SUBJECT, String.class))
                .text(MAIL_COMMENT_BE_REPLIED_TEMPLATE
                        .replace("{{emailHeaderImg}}", systemConfigService.getConfigValue(MAIL_HEADER_IMG, String.class))
                        .replace("{{replyCommentAuthor}}", replyComment.getNickname())
                        .replace("{{articleUrl}}", String.format("%s/%s", blogHomeUrl,
                                isAboutPageComment
                                        ? "about"
                                        : String.format("article/%s", articleRouteAndTitle.getRoute())))
                        .replace("{{articleTitle}}", isAboutPageComment ? "?????????" : articleRouteAndTitle.getTitle())
                        .replace("{{replyCommentContent}}", CommonUtil.removeTag(String.format("%s%s",
                                replyCommentContent.substring(0, Math.min(50, replyCommentContent.length())),
                                replyCommentContent.length() > 50 ? " ..." : "")))
                        .replace("{{commentAuthor}}", commentEntity.getNickname())
                        .replace("{{commentContent}}", CommonUtil.removeTag(String.format("%s%s",
                                commentEntityContent.substring(0, Math.min(50, commentEntityContent.length())),
                                commentEntityContent.length() > 50 ? " ..." : "")))
                        .replace("{{emailFooterImg}}", systemConfigService.getConfigValue(MAIL_FOOTER_IMG, String.class))
                        .replace("{{commentUrl}}", isAboutPageComment
                                ? String.format("%s/about#comment-%d", blogHomeUrl, replyCommentId)
                                : String.format("%s/article/%s#comment-%d", blogHomeUrl, articleRouteAndTitle.getRoute(), replyCommentId)))
                .type(COMMENT_BE_REPLIED)
                .sendTime(LocalDateTime.now())
                .build();
        mailService.sendMimeMail(sendMailDTO);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param articleId ?????? id
     * @return ????????????
     */
    private List<CommentEntity> getCommentListFromDB(Long articleId) {
        return lambdaQuery()
                .select(CommentEntity::getId, CommentEntity::getNickname, CommentEntity::getAvatar,
                        CommentEntity::getContent, CommentEntity::getCreateTime, CommentEntity::getParentId,
                        CommentEntity::getWebsite, CommentEntity::getReplyId)
                .eq(CommentEntity::getArticleId, articleId)
                .list();
    }

    /**
     * ????????????????????????
     *
     * @param email ?????????????????????
     * @return ????????? qq ?????????????????? qq ??????????????? qq ????????? qq ????????????????????????????????? MD5 ?????? gravatar ????????????
     */
    private String generateAvatar(String email) throws URISyntaxException, IOException {
        email = email.toLowerCase();
        int lastIndexOfAt = email.lastIndexOf("@");
        String emailUsername = email.substring(0, lastIndexOfAt);
        String emailDomain = email.substring(lastIndexOfAt);
        int indexOfDot = emailUsername.length() + emailDomain.indexOf(".");
        String domainName = email.substring(lastIndexOfAt + 1, indexOfDot);
        if (Objects.equals(domainName, "qq") && emailUsername.matches(ALL_NUMBER_REGEX)) {
            return this.getQQAvatar(emailUsername);
        }
        return this.getGravatarUrl(email);
    }

    /**
     * ?????? QQ ??????
     *
     * @param qqNum QQ ???
     * @return QQ ????????????
     */
    private String getQQAvatar(String qqNum) throws URISyntaxException, IOException {
        HttpClientResult httpClientResult = httpClientUtil.doGet(systemConfigService.getConfigValue(SYSTEM_QQ_NUM_EXCHANGE_K_URL, String.class)
                .replace("{qq}", qqNum));
        String content = httpClientResult.getContent();
        String k = content.substring(content.indexOf("&k=") + "&k=".length(), content.indexOf("&s="));
        return systemConfigService.getConfigValue(SYSTEM_QQ_AVATAR_REQUEST_URL, String.class)
                .replace("{k}", k);
    }

    /**
     * ?????? gravatar ??????
     *
     * @param email ????????????
     * @return gravatar ????????????
     */
    private String getGravatarUrl(String email) {
        try {
            return systemConfigService.getConfigValue(SYSTEM_GRAVATAR_REQUEST_URL, String.class)
                    .replace("{hash}", MD5Util.md5Hex(email))
                    .replace("{d}", systemConfigService.getConfigValue(SYSTEM_GRAVATAR_DEFAULT_AVATAR_PARAM, String.class));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new SystemInternalException("MD5 ????????????", e);
        }
    }

    /**
     * ??????????????????
     *
     * @param commentEntity  {@link CommentEntity}
     * @param commentReplyTo ???????????????????????????
     * @return {@link CommentItemVO}
     */
    private CommentItemVO buildCommentItem(CommentEntity commentEntity, CommentReplyToVO commentReplyTo) {
        CommentItemVO commentItem = new CommentItemVO();
        commentItem.setId(commentEntity.getId());
        String content = commentEntity.getContent();
        commentItem.setContent(Objects.isNull(commentReplyTo)
                ? content
                : String.format("<a href=#comment-%s>@%s</a>%s", commentReplyTo.getCommentId(), commentReplyTo.getNickname(), content));

        CommentTimeVO commentTime = new CommentTimeVO();
        LocalDateTime createTime = commentEntity.getCreateTime();
        commentTime.setFormattedTime(DateUtil.formatLocalDateTime(createTime, FORMAT_YEAR_TO_SECOND));
        commentTime.setStandardTime(createTime.toString());
        commentTime.setBeautifyTime(DateUtil.beautifyTime(createTime));
        commentItem.setTime(commentTime);

        CommentUserVO commentUser = new CommentUserVO();
        commentUser.setAvatar(commentEntity.getAvatar());
        commentUser.setNickname(commentEntity.getNickname());
        commentUser.setWebsite(commentEntity.getWebsite());
        commentItem.setUser(commentUser);

        return commentItem;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param commentEntityList ?????????????????????
     * @param parentId          ???????????? id
     * @return ?????????????????????????????????????????????
     */
    private List<CommentItemVO> findChildCommentList(List<CommentEntity> commentEntityList, Long parentId) {
        AssertUtil.notNull(parentId, "parentId ????????????");
        List<CommentItemVO> childCommentList = new ArrayList<>(4);
        // ?????????????????????
        for (CommentEntity commentEntity : commentEntityList) {
            Long replyId = commentEntity.getReplyId();
            if (Objects.equals(commentEntity.getParentId(), parentId) && !Objects.equals(replyId, 0L)) {

                CommentReplyToVO commentReplyTo = new CommentReplyToVO();
                commentReplyTo.setCommentId(replyId);
                Optional<CommentEntity> replyToCommentOpt = commentEntityList.stream()
                        .filter(comment -> Objects.equals(comment.getId(), replyId))
                        .findFirst();
                commentReplyTo.setNickname(replyToCommentOpt.isPresent() ? replyToCommentOpt.get().getNickname() : "????????????");
                CommentItemVO commentItem = this.buildCommentItem(commentEntity, commentReplyTo);
                childCommentList.add(commentItem);
            }
        }
        // ????????????????????????????????????
        Collections.sort(childCommentList);
        return childCommentList;
    }
}
