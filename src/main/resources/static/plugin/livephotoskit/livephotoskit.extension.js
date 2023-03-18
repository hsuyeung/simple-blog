'use strict';
// live photo 照片的最大宽度，这个宽度展示效果要好些
const livePhotoMaxWidth = 360
// 评论 id，用于当页面大小发生变化时不知道需要去更新哪个评论的预览区的 live photo
let commentId = 0

/**
 * 页面尺寸发生变化时调用该方法修改文章内的播放器尺寸
 */
function updateArticleLivePhotoPlayer() {
  const containerWidth = document.querySelector(".container").clientWidth
  const els = document.querySelectorAll(".container article .livePhotoContainer");
  for (let i = 0; i < els.length; i++) {
    _updateArticleLivePhotoPlayerSize(els[i], containerWidth, LivePhotosKit.augmentElementAsPlayer(els[i]))
  }
}

/**
 * 初始化页面时调用该方法设置文章内的播放器尺寸
 */
function initArticleLivePhotoPlayer() {
  const containerWidth = document.querySelector(".container").clientWidth
  const els = document.querySelectorAll(".container article .livePhotoContainer");
  for (let i = 0; i < els.length; i++) {
    const liveContainer = els[i];
    const player = LivePhotosKit.augmentElementAsPlayer(liveContainer);
    player.addEventListener('photoload', (ev) => {
      _updateArticleLivePhotoPlayerSize(liveContainer, containerWidth, player);
    })
  }
}

/**
 * 更新文章内的播放器尺寸
 * @param livePhotoContainer 播放器节点
 * @param containerWidth 外层 div.container 节点的宽度
 * @param player 播放器
 * @private
 */
function _updateArticleLivePhotoPlayerSize(livePhotoContainer, containerWidth, player) {
  const livePhotoRawWidth = livePhotoContainer.getAttribute("data-photo-width");
  const livePhotoRawHeight = livePhotoContainer.getAttribute("data-photo-height");
  const livePhotoRatio = livePhotoRawWidth / livePhotoRawHeight
  // 数值来源于 common.css
  const paddingLeftOrRight = screen.width < 700 ? 15 : 55
  const calcWidth = containerWidth - paddingLeftOrRight * 2;
  let livePhotoShowWidth = calcWidth > livePhotoMaxWidth ? livePhotoMaxWidth : calcWidth;
  player.updateSize(livePhotoShowWidth, livePhotoShowWidth / livePhotoRatio)
}

/**
 * 页面尺寸发生变化时调用该方法修改评论区预览的播放器尺寸
 */
function updateCommentPreviewLivePhotoPlayer(recursive = false) {
  const previewTextId = "preview-text-" + (recursive ? commentId : 0);
  const previewTextWidth = document.getElementById(previewTextId).clientWidth
  const els = document.querySelectorAll(`#${previewTextId} .livePhotoContainer`);
  for (let i = 0; i < els.length; i++) {
    _updateCommentPreviewLivePhotoPlayerSize(els[i], previewTextWidth, LivePhotosKit.augmentElementAsPlayer(els[i]))
  }
  if (recursive) {
    // 同时更新下最顶部评论区预览的播放器尺寸
    updateCommentPreviewLivePhotoPlayer(false)
  }
}

/**
 * 初始化页面时调用该方法设置评论区预览的播放器尺寸
 * @param replyCommentId 回复的评论 id
 */
function initCommentPreviewLivePhotoPlayer(replyCommentId) {
  if (replyCommentId >= 0) {
    commentId = replyCommentId
  }
  const previewTextId = "preview-text-" + commentId;
  const previewTextWidth = document.getElementById(previewTextId).clientWidth
  const els = document.querySelectorAll(`#${previewTextId} .livePhotoContainer`);
  for (let i = 0; i < els.length; i++) {
    const liveContainer = els[i];
    const player = LivePhotosKit.augmentElementAsPlayer(liveContainer);
    player.addEventListener('photoload', (ev) => {
      _updateCommentPreviewLivePhotoPlayerSize(liveContainer, previewTextWidth, player);
    })
  }
}

/**
 * 更新评论区预览的播放器尺寸
 * @param livePhotoContainer 播放器节点
 * @param previewTextWidth 预览文本节点的宽度
 * @param player 播放器
 * @private
 */
function _updateCommentPreviewLivePhotoPlayerSize(livePhotoContainer, previewTextWidth, player) {
  const livePhotoRawWidth = livePhotoContainer.getAttribute("data-photo-width");
  const livePhotoRawHeight = livePhotoContainer.getAttribute("data-photo-height");
  const livePhotoRatio = livePhotoRawWidth / livePhotoRawHeight
  const livePhotoShowWidth = previewTextWidth > livePhotoMaxWidth ? livePhotoMaxWidth : previewTextWidth;
  player.updateSize(livePhotoShowWidth, livePhotoShowWidth / livePhotoRatio)
}

/**
 * 页面尺寸发生变化时调用该方法修改评论区的播放器尺寸
 */
function updateCommentLivePhotoPlayer() {
  const commentContentNodes = document.querySelectorAll(".comment-item .text");
  if (commentContentNodes.length === 0) {
    return
  }
  const commentItemWidth = commentContentNodes[0].clientWidth
  const els = document.querySelectorAll("#comment-root .livePhotoContainer");
  for (let i = 0; i < els.length; i++) {
    _updateCommentLivePhotoPlayerSize(els[i], commentItemWidth, LivePhotosKit.augmentElementAsPlayer(els[i]))
  }
}

/**
 * 初始化页面时调用该方法设置评论区的播放器尺寸
 */
function initCommentLivePhotoPlayer() {
  const commentContentNodes = document.querySelectorAll(".comment-item .text");
  if (commentContentNodes.length === 0) {
    return
  }
  const containerWidth = commentContentNodes[0].clientWidth
  const els = document.querySelectorAll("#comment-root .livePhotoContainer");
  for (let i = 0; i < els.length; i++) {
    const liveContainer = els[i];
    const player = LivePhotosKit.augmentElementAsPlayer(liveContainer);
    player.addEventListener('photoload', (ev) => {
      _updateCommentLivePhotoPlayerSize(liveContainer, containerWidth, player);
    })
  }
}

/**
 * 更新评论区的播放器尺寸
 * @param livePhotoContainer 播放器节点
 * @param commentItemWidth 外层评论节点的宽度
 * @param player 播放器
 * @private
 */
function _updateCommentLivePhotoPlayerSize(livePhotoContainer, commentItemWidth, player) {
  const livePhotoRawWidth = livePhotoContainer.getAttribute("data-photo-width");
  const livePhotoRawHeight = livePhotoContainer.getAttribute("data-photo-height");
  const livePhotoRatio = livePhotoRawWidth / livePhotoRawHeight
  // 数值来源于 comment.css
  const calcWidth = commentItemWidth * 0.9;
  const livePhotoShowWidth = calcWidth > livePhotoMaxWidth ? livePhotoMaxWidth : calcWidth;
  player.updateSize(livePhotoShowWidth, livePhotoShowWidth / livePhotoRatio)
}

