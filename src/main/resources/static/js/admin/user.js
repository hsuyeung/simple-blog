function resetPageNumAndLoadUserTableData() {
  document.getElementById('user-current-page').textContent = '1'
  loadUserTableData()
}

function loadUserTableData(loadPrev = false, loadNext = false, jumpTo = false) {
  clearUserTable()
  const username = document.querySelector('#user-data-search-form #admin-username')?.value
  const nickname = document.querySelector('#user-data-search-form #admin-nickname')?.value
  const enabledSelectionNode = document.querySelector('#user-data-search-form #enabled')
  const enabled = enabledSelectionNode?.options[enabledSelectionNode?.selectedIndex]?.value
  let pageNum
  if (jumpTo) {
    const jumpToVal = parseInt(document.getElementById('user-page-jump-to')?.value)
    if (typeof jumpToVal === 'number' && !isNaN(jumpToVal)) {
      pageNum = jumpToVal
    } else {
      return
    }
  } else {
    pageNum = document.querySelector('#user-data-table-footer #user-current-page')?.textContent || 1
  }
  const pageSizeSelectNode = document.querySelector('#user-data-table-footer #user-page-size')
  const pageSize = pageSizeSelectNode?.options[pageSizeSelectNode?.selectedIndex]?.value || 10
  const xhr = new XMLHttpRequest()
  xhr.open(
          'GET',
          '/api/user/page'
          + '?username=' + (username || '')
          + '&nickname=' + (nickname || '')
          + '&enabled=' + (!enabled || enabled === '' ? '' : enabled === '1')
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
        generateUserDataTable(resp.data.total, pageNum, pageSize, resp.data.data)
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

function deleteUserRequest(uid) {
  const xhr = new XMLHttpRequest()
  xhr.open('DELETE', `/api/user/${uid}`, true)
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
        loadUserTableData()
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

function userStatusChangeAction(uid) {
  const enabledCheckBoxNode = document.getElementById(`user-enabled-checkbox-${uid}`);
  if (!confirm("??????????????????????????????????????????")) {
    enabledCheckBoxNode.checked = !enabledCheckBoxNode.checked
    return
  }
  const enabled = enabledCheckBoxNode.checked
  const xhr = new XMLHttpRequest()
  xhr.open('POST', `/api/user/${enabled === true ? 'unlock' : 'lock'}/${uid}`, true)
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
        loadUserTableData()
      } else if (resp.code === 401) {
        new Message().show({
          type: 'error',
          text: resp.msg
        })
        // ??????????????? checkbox ??????????????????????????????
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

function deleteUserAction(uid) {
  confirm("???????????????????????????\n??????????????????????????????????????????????????????") && deleteUserRequest(uid);
}

function generateUserDataTable(total, pageNum, pageSize, users) {
  total = parseInt(total)
  pageNum = parseInt(pageNum)
  pageSize = parseInt(pageSize)
  const tableHeadText = `
    <thead>
      <tr>
        <th>??????</th>
        <th>?????????</th>
        <th>??????</th>
        <th>????????????</th>
        <th>????????????</th>
        <th>?????????</th>
        <th>????????????</th>
        <th>?????????</th>
        <th>??????</th>
      </tr>
    </thead>
  `
  let tableBodyText = ''
  let index = 1
  if (users.length <= 0) {
    tableBodyText = `
      <tr>????????????</tr>
    `
  } else {
    users.forEach(user => {
      tableBodyText += `
      <tr>
        <td id=${'index-' + user.id}>${index++}</td>
        <td id=${'username-' + user.id}>${user.username}</td>
        <td id=${'nickname-' + user.id}>${user.nickname}</td>
        <td id=${'user-enabled-' + user.id}>
          <label for=${'user-enabled-checkbox-' + user.id}></label>
          <input onchange='userStatusChangeAction(${user.id})' type='checkbox' name=${'user-enabled-checkbox-' + user.id} id=${'user-enabled-checkbox-' + user.id} ${user.enabled ? 'checked' : ''}>
        </td>
        <td id=${'user-create-time-' + user.id}>${user.createTime}</td>
        <td id=${'user-create-by-' + user.id}>${user.createBy}</td>
        <td id=${'user-update-time-' + user.id}>${user.updateTime}</td>
        <td id=${'user-update-by-' + user.id}>${user.updateBy}</td>
        <td>
          <button id=${'delete-user-' + user.id} onclick='deleteUserAction(${user.id})'>??????</button>
          <button id=${'update-user-' + user.id} onclick='editUserAction(${user.id}, "${user.username}", "${user.nickname}", ${user.enabled})'>??????</button>
          <button id=${'assign-user-role-' + user.id} onclick='assignRoleAction(${user.id}, "${user.username}", "${user.nickname}", ${user.enabled})'>????????????</button>
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
  document.getElementById('user-data-table-body').appendChild(tableNode)

  const totalPage = Math.ceil(total / pageSize)
  document.getElementById('user-total-page').textContent = `??? ${totalPage} ???`
  document.getElementById('user-current-page').textContent = pageNum
  document.getElementById('user-total-size').textContent = `??? ${total} ???`
  document.getElementById('user-prev-page').innerHTML =
          `<button onclick='loadPrevPageUserData()' ${(pageNum === 1 || total === 0) ? 'disabled' : ''}>?????????</button>`
  document.getElementById('user-page-jump').innerHTML = `
           <label for='user-page-jump-to'>????????????</label>
           <input id='user-page-jump-to' onchange='loadUserTableData(false, false, true)'>???
  `
  document.getElementById('user-next-page').innerHTML =
          `<button onclick='loadNextPageUserData()' ${(pageNum === totalPage || total === 0) ? 'disabled' : ''}>?????????</button>`
}

function clearUserTable() {
  document.getElementById('user-data-table-body').innerHTML = ''
}

function loadPrevPageUserData() {
  // ???????????? +1
  const pageNumNode = document.querySelector('#user-data-table-footer #user-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) - 1}`
  loadUserTableData(true)
}

function loadNextPageUserData() {
  // ???????????? +1
  const pageNumNode = document.querySelector('#user-data-table-footer #user-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) + 1}`
  loadUserTableData(false, true)
}

function editUserAction(uid, username, nickname, enabled) {
  openEditUserPanel(uid, username, nickname, enabled)
}

function openUserDataTable() {
  document.getElementById('user-data-table').style.display = 'block'
}


function openEditUserPanel(uid, username, nickname, enabled) {
  closeUserDataTable()
  const editPanel = document.getElementById('edit-user-panel')
  editPanel.style.display = 'block'
  document.getElementById('edit-user-id').textContent = uid
  document.getElementById('edit-username').value = username
  document.getElementById('edit-nickname').value = nickname
  document.getElementById('edit-user-enabled').checked = enabled
  document.getElementById('edit-user-password-panel').style.display = 'none'
  document.getElementById('edit-user-old-password').value = ''
  document.getElementById('edit-user-new-password').value = ''
  document.getElementById('edit-user-reconfirm-password').value = ''
}

function closeEditUserAction() {
  document.getElementById('edit-user-panel').style.display = 'none'
  document.getElementById('edit-user-password').textContent = '????????????????????????'
  openUserDataTable()
}

function confirmEditUserAction() {
  const editUserParam = {}
  const [userIdCheckPassed, userId] = checkUserId()
  if (!userIdCheckPassed) {
    return
  }
  editUserParam.id = userId
  const [editUsernameCheckPassed, editUsername] = checkEditUsername()
  if (!editUsernameCheckPassed) {
    return
  }
  editUserParam.username = editUsername
  const [editNicknameCheckPassed, editNickname] = checkEditNickname()
  if (!editNicknameCheckPassed) {
    return
  }
  editUserParam.nickname = editNickname
  const [editUserEnabledCheckPassed, editUserEnabled] = checkEditUserEnabled()
  if (!editUserEnabledCheckPassed) {
    return
  }
  editUserParam.enabled = editUserEnabled
  // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
  let needEditPassword = document.getElementById('edit-user-password-panel').style.display !== 'none'
          && document.getElementById('edit-user-new-password').value || false
  if (needEditPassword) {
    const [editUserOldPasswordCheckPassed, editUserOldPassword] = checkEditUserOldPassword()
    if (!editUserOldPasswordCheckPassed) {
      return
    }
    editUserParam.oldPassword = editUserOldPassword
    const [editUserNewPasswordCheckPassed, editUserNewPassword] = checkEditUserNewPassword(editUserOldPassword)
    if (!editUserNewPasswordCheckPassed) {
      return
    }
    editUserParam.newPassword = editUserNewPassword
    const [editUserReconfirmPasswordCheckPassed, editUserReconfirmPassword] = checkEditUserReconfirmPassword(editUserNewPassword)
    if (!editUserReconfirmPasswordCheckPassed) {
      return
    }
    editUserParam.reconfirmNewPassword = editUserReconfirmPassword
  }
  editUserRequest(editUserParam)
}

function editUserRequest(editUserParam) {
  const xhr = new XMLHttpRequest()
  xhr.open('POST', `/api/user`, true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify(editUserParam))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '????????????'
        })
        loadUserTableData()
        setTimeout(closeEditUserAction, 2000)
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

function toggleEditUserPasswordPanel() {
  const editPasswordPanelNode = document.getElementById('edit-user-password-panel');
  const isHidden = editPasswordPanelNode.style.display === 'none';
  editPasswordPanelNode.style.display = isHidden ? 'block' : 'none'
  document.getElementById('edit-user-password').textContent = `${isHidden ? '??????' : '??????'}??????????????????`
}

function checkEditUserReconfirmPassword(editUserNewPassword) {
  const editUserReconfirmPasswordNode = document.getElementById('edit-user-reconfirm-password');
  if (!editUserReconfirmPasswordNode) {
    new Message().show({
      type: 'error',
      text: '????????????????????????????????????'
    })
    return [false, '']
  }
  let editUserReconfirmPassword = editUserReconfirmPasswordNode.value
  if (!editUserReconfirmPassword || editUserReconfirmPassword.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '???????????????????????????'
    })
    editUserReconfirmPasswordNode.focus()
    return [false, '']
  }
  editUserReconfirmPassword = editUserReconfirmPassword.trim()
  if (editUserReconfirmPassword !== editUserNewPassword) {
    new Message().show({
      type: 'warning',
      text: '???????????????????????????????????????'
    })
    editUserReconfirmPasswordNode.focus()
    return [false, '']
  }
  return [true, editUserReconfirmPassword]
}

function checkEditUserNewPassword(editUserOldPassword) {
  const editUserNewPasswordNode = document.getElementById('edit-user-new-password');
  if (!editUserNewPasswordNode) {
    new Message().show({
      type: 'error',
      text: '???????????????????????????'
    })
    return [false, '']
  }
  let editUserNewPassword = editUserNewPasswordNode.value
  if (!editUserNewPassword || editUserNewPassword.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '??????????????????'
    })
    editUserNewPasswordNode.focus()
    return [false, '']
  }
  editUserNewPassword = editUserNewPassword.trim()
  if (editUserNewPassword === editUserOldPassword) {
    new Message().show({
      type: 'warning',
      text: '?????????????????????????????????'
    })
    editUserNewPasswordNode.focus()
    return [false, '']
  }
  if (editUserNewPassword.length < 8 || editUserNewPassword.length > 16) {
    new Message().show({
      type: 'warning',
      text: '???????????????????????? 8-16 ???'
    })
    editUserNewPassword.focus()
    return [false, '']
  }
  if (!PASSWORD_REGEX.test(editUserNewPassword)) {
    new Message().show({
      type: 'warning',
      text: '??????????????????????????????????????????????????????????????????!@%???????????????????????????????????????'
    })
    editUserNewPassword.focus()
    return [false, '']
  }
  return [true, editUserNewPassword]
}

function checkEditUserOldPassword() {
  const editUserOldPasswordNode = document.getElementById('edit-user-old-password')
  if (!editUserOldPasswordNode) {
    new Message().show({
      type: 'error',
      text: '???????????????????????????'
    })
    return [false, '']
  }
  let editUserOldPassword = editUserOldPasswordNode.value
  if (!editUserOldPassword || editUserOldPassword.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '??????????????????'
    })
    editUserOldPasswordNode.focus()
    return [false, '']
  }
  editUserOldPassword = editUserOldPassword.trim()
  if (editUserOldPassword.length < 8 || editUserOldPassword.length > 16) {
    new Message().show({
      type: 'warning',
      text: '???????????????????????? 8-16 ???'
    })
    editUserOldPassword.focus()
    return [false, '']
  }
  if (!PASSWORD_REGEX.test(editUserOldPassword)) {
    new Message().show({
      type: 'warning',
      text: '??????????????????????????????????????????????????????????????????!@%???????????????????????????????????????'
    })
    editUserOldPasswordNode.focus()
    return [false, '']
  }
  return [true, editUserOldPassword]
}

function checkUserId() {
  const editUserIdNode = document.getElementById('edit-user-id')
  if (!editUserIdNode) {
    new Message().show({
      type: 'error',
      text: '???????????? id ??????'
    })
    return [false, '']
  }
  return [true, editUserIdNode.textContent]
}

function checkEditUserEnabled() {
  const editUserEnabledNode = document.getElementById('edit-user-enabled')
  if (!editUserEnabledNode) {
    new Message().show({
      type: 'error',
      text: '??????????????????????????????'
    })
    return [false, '']
  }
  return [true, editUserEnabledNode.checked]
}

function checkEditNickname() {
  const editNicknameNode = document.getElementById('edit-nickname')
  if (!editNicknameNode) {
    new Message().show({
      type: 'error',
      text: '??????????????????'
    })
    return [false, '']
  }
  let editNickname = editNicknameNode.value
  if (!editNickname || editNickname.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '???????????????'
    })
    editNicknameNode.focus()
    return [false, '']
  }
  editNickname = editNickname.trim()
  if (!NICKNAME_REGEX.test(editNickname)) {
    new Message().show({
      type: 'warning',
      text: '????????????????????????????????????????????????'
    })
    return [false, '']
  }
  if (editNickname.length > 8) {
    new Message().show({
      type: 'warning',
      text: '???????????????????????? 8 ?????????'
    })
    return [false, '']
  }
  return [true, editNickname]
}

