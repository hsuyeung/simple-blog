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
  xhr.open(
          'GET',
          '/api/comment/page'
          + '?nickname=' + (nickname || '')
          + '&email=' + (email || '')
          + '&website=' + (website || '')
          + '&parentNickname=' + (parentNickname || '')
          + '&replyNickname=' + (replyNickname || '')
          + '&articleId=' + (articleId || '')
          + '&ip=' + (ip || '')
          + '&notification=' + (notification || '')
          + '&startTimestamp=' + (startTimestamp || '')
          + '&endTimestamp=' + (endTimestamp || '')
          + '&pageNum=' + pageNum
          + '&pageSize=' + pageSize,
          true)
  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send()
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
        // ?????????????????????????????????????????????????????????????????????
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
        <th>??????</th>
        <th>??????</th>
        <th>??????</th>
        <th>??????</th>
        <th>??????</th>
        <th>??????</th>
        <th>???????????????</th>
        <th>???????????????</th>
        <th>????????????</th>
        <th>ip</th>
        <th>????????????????????????</th>
        <th>????????????</th>
        <th>??????</th>
      </tr>
    </thead>
  `
  let tableBodyText = ''
  let index = 1
  if (comments.length <= 0) {
    tableBodyText = `
      <tr>????????????</tr>
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
        <td id=${'comment-content-preview-url-' + comment.id} title='${comment.contentPreviewUrl}'><a href='${comment.contentPreviewUrl}' target='_blank' rel='noopener noreferrer nofollow'>????????????????????????</a></td>
        <td id=${'comment-parent-nickname-' + comment.id} title='${comment.parentNickname}'>${subAndAppend(comment.parentNickname, 10, '...')}</td>
        <td id=${'comment-reply-nickname-' + comment.id} title='${comment.replyNickname}'>${subAndAppend(comment.replyNickname, 10, '...')}</td>
        <td id=${'comment-article-title-' + comment.id} title='${comment.articleTitle}'><a href='${comment.articleUrl}' target='_blank' rel='noopener noreferrer nofollow'>${subAndAppend(comment.articleTitle, 10, '...')}</a></td>
        <td id=${'comment-ip-' + comment.id} title='${comment.ip}'>${subAndAppend(comment.ip, 10, '...')}</td>
        <td id=${'comment-notification-' + comment.id}>${comment.notification ? "??????" : "?????????"}</td>
        <td id=${'comment-create-time-' + comment.id}>${comment.createTime}</td>
        <td>
          <button id=${'delete-comment-' + comment.id} onclick='deleteCommentAction(${comment.id})'>??????</button>
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
  document.getElementById('comment-total-page').textContent = `??? ${totalPage} ???`
  document.getElementById('comment-current-page').textContent = pageNum
  document.getElementById('comment-total-size').textContent = `??? ${total} ???`
  document.getElementById('comment-prev-page').innerHTML =
          `<button onclick='loadPrevPageCommentData()' ${(pageNum === 1 || total === 0) ? 'disabled' : ''}>?????????</button>`
  document.getElementById('comment-page-jump').innerHTML = `
           <label for='comment-page-jump-to'>????????????</label>
           <input id='comment-page-jump-to' onchange='loadCommentTableData(false, false, true)'>???
  `
  document.getElementById('comment-next-page').innerHTML =
          `<button onclick='loadNextPageCommentData()' ${(pageNum === totalPage || total === 0) ? 'disabled' : ''}>?????????</button>`
}

function clearCommentTable() {
  document.getElementById('comment-data-table-body').innerHTML = ''
}

function loadPrevPageCommentData() {
  // ???????????? +1
  const pageNumNode = document.getElementById('comment-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) - 1}`
  loadCommentTableData(true)
}

function loadNextPageCommentData() {
  // ???????????? +1
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
    <option value='' selected>??????</option>
    <option value='0'>?????????</option>
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
  confirm("???????????????????????????\n??????????????????????????????????????????????????????") && deleteCommentRequest(commentId);
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
          text: '????????????'
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
