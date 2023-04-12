function resetPageNumAndLoadArticleTableData() {
  document.getElementById('article-current-page').textContent = '1'
  loadArticleTableData()
}

function loadArticleTableData(loadPrev = false, loadNext = false, jumpTo = false) {
  clearArticleTable()
  const title = document.getElementById('search-article-title')?.value
  const author = document.getElementById('search-article-author')?.value
  const keywords = document.getElementById('search-article-keywords')?.value
  const desc = document.getElementById('search-article-desc')?.value
  const pin = document.getElementById('search-article-pin')?.value
  const startTimeStr = document.getElementById('search-article-time-start')?.value
  let startTimestamp
  if (startTimeStr) {
    startTimestamp = new Date(startTimeStr).getTime()
  }
  const endTimeStr = document.getElementById('search-article-time-end')?.value
  let endTimestamp
  if (endTimeStr) {
    endTimestamp = new Date(endTimeStr).getTime()
  }
  let pageNum
  if (jumpTo) {
    const jumpToVal = parseInt(document.getElementById('article-page-jump-to')?.value)
    if (typeof jumpToVal === 'number' && !isNaN(jumpToVal)) {
      pageNum = jumpToVal
    } else {
      return
    }
  } else {
    pageNum = document.getElementById('article-current-page')?.textContent || 1
  }
  const pageSizeSelectNode = document.getElementById('article-page-size')
  const pageSize = pageSizeSelectNode?.options[pageSizeSelectNode?.selectedIndex]?.value || 10
  const xhr = new XMLHttpRequest()
  xhr.open(
          'GET',
          '/api/article/page'
          + '?title=' + (title || '')
          + '&author=' + (author || '')
          + '&keywords=' + (keywords || '')
          + '&desc=' + (desc || '')
          + '&pin=' + (pin || '')
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
        generateArticleDataTable(resp.data.total, pageNum, pageSize, resp.data.data)
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

function deleteArticleRequest(aid) {
  const xhr = new XMLHttpRequest()
  xhr.open('DELETE', `/api/article/${aid}`, true)
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
        loadArticleTableData()
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

function deleteArticleAction(aid) {
  confirm('确认删除该文章吗？') && deleteArticleRequest(aid);
}

function generateArticleDataTable(total, pageNum, pageSize, articles) {
  total = parseInt(total)
  pageNum = parseInt(pageNum)
  pageSize = parseInt(pageSize)
  const tableHeadText = `
    <thead>
      <tr>
        <th>序号</th>
        <th>标题</th>
        <th>路由</th>
        <th>作者</th>
        <th>关键词</th>
        <th>描述</th>
        <th>内容</th>
        <th>是否置顶</th>
        <th>评论数</th>
        <th>创建时间</th>
        <th>创建人</th>
        <th>更新时间</th>
        <th>更新人</th>
        <th>操作</th>
      </tr>
    </thead>
  `
  let tableBodyText = ''
  let index = 1
  if (articles.length <= 0) {
    tableBodyText = `
      <tr>暂无数据</tr>
    `
  } else {
    articles.forEach(article => {
      tableBodyText += `
      <tr>
        <td id=${'index-' + article.id}>${index++}</td>
        <td id=${'article-title-' + article.id} title='${article.title}'>${subAndAppend(article.title, 10, '...')}</td>
        <td id=${'article-route-' + article.id} title='${article.route}'>${subAndAppend(article.route, 10, '...')}</td>
        <td id=${'article-author-' + article.id} title='${article.author}'>${subAndAppend(article.author, 10, '...')}</td>
        <td id=${'article-keywords-' + article.id} title='${article.keywords}'>${subAndAppend(article.keywords, 10, '...')}</td>
        <td id=${'article-desc-' + article.id} title='${article.description}'>${subAndAppend(article.description, 10, '...')}</td>
        <td id=${'article-url-' + article.id} title='${article.url}'><a href='${article.url}' target='_blank' rel='noopener noreferrer nofollow'>点击查看文章详情</a></td>
        <td id=${'article-pin-' + article.id}>${article.pin ? "置顶" : "未置顶"}</td>
        <td id=${'article-comment-num-' + article.id} title='${article.url + "#comment-root"}'><a href='${article.url + "#comment-root"}' target='_blank' rel='noopener noreferrer nofollow'>${article.commentNum}</a></td>
        <td id=${'article-create-time-' + article.id}>${article.createTime}</td>
        <td id=${'article-create-by-' + article.id}>${article.createBy}</td>
        <td id=${'article-update-time-' + article.id}>${article.updateTime}</td>
        <td id=${'article-update-by-' + article.id}>${article.updateBy}</td>
        <td>
          <button id=${'delete-article-' + article.id} onclick='deleteArticleAction(${article.id})'>删除</button>
          <button id=${'update-article-' + article.id} onclick='editArticleAction(${article.id}, "${article.title}", "${article.route}", "${article.author}", "${article.keywords}", "${article.description}", ${article.pin})'>编辑</button>
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
  document.getElementById('article-data-table-body').appendChild(tableNode)

  const totalPage = Math.ceil(total / pageSize)
  document.getElementById('article-total-page').textContent = `共 ${totalPage} 页`
  document.getElementById('article-current-page').textContent = pageNum
  document.getElementById('article-total-size').textContent = `共 ${total} 条`
  document.getElementById('article-prev-page').innerHTML =
          `<button onclick='loadPrevPageArticleData()' ${(pageNum === 1 || total === 0) ? 'disabled' : ''}>上一页</button>`
  document.getElementById('article-page-jump').innerHTML = `
           <label for='article-page-jump-to'>跳转到第</label>
           <input id='article-page-jump-to' onchange='loadArticleTableData(false, false, true)'>页
  `
  document.getElementById('article-next-page').innerHTML =
          `<button onclick='loadNextPageArticleData()' ${(pageNum === totalPage || total === 0) ? 'disabled' : ''}>下一页</button>`
}

function clearArticleTable() {
  document.getElementById('article-data-table-body').innerHTML = ''
}

function loadPrevPageArticleData() {
  // 将当前页 +1
  const pageNumNode = document.getElementById('article-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) - 1}`
  loadArticleTableData(true)
}

function loadNextPageArticleData() {
  // 将当前页 +1
  const pageNumNode = document.getElementById('article-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) + 1}`
  loadArticleTableData(false, true)
}

function editArticleAction(aid, title, route, author, keywords, description, pin) {
  closeAdminPanel()
  loadArticleContent(aid)
  openEditArticlePanel();
  document.getElementById('edit-article-id').textContent = aid
  document.getElementById('edit-article-title').value = title
  document.getElementById('edit-article-route').value = route
  document.getElementById('edit-article-author').value = author
  document.getElementById('edit-article-keywords').value = keywords
  document.getElementById('edit-article-desc').value = description
  document.getElementById('edit-article-pin').value = pin ? 1 : 0
}

function loadArticleContent(aid) {
  const xhr = new XMLHttpRequest()
  xhr.open('GET', `/api/article/${aid}/md/content`, true)
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send()
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        document.getElementById('edit-article-content').value = resp.data
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

function openEditArticlePanel() {
  document.getElementById('edit-article-panel').style.display = 'block'
}

function closeEditArticlePanelAction() {
  clearEditArticleInfo()
  closeEditArticlePanel()
  openAdminPanel()
}

function closeEditArticlePanel() {
  document.getElementById('edit-article-panel').style.display = 'none'
}

function clearEditContentAction() {
  document.getElementById('edit-article-content').value = ''
}

function checkEditArticleRoute() {
  const editArticleRouteNode = document.getElementById('edit-article-route')
  if (!editArticleRouteNode) {
    new Message().show({
      type: 'error',
      text: '获取文章路由失败'
    })
    return [false, '']
  }
  let addArticleRoute = editArticleRouteNode.value
  if (!addArticleRoute || addArticleRoute.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入文章路由'
    })
    editArticleRouteNode.focus()
    return [false, '']
  }
  return [true, addArticleRoute.trim()]
}

function checkEditArticleAuthor() {
  const editArticleAuthorNode = document.getElementById('edit-article-author')
  if (!editArticleAuthorNode) {
    new Message().show({
      type: 'error',
      text: '获取文章作者失败'
    })
    return [false, '']
  }
  let editArticleAuthor = editArticleAuthorNode.value
  if (!editArticleAuthor || editArticleAuthor.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入文章作者'
    })
    editArticleAuthorNode.focus()
    return [false, '']
  }
  return [true, editArticleAuthor.trim()]
}

function checkEditArticleKeywords() {
  const editArticleKeywordsNode = document.getElementById('edit-article-keywords')
  if (!editArticleKeywordsNode) {
    new Message().show({
      type: 'error',
      text: '获取文章关键词失败'
    })
    return [false, '']
  }
  let editArticleKeywords = editArticleKeywordsNode.value
  if (!editArticleKeywords || editArticleKeywords.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入文章关键词'
    })
    editArticleKeywordsNode.focus()
    return [false, '']
  }
  return [true, editArticleKeywords.trim()]
}

function checkEditArticlePin() {
  const editArticlePinNode = document.getElementById('edit-article-pin')
  if (!editArticlePinNode) {
    new Message().show({
      type: 'error',
      text: '获取文章是否置顶失败'
    })
    return [false, '']
  }
  let editArticlePin = editArticlePinNode.value
  if (!editArticlePin || editArticlePin.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请选择文章是否置顶'
    })
    editArticlePinNode.focus()
    return [false, '']
  }
  return [true, editArticlePin.trim()]
}

