function resetPageNumAndLoadFriendLinkTableData() {
  document.getElementById('friend-link-current-page').textContent = '1'
  loadFriendLinkTableData()
}

function loadFriendLinkTableData(loadPrev = false, loadNext = false, jumpTo = false) {
  clearFriendLinkTable()
  const friendLinkName = document.getElementById('search-friend-link-name')?.value
  const friendLinkUrl = document.getElementById('search-friend-link-url')?.value
  const friendLinkDesc = document.getElementById('search-friend-link-desc')?.value
  const friendLinkGroup = document.getElementById('search-friend-link-group')?.value
  let pageNum
  if (jumpTo) {
    const jumpToVal = parseInt(document.getElementById('friend-link-page-jump-to')?.value)
    if (typeof jumpToVal === 'number' && !isNaN(jumpToVal)) {
      pageNum = jumpToVal
    } else {
      return
    }
  } else {
    pageNum = document.getElementById('friend-link-current-page')?.textContent || 1
  }
  const pageSizeSelectNode = document.getElementById('friend-link-page-size')
  const pageSize = pageSizeSelectNode?.options[pageSizeSelectNode?.selectedIndex]?.value || 10
  const xhr = new XMLHttpRequest()
  xhr.open(
          'POST', '/api/friend/link/actions/page', true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify({
    'pageParam': {
      'pageNum': pageNum,
      'pageSize': pageSize
    },
    'searchParam': {
      'linkName': friendLinkName,
      'linkUrl': friendLinkUrl,
      'linkDesc': friendLinkDesc,
      'linkGroup': friendLinkGroup
    }
  }))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        generateFriendLinkDataTable(resp.data.total, pageNum, pageSize, resp.data.data)
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

function deleteFriendLinkRequest(friendLinkId) {
  const xhr = new XMLHttpRequest()
  xhr.open('DELETE', `/api/friend/link/${friendLinkId}`, true)
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
        loadFriendLinkTableData()
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

function deleteFriendLinkAction(friendLinkId) {
  confirm("确认删除该友链吗？") && deleteFriendLinkRequest(friendLinkId);
}

function generateFriendLinkDataTable(total, pageNum, pageSize, friendLinks) {
  total = parseInt(total)
  pageNum = parseInt(pageNum)
  pageSize = parseInt(pageSize)
  const tableHeadText = `
    <thead>
      <tr>
        <th>序号</th>
        <th>友链名称</th>
        <th>友链链接</th>
        <th>友链头像</th>
        <th>友链描述</th>
        <th>友链分组</th>
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
  if (friendLinks.length <= 0) {
    tableBodyText = `
      <tr>暂无数据</tr>
    `
  } else {
    friendLinks.forEach(friendLink => {
      tableBodyText += `
      <tr>
        <td id=${'index-' + friendLink.id}>${index++}</td>
        <td id=${'friend-link-name-' + friendLink.id}>${friendLink.linkName}</td>
        <td id=${'friend-link-url-' + friendLink.id}><a href='${friendLink.linkUrl}' title='${friendLink.linkUrl}' target='_blank' rel='noopener noreferrer nofollow'>${subAndAppend(friendLink.linkUrl, 20, '...')}</a></td>
        <td id=${'friend-link-avatar-' + friendLink.id}><a href='${friendLink.linkAvatar}' title='${friendLink.linkAvatar}' target='_blank' rel='noopener noreferrer nofollow'>${subAndAppend(friendLink.linkAvatar, 20, '...')}</a></td>
        <td id=${'friend-link-desc-' + friendLink.id} title='${friendLink.linkDesc}'>${subAndAppend(friendLink.linkDesc, 10, '...')}</td>
        <td id=${'friend-link-group-' + friendLink.id}>${friendLink.linkGroup}</td>
        <td id=${'friend-link-create-time-' + friendLink.id}>${friendLink.createTime}</td>
        <td id=${'friend-link-create-by-' + friendLink.id}>${friendLink.createBy}</td>
        <td id=${'friend-link-update-time-' + friendLink.id}>${friendLink.updateTime}</td>
        <td id=${'friend-link-update-by-' + friendLink.id}>${friendLink.updateBy}</td>
        <td>
          <button id=${'delete-friend-link-' + friendLink.id} onclick='deleteFriendLinkAction(${friendLink.id})'>删除</button>
          <button id=${'update-friend-link-' + friendLink.id} onclick='editFriendLinkAction(${friendLink.id}, "${friendLink.linkName}", "${friendLink.linkUrl}", "${friendLink.linkAvatar}", "${friendLink.linkDesc}", "${friendLink.linkGroup}")'>编辑</button>
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
  document.getElementById('friend-link-data-table-body').appendChild(tableNode)

  const totalPage = Math.ceil(total / pageSize)
  document.getElementById('friend-link-total-page').textContent = `共 ${totalPage} 页`
  document.getElementById('friend-link-current-page').textContent = pageNum
  document.getElementById('friend-link-total-size').textContent = `共 ${total} 条`
  document.getElementById('friend-link-prev-page').innerHTML =
          `<button onclick='loadPrevPageFriendLinkData()' ${(pageNum === 1 || total === 0) ? 'disabled' : ''}>上一页</button>`
  document.getElementById('friend-link-page-jump').innerHTML = `
           <label for='friend-link-page-jump-to'>跳转到第</label>
           <input id='friend-link-page-jump-to' onchange='loadFriendLinkTableData(false, false, true)'>页
  `
  document.getElementById('friend-link-next-page').innerHTML =
          `<button onclick='loadNextPageFriendLinkData()' ${(pageNum === totalPage || total === 0) ? 'disabled' : ''}>下一页</button>`
}

function clearFriendLinkTable() {
  document.getElementById('friend-link-data-table-body').innerHTML = ''
}

function loadPrevPageFriendLinkData() {
  // 将当前页 +1
  const pageNumNode = document.getElementById('friend-link-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) - 1}`
  loadFriendLinkTableData(true)
}

function loadNextPageFriendLinkData() {
  // 将当前页 +1
  const pageNumNode = document.getElementById('friend-link-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) + 1}`
  loadFriendLinkTableData(false, true)
}

function editFriendLinkAction(friendLinkId, linkName, linkUrl, linkAvatar, linkDesc, linkGroup) {
  openEditFriendLinkPanel(friendLinkId, linkName, linkUrl, linkAvatar, linkDesc, linkGroup)
}

function openFriendLinkDataTable() {
  document.getElementById('friend-link-data-table').style.display = 'block'
}

function openEditFriendLinkPanel(friendLinkId, linkName, linkUrl, linkAvatar, linkDesc, linkGroup) {
  closeFriendLinkDataTable()
  document.getElementById('edit-friend-link-panel').style.display = 'block'
  document.getElementById('edit-friend-link-id').textContent = friendLinkId
  document.getElementById('edit-friend-link-name').value = linkName
  document.getElementById('edit-friend-link-url').value = linkUrl
  document.getElementById('edit-friend-link-avatar').value = linkAvatar
  document.getElementById('edit-friend-link-desc').value = linkDesc
  document.getElementById('edit-friend-link-group').value = linkGroup
}

function closeEditFriendLinkAction() {
  document.getElementById('edit-friend-link-panel').style.display = 'none'
  openFriendLinkDataTable()
}

function checkEditFriendLinkUrl() {
  const editFriendLinkUrlNode = document.getElementById('edit-friend-link-url')
  if (!editFriendLinkUrlNode) {
    new Message().show({
      type: 'error',
      text: '获取友链链接失败'
    })
    return [false, '']
  }
  let addFriendLinkUrl = editFriendLinkUrlNode.value
  if (!addFriendLinkUrl || addFriendLinkUrl.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入友链链接'
    })
    editFriendLinkUrlNode.focus()
    return [false, '']
  }
  return [true, addFriendLinkUrl.trim()]
}

function checkEditFriendLinkAvatar() {
  const editFriendLinkAvatarNode = document.getElementById('edit-friend-link-avatar')
  if (!editFriendLinkAvatarNode) {
    new Message().show({
      type: 'error',
      text: '获取友链头像失败'
    })
    return [false, '']
  }
  let addFriendLinkAvatar = editFriendLinkAvatarNode.value
  if (!addFriendLinkAvatar || addFriendLinkAvatar.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入友链头像'
    })
    editFriendLinkAvatarNode.focus()
    return [false, '']
  }
  return [true, addFriendLinkAvatar.trim()]
}

