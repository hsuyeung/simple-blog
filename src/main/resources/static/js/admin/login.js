window.onload = () => {
  const [username, nickname] = getCurrentLoginUserInfo();
  renderCurrentUserInfo(username, nickname)
}

function loginAction() {
  const [usernameContentCheckPassed, username] = usernameContentCheck()
  if (!usernameContentCheckPassed) {
    return
  }
  const [passwordContentCheckPassed, password] = passwordContentCheck()
  if (!passwordContentCheckPassed) {
    return
  }
  checkIsRememberMe(username, password);
  loginRequest(username, password)
}

function checkIsRememberMe(username, password) {
  const rememberMeNode = document.getElementById('remember-me')
  if (!rememberMeNode) {
    new Message().show({
      type: 'error',
      text: '获取“记住我”出错'
    })
    return [false, '']
  }
  const checked = rememberMeNode.checked;
  localStorage.setItem('rememberMe', checked)
  if (checked) {
    localStorage.setItem('loginUsername', username)
    localStorage.setItem('loginPassword', password)
  } else {
    localStorage.removeItem('loginUsername')
    localStorage.removeItem('loginPassword')
  }
}

function clearLoginParam() {
  document.getElementById('login-username').value = ''
  document.getElementById('login-password').value = ''
}

function loginRequest(username, password) {
  const xhr = new XMLHttpRequest()
  xhr.open('POST', '/api/user/actions/login', false)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.send(JSON.stringify({
    'username': username,
    'password': password
  }))
  if (xhr.readyState === 4 && xhr.status === 200) {
    const resp = JSON.parse(xhr.responseText)
    if (resp.code === 200) {
      new Message().show({
        type: 'info',
        text: '登录成功'
      })
      setTokenToLocal(resp.data)
      setCurrentUserNicknameRequestAction(username)
      setTimeout(openAdminPanelAction, 2000)
      setTimeout(loadAllData, 2000)
      setTimeout(clearLoginParam, 2000)
    } else {
      new Message().show({
        type: 'error',
        text: resp.msg
      })
    }
  }
}

function usernameContentCheck() {
  const usernameNode = document.getElementById('login-username')
  if (!usernameNode) {
    new Message().show({
      type: 'error',
      text: '获取用户名出错'
    })
    return [false, '']
  }
  const username = usernameNode.value
  if (!username || username.trim().length <= 0) {
    usernameNode.focus()
    new Message().show({
      type: 'warning',
      text: '请输入用户名'
    })
    return [false, '']
  }
  if (!USERNAME_REGEX.test(username)) {
    usernameNode.focus()
    new Message().show({
      type: 'warning',
      text: '用户名非法，仅支持大小写字母、数字、下划线，且不能以数字、下划线开头'
    })
  }
  return [true, username]
}

function passwordContentCheck() {
  const passwordNode = document.getElementById('login-password')
  if (!passwordNode) {
    new Message().show({
      type: 'error',
      text: '获取密码出错'
    })
    return [false, '']
  }
  const password = passwordNode.value
  if (!password || password.length <= 0) {
    passwordNode.focus()
    new Message().show({
      type: 'warning',
      text: '请输入密码'
    })
    return [false, '']
  }
  if (password.length < 8 || password.length > 16) {
    passwordNode.focus()
    new Message().show({
      type: 'warning',
      text: '密码长度必须为 8-16 位'
    })
    return [false, '']
  }
  if (!PASSWORD_REGEX.test(password)) {
    passwordNode.focus()
    new Message().show({
      type: 'warning',
      text: '密码非法，仅支持大小写字母、数字、下划线和!@%，且不能以数字、下划线开头'
    })
    return [false, '']
  }
  return [true, password]
}

function renderCurrentUserInfo(username, nickname) {
  document.getElementById('current-username').textContent = username
  document.getElementById('current-nickname').textContent = nickname
}

/**
 * 获取当前登录人的昵称
 */
function setCurrentUserNicknameRequestAction(username) {
  const xhr = new XMLHttpRequest()
  xhr.open('GET', '/api/user/nickname', false)
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send()
  if (xhr.readyState === 4 && xhr.status === 200) {
    const resp = JSON.parse(xhr.responseText)
    if (resp.code === 200) {
      setCurrentUserInfo(username, resp.data)
      renderCurrentUserInfo(username, resp.data)
    } else {
      new Message().show({
        type: 'error',
        text: resp.msg
      })
    }
  }
}

function setCurrentUserInfo(username, nickname) {
  localStorage.setItem('currentLoginUsername', username)
  localStorage.setItem('currentLoginNickname', nickname)
}

function getCurrentLoginUserInfo() {
  return [localStorage.getItem('currentLoginUsername'), localStorage.getItem('currentLoginNickname')]
}