function checkEditArticleContent() {
  const editArticleContentNode = document.getElementById('edit-article-content')
  if (!editArticleContentNode) {
    new Message().show({
      type: 'error',
      text: '获取文章内容失败'
    })
    return [false, '']
  }
  let editArticleContent = editArticleContentNode.value
  if (!editArticleContent || editArticleContent.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入文章内容'
    })
    editArticleContentNode.focus()
    return [false, '']
  }
  return [true, editArticleContent.trim()]
}

function confirmEditArticleAction() {
  const editArticleParam = {}
  const [articleIdCheckPassed, articleId] = checkArticleId()
  if (!articleIdCheckPassed) {
    return
  }
  editArticleParam.id = articleId
  const [editArticleTitleCheckPassed, editArticleTitle] = checkEditArticleTitle()
  if (!editArticleTitleCheckPassed) {
    return
  }
  editArticleParam.title = editArticleTitle
  const [editArticleRouteCheckPassed, editArticleRoute] = checkEditArticleRoute()
  if (!editArticleRouteCheckPassed) {
    return
  }
  editArticleParam.route = editArticleRoute
  const [editArticleAuthorCheckPassed, editArticleAuthor] = checkEditArticleAuthor()
  if (!editArticleAuthorCheckPassed) {
    return
  }
  editArticleParam.author = editArticleAuthor
  const [editArticleKeywordsCheckPassed, editArticleKeywords] = checkEditArticleKeywords()
  if (!editArticleKeywordsCheckPassed) {
    return
  }
  editArticleParam.keywords = editArticleKeywords
  const [editArticleDescCheckPassed, editArticleDesc] = checkEditArticleDesc()
  if (!editArticleDescCheckPassed) {
    return
  }
  editArticleParam.description = editArticleDesc
  const [editArticlePinCheckPassed, editArticlePin] = checkEditArticlePin()
  if (!editArticlePinCheckPassed) {
    return
  }
  editArticleParam.pin = editArticlePin === '1'
  const [editArticleContentCheckPassed, editArticleContent] = checkEditArticleContent()
  if (!editArticleContentCheckPassed) {
    return
  }
  editArticleParam.mdContent = editArticleContent
  editArticleParam.htmlContent = DOMPurify.sanitize(marked.parse(document.getElementById('edit-article-content').value))
  editArticleRequest(editArticleParam)
}