function checkEditUsername() {
  const editUsernameNode = document.getElementById('edit-username')
  if (!editUsernameNode) {
    new Message().show({
      type: 'error',
      text: '?????????????????????'
    })
    return [false, '']
  }
  let editUsername = editUsernameNode.value
  if (!editUsername || editUsername.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '??????????????????'
    })
    editUsernameNode.focus()
    return [false, '']
  }
  editUsername = editUsername.trim()
  if (!USERNAME_REGEX.test(editUsername)) {
    new Message().show({
      type: 'warning',
      text: '??????????????????????????????????????????????????????????????????????????????????????????????????????'
    })
    return [false, '']
  }
  return [true, editUsername]
}

function generateRoleList(uid, roles) {
  let roleListHTML = ''
  if (roles.length <= 0) {
    roleListHTML = '????????????'
  } else {
    roles.forEach(role => {
      roleListHTML += `
        <span>
          <input type='checkbox' name='assign-role-all-enabled-role' id=${'assign-role-all-enabled-role-' + role.id} data-id=${role.id}>
          <label for=${'assign-role-all-enabled-role-' + role.id}>${role.roleCode}(${role.roleDesc})</label>
        </span>
      `
    })
  }
  document.getElementById('assign-role-all-enabled-roles').innerHTML = roleListHTML
  // ?????????????????????????????????
  loadUserRole(uid)
}

