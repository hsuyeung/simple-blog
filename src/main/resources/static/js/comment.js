function cleanAllCommentTextAreaHeightRecord() {
  for (let i = 0; i < localStorage.length; i++) {
    const key = localStorage.key(i);
    if (key.startsWith('commentTextareaHeightRecord-')) {
      localStorage.removeItem(key)
    }
  }
}

function setCurrentTextAreaHeight(commentId, height) {
  localStorage.setItem(`${'commentTextareaHeightRecord-' + commentId}`, height)
}

function getCurrentTextAreaHeight(commentId) {
  return localStorage.getItem(`${'commentTextareaHeightRecord-' + commentId}`)
}

function previewAction(replyCommentId) {
  document.getElementById('preview-text-' + replyCommentId).innerHTML = DOMPurify.sanitize(marked.parse(document.getElementById('comment-textarea-' + replyCommentId).value))
  hljs.configure({
    ignoreUnescapedHTML: true
  })
  hljs.highlightAll()
  lazyLoad({
    lazyAttr: 'data-src',
    loadType: 'src',
    errorPath: '/img/err_img.jpg'
  }).observeLazyLoadNode()
  document.querySelector('#preview-textarea-' + replyCommentId).setAttribute('style', 'display: block;')
  document.querySelector('#comment-box-' + replyCommentId + ' > .form-wrapper input[name="edit"]').setAttribute('style', 'display:inline;')
  document.querySelector('#comment-box-' + replyCommentId + ' > .form-wrapper input[name="preview"]').setAttribute('style', 'display: none;')
  const commentTextAreaNode = document.querySelector('#comment-box-' + replyCommentId + ' > .form-wrapper #comment-textarea-' + replyCommentId);
  setCurrentTextAreaHeight(replyCommentId, commentTextAreaNode.style.height)
  commentTextAreaNode.setAttribute('style', 'display: none;')
}

function editAction(replyCommentId) {
  document.querySelector('#preview-textarea-' + replyCommentId).setAttribute('style', 'display: none;')
  document.querySelector('#comment-box-' + replyCommentId + ' > .form-wrapper input[name="edit"]').setAttribute('style', 'display:none;')
  document.querySelector('#comment-box-' + replyCommentId + ' > .form-wrapper input[name="preview"]').setAttribute('style', 'display: inline;')
  const h = getCurrentTextAreaHeight(replyCommentId);
  document.querySelector('#comment-box-' + replyCommentId + ' > .form-wrapper #comment-textarea-' + replyCommentId).setAttribute('style', 'display: block;' + (h ? 'height: ' + h : ''))
}

function submitCommentAction(parentCommentId, replyCommentId) {
  // ??? articleId ?????????????????????????????????????????????????????????????????????????????????
  const articleIdNode = document.getElementById('article-id')
  const articleId = articleIdNode ? articleIdNode.textContent : 0
  const isNotificationChecked = document.getElementById('notification-' + replyCommentId).checked
  const [commentContestCheckPassed, commentHTMLContent] = commentContentCheck(replyCommentId)
  if (!commentContestCheckPassed) {
    return
  }
  const [nicknameContentCheckPassed, nickname] = nicknameContentCheck(replyCommentId)
  if (!nicknameContentCheckPassed) {
    return
  }
  const [emailContentCheckPassed, email] = emailContentCheck(replyCommentId)
  if (!emailContentCheckPassed) {
    return
  }
  const [websiteContentCheckPassed, website] = websiteContentCheck(replyCommentId)
  if (!websiteContentCheckPassed) {
    return
  }
  setUserInfoToLocalStorage(nickname, email, website, isNotificationChecked)
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', '/api/comment', true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.send(JSON.stringify({
    'articleId': articleId || 0,
    'isNotificationChecked': isNotificationChecked || false,
    'parentCommentId': parentCommentId === 0 && replyCommentId !== 0 ? replyCommentId : parentCommentId,
    'replyCommentId': replyCommentId,
    'content': commentHTMLContent,
    'nickname': nickname,
    'email': email,
    'website': website,
  }))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '????????????'
        })
        const indexOf = location.href.indexOf('#')
        setTimeout(() => {
          location.replace(location.href.substring(0, indexOf).concat('#comment-', resp.data))
          location.reload()
        }, 2000)
      } else {
        new Message().show({
          type: 'error',
          text: resp.msg
        })
      }
    }
  }
}