function checkEditFriendLinkGroup() {
  const editFriendLinkGroupNode = document.getElementById('edit-friend-link-group')
  if (!editFriendLinkGroupNode) {
    new Message().show({
      type: 'error',
      text: '获取友链分组失败'
    })
    return [false, '']
  }
  let editFriendLinkGroup = editFriendLinkGroupNode.value
  if (!editFriendLinkGroup || editFriendLinkGroup.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入友链分组'
    })
    editFriendLinkGroupNode.focus()
    return [false, '']
  }
  return [true, editFriendLinkGroup.trim()]
}

function confirmEditFriendLinkAction() {
  const editFriendLinkParam = {}
  const [friendLinkIdCheckPassed, friendLinkId] = checkFriendLinkId()
  if (!friendLinkIdCheckPassed) {
    return
  }
  editFriendLinkParam.id = friendLinkId
  const [editFriendLinkNameCheckPassed, editFriendLinkName] = checkEditFriendLinkName()
  if (!editFriendLinkNameCheckPassed) {
    return
  }
  editFriendLinkParam.linkName = editFriendLinkName
  const [editFriendLinkUrlCheckPassed, editFriendLinkUrl] = checkEditFriendLinkUrl()
  if (!editFriendLinkUrlCheckPassed) {
    return
  }
  editFriendLinkParam.linkUrl = editFriendLinkUrl
  const [editFriendLinkAvatarCheckPassed, editFriendLinkAvatar] = checkEditFriendLinkAvatar()
  if (!editFriendLinkAvatarCheckPassed) {
    return
  }
  editFriendLinkParam.linkAvatar = editFriendLinkAvatar
  const [editFriendLinkDescCheckPassed, editFriendLinkDesc] = checkEditFriendLinkDesc()
  if (!editFriendLinkDescCheckPassed) {
    return
  }
  editFriendLinkParam.linkDesc = editFriendLinkDesc
  const [editFriendLinkGroupCheckPassed, editFriendLinkGroup] = checkEditFriendLinkGroup()
  if (!editFriendLinkGroupCheckPassed) {
    return
  }
  editFriendLinkParam.linkGroup = editFriendLinkGroup
  editFriendLinkRequest(editFriendLinkParam)
}

