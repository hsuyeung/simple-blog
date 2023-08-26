function resetPageNumAndLoadCommentTableData() {
  document.getElementById('comment-current-page').textContent = '1'
  loadCommentTableData()
}

function loadCommentTableData(loadPrev = false, loadNext = false, jumpTo = false) {
  clearCommentTable()
  const nickname = document.getElementById('search-comment-nickname')?.value
  const email = document.getElementById('search-comment-email')?.value
  const website = document.getElementById('search-comment-website')?.value
  const parentNickname = document.getElementById('search-comment-parent-nickname')?.value
  const replyNickname = document.getElementById('search-comment-reply-nickname')?.value
  const articleSelectionNode = document.getElementById('search-comment-article')
  const articleId = articleSelectionNode?.options[articleSelectionNode?.selectedIndex]?.value
  const ip = document.getElementById('search-comment-ip')?.value
  const notificationSelectionNode = document.getElementById('search-comment-notification')
  const notification = notificationSelectionNode?.options[notificationSelectionNode?.selectedIndex]?.value
  const startTimeStr = document.getElementById('search-comment-time-start')?.value
  let startTimestamp
  if (startTimeStr) {
    startTimestamp = new Date(startTimeStr).getTime()
  }
  const endTimeStr = document.getElementById('search-comment-time-end')?.value
  let endTimestamp
  if (endTimeStr) {
    endTimestamp = new Date(endTimeStr).getTime()
  }
  let pageNum
  if (jumpTo) {
    const jumpToVal = parseInt(document.getElementById('comment-page-jump-to')?.value)
    if (typeof jumpToVal === 'number' && !isNaN(jumpToVal)) {
      pageNum = jumpToVal
    } else {
      return
    }
  } else {
    pageNum = document.getElementById('comment-current-page')?.textContent || 1
  }
  const pageSizeSelectNode = document.getElementById('comment-page-size')
  const pageSize = pageSizeSelectNode?.options[pageSizeSelectNode?.selectedIndex]?.value || 10
  const xhr = new XMLHttpRequest()
  xhr.open('POST', '/api/comment/actions/page', true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify({
    'pageParam': {
      'pageNum': pageNum,
      'pageSize': pageSize
    },
    'searchParam': {
      'nickname': nickname,
      'email': email,
      'website': website,
      'parentNickname': parentNickname,
      'replyNickname': replyNickname,
      'articleId': articleId,
      'ip': ip,
      'notification': notification,
      'startTimestamp': startTimestamp,
      'endTimestamp': endTimestamp
    }
  }))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        generateCommentDataTable(resp.data.total, pageNum, pageSize, resp.data.data)
      } else if (resp.code === 401) {
        new Message().show({
          type: 'error',
          text: resp.msg
        })
        setTimeout(openLoginPanelAction, 2000)
      } else {
        new Message().show({
          type: 'error',
          text: resp.msg
        })
        // 如果是加载上一页或下一页失败，则将当前页码恢复
        if (pageNumNode) {
          if (loadPrev) {
            pageNumNode.textContent = `${parseInt(pageNumNode.textContent) + 1}`
          } else if (loadNext) {
            pageNumNode.textContent = `${parseInt(pageNumNode.textContent) - 1}`
          }
        }
      }
    }
  }
}