function editArticleRequest(editArticleParam) {
  const xhr = new XMLHttpRequest()
  xhr.open('POST', `/api/article`, true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify(editArticleParam))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '操作成功'
        })
        loadArticleTableData()
        setTimeout(closeEditArticlePanelAction, 2000)
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

function checkArticleId() {
  const editArticleIdNode = document.getElementById('edit-article-id')
  if (!editArticleIdNode) {
    new Message().show({
      type: 'error',
      text: '获取文章 id 失败'
    })
    return [false, '']
  }
  return [true, editArticleIdNode.textContent]
}

function checkEditArticleDesc() {
  const editArticleDescNode = document.getElementById('edit-article-desc')
  if (!editArticleDescNode) {
    new Message().show({
      type: 'error',
      text: '获取文章描述失败'
    })
    return [false, '']
  }
  let addArticleDesc = editArticleDescNode.value
  if (!addArticleDesc || addArticleDesc.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入文章描述'
    })
    return [false, '']
  }
  return [true, addArticleDesc.trim()]
}

function checkEditArticleTitle() {
  const editArticleTitleNode = document.getElementById('edit-article-title')
  if (!editArticleTitleNode) {
    new Message().show({
      type: 'error',
      text: '获取文章标题失败'
    })
    return [false, '']
  }
  let editArticleTitle = editArticleTitleNode.value
  if (!editArticleTitle || editArticleTitle.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入文章标题'
    })
    editArticleTitleNode.focus()
    return [false, '']
  }
  return [true, editArticleTitle.trim()]
}