function commentContentCheck(replyCommentId) {
  const commentTextAreaNode = document.getElementById('comment-textarea-' + replyCommentId)
  if (!commentTextAreaNode) {
    new Message().show({
      type: 'error',
      text: '????????????????????????'
    })
    return [false, '']
  }
  const commentMarkdownContent = commentTextAreaNode.value
  const commentHTMLContent = DOMPurify.sanitize(marked.parse(commentMarkdownContent))
  if (!commentHTMLContent || commentHTMLContent.trim().length <= 0) {
    commentTextAreaNode.focus()
    new Message().show({
      type: 'warning',
      text: '?????????????????????'
    })
    return [false, '']
  }
  return [true, commentHTMLContent.trim()]
}

function nicknameContentCheck(replyCommentId) {
  const nicknameNode = document.getElementById('nickname-' + replyCommentId)
  if (!nicknameNode) {
    new Message().show({
      type: 'error',
      text: '??????????????????'
    })
    return [false, '']
  }
  const nickname = nicknameNode.value.trim()
  if (!nickname || nickname.length <= 0) {
    nicknameNode.focus()
    new Message().show({
      type: 'warning',
      text: '???????????????'
    })
    return [false, '']
  }
  if (!NICKNAME_REGEX.test(nickname)) {
    new Message().show({
      type: 'warning',
      text: '????????????????????????????????????????????????'
    })
    return [false, '']
  }
  if (nickname.length > 8) {
    new Message().show({
      type: 'warning',
      text: '???????????????????????? 8 ?????????'
    })
    return [false, '']
  }
  return [true, nickname]
}

function emailContentCheck(replyCommentId) {
  const emailNode = document.getElementById('email-' + replyCommentId)
  if (!emailNode) {
    new Message().show({
      type: 'error',
      text: '????????????????????????'
    })
    return [false, '']
  }
  const email = emailNode.value.trim()
  if (!email || email.length <= 0) {
    emailNode.focus()
    new Message().show({
      type: 'warning',
      text: '?????????????????????'
    })
    return [false, '']
  }
  if (!EMAIL_REGEX.test(email)) {
    new Message().show({
      type: 'warning',
      text: '??????????????????????????????'
    })
    return [false, '']
  }
  if (email.length > 64) {
    new Message().show({
      type: 'warning',
      text: '?????????????????????????????? 64 ?????????'
    })
    return [false, '']
  }
  return [true, email]
}

function websiteContentCheck(replyCommentId) {
  const websiteNode = document.getElementById('website-' + replyCommentId)
  if (!websiteNode) {
    new Message().show({
      type: 'error',
      text: '??????????????????'
    })
    return [false, '']
  }
  let website = websiteNode.value.trim()
  if (!website || website.length <= 0) {
    // ??????????????????
    return [true, '']
  }
  if (website.length > 64) {
    new Message().show({
      type: 'warning',
      textL: '?????????????????????????????? 64 ?????????'
    })
    return [false, '']
  }
  return [true, website]
}

function getUserInfoFromLocalStorage(commentId) {
  const nickname = localStorage.getItem('nickname')
  const email = localStorage.getItem('email')
  const website = localStorage.getItem('website')
  const isNotification = localStorage.getItem('isNotification')

  if (nickname) {
    document.getElementById('nickname-' + commentId).value = nickname
  }
  if (email) {
    document.getElementById('email-' + commentId).value = email
  }
  if (website) {
    document.getElementById('website-' + commentId).value = website
  }
  if (isNotification === true || isNotification === 'true') {
    document.getElementById('notification-' + commentId).checked = true
  }
}

function setUserInfoToLocalStorage(nickname, email, website, isNotification) {
  localStorage.setItem('nickname', nickname)
  localStorage.setItem('email', email)
  localStorage.setItem('website', website)
  localStorage.setItem('isNotification', isNotification)
}

