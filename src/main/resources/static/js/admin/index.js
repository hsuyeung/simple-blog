function getTokenFromLocal() {
  return localStorage.getItem('token') || ''
}

function setTokenToLocal(token) {
  localStorage.setItem('token', token)
}

function removeToken() {
  localStorage.removeItem('token')
}

function checkTokenValidation() {
  const token = getTokenFromLocal()
  if (!token || token.trim().length <= 0) {
    return false
  }
  let isValid = false
  const xhr = new XMLHttpRequest()
  xhr.open('GET', '/api/user/token/validation?token=' + token, false)
  xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
  xhr.send()
  if (xhr.readyState === 4 && xhr.status === 200) {
    const resp = JSON.parse(xhr.responseText)
    if (resp.code === 200) {
      isValid = resp.data
    } else {
      new Message().show({
        type: 'error',
        text: resp.msg
      })
    }
  }
  return isValid
}

function openAdminPanelAction() {
  // 关闭登录面板
  closeLoginPanel()
  // 打开管理面板
  openAdminPanel()
}

function openLoginPanelAction() {
  // 关闭管理面板
  closeAdminPanel()
  // 打开登录面板
  openLoginPanel()
  // 加载“记住我”参数
  loadRememberMeParam()
}

function openLoginPanel() {
  document.getElementById('login-panel').style.display = 'block'
}

function closeLoginPanel() {
  document.getElementById('login-panel').style.display = 'none'
}

function openAdminPanel() {
  document.getElementById('admin-panel').style.display = 'block'
}

function closeAdminPanel() {
  document.getElementById('admin-panel').style.display = 'none'
}

function loadAllData() {
  loadUserTableData()
  loadRoleTableData()
  loadPermissionTableData()
  loadFriendLinkTableData()
  loadSystemConfigTableData()
  loadMailTableData()
  loadCommentTableData()
  loadArticleTitleList()
  loadFileTableData()
  loadArticleTableData()
}

function logoutAction() {
  if (confirm('确认退出登录吗？')) {
    logoutRequest()
  }
}

function logoutRequest() {
  const xhr = new XMLHttpRequest()
  xhr.open('POST', '/api/user/logout', false)
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send()
  if (xhr.readyState === 4 && xhr.status === 200) {
    const resp = JSON.parse(xhr.responseText)
    if (resp.code === 200) {
      new Message().show({
        type: 'info',
        text: '退出登录成功'
      })
      openLoginPanelAction()
      removeToken()
    } else {
      new Message().show({
        type: 'error',
        text: resp.msg
      })
    }
  }
}

function loadRememberMeParam() {
  const isRememberMe = localStorage.getItem('rememberMe') === 'true';
  if (isRememberMe) {
    document.getElementById('login-username').value = localStorage.getItem('loginUsername')
    document.getElementById('login-password').value = localStorage.getItem('loginPassword')
    document.getElementById('remember-me').checked = isRememberMe
  }
}