function openAddArticlePanel() {
  document.getElementById('add-article-panel').style.display = 'block'
}

function addArticleAction() {
  closeAdminPanel()
  openAddArticlePanel()
}

function closeAddArticlePanel() {
  document.getElementById('add-article-panel').style.display = 'none'
}

function closeAddArticlePanelAction() {
  closeAddArticlePanel();
  clearAddArticleInfo()
  openAdminPanel()
}

function checkAddArticleTitle() {
  const addArticleTitleNode = document.getElementById('add-article-title')
  if (!addArticleTitleNode) {
    new Message().show({
      type: 'error',
      text: '获取文章标题失败'
    })
    return [false, '']
  }
  let addArticleTitle = addArticleTitleNode.value
  if (!addArticleTitle || addArticleTitle.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入文章标题'
    })
    addArticleTitleNode.focus()
    return [false, '']
  }
  return [true, addArticleTitle.trim()]
}

function checkAddArticleDesc() {
  const addArticleDescNode = document.getElementById('add-article-desc')
  if (!addArticleDescNode) {
    new Message().show({
      type: 'error',
      text: '获取文章描述失败'
    })
    return [false, '']
  }
  let addArticleDesc = addArticleDescNode.value
  if (!addArticleDesc || addArticleDesc.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入文章描述'
    })
    return [false, '']
  }
  return [true, addArticleDesc.trim()]
}

