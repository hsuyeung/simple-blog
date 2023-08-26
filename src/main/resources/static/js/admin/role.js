function resetPageNumAndLoadRoleTableData() {
  document.getElementById('role-current-page').textContent = '1'
  loadRoleTableData()
}

function loadRoleTableData(loadPrev = false, loadNext = false, jumpTo = false) {
  clearRoleTable()
  const roleCode = document.getElementById('search-role-code')?.value
  const enabledSelectionNode = document.getElementById('search-role-enabled')
  const enabled = enabledSelectionNode?.options[enabledSelectionNode?.selectedIndex]?.value
  let pageNum
  if (jumpTo) {
    const jumpToVal = parseInt(document.getElementById('role-page-jump-to')?.value)
    if (typeof jumpToVal === 'number' && !isNaN(jumpToVal)) {
      pageNum = jumpToVal
    } else {
      return
    }
  } else {
    pageNum = document.getElementById('role-current-page')?.textContent || 1
  }
  const pageSizeSelectNode = document.getElementById('role-page-size')
  const pageSize = pageSizeSelectNode?.options[pageSizeSelectNode?.selectedIndex]?.value || 10
  const xhr = new XMLHttpRequest()
  xhr.open('POST', '/api/role/actions/page', true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify({
    'pageParam': {
      'pageNum': pageNum,
      'pageSize': pageSize
    },
    'searchParam': {
      'roleCode': roleCode,
      'enabled': enabled === '' ? null : enabled === '1'
    }
  }))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        generateRoleDataTable(resp.data.total, pageNum, pageSize, resp.data.data)
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

function deleteRoleRequest(rid) {
  const xhr = new XMLHttpRequest()
  xhr.open('DELETE', `/api/role/${rid}`, true)
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
        loadRoleTableData()
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

function roleStatusChangeAction(uid) {
  const enabledCheckBoxNode = document.getElementById(`role-enabled-checkbox-${uid}`);
  if (!confirm("确认变更该角色的可用状态吗？")) {
    enabledCheckBoxNode.checked = !enabledCheckBoxNode.checked
    return
  }
  const enabled = enabledCheckBoxNode.checked
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/role/actions/${enabled === true ? 'unlock' : 'lock'}/${uid}`, true)
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
        loadRoleTableData()
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

function deleteRoleAction(rid) {
  confirm("确认删除该角色吗？\n删除后该角色及其拥有的权限将被删除！") && deleteRoleRequest(rid);
}

function generateRoleDataTable(total, pageNum, pageSize, roles) {
  total = parseInt(total)
  pageNum = parseInt(pageNum)
  pageSize = parseInt(pageSize)
  const tableHeadText = `
    <thead>
      <tr>
        <th>序号</th>
        <th>角色编码</th>
        <th>角色描述</th>
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
  if (roles.length <= 0) {
    tableBodyText = `
      <tr>暂无数据</tr>
    `
  } else {
    roles.forEach(role => {
      tableBodyText += `
      <tr>
        <td id=${'index-' + role.id}>${index++}</td>
        <td id=${'role-code-' + role.id}>${role.roleCode}</td>
        <td id=${'role-desc-' + role.id}>${role.roleDesc}</td>
        <td id=${'role-enabled-' + role.id}>
          <label for=${'role-enabled-checkbox-' + role.id}></label>
          <input onchange='roleStatusChangeAction(${role.id})' type='checkbox' name=${'role-enabled-checkbox-' + role.id} id=${'role-enabled-checkbox-' + role.id} ${role.enabled ? 'checked' : ''}>
        </td>
        <td id=${'role-create-time-' + role.id}>${role.createTime}</td>
        <td id=${'role-create-by-' + role.id}>${role.createBy}</td>
        <td id=${'role-update-time-' + role.id}>${role.updateTime}</td>
        <td id=${'role-update-by-' + role.id}>${role.updateBy}</td>
        <td>
          <button id=${'delete-role-' + role.id} onclick='deleteRoleAction(${role.id})'>删除</button>
          <button id=${'update-role-' + role.id} onclick='editRoleAction(${role.id}, "${role.roleCode}", "${role.roleDesc}", ${role.enabled})'>编辑</button>
          <button id=${'assign-role-permission-' + role.id} onclick='assignPermissionAction(${role.id}, "${role.roleCode}", "${role.roleDesc}", ${role.enabled})'>分配权限</button>
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
  document.getElementById('role-data-table-body').appendChild(tableNode)

  const totalPage = Math.ceil(total / pageSize)
  document.getElementById('role-total-page').textContent = `共 ${totalPage} 页`
  document.getElementById('role-current-page').textContent = pageNum
  document.getElementById('role-total-size').textContent = `共 ${total} 条`
  document.getElementById('role-prev-page').innerHTML =
          `<button onclick='loadPrevPageRoleData()' ${(pageNum === 1 || total === 0) ? 'disabled' : ''}>上一页</button>`
  document.getElementById('role-page-jump').innerHTML = `
           <label for='role-page-jump-to'>跳转到第</label>
           <input id='role-page-jump-to' onchange='loadRoleTableData(false, false, true)'>页
  `
  document.getElementById('role-next-page').innerHTML =
          `<button onclick='loadNextPageRoleData()' ${(pageNum === totalPage || total === 0) ? 'disabled' : ''}>下一页</button>`
}

function clearRoleTable() {
  document.getElementById('role-data-table-body').innerHTML = ''
}

function loadPrevPageRoleData() {
  // 将当前页 +1
  const pageNumNode = document.getElementById('role-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) - 1}`
  loadRoleTableData(true)
}

function loadNextPageRoleData() {
  // 将当前页 +1
  const pageNumNode = document.getElementById('role-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) + 1}`
  loadRoleTableData(false, true)
}

function editRoleAction(uid, roleCode, roleDesc, enabled) {
  openEditRolePanel(uid, roleCode, roleDesc, enabled)
}

function openRoleDataTable() {
  document.getElementById('role-data-table').style.display = 'block'
}


function openEditRolePanel(uid, roleCode, roleDesc, enabled) {
  closeRoleDataTable()
  document.getElementById('edit-role-panel').style.display = 'block'
  document.getElementById('edit-role-id').textContent = uid
  document.getElementById('edit-role-code').value = roleCode
  document.getElementById('edit-role-desc').value = roleDesc
  document.getElementById('edit-role-enabled').checked = enabled
}

function closeEditRoleAction() {
  document.getElementById('edit-role-panel').style.display = 'none'
  openRoleDataTable()
}

function confirmEditRoleAction() {
  const editRoleParam = {}
  const [roleIdCheckPassed, roleId] = checkRoleId()
  if (!roleIdCheckPassed) {
    return
  }
  editRoleParam.id = roleId
  const [editRoleCodeCheckPassed, editRoleCode] = checkEditRoleCode()
  if (!editRoleCodeCheckPassed) {
    return
  }
  editRoleParam.roleCode = editRoleCode
  const [editRoleDescCheckPassed, editRoleDesc] = checkEditRoleDesc()
  if (!editRoleDescCheckPassed) {
    return
  }
  editRoleParam.roleDesc = editRoleDesc
  const [editRoleEnabledCheckPassed, editRoleEnabled] = checkEditRoleEnabled()
  if (!editRoleEnabledCheckPassed) {
    return
  }
  editRoleParam.enabled = editRoleEnabled
  editRoleRequest(editRoleParam)
}

function editRoleRequest(editRoleParam) {
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/role`, true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify(editRoleParam))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '操作成功'
        })
        loadRoleTableData()
        setTimeout(closeEditRoleAction, 2000)
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

function checkRoleId() {
  const editRoleIdNode = document.getElementById('edit-role-id')
  if (!editRoleIdNode) {
    new Message().show({
      type: 'error',
      text: '获取用角色 id 失败'
    })
    return [false, '']
  }
  return [true, editRoleIdNode.textContent]
}

function checkEditRoleEnabled() {
  const editRoleEnabledNode = document.getElementById('edit-role-enabled')
  if (!editRoleEnabledNode) {
    new Message().show({
      type: 'error',
      text: '获取角色是否可用失败'
    })
    return [false, '']
  }
  return [true, editRoleEnabledNode.checked]
}

function checkEditRoleDesc() {
  const editRoleDescNode = document.getElementById('edit-role-desc')
  if (!editRoleDescNode) {
    new Message().show({
      type: 'error',
      text: '获取角色描述失败'
    })
    return [false, '']
  }
  let editRoleDesc = editRoleDescNode.value
  if (!editRoleDesc || editRoleDesc.trim().length <= 0) {
    // 角色描述是可选项
    return [true, '']
  }
  return [true, editRoleDesc.trim()]
}

function checkEditRoleCode() {
  const editRoleCodeNode = document.getElementById('edit-role-code')
  if (!editRoleCodeNode) {
    new Message().show({
      type: 'error',
      text: '获取角色编码失败'
    })
    return [false, '']
  }
  let editRoleCode = editRoleCodeNode.value
  if (!editRoleCode || editRoleCode.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入角色编码'
    })
    editRoleCodeNode.focus()
    return [false, '']
  }
  editRoleCode = editRoleCode.trim()
  if (!ROLE_CODE_REGEX.test(editRoleCode)) {
    new Message().show({
      type: 'warning',
      text: '角色编码非法，仅支持大小写字母、数字、下划线，且不能以数字、下划线开头'
    })
    return [false, '']
  }
  return [true, editRoleCode]
}

function generatePermissionList(rid, permissions) {
  let permissionListHTML = ''
  if (permissions.length <= 0) {
    permissionListHTML = '暂无权限'
  } else {
    permissions.forEach(permission => {
      permissionListHTML += `
        <span>
          <input type='checkbox' name='assign-permission-all-enabled-permission' id=${'assign-permission-all-enabled-permission-' + permission.id} data-id=${permission.id}>
          <label for=${'assign-permission-all-enabled-permission-' + permission.id}>${permission.permissionDesc}</label>
        </span>
      `
    })
  }
  document.getElementById('assign-permission-all-enabled-permissions').innerHTML = permissionListHTML
  // 加载角色拥有的所有权限
  loadRolePermission(rid)
}

function loadAllPermission(rid) {
  const xhr = new XMLHttpRequest()
  xhr.open('GET', `/api/permission/all/enabled`, true)
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send()
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        generatePermissionList(rid, resp.data)
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

/**
 * 渲染角色多选框
 * @param permissions 角色拥有的权限
 */
function renderPermissionCheckBox(permissions) {
  if (permissions.length <= 0) {
    return
  }
  permissions.forEach(permission => {
    document.getElementById(`${'assign-permission-all-enabled-permission-' + permission.id}`).checked = true
  })
}

function loadRolePermission(rid) {
  const xhr = new XMLHttpRequest()
  xhr.open('GET', `/api/role/${rid}/all/enabled/permission`, true)
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send()
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        renderPermissionCheckBox(resp.data)
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

function assignPermissionAction(rid, roleCode, roleDesc, enabled) {
  document.getElementById('assign-permission-role-id').textContent = rid
  document.getElementById('assign-permission-role-code').textContent = roleCode
  document.getElementById('assign-permission-role-desc').textContent = roleDesc
  document.getElementById('assign-permission-role-enabled').textContent = enabled ? '可用' : '不可用'
  // 加载所有的权限列表
  loadAllPermission(rid)
  openAssignPermissionPanel()
}

function closeRoleDataTable() {
  document.getElementById('role-data-table').style.display = 'none'
}

function openAssignPermissionPanel() {
  closeRoleDataTable();
  document.getElementById('assign-permission-panel').style.display = 'block'
}

function closeAssignPermissionPanel() {
  document.getElementById('assign-permission-panel').style.display = 'none'
  document.getElementById('assign-permission-all-enabled-permissions').innerHTML = ''
  openRoleDataTable()
}

/**
 * 给角色授予指定的权限
 * @param rid 角色 id
 * @param pids 权限 id 数组
 */
function assignPermissionRequest(rid, pids) {
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/role/${rid}/permission?pids=${pids.join(',')}`, true)
  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
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
        setTimeout(closeAssignPermissionPanel, 2000)
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

function confirmAssignPermissionAction() {
  // 获取出所有选中的权限
  const allPermissionCheckBoxNode = document.getElementsByName('assign-permission-all-enabled-permission');
  if (!allPermissionCheckBoxNode) {
    return
  }
  let selectedPermissionIds = []
  allPermissionCheckBoxNode.forEach(checkbox => {
    if (checkbox.checked) {
      selectedPermissionIds.push(checkbox.getAttribute('data-id'))
    }
  })
  assignPermissionRequest(document.getElementById('assign-permission-role-id').textContent, selectedPermissionIds)
}

function addRoleAction() {
  openAddRolePanel()
}

function openAddRolePanel() {
  closeRoleDataTable()
  document.getElementById('add-role-panel').style.display = 'block'
}

function closeAddRolePanel() {
  document.getElementById('add-role-panel').style.display = 'none'
  clearAddRoleInfo()
  openRoleDataTable()
}

function checkAddRoleCode() {
  const addRoleCodeNode = document.getElementById('add-role-code')
  if (!addRoleCodeNode) {
    new Message().show({
      type: 'error',
      text: '获取角色编码失败'
    })
    return [false, '']
  }
  let addRoleCode = addRoleCodeNode.value
  if (!addRoleCode || addRoleCode.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '请输入角色编码'
    })
    addRoleCodeNode.focus()
    return [false, '']
  }
  addRoleCode = addRoleCode.trim()
  if (!ROLE_CODE_REGEX.test(addRoleCode)) {
    new Message().show({
      type: 'warning',
      text: '角色编码非法，仅支持大小写字母、数字、下划线，且不能以数字、下划线开头'
    })
    return [false, '']
  }
  return [true, addRoleCode]
}

function checkAddRoleDesc() {
  const addRoleDescNode = document.getElementById('add-role-desc')
  if (!addRoleDescNode) {
    new Message().show({
      type: 'error',
      text: '获取角色描述失败'
    })
    return [false, '']
  }
  let addRoleDesc = addRoleDescNode.value
  // 角色编码是非必填项
  if (!addRoleDesc || addRoleDesc.trim().length <= 0) {
    return [true, '']
  }
  return [true, addRoleDesc.trim()]
}

function checkAddRoleEnabled() {
  const addRoleEnabledNode = document.getElementById('add-role-enabled')
  if (!addRoleEnabledNode) {
    new Message().show({
      type: 'error',
      text: '获取角色是否可用失败'
    })
    return [false, '']
  }
  return [true, addRoleEnabledNode.checked]
}

function addRoleRequest(addRoleParam) {
  const xhr = new XMLHttpRequest()
  xhr.open('POST', `/api/role`, true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify(addRoleParam))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '操作成功'
        })
        loadRoleTableData()
        setTimeout(closeAddRolePanel, 2000)
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

function confirmAddRoleAction() {
  const addRoleParam = {}
  const [addRoleCodeCheckPassed, addRoleCode] = checkAddRoleCode()
  if (!addRoleCodeCheckPassed) {
    return
  }
  addRoleParam.roleCode = addRoleCode
  const [addRoleDescCheckPassed, addRoleDesc] = checkAddRoleDesc()
  if (!addRoleDescCheckPassed) {
    return
  }
  addRoleParam.roleDesc = addRoleDesc
  const [addRoleEnabledCheckPassed, addRoleEnabled] = checkAddRoleEnabled()
  if (!addRoleEnabledCheckPassed) {
    return
  }
  addRoleParam.enabled = addRoleEnabled
  addRoleRequest(addRoleParam)
}

function clearAddRoleInfo() {
  document.getElementById('add-role-code').value = ''
  document.getElementById('add-role-desc').value = ''
  document.getElementById('add-role-enabled').checked = false
}
