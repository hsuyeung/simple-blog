function resetPageNumAndLoadFileTableData() {
  document.getElementById('file-current-page').textContent = '1'
  loadFileTableData()
}

function loadFileTableData(loadPrev = false, loadNext = false, jumpTo = false) {
  clearFileTable()
  const url = document.getElementById('search-file-url')?.value
  const startTimeStr = document.getElementById('search-file-send-time-start')?.value
  let startTimestamp
  if (startTimeStr) {
    startTimestamp = new Date(startTimeStr).getTime()
  }
  const endTimeStr = document.getElementById('search-file-send-time-end')?.value
  let endTimestamp
  if (endTimeStr) {
    endTimestamp = new Date(endTimeStr).getTime()
  }
  let pageNum
  if (jumpTo) {
    const jumpToVal = parseInt(document.getElementById('file-page-jump-to')?.value)
    if (typeof jumpToVal === 'number' && !isNaN(jumpToVal)) {
      pageNum = jumpToVal
    } else {
      return
    }
  } else {
    pageNum = document.getElementById('file-current-page')?.textContent || 1
  }
  const pageSizeSelectNode = document.getElementById('file-page-size')
  const pageSize = pageSizeSelectNode?.options[pageSizeSelectNode?.selectedIndex]?.value || 10
  const xhr = new XMLHttpRequest()
  xhr.open(
          'GET',
          '/api/file/page'
          + '?url=' + (url || '')
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
        generateFileDataTable(resp.data.total, pageNum, pageSize, resp.data.data)
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

function generateFileDataTable(total, pageNum, pageSize, files) {
  total = parseInt(total)
  pageNum = parseInt(pageNum)
  pageSize = parseInt(pageSize)
  const tableHeadText = `
    <thead>
      <tr>
        <th>??????</th>
        <th>url</th>
        <th>????????????</th>
        <th>?????????</th>
      </tr>
    </thead>
  `
  let tableBodyText = ''
  let index = 1
  if (files.length <= 0) {
    tableBodyText = `
      <tr>????????????</tr>
    `
  } else {
    files.forEach(file => {
      tableBodyText += `
      <tr>
        <td id=${'index-' + file.id}>${index++}</td>
        <td id=${'file-url-' + file.id} title='${file.url}'><a href='${file.url}' target='_blank' rel='noopener noreferrer nofollow'>${subAndAppend(file.url, 50, '...')}</a></td>
        <td id=${'file-create-time-' + file.id}>${file.createTime}</td>
        <td id=${'file-create-by-' + file.id} title='${file.createBy}'>${subAndAppend(file.createBy, 20, '...')}</td>
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
  document.getElementById('file-data-table-body').appendChild(tableNode)

  const totalPage = Math.ceil(total / pageSize)
  document.getElementById('file-total-page').textContent = `??? ${totalPage} ???`
  document.getElementById('file-current-page').textContent = pageNum
  document.getElementById('file-total-size').textContent = `??? ${total} ???`
  document.getElementById('file-prev-page').innerHTML =
          `<button onclick='loadPrevPageFileData()' ${(pageNum === 1 || total === 0) ? 'disabled' : ''}>?????????</button>`
  document.getElementById('file-page-jump').innerHTML = `
           <label for='file-page-jump-to'>????????????</label>
           <input id='file-page-jump-to' onchange='loadFileTableData(false, false, true)'>???
  `
  document.getElementById('file-next-page').innerHTML =
          `<button onclick='loadNextPageFileData()' ${(pageNum === totalPage || total === 0) ? 'disabled' : ''}>?????????</button>`
}

function clearFileTable() {
  document.getElementById('file-data-table-body').innerHTML = ''
}

function loadPrevPageFileData() {
  // ???????????? +1
  const pageNumNode = document.getElementById('file-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) - 1}`
  loadFileTableData(true)
}

function loadNextPageFileData() {
  // ???????????? +1
  const pageNumNode = document.getElementById('file-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) + 1}`
  loadFileTableData(false, true)
}