function addArticleRequest(addArticleParam) {
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/article`, true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify(addArticleParam))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '操作成功'
        })
        loadArticleTableData()
        setTimeout(closeAddArticlePanelAction, 2000)
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

function checkAddArticleRoute() {
  const addArticleRouteNode = document.getElementById('add-article-route')
  if (!addArticleRouteNode) {
    new Message().show({
      type: 'error',
      text: '获取文章路由失败'
    })
    return [false, '']
  }
  let addArticleRoute = addArticleRouteNode.value
  if (!addArticleRoute || addArticleRoute.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入文章路由'
    })
    addArticleRouteNode.focus()
    return [false, '']
  }
  return [true, addArticleRoute.trim()]
}

function checkAddArticleAuthor() {
  const addArticleAuthorNode = document.getElementById('add-article-author')
  if (!addArticleAuthorNode) {
    new Message().show({
      type: 'error',
      text: '获取文章作者失败'
    })
    return [false, '']
  }
  let addArticleAuthor = addArticleAuthorNode.value
  if (!addArticleAuthor || addArticleAuthor.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入文章作者'
    })
    addArticleAuthorNode.focus()
    return [false, '']
  }
  return [true, addArticleAuthor.trim()]
}

function checkAddArticleKeywords() {
  const addArticleKeywordsNode = document.getElementById('add-article-keywords')
  if (!addArticleKeywordsNode) {
    new Message().show({
      type: 'error',
      text: '获取文章关键词失败'
    })
    return [false, '']
  }
  let addArticleKeywords = addArticleKeywordsNode.value
  if (!addArticleKeywords || addArticleKeywords.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入文章关键词'
    })
    addArticleKeywordsNode.focus()
    return [false, '']
  }
  return [true, addArticleKeywords.trim()]
}

function checkAddArticlePin() {
  const addArticlePinNode = document.getElementById('add-article-pin')
  if (!addArticlePinNode) {
    new Message().show({
      type: 'error',
      text: '获取文章是否置顶失败'
    })
    return [false, '']
  }
  let addArticlePin = addArticlePinNode.value
  if (!addArticlePin || addArticlePin.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请选择文章是否置顶'
    })
    addArticlePinNode.focus()
    return [false, '']
  }
  return [true, addArticlePin.trim()]
}

function checkAddArticleContent() {
  const addArticleContentNode = document.getElementById('add-article-content')
  if (!addArticleContentNode) {
    new Message().show({
      type: 'error',
      text: '获取文章内容失败'
    })
    return [false, '']
  }
  let addArticleContent = addArticleContentNode.value
  if (!addArticleContent || addArticleContent.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入文章内容'
    })
    addArticleContentNode.focus()
    return [false, '']
  }
  return [true, addArticleContent.trim()]
}

function confirmAddArticleAction() {
  const addArticleParam = {}
  const [addArticleTitleCheckPassed, addArticleTitle] = checkAddArticleTitle()
  if (!addArticleTitleCheckPassed) {
    return
  }
  addArticleParam.title = addArticleTitle
  const [addArticleRouteCheckPassed, addArticleRoute] = checkAddArticleRoute()
  if (!addArticleRouteCheckPassed) {
    return
  }
  addArticleParam.route = addArticleRoute
  const [addArticleAuthorCheckPassed, addArticleAuthor] = checkAddArticleAuthor()
  if (!addArticleAuthorCheckPassed) {
    return
  }
  addArticleParam.author = addArticleAuthor
  const [addArticleKeywordsCheckPassed, addArticleKeywords] = checkAddArticleKeywords()
  if (!addArticleKeywordsCheckPassed) {
    return
  }
  addArticleParam.keywords = addArticleKeywords
  const [addArticleDescCheckPassed, addArticleDesc] = checkAddArticleDesc()
  if (!addArticleDescCheckPassed) {
    return
  }
  addArticleParam.description = addArticleDesc
  const [addArticlePinCheckPassed, addArticlePin] = checkAddArticlePin()
  if (!addArticlePinCheckPassed) {
    return
  }
  addArticleParam.pin = addArticlePin === '1'
  const [addArticleContentCheckPassed, addArticleContent] = checkAddArticleContent()
  if (!addArticleContentCheckPassed) {
    return
  }
  addArticleParam.mdContent = addArticleContent
  addArticleParam.htmlContent = DOMPurify.sanitize(marked.parse(document.getElementById('add-article-content').value))
  addArticleRequest(addArticleParam)
}

function clearEditArticleInfo() {
  document.getElementById('edit-article-id').textContent = ''
  document.getElementById('edit-article-title').value = ''
  document.getElementById('edit-article-route').value = ''
  document.getElementById('edit-article-author').value = ''
  document.getElementById('edit-article-keywords').value = ''
  document.getElementById('edit-article-desc').value = ''
  document.getElementById('edit-article-pin').value = ''
  document.getElementById('edit-article-content').value = ''
}

function clearAddArticleInfo() {
  document.getElementById('add-article-title').value = ''
  document.getElementById('add-article-route').value = ''
  document.getElementById('add-article-author').value = ''
  document.getElementById('add-article-keywords').value = ''
  document.getElementById('add-article-desc').value = ''
  document.getElementById('add-article-pin').value = '0'
  document.getElementById('add-article-content').value = ''
}

function openPreviewArticlePanel() {
  document.getElementById('preview-article-panel').style.display = 'block'
}

function previewAddArticleAction() {
  closeAddArticlePanel()
  openPreviewArticlePanel()
  document.getElementById('preview-article-text').innerHTML = DOMPurify.sanitize(marked.parse(document.getElementById('add-article-content').value))
  mediumZoom('img.zoom-img', {background: '#faf5e3'})
  hljs.configure({
    ignoreUnescapedHTML: true
  })
  hljs.highlightAll()
  lazyLoad({
    lazyAttr: 'data-src',
    loadType: 'src',
    errorPath: '/img/err_img.jpg'
  }).observeLazyLoadNode()
  initArticleLivePhotoPlayer()
}

function previewEditArticleAction() {
  closeEditArticlePanel()
  openPreviewArticlePanel()
  document.getElementById('preview-article-text').innerHTML = DOMPurify.sanitize(marked.parse(document.getElementById('edit-article-content').value))
  hljs.configure({
    ignoreUnescapedHTML: true
  })
  hljs.highlightAll()
  mediumZoom('img.zoom-img', {background: '#faf5e3'})
  lazyLoad({
    lazyAttr: 'data-src',
    loadType: 'src',
    errorPath: '/img/err_img.jpg'
  }).observeLazyLoadNode()
  initArticleLivePhotoPlayer()
}

function closePreviewArticlePanel() {
  document.getElementById('preview-article-panel').style.display = 'none'
}

function closePreviewArticleAction() {
  closePreviewArticlePanel()
  const isEditOperation = !!document.getElementById('edit-article-id').textContent
  isEditOperation ? openEditArticlePanel() : openAddArticlePanel()
}

function saveDraftAction() {
  saveDraft()
}

function saveDraft() {
  const content = document.getElementById('add-article-content').value;
  if (content) {
    localStorage.setItem('articleDraft', content)
    new Message().show({
      type: 'info',
      text: '保存草稿成功'
    })
  }
}

function loadDraftContentAction() {
  const draftContent = localStorage.getItem('articleDraft')
  if (!draftContent) {
    if (!confirm('草稿箱为空，确定要覆盖当前输入内容吗？')) {
      return
    }
  }
  document.getElementById('add-article-content').value = draftContent
}

function clearAddContentAction() {
  saveDraft()
  document.getElementById('add-article-content').value = ''
}

function clearDraftContentAction() {
  // 稍微延时一下再删，避免和内容输入框的 onchange 事件发生顺序不确定导致草稿箱未清空的问题
  setTimeout(() => localStorage.removeItem('articleDraft'), 100)
}

function addArticleUploadFileAction() {
  const input = document.getElementById('file-upload-input');
  if (input.files.length === 0) {
    new Message().show({
      type: 'warning',
      text: '未选择文件'
    })
    return;
  }
  const formData = new FormData();
  const file = input.files[0];
  formData.append('file', file);

  const xhr = new XMLHttpRequest()
  xhr.open('POST', `/api/file/upload`, true)
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(formData)
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '上传成功'
        })
        const divElement = document.createElement('div')
        const list = document.getElementById('add-article-file-upload-list')
        divElement.innerHTML = `
          <span>${'文件 ' + (list.children.length + 1)}</span>:&nbsp;
          <span>
            <code>${resp.data}</code>
          </span>
        `
        list.appendChild(divElement)
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

function editArticleUploadFileAction() {
  const input = document.getElementById('edit-file-upload-input');
  if (input.files.length === 0) {
    new Message().show({
      type: 'warning',
      text: '未选择文件'
    })
    return;
  }
  const formData = new FormData();
  const file = input.files[0];
  formData.append('file', file);

  const xhr = new XMLHttpRequest()
  xhr.open('POST', `/api/file/upload`, true)
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(formData)
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '上传成功'
        })
        const divElement = document.createElement('div')
        const list = document.getElementById('edit-article-file-upload-list')
        divElement.innerHTML = `
          <span>${'文件 ' + (list.children.length + 1)}</span>:&nbsp;
          <span>
            <code>${resp.data}</code>
          </span>
        `
        list.appendChild(divElement)
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

function clearAddArticleFileUploadList() {
  document.getElementById('add-article-file-upload-list').innerHTML = ''
}

function clearEditArticleFileUploadList() {
  document.getElementById('edit-article-file-upload-list').innerHTML = ''
}