function generateCommentDataTable(total, pageNum, pageSize, comments) {
  total = parseInt(total)
  pageNum = parseInt(pageNum)
  pageSize = parseInt(pageSize)
  const tableHeadText = `
    <thead>
      <tr>
        <th>序号</th>
        <th>昵称</th>
        <th>头像</th>
        <th>邮箱</th>
        <th>网站</th>
        <th>内容</th>
        <th>父级评论人</th>
        <th>回复评论人</th>
        <th>文章标题</th>
        <th>ip</th>
        <th>是否接收邮件提醒</th>
        <th>评论时间</th>
        <th>操作</th>
      </tr>
    </thead>
  `
  let tableBodyText = ''
  let index = 1
  if (comments.length <= 0) {
    tableBodyText = `
      <tr>暂无数据</tr>
    `
  } else {
    comments.forEach(comment => {
      tableBodyText += `
      <tr>
        <td id=${'index-' + comment.id}>${index++}</td>
        <td id=${'comment-nickname-' + comment.id} title='${comment.nickname}'>${subAndAppend(comment.nickname, 10, '...')}</td>
        <td id=${'comment-avatar-' + comment.id} title='${comment.avatar}'><a href='${comment.avatar}' target='_blank' rel='noopener noreferrer nofollow'><img class='comment-avatar' src='${comment.avatar}' alt='avatar'></a></td>
        <td id=${'comment-email-' + comment.id} title='${comment.email}'>${subAndAppend(comment.email, 10, '...')}</td>
        <td id=${'comment-website-' + comment.id} title='${comment.website}'><a href=${comment.website} target='_blank' rel='noopener noreferrer nofollow'>${subAndAppend(comment.website, 10, '...')}</a></td>
        <td id=${'comment-content-preview-url-' + comment.id} title='${comment.contentPreviewUrl}'><a href='${comment.contentPreviewUrl}' target='_blank' rel='noopener noreferrer nofollow'>点击查看评论内容</a></td>
        <td id=${'comment-parent-nickname-' + comment.id} title='${comment.parentNickname}'>${subAndAppend(comment.parentNickname, 10, '...')}</td>
        <td id=${'comment-reply-nickname-' + comment.id} title='${comment.replyNickname}'>${subAndAppend(comment.replyNickname, 10, '...')}</td>
        <td id=${'comment-article-title-' + comment.id} title='${comment.articleTitle}'><a href='${comment.articleUrl}' target='_blank' rel='noopener noreferrer nofollow'>${subAndAppend(comment.articleTitle, 10, '...')}</a></td>
        <td id=${'comment-ip-' + comment.id} title='${comment.ip}'>${subAndAppend(comment.ip, 10, '...')}</td>
        <td id=${'comment-notification-' + comment.id}>${comment.notification ? "接收" : "不接收"}</td>
        <td id=${'comment-create-time-' + comment.id}>${comment.createTime}</td>
        <td>
          <button id=${'delete-comment-' + comment.id} onclick='deleteCommentAction(${comment.id})'>删除</button>
        </td>
      </tr>
    `
    })
  }
  const tableNode = document.createElement('table')
  const theadNode = document.createElement('thead')
  theadNode.innerHTML = tableHeadText
  const tbodyNode = document.createElement('tbody')
  tbodyNode.innerHTML = tableBodyText
  tableNode.appendChild(theadNode)
  tableNode.appendChild(tbodyNode)
  document.getElementById('comment-data-table-body').appendChild(tableNode)

  const totalPage = Math.ceil(total / pageSize)
  document.getElementById('comment-total-page').textContent = `共 ${totalPage} 页`
  document.getElementById('comment-current-page').textContent = pageNum
  document.getElementById('comment-total-size').textContent = `共 ${total} 条`
  document.getElementById('comment-prev-page').innerHTML =
          `<button onclick='loadPrevPageCommentData()' ${(pageNum === 1 || total === 0) ? 'disabled' : ''}>上一页</button>`
  document.getElementById('comment-page-jump').innerHTML = `
           <label for='comment-page-jump-to'>跳转到第</label>
           <input id='comment-page-jump-to' onchange='loadCommentTableData(false, false, true)'>页
  `
  document.getElementById('comment-next-page').innerHTML =
          `<button onclick='loadNextPageCommentData()' ${(pageNum === totalPage || total === 0) ? 'disabled' : ''}>下一页</button>`
}

function clearCommentTable() {
  document.getElementById('comment-data-table-body').innerHTML = ''
}

function loadPrevPageCommentData() {
  // 将当前页 +1
  const pageNumNode = document.getElementById('comment-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) - 1}`
  loadCommentTableData(true)
}

function loadNextPageCommentData() {
  // 将当前页 +1
  const pageNumNode = document.getElementById('comment-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) + 1}`
  loadCommentTableData(false, true)
}

function loadArticleTitleList() {
  const xhr = new XMLHttpRequest()
  xhr.open('GET', '/api/article/title/list', true)
  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send()
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        generateCommentArticleOption(resp.data)
      } else if (resp.code === 401) {
        new Message().show({
          type: 'error',
          text: resp.msg
        })
        setTimeout(openLoginPanelAction, 2000)
      } else {
        new Message().show({
          type: 'error',
          text: resp.msg
        })
      }
    }
  }
}

function generateCommentArticleOption(articles) {
  let articleOptionText = `
    <option value='' selected>全部</option>
    <option value='0'>留言板</option>
  `
  articles.forEach(article => {
    articleOptionText += `
      <option value='${article.id}'>${article.title}</option>
    `
  })
  const articleSelectNode = document.createElement('select');
  articleSelectNode.setAttribute('id', 'search-comment-article')
  articleSelectNode.setAttribute('name', 'search-comment-article')
  articleSelectNode.innerHTML = articleOptionText
  document.getElementById('search-comment-article-span').appendChild(articleSelectNode)
}

function deleteCommentAction(commentId) {
  confirm("确认删除该评论吗？\n删除后该评论下的所有子评论也会被删除") && deleteCommentRequest(commentId);
}

function deleteCommentRequest(commentId) {
  const xhr = new XMLHttpRequest()
  xhr.open('DELETE', `/api/comment/${commentId}`, true)
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send()
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '操作成功'
        })
        setTimeout(loadCommentTableData, 2000)
      } else if (resp.code === 401) {
        new Message().show({
          type: 'error',
          text: resp.msg
        })
        setTimeout(openLoginPanelAction, 2000)
      } else {
        new Message().show({
          type: 'error',
          text: resp.msg
        })
      }
    }
  }
}
