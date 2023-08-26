function resetPageNumAndLoadPermissionTableData() {
  document.getElementById('permission-current-page').textContent = '1'
  loadPermissionTableData()
}

function loadPermissionTableData(loadPrev = false, loadNext = false, jumpTo = false) {
  clearPermissionTable()
  const permissionPath = document.getElementById('search-permission-path')?.value
  const permissionMethod = document.getElementById('search-permission-method')?.value
  const permissionDesc = document.getElementById('search-permission-desc')?.value
  const enabledSelectionNode = document.getElementById('search-permission-enabled')
  const enabled = enabledSelectionNode?.options[enabledSelectionNode?.selectedIndex]?.value
  let pageNum
  if (jumpTo) {
    const jumpToVal = parseInt(document.getElementById('permission-page-jump-to')?.value)
    if (typeof jumpToVal === 'number' && !isNaN(jumpToVal)) {
      pageNum = jumpToVal
    } else {
      return
    }
  } else {
    pageNum = document.getElementById('permission-current-page')?.textContent || 1
  }
  const pageSizeSelectNode = document.getElementById('permission-page-size')
  const pageSize = pageSizeSelectNode?.options[pageSizeSelectNode?.selectedIndex]?.value || 10
  const xhr = new XMLHttpRequest()
  xhr.open('POST', '/api/permission/actions/page', true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify({
    'pageParam': {
      'pageNum': pageNum,
      'pageSize': pageSize
    },
    'searchParam': {
      'path': permissionPath,
      'method': permissionMethod,
      'permissionDesc': permissionDesc,
      'enabled': enabled === '' ? null : enabled === '1'
    }
  }))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        generatePermissionDataTable(resp.data.total, pageNum, pageSize, resp.data.data)
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

function deletePermissionRequest(pid) {
  const xhr = new XMLHttpRequest()
  xhr.open('DELETE', `/api/permission/${pid}`, true)
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
        loadPermissionTableData()
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

function permissionStatusChangeAction(uid) {
  const enabledCheckBoxNode = document.getElementById(`permission-enabled-checkbox-${uid}`);
  if (!confirm("确认变更该权限的可用状态吗？")) {
    enabledCheckBoxNode.checked = !enabledCheckBoxNode.checked
    return
  }
  const enabled = enabledCheckBoxNode.checked
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/permission/actions/${enabled === true ? 'unlock' : 'lock'}/${uid}`, true)
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
        loadPermissionTableData()
      } else if (resp.code === 401) {
        new Message().show({
          type: 'error',
          text: resp.msg
        })
        // 操作失败将 checkbox 值恢复到改变前的状态
        enabledCheckBoxNode.checked = !enabled
        setTimeout(openLoginPanelAction, 2000)
      } else {
        enabledCheckBoxNode.checked = !enabled
        new Message().show({
          type: 'error',
          text: resp.msg
        })
      }
    }
  }
}

function deletePermissionAction(pid) {
  confirm("确认删除该权限吗？") && deletePermissionRequest(pid);
}

function generatePermissionDataTable(total, pageNum, pageSize, permissions) {
  total = parseInt(total)
  pageNum = parseInt(pageNum)
  pageSize = parseInt(pageSize)
  const tableHeadText = `
    <thead>
      <tr>
        <th>序号</th>
        <th>路径</th>
        <th>请求类型</th>
        <th>描述</th>
        <th>是否可用</th>
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
  if (permissions.length <= 0) {
    tableBodyText = `
      <tr>暂无数据</tr>
    `
  } else {
    permissions.forEach(permission => {
      tableBodyText += `
      <tr>
        <td id=${'index-' + permission.id}>${index++}</td>
        <td id=${'permission-path-' + permission.id}>${permission.path}</td>
        <td id=${'permission-method-' + permission.id}>${permission.method}</td>
        <td id=${'permission-desc-' + permission.id} title='${permission.permissionDesc}'>${subAndAppend(permission.permissionDesc, 10, '...')}</td>
        <td id=${'permission-enabled-' + permission.id}>
          <label for=${'permission-enabled-checkbox-' + permission.id}></label>
          <input onchange='permissionStatusChangeAction(${permission.id})' type='checkbox' name=${'permission-enabled-checkbox-' + permission.id} id=${'permission-enabled-checkbox-' + permission.id} ${permission.enabled ? 'checked' : ''}>
        </td>
        <td id=${'permission-create-time-' + permission.id}>${permission.createTime}</td>
        <td id=${'permission-create-by-' + permission.id}>${permission.createBy}</td>
        <td id=${'permission-update-time-' + permission.id}>${permission.updateTime}</td>
        <td id=${'permission-update-by-' + permission.id}>${permission.updateBy}</td>
        <td>
          <button id=${'delete-permission-' + permission.id} onclick='deletePermissionAction(${permission.id})'>删除</button>
          <button id=${'update-permission-' + permission.id} onclick='editPermissionAction(${permission.id}, "${permission.path}", "${permission.method}", "${permission.permissionDesc}", ${permission.enabled})'>编辑</button>
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
  document.getElementById('permission-data-table-body').appendChild(tableNode)

  const totalPage = Math.ceil(total / pageSize)
  document.getElementById('permission-total-page').textContent = `共 ${totalPage} 页`
  document.getElementById('permission-current-page').textContent = pageNum
  document.getElementById('permission-total-size').textContent = `共 ${total} 条`
  document.getElementById('permission-prev-page').innerHTML =
          `<button onclick='loadPrevPagePermissionData()' ${(pageNum === 1 || total === 0) ? 'disabled' : ''}>上一页</button>`
  document.getElementById('permission-page-jump').innerHTML = `
           <label for='permission-page-jump-to'>跳转到第</label>
           <input id='permission-page-jump-to' onchange='loadPermissionTableData(false, false, true)'>页
  `
  document.getElementById('permission-next-page').innerHTML =
          `<button onclick='loadNextPagePermissionData()' ${(pageNum === totalPage || total === 0) ? 'disabled' : ''}>下一页</button>`
}

function clearPermissionTable() {
  document.getElementById('permission-data-table-body').innerHTML = ''
}

function loadPrevPagePermissionData() {
  // 将当前页 +1
  const pageNumNode = document.getElementById('permission-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) - 1}`
  loadPermissionTableData(true)
}

function loadNextPagePermissionData() {
  // 将当前页 +1
  const pageNumNode = document.getElementById('permission-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) + 1}`
  loadPermissionTableData(false, true)
}

function editPermissionAction(pid, permissionPath, permissionMethod, permissionDesc, enabled) {
  openEditPermissionPanel(pid, permissionPath, permissionMethod, permissionDesc, enabled)
}

function openPermissionDataTable() {
  document.getElementById('permission-data-table').style.display = 'block'
}

function openEditPermissionPanel(pid, permissionPath, permissionMethod, permissionDesc, enabled) {
  closePermissionDataTable()
  document.getElementById('edit-permission-panel').style.display = 'block'
  document.getElementById('edit-permission-id').textContent = pid
  document.getElementById('edit-permission-path').value = permissionPath
  document.getElementById('edit-permission-method').value = permissionMethod
  document.getElementById('edit-permission-desc').value = permissionDesc
  document.getElementById('edit-permission-enabled').checked = enabled
}

function closeEditPermissionAction() {
  document.getElementById('edit-permission-panel').style.display = 'none'
  openPermissionDataTable()
}

function checkEditPermissionMethod() {
  const editPermissionMethodNode = document.getElementById('edit-permission-method')
  if (!editPermissionMethodNode) {
    new Message().show({
      type: 'error',
      text: '获取请求类型失败'
    })
    return [false, '']
  }
  let addPermissionMethod = editPermissionMethodNode.value
  if (!addPermissionMethod || addPermissionMethod.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入请求类型'
    })
    editPermissionMethodNode.focus()
    return [false, '']
  }
  addPermissionMethod = addPermissionMethod.trim()
  if (!HTTP_METHOD_REGEX.test(addPermissionMethod)) {
    new Message().show({
      type: 'warning',
      text: '请求类型目前仅支持：GET、PUT、POST、DELETE，不区分大小写'
    })
    addPermissionMethod.focus()
    return [false, '']
  }
  return [true, addPermissionMethod]
}

function confirmEditPermissionAction() {
  const editPermissionParam = {}
  const [permissionIdCheckPassed, permissionId] = checkPermissionId()
  if (!permissionIdCheckPassed) {
    return
  }
  editPermissionParam.id = permissionId
  const [editPermissionPathCheckPassed, editPermissionPath] = checkEditPermissionPath()
  if (!editPermissionPathCheckPassed) {
    return
  }
  editPermissionParam.path = editPermissionPath
  const [editPermissionMethodCheckPassed, editPermissionMethod] = checkEditPermissionMethod()
  if (!editPermissionMethodCheckPassed) {
    return
  }
  editPermissionParam.method = editPermissionMethod
  const [editPermissionDescCheckPassed, editPermissionDesc] = checkEditPermissionDesc()
  if (!editPermissionDescCheckPassed) {
    return
  }
  editPermissionParam.permissionDesc = editPermissionDesc
  const [editPermissionEnabledCheckPassed, editPermissionEnabled] = checkEditPermissionEnabled()
  if (!editPermissionEnabledCheckPassed) {
    return
  }
  editPermissionParam.enabled = editPermissionEnabled
  editPermissionRequest(editPermissionParam)
}

function editPermissionRequest(editPermissionParam) {
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/permission`, true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify(editPermissionParam))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '操作成功'
        })
        loadPermissionTableData()
        setTimeout(closeEditPermissionAction, 2000)
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

function checkPermissionId() {
  const editPermissionIdNode = document.getElementById('edit-permission-id')
  if (!editPermissionIdNode) {
    new Message().show({
      type: 'error',
      text: '获取权限 id 失败'
    })
    return [false, '']
  }
  return [true, editPermissionIdNode.textContent]
}

function checkEditPermissionEnabled() {
  const editPermissionEnabledNode = document.getElementById('edit-permission-enabled')
  if (!editPermissionEnabledNode) {
    new Message().show({
      type: 'error',
      text: '获取权限是否可用失败'
    })
    return [false, '']
  }
  return [true, editPermissionEnabledNode.checked]
}

function checkEditPermissionDesc() {
  const editPermissionDescNode = document.getElementById('edit-permission-desc')
  if (!editPermissionDescNode) {
    new Message().show({
      type: 'error',
      text: '获取权限描述失败'
    })
    return [false, '']
  }
  let addPermissionDesc = editPermissionDescNode.value
  if (!addPermissionDesc || addPermissionDesc.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入权限描述'
    })
    return [false, '']
  }
  return [true, addPermissionDesc.trim()]
}

function checkEditPermissionPath() {
  const editPermissionPathNode = document.getElementById('edit-permission-path')
  if (!editPermissionPathNode) {
    new Message().show({
      type: 'error',
      text: '获取权限路径失败'
    })
    return [false, '']
  }
  let editPermissionPath = editPermissionPathNode.value
  if (!editPermissionPath || editPermissionPath.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入权限路径'
    })
    editPermissionPathNode.focus()
    return [false, '']
  }
  return [true, editPermissionPath.trim()]
}


function closePermissionDataTable() {
  document.getElementById('permission-data-table').style.display = 'none'
}

function addPermissionAction() {
  openAddPermissionPanel()
}

function openAddPermissionPanel() {
  closePermissionDataTable()
  document.getElementById('add-permission-panel').style.display = 'block'
}

function closeAddPermissionPanel() {
  document.getElementById('add-permission-panel').style.display = 'none'
  clearAddPermissionInfo()
  openPermissionDataTable()
}

function checkAddPermissionPath() {
  const addPermissionPathNode = document.getElementById('add-permission-path')
  if (!addPermissionPathNode) {
    new Message().show({
      type: 'error',
      text: '获取权限路径失败'
    })
    return [false, '']
  }
  let addPermissionPath = addPermissionPathNode.value
  if (!addPermissionPath || addPermissionPath.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入权限路径'
    })
    addPermissionPathNode.focus()
    return [false, '']
  }
  return [true, addPermissionPath.trim()]
}

function checkAddPermissionDesc() {
  const addPermissionDescNode = document.getElementById('add-permission-desc')
  if (!addPermissionDescNode) {
    new Message().show({
      type: 'error',
      text: '获取权限描述失败'
    })
    return [false, '']
  }
  let addPermissionDesc = addPermissionDescNode.value
  if (!addPermissionDesc || addPermissionDesc.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入权限描述'
    })
    return [false, '']
  }
  return [true, addPermissionDesc.trim()]
}

function checkAddPermissionEnabled() {
  const addPermissionEnabledNode = document.getElementById('add-permission-enabled')
  if (!addPermissionEnabledNode) {
    new Message().show({
      type: 'error',
      text: '获取权限是否可用失败'
    })
    return [false, '']
  }
  return [true, addPermissionEnabledNode.checked]
}

function addPermissionRequest(addPermissionParam) {
  const xhr = new XMLHttpRequest()
  xhr.open('POST', `/api/permission`, true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify(addPermissionParam))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '操作成功'
        })
        loadPermissionTableData()
        setTimeout(closeAddPermissionPanel, 2000)
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

function checkAddPermissionMethod() {
  const addPermissionMethodNode = document.getElementById('add-permission-method')
  if (!addPermissionMethodNode) {
    new Message().show({
      type: 'error',
      text: '获取请求类型失败'
    })
    return [false, '']
  }
  let addPermissionMethod = addPermissionMethodNode.value
  if (!addPermissionMethod || addPermissionMethod.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入请求类型'
    })
    addPermissionMethodNode.focus()
    return [false, '']
  }
  addPermissionMethod = addPermissionMethod.trim()
  if (!HTTP_METHOD_REGEX.test(addPermissionMethod)) {
    new Message().show({
      type: 'warning',
      text: '请求类型目前仅支持：GET、PUT、POST、DELETE，不区分大小写'
    })
    addPermissionMethod.focus()
    return [false, '']
  }
  return [true, addPermissionMethod]
}

function confirmAddPermissionAction() {
  const addPermissionParam = {}
  const [addPermissionPathCheckPassed, addPermissionPath] = checkAddPermissionPath()
  if (!addPermissionPathCheckPassed) {
    return
  }
  addPermissionParam.path = addPermissionPath
  const [addPermissionMethodCheckPassed, addPermissionMethod] = checkAddPermissionMethod()
  if (!addPermissionMethodCheckPassed) {
    return
  }
  addPermissionParam.method = addPermissionMethod
  const [addPermissionDescCheckPassed, addPermissionDesc] = checkAddPermissionDesc()
  if (!addPermissionDescCheckPassed) {
    return
  }
  addPermissionParam.permissionDesc = addPermissionDesc
  const [addPermissionEnabledCheckPassed, addPermissionEnabled] = checkAddPermissionEnabled()
  if (!addPermissionEnabledCheckPassed) {
    return
  }
  addPermissionParam.enabled = addPermissionEnabled
  addPermissionRequest(addPermissionParam)
}

function clearAddPermissionInfo() {
  document.getElementById('add-permission-path').value = ''
  document.getElementById('add-permission-method').value = ''
  document.getElementById('add-permission-desc').value = ''
  document.getElementById('add-permission-enabled').checked = false
}