function editFriendLinkRequest(editFriendLinkParam) {
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/friend/link`, true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify(editFriendLinkParam))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '操作成功'
        })
        loadFriendLinkTableData()
        setTimeout(closeEditFriendLinkAction, 2000)
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

function checkFriendLinkId() {
  const editFriendLinkIdNode = document.getElementById('edit-friend-link-id')
  if (!editFriendLinkIdNode) {
    new Message().show({
      type: 'error',
      text: '获取友链 id 失败'
    })
    return [false, '']
  }
  return [true, editFriendLinkIdNode.textContent]
}

function checkEditFriendLinkDesc() {
  const editFriendLinkDescNode = document.getElementById('edit-friend-link-desc')
  if (!editFriendLinkDescNode) {
    new Message().show({
      type: 'error',
      text: '获取友链描述失败'
    })
    return [false, '']
  }
  let addFriendLinkDesc = editFriendLinkDescNode.value
  // 友链描述是可选项
  if (!addFriendLinkDesc || addFriendLinkDesc.trim().length <= 0) {
    return [true, '']
  }
  return [true, addFriendLinkDesc.trim()]
}

function checkEditFriendLinkName() {
  const editFriendLinkNameNode = document.getElementById('edit-friend-link-name')
  if (!editFriendLinkNameNode) {
    new Message().show({
      type: 'error',
      text: '获取友链名字失败'
    })
    return [false, '']
  }
  let editFriendLinkName = editFriendLinkNameNode.value
  if (!editFriendLinkName || editFriendLinkName.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入友链名字'
    })
    editFriendLinkNameNode.focus()
    return [false, '']
  }
  return [true, editFriendLinkName.trim()]
}


function closeFriendLinkDataTable() {
  document.getElementById('friend-link-data-table').style.display = 'none'
}

function addFriendLinkAction() {
  openAddFriendLinkPanel()
}

function openAddFriendLinkPanel() {
  closeFriendLinkDataTable()
  document.getElementById('add-friend-link-panel').style.display = 'block'
}

function closeAddFriendLinkPanel() {
  document.getElementById('add-friend-link-panel').style.display = 'none'
  clearAddFriendLinkInfo()
  openFriendLinkDataTable()
}

function checkAddFriendLinkName() {
  const addFriendLinkNameNode = document.getElementById('add-friend-link-name')
  if (!addFriendLinkNameNode) {
    new Message().show({
      type: 'error',
      text: '获取友链名称失败'
    })
    return [false, '']
  }
  let addFriendLinkName = addFriendLinkNameNode.value
  if (!addFriendLinkName || addFriendLinkName.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入友链名称'
    })
    addFriendLinkNameNode.focus()
    return [false, '']
  }
  return [true, addFriendLinkName.trim()]
}

function checkAddFriendLinkDesc() {
  const addFriendLinkDescNode = document.getElementById('add-friend-link-desc')
  if (!addFriendLinkDescNode) {
    new Message().show({
      type: 'error',
      text: '获取友链描述失败'
    })
    return [false, '']
  }
  let addFriendLinkDesc = addFriendLinkDescNode.value
  // 友链描述是可选项
  if (!addFriendLinkDesc || addFriendLinkDesc.trim().length <= 0) {
    return [true, '']
  }
  return [true, addFriendLinkDesc.trim()]
}

function checkAddFriendLinkGroup() {
  const addFriendLinkGroupNode = document.getElementById('add-friend-link-group')
  if (!addFriendLinkGroupNode) {
    new Message().show({
      type: 'error',
      text: '获取友链分组失败'
    })
    return [false, '']
  }
  let addFriendLinkGroup = addFriendLinkGroupNode.value
  if (!addFriendLinkGroup || addFriendLinkGroup.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入友链分组'
    })
    addFriendLinkGroupNode.focus()
    return [false, '']
  }
  return [true, addFriendLinkGroup.trim()]
}

function addFriendLinkRequest(addFriendLinkParam) {
  const xhr = new XMLHttpRequest()
  xhr.open('POST', `/api/friend/link`, true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify(addFriendLinkParam))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '操作成功'
        })
        loadFriendLinkTableData()
        setTimeout(closeAddFriendLinkPanel, 2000)
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

function checkAddFriendLinkUrl() {
  const addFriendLinkUrlNode = document.getElementById('add-friend-link-url')
  if (!addFriendLinkUrlNode) {
    new Message().show({
      type: 'error',
      text: '获取友链链接失败'
    })
    return [false, '']
  }
  let addFriendLinkUrl = addFriendLinkUrlNode.value
  if (!addFriendLinkUrl || addFriendLinkUrl.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入友链链接'
    })
    addFriendLinkUrlNode.focus()
    return [false, '']
  }
  return [true, addFriendLinkUrl.trim()]
}

function checkAddFriendLinkAvatar() {
  const addFriendLinkAvatarNode = document.getElementById('add-friend-link-avatar')
  if (!addFriendLinkAvatarNode) {
    new Message().show({
      type: 'error',
      text: '获取友链头像失败'
    })
    return [false, '']
  }
  let addFriendLinkAvatar = addFriendLinkAvatarNode.value
  if (!addFriendLinkAvatar || addFriendLinkAvatar.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入友链头像'
    })
    addFriendLinkAvatarNode.focus()
    return [false, '']
  }
  return [true, addFriendLinkAvatar.trim()]
}

function confirmAddFriendLinkAction() {
  const addFriendLinkParam = {}
  const [addFriendLinkNameCheckPassed, addFriendLinkName] = checkAddFriendLinkName()
  if (!addFriendLinkNameCheckPassed) {
    return
  }
  addFriendLinkParam.linkName = addFriendLinkName
  const [addFriendLinkUrlCheckPassed, addFriendLinkUrl] = checkAddFriendLinkUrl()
  if (!addFriendLinkUrlCheckPassed) {
    return
  }
  addFriendLinkParam.linkUrl = addFriendLinkUrl
  const [addFriendLinkAvatarCheckPassed, addFriendLinkAvatar] = checkAddFriendLinkAvatar()
  if (!addFriendLinkAvatarCheckPassed) {
    return
  }
  addFriendLinkParam.linkAvatar = addFriendLinkAvatar
  const [addFriendLinkDescCheckPassed, addFriendLinkDesc] = checkAddFriendLinkDesc()
  if (!addFriendLinkDescCheckPassed) {
    return
  }
  addFriendLinkParam.linkDesc = addFriendLinkDesc
  const [addFriendLinkGroupCheckPassed, addFriendLinkGroup] = checkAddFriendLinkGroup()
  if (!addFriendLinkGroupCheckPassed) {
    return
  }
  addFriendLinkParam.linkGroup = addFriendLinkGroup
  addFriendLinkRequest(addFriendLinkParam)
}

function clearAddFriendLinkInfo() {
  document.getElementById('add-friend-link-name').value = ''
  document.getElementById('add-friend-link-url').value = ''
  document.getElementById('add-friend-link-avatar').value = ''
  document.getElementById('add-friend-link-desc').value = ''
  document.getElementById('add-friend-link-group').value = ''
}

function refreshFriendLinkAction() {
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/friend/link/refresh/cache`, true)
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send()
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '刷新缓存成功'
        })
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