function loadAllRole(uid) {
  const xhr = new XMLHttpRequest()
  xhr.open('GET', `/api/role/all/enabled`, true)
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send()
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        generateRoleList(uid, resp.data)
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
 * ?????????????????????
 * @param roles ?????????????????????
 */
function renderRoleCheckBox(roles) {
  if (roles.length <= 0) {
    return
  }
  roles.forEach(role => {
    document.getElementById(`${'assign-role-all-enabled-role-' + role.id}`).checked = true
  })
}

function loadUserRole(uid) {
  const xhr = new XMLHttpRequest()
  xhr.open('GET', `/api/user/${uid}/all/enabled/role`, true)
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send()
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        renderRoleCheckBox(resp.data)
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

function assignRoleAction(uid, username, nickname, enabled) {
  document.getElementById('assign-role-user-id').textContent = uid
  document.getElementById('assign-role-username').textContent = username
  document.getElementById('assign-role-nickname').textContent = nickname
  document.getElementById('assign-role-user-enabled').textContent = enabled ? '??????' : '?????????'
  // ???????????????????????????
  loadAllRole(uid)
  openAssignRolePanel()
}

function closeUserDataTable() {
  document.getElementById('user-data-table').style.display = 'none'
}

function openAssignRolePanel() {
  closeUserDataTable();
  document.getElementById('assign-role-panel').style.display = 'block'
}

function closeAssignRolePanel() {
  document.getElementById('assign-role-panel').style.display = 'none'
  document.getElementById('assign-role-all-enabled-roles').innerHTML = ''
  openUserDataTable()
}

/**
 * ??????????????????????????????
 * @param uid ?????? id
 * @param rids ?????? id ??????
 */
function assignRoleRequest(uid, rids) {
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/user/${uid}/role?rids=${rids.join(',')}`, true)
  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
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
        setTimeout(closeAssignRolePanel, 2000)
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

function confirmAssignRoleAction() {
  // ??????????????????????????????
  const allRoleCheckBoxNode = document.getElementsByName('assign-role-all-enabled-role');
  if (!allRoleCheckBoxNode) {
    return
  }
  let selectedRoleIds = []
  allRoleCheckBoxNode.forEach(checkbox => {
    if (checkbox.checked) {
      selectedRoleIds.push(checkbox.getAttribute('data-id'))
    }
  })
  assignRoleRequest(document.getElementById('assign-role-user-id').textContent, selectedRoleIds)
}

function addUserAction() {
  openAddUserPanel()
}

function openAddUserPanel() {
  closeUserDataTable()
  document.getElementById('add-user-panel').style.display = 'block'
}

function closeAddUserPanel() {
  document.getElementById('add-user-panel').style.display = 'none'
  clearAddUserInfo()
  openUserDataTable()
}

function checkAddUsername() {
  const addUsernameNode = document.getElementById('add-username')
  if (!addUsernameNode) {
    new Message().show({
      type: 'error',
      text: '?????????????????????'
    })
    return [false, '']
  }
  let addUsername = addUsernameNode.value
  if (!addUsername || addUsername.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '??????????????????'
    })
    addUsernameNode.focus()
    return [false, '']
  }
  addUsername = addUsername.trim()
  if (!USERNAME_REGEX.test(addUsername)) {
    new Message().show({
      type: 'warning',
      text: '??????????????????????????????????????????????????????????????????????????????????????????????????????'
    })
    return [false, '']
  }
  return [true, addUsername]
}

function checkAddNickname() {
  const addNicknameNode = document.getElementById('add-nickname')
  if (!addNicknameNode) {
    new Message().show({
      type: 'error',
      text: '??????????????????'
    })
    return [false, '']
  }
  let addNickname = addNicknameNode.value
  if (!addNickname || addNickname.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '???????????????'
    })
    addNicknameNode.focus()
    return [false, '']
  }
  addNickname = addNickname.trim()
  if (!NICKNAME_REGEX.test(addNickname)) {
    new Message().show({
      type: 'warning',
      text: '????????????????????????????????????????????????'
    })
    return [false, '']
  }
  if (addNickname.length > 8) {
    new Message().show({
      type: 'warning',
      text: '???????????????????????? 8 ?????????'
    })
    return [false, '']
  }
  return [true, addNickname]
}

function checkAddUserEnabled() {
  const addUserEnabledNode = document.getElementById('add-user-enabled')
  if (!addUserEnabledNode) {
    new Message().show({
      type: 'error',
      text: '??????????????????????????????'
    })
    return [false, '']
  }
  return [true, addUserEnabledNode.checked]
}

function checkAddUserPassword() {
  const addUserPasswordNode = document.getElementById('add-password')
  if (!addUserPasswordNode) {
    new Message().show({
      type: 'error',
      text: '????????????????????????'
    })
    return [false, '']
  }
  let addUserPassword = addUserPasswordNode.value
  if (!addUserPassword || addUserPassword.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '???????????????'
    })
    addUserPasswordNode.focus()
    return [false, '']
  }
  addUserPassword = addUserPassword.trim()
  if (addUserPassword.length < 8 || addUserPassword.length > 16) {
    new Message().show({
      type: 'warning',
      text: '????????????????????? 8-16 ???'
    })
    addUserPassword.focus()
    return [false, '']
  }
  if (!PASSWORD_REGEX.test(addUserPassword)) {
    new Message().show({
      type: 'warning',
      text: '???????????????????????????????????????????????????????????????!@%???????????????????????????????????????'
    })
    addUserPasswordNode.focus()
    return [false, '']
  }
  return [true, addUserPassword]
}

function checkAddUserReconfirmPassword(addUserPassword) {
  const addUserReconfirmPasswordNode = document.getElementById('add-reconfirm-password');
  if (!addUserReconfirmPasswordNode) {
    new Message().show({
      type: 'error',
      text: '????????????????????????'
    })
    return [false, '']
  }
  let addUserReconfirmPassword = addUserReconfirmPasswordNode.value
  if (!addUserReconfirmPassword || addUserReconfirmPassword.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '?????????????????????'
    })
    addUserReconfirmPasswordNode.focus()
    return [false, '']
  }
  addUserReconfirmPassword = addUserReconfirmPassword.trim()
  if (addUserReconfirmPassword !== addUserPassword) {
    new Message().show({
      type: 'warning',
      text: '???????????????????????????'
    })
    addUserReconfirmPasswordNode.focus()
    return [false, '']
  }
  return [true, addUserReconfirmPassword]
}

function addUserRequest(addUserParam) {
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/user`, true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify(addUserParam))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '????????????'
        })
        loadUserTableData()
        setTimeout(closeAddUserPanel, 2000)
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

