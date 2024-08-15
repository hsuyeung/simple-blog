function resetPageNumAndLoadSystemConfigTableData() {
  document.getElementById('system-config-current-page').textContent = '1'
  loadSystemConfigTableData()
}

function loadSystemConfigTableData(loadPrev = false, loadNext = false, jumpTo = false) {
  clearSystemConfigTable()
  const systemConfigKey = document.getElementById('search-system-config-key')?.value
  const systemConfigDesc = document.getElementById('search-system-config-desc')?.value
  const systemConfigGroup = document.getElementById('search-system-config-group')?.value
  const enabledSelectionNode = document.getElementById('search-system-config-enabled')
  const enabled = enabledSelectionNode?.options[enabledSelectionNode?.selectedIndex]?.value
  let pageNum
  if (jumpTo) {
    const jumpToVal = parseInt(document.getElementById('system-config-page-jump-to')?.value)
    if (typeof jumpToVal === 'number' && !isNaN(jumpToVal)) {
      pageNum = jumpToVal
    } else {
      return
    }
  } else {
    pageNum = document.getElementById('system-config-current-page')?.textContent || 1
  }
  const pageSizeSelectNode = document.getElementById('system-config-page-size')
  const pageSize = pageSizeSelectNode?.options[pageSizeSelectNode?.selectedIndex]?.value || 10
  const xhr = new XMLHttpRequest()
  xhr.open('POST', '/api/system/config/actions/page', true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify({
    'pageParam': {
      'pageNum': pageNum,
      'pageSize': pageSize
    },
    'searchParam': {
      'key': systemConfigKey,
      'group': systemConfigGroup,
      'desc': systemConfigDesc,
      'enabled': enabled === '' ? null : enabled === '1'
    }
  }))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        generateSystemConfigDataTable(resp.data.total, pageNum, pageSize, resp.data.data)
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

function generateSystemConfigDataTable(total, pageNum, pageSize, systemConfigs) {
  total = parseInt(total)
  pageNum = parseInt(pageNum)
  pageSize = parseInt(pageSize)
  const tableHeadText = `
    <thead>
      <tr>
        <th>序号</th>
        <th>key</th>
        <th>值</th>
        <th>默认值</th>
        <th>分组</th>
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
  if (systemConfigs.length <= 0) {
    tableBodyText = `
      <tr>暂无数据</tr>
    `
  } else {
    systemConfigs.forEach(systemConfig => {
      tableBodyText += `
      <tr>
        <td id=${'index-' + systemConfig.id}>${index++}</td>
        <td id=${'system-config-key-' + systemConfig.id}>${systemConfig.confKey}</td>
        <td id=${'system-config-value-' + systemConfig.id} title="${systemConfig.confValue}">${subAndAppend(systemConfig.confValue, 10, '...')}</td>
        <td id=${'system-config-default-value-' + systemConfig.id} title="${systemConfig.confDefaultValue}">${subAndAppend(systemConfig.confDefaultValue, 10, '...')}</td>
        <td id=${'system-config-group-' + systemConfig.id}>${systemConfig.confGroup}</td>
        <td id=${'system-config-desc-' + systemConfig.id} title='${systemConfig.description}'>${subAndAppend(systemConfig.description, 10, '...')}</td>
        <td id=${'system-config-enabled-' + systemConfig.id}>${systemConfig.enabled ? '可用' : '禁用'}</td>
        <td id=${'system-config-create-time-' + systemConfig.id}>${systemConfig.createTime}</td>
        <td id=${'system-config-create-by-' + systemConfig.id}>${systemConfig.createBy}</td>
        <td id=${'system-config-update-time-' + systemConfig.id}>${systemConfig.updateTime}</td>
        <td id=${'system-config-update-by-' + systemConfig.id}>${systemConfig.updateBy}</td>
        <td>
          <button id=${'update-system-config-' + systemConfig.id} onclick='editSystemConfigAction(${systemConfig.id}, "${encode(systemConfig.confKey)}", "${encode(systemConfig.confValue)}", "${encode(systemConfig.confDefaultValue)}", "${encode(systemConfig.confGroup)}", "${encode(systemConfig.description)}", ${systemConfig.enabled})'>编辑</button>
          <button id=${'refresh-system-config-' + systemConfig.id} onclick='refreshOneSystemConfigAction(${systemConfig.id})'>刷新缓存</button>
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
  document.getElementById('system-config-data-table-body').appendChild(tableNode)

  const totalPage = Math.ceil(total / pageSize)
  document.getElementById('system-config-total-page').textContent = `共 ${totalPage} 页`
  document.getElementById('system-config-current-page').textContent = pageNum
  document.getElementById('system-config-total-size').textContent = `共 ${total} 条`
  document.getElementById('system-config-prev-page').innerHTML =
          `<button onclick='loadPrevPageSystemConfigData()' ${(pageNum === 1 || total === 0) ? 'disabled' : ''}>上一页</button>`
  document.getElementById('system-config-page-jump').innerHTML = `
           <label for='system-config-page-jump-to'>跳转到第</label>
           <input id='system-config-page-jump-to' onchange='loadSystemConfigTableData(false, false, true)'>页
  `
  document.getElementById('system-config-next-page').innerHTML =
          `<button onclick='loadNextPageSystemConfigData()' ${(pageNum === totalPage || total === 0) ? 'disabled' : ''}>下一页</button>`
}

function clearSystemConfigTable() {
  document.getElementById('system-config-data-table-body').innerHTML = ''
}

function loadPrevPageSystemConfigData() {
  // 将当前页 +1
  const pageNumNode = document.getElementById('system-config-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) - 1}`
  loadSystemConfigTableData(true)
}

function loadNextPageSystemConfigData() {
  // 将当前页 +1
  const pageNumNode = document.getElementById('system-config-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) + 1}`
  loadSystemConfigTableData(false, true)
}

function editSystemConfigAction(confId, encodeConfKey, encodeConfValue, encodeConfDefaultValue, encodeConfGroup, encodeDescription, enabled) {
  openEditSystemConfigPanel(confId, decode(encodeConfKey), decode(encodeConfValue), decode(encodeConfDefaultValue), decode(encodeConfGroup), decode(encodeDescription), enabled)
}

function openSystemConfigDataTable() {
  document.getElementById('system-config-data-table').style.display = 'block'
}

function openEditSystemConfigPanel(confId, confKey, confValue, confDefaultValue, confGroup, description, enabled) {
  closeSystemConfigDataTable()
  document.getElementById('edit-system-config-panel').style.display = 'block'
  document.getElementById('edit-system-config-id').textContent = confId
  document.getElementById('edit-system-config-key').textContent = confKey
  document.getElementById('edit-system-config-value').value = confValue
  document.getElementById('edit-system-config-default-value').textContent = confDefaultValue
  document.getElementById('edit-system-config-group').textContent = confGroup
  document.getElementById('edit-system-config-desc').textContent = description
  document.getElementById('edit-system-config-enabled').checked = enabled
}

function closeEditSystemConfigAction() {
  document.getElementById('edit-system-config-panel').style.display = 'none'
  openSystemConfigDataTable()
}

function confirmEditSystemConfigAction() {
  const editSystemConfigParam = {}
  const [systemConfigIdCheckPassed, systemConfigId] = checkSystemConfigId()
  if (!systemConfigIdCheckPassed) {
    return
  }
  editSystemConfigParam.id = systemConfigId
  const [editSystemConfigValueCheckPassed, editSystemConfigValue] = checkEditSystemConfigValue()
  if (!editSystemConfigValueCheckPassed) {
    return
  }
  editSystemConfigParam.confValue = editSystemConfigValue
  const [editSystemConfigEnabledCheckPassed, editSystemConfigEnabled] = checkEditSystemConfigEnabled()
  if (!editSystemConfigEnabledCheckPassed) {
    return
  }
  editSystemConfigParam.enabled = editSystemConfigEnabled
  editSystemConfigRequest(editSystemConfigParam)
}

function editSystemConfigRequest(editSystemConfigParam) {
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/system/config`, true)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.setRequestHeader('token', getTokenFromLocal())
  xhr.send(JSON.stringify(editSystemConfigParam))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const resp = JSON.parse(xhr.responseText)
      if (resp.code === 200) {
        new Message().show({
          type: 'info',
          text: '操作成功'
        })
        loadSystemConfigTableData()
        setTimeout(closeEditSystemConfigAction, 2000)
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

function checkSystemConfigId() {
  const editSystemConfigIdNode = document.getElementById('edit-system-config-id')
  if (!editSystemConfigIdNode) {
    new Message().show({
      type: 'error',
      text: '获取系统配置 id 失败'
    })
    return [false, '']
  }
  return [true, editSystemConfigIdNode.textContent]
}

function checkEditSystemConfigValue() {
  const editSystemConfigValueNode = document.getElementById('edit-system-config-value')
  if (!editSystemConfigValueNode) {
    new Message().show({
      type: 'error',
      text: '获取系统配置 value 失败'
    })
    return [false, '']
  }
  let addSystemConfigValue = editSystemConfigValueNode.value
  if (!addSystemConfigValue || addSystemConfigValue.trim().length <= 0) {
    new Message().show({
      type: 'warning',
      text: '系统配置 value 不能为空'
    })
    return [false, '']
  }
  return [true, addSystemConfigValue.trim()]
}

function checkEditSystemConfigEnabled() {
  const editSystemConfigEnabledNode = document.getElementById('edit-system-config-enabled')
  if (!editSystemConfigEnabledNode) {
    new Message().show({
      type: 'error',
      text: '获取配置是否可用失败'
    })
    return [false, '']
  }
  return [true, editSystemConfigEnabledNode.checked]
}

function closeSystemConfigDataTable() {
  document.getElementById('system-config-data-table').style.display = 'none'
}

function refreshOneSystemConfigAction(confId) {
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/system/config/actions/refresh/cache/${confId}`, true)
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

function refreshAllSystemConfigAction() {
  const xhr = new XMLHttpRequest()
  xhr.open('PUT', `/api/system/config/actions/refresh/cache`, true)
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