function toggleCommentBox(parentCommentId, replyCommentId) {
  let currentCommentBoxNode = document.getElementById('comment-box-' + replyCommentId)
  const currentReplyNode = document.getElementById('reply-' + replyCommentId)
  const currentCommentItemFooterNode = document.getElementById('comment-item-footer-' + replyCommentId)
  if (currentCommentBoxNode) {
    currentReplyNode.textContent = '??????'
    currentCommentItemFooterNode.removeChild(currentCommentBoxNode)
  } else {
    const allCommentBoxNodes = document.querySelectorAll('[id^=comment-box-]')
    const commentBoxIdPrefixLen = 'comment-box-'.length
    let commentBoxNode
    for (let i = 0; i < allCommentBoxNodes.length; i++) {
      commentBoxNode = allCommentBoxNodes[i]
      const commentIdStr = commentBoxNode.id.substring(commentBoxIdPrefixLen)
      if (commentIdStr !== '0' && commentIdStr !== String(replyCommentId)) {
        document.getElementById('reply-' + commentIdStr).textContent = '??????'
        document.getElementById('comment-item-footer-' + commentIdStr).removeChild(document.getElementById('comment-box-' + commentIdStr))
      }
    }
    const commentBoxText = `
      <div id='${"comment-box-" + replyCommentId}' class='reply'>
        <div class='form-wrapper'>
          <div class='textarea-wrapper'>
            <div>
              <label for='${"comment-textarea-" + replyCommentId}'></label>
              <textarea id='${"comment-textarea-" + replyCommentId}' class='textarea placeholder'></textarea>
            </div>
            <div id='${"preview-textarea-" + replyCommentId}'>
              <div style='border-top: none;'>
                <div class='text-wrapper'>
                  <div id='${"preview-text-" + replyCommentId}'></div>
                </div>
              </div>
            </div>
          </div>
          <section class='comment-form-section'>
            <div class='input-tips gray'>??????<a href='https://markdown.com.cn/basic-syntax/' target='_blank'
                                         rel='noopener noreferrer nofollow'> Markdown </a>??????
            </div>
            <p class='input-wrapper'>
              <input id='${"nickname-" + replyCommentId}' type='text' name='nickname' placeholder='??????????????????' value='' maxlength='8'>
              <label for='${"nickname-" + replyCommentId}'></label>
            </p>
            <p class='input-wrapper'>
              <input id='${"email-" + replyCommentId}' type='email' name='email' placeholder='????????????????????????????????????' value='' maxlength='64'>
              <label for='${"email-" + replyCommentId}>'</label>
            </p>
            <p class='input-wrapper'>
              <input id='${"website-" + replyCommentId}' type='text' name='website' placeholder='??????????????????' value='' maxlength='64'>
              <label for='${"website-" + replyCommentId}>'</label>
            </p>
            <p class='comment-action'>
              <input type='submit' id='${"submit-" + replyCommentId}' value='??????'
                     onclick='submitCommentAction(${parentCommentId}, ${replyCommentId})'>
            </p>
            <p class='comment-action'>
              <input type='button' id='${"preview-" + replyCommentId}' name='preview' value='??????'
                     onclick=previewAction(${replyCommentId})>
            </p>
            <p class='comment-action'>
              <input type='button' id='${"edit-" + replyCommentId}' name='edit' value='??????'
                     onclick=editAction(${replyCommentId})>
            </p>
          </section>
          <section class='notification-section'>
            <input id='${"notification-" + replyCommentId}' type='checkbox' name='notification'>
            <label for='${"notification-" + replyCommentId}'>?????????????????????????????????</label>
          </section>
        </div>
      </div>
    `
    currentCommentBoxNode = document.createElement('div')
    currentCommentBoxNode.innerHTML = commentBoxText
    let childNode
    for (let i = 0; i < currentCommentBoxNode.childNodes.length; i++) {
      childNode = currentCommentBoxNode.childNodes[i]
      if (childNode.id !== `${'comment-box-' + replyCommentId}`) {
        continue
      }
      currentCommentItemFooterNode.appendChild(childNode)
    }
    currentReplyNode.textContent = '??????'
    document.getElementById('comment-textarea-' + replyCommentId).focus()
    getUserInfoFromLocalStorage(replyCommentId)
  }
}