function confirmAddUserAction() {
  const addUserParam = {}
  const [addUsernameCheckPassed, addUsername] = checkAddUsername()
  if (!addUsernameCheckPassed) {
    return
  }
  addUserParam.username = addUsername
  const [addNicknameCheckPassed, addNickname] = checkAddNickname()
  if (!addNicknameCheckPassed) {
    return
  }
  addUserParam.nickname = addNickname
  const [addUserEnabledCheckPassed, addUserEnabled] = checkAddUserEnabled()
  if (!addUserEnabledCheckPassed) {
    return
  }
  addUserParam.enabled = addUserEnabled
  const [addUserPasswordCheckPassed, addUserPassword] = checkAddUserPassword()
  if (!addUserPasswordCheckPassed) {
    return
  }
  addUserParam.password = addUserPassword
  const [addUserReconfirmPasswordCheckPassed, addUserReconfirmPassword] = checkAddUserReconfirmPassword(addUserPassword)
  if (!addUserReconfirmPasswordCheckPassed) {
    return
  }
  addUserParam.reconfirmPassword = addUserReconfirmPassword
  addUserRequest(addUserParam)
}

function clearAddUserInfo() {
  document.getElementById('add-username').value = ''
  document.getElementById('add-nickname').value = ''
  document.getElementById('add-password').value = ''
  document.getElementById('add-reconfirm-password').value = ''
  document.getElementById('add-user-enabled').checked = false
}
