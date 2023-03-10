function resetPageNumAndLoadMailTableData() {
  document.getElementById('mail-current-page').textContent = '1'
  loadMailTableData()
}

function loadMailTableData(loadPrev = false, loadNext = false, jumpTo = false) {
  clearMailTable()
  const from = document.getElementById('search-mail-from')?.value
  const to = document.getElementById('search-mail-to')?.value
  const subject = document.getElementById('search-mail-subject')?.value
  const cc = document.getElementById('search-mail-cc')?.value
  const bcc = document.getElementById('search-mail-bcc')?.value
  const statusSelectionNode = document.getElementById('search-mail-status')
  const status = statusSelectionNode?.options[statusSelectionNode?.selectedIndex]?.value
  const typeSelectionNode = document.getElementById('search-mail-type')
  const type = typeSelectionNode?.options[typeSelectionNode?.selectedIndex]?.value
  const startTimeStr = document.getElementById('search-mail-send-time-start')?.value
  let startTimestamp
  if (startTimeStr) {
    startTimestamp = new Date(startTimeStr).getTime()
  }
  const endTimeStr = document.getElementById('search-mail-send-time-end')?.value
  let endTimestamp
  if (endTimeStr) {
    endTimestamp = new Date(endTimeStr).getTime()
  }
  let pageNum
  if (jumpTo) {
    const jumpToVal = parseInt(document.getElementById('mail-page-jump-to')?.value)
    if (typeof jumpToVal === 'number' && !isNaN(jumpToVal)) {
      pageNum = jumpToVal
    } else {
      return
    }
  } else {
    pageNum = document.getElementById('mail-current-page')?.textContent || 1
  }
  const pageSizeSelectNode = document.getElementById('mail-page-size')
  const pageSize = pageSizeSelectNode?.options[pageSizeSelectNode?.selectedIndex]?.value || 10
  const xhr = new XMLHttpRequest()
  xhr.open(
          'GET',
          '/api/mail/page'
          + '?from=' + (from || '')
          + '&to=' + (to || '')
          + '&subject=' + (subject || '')
          + '&cc=' + (cc || '')
          + '&bcc=' + (bcc || '')
          + '&status=' + (status || '')
          + '&type=' + (type || '')
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
        generateMailDataTable(resp.data.total, pageNum, pageSize, resp.data.data)
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

function generateMailDataTable(total, pageNum, pageSize, mails) {
  total = parseInt(total)
  pageNum = parseInt(pageNum)
  pageSize = parseInt(pageSize)
  const tableHeadText = `
    <thead>
      <tr>
        <th>??????</th>
        <th>?????????</th>
        <th>?????????</th>
        <th>??????</th>
        <th>??????</th>
        <th>??????</th>
        <th>??????</th>
        <th>??????</th>
        <th>????????????</th>
        <th>??????</th>
        <th>????????????</th>
        <th>????????????</th>
      </tr>
    </thead>
  `
  let tableBodyText = ''
  let index = 1
  if (mails.length <= 0) {
    tableBodyText = `
      <tr>????????????</tr>
    `
  } else {
    mails.forEach(mail => {
      tableBodyText += `
      <tr>
        <td id=${'index-' + mail.id}>${index++}</td>
        <td id=${'mail-from-' + mail.id} title='${mail.mfrom}'>${subAndAppend(mail.mfrom, 10, '...')}</td>
        <td id=${'mail-to-' + mail.id} title='${mail.mto}'>${subAndAppend(mail.mto, 10, '...')}</td>
        <td id=${'mail-subject-' + mail.id} title='${mail.msubject}'>${subAndAppend(mail.msubject, 10, '...')}</td>
        <td id=${'mail-text-preview-url-' + mail.id}><a href=${mail.textPreviewUrl + '?token=' + getTokenFromLocal()} target='_blank' rel='noopener noreferrer nofollow'>????????????????????????</a></td>
        <td id=${'mail-cc-' + mail.id} title='${mail.cc}'>${subAndAppend(mail.cc, 10, '...')}</td>
        <td id=${'mail-bcc-' + mail.id} title='${mail.bcc}'>${subAndAppend(mail.bcc, 10, '...')}</td>
        <td id=${'mail-status-' + mail.id}>${mail.mstatus}</td>
        <td id=${'mail-err-msg-' + mail.id} title='${mail.errorMsg}'>${subAndAppend(mail.errorMsg, 10, '...')}</td>
        <td id=${'mail-type-' + mail.id}>${mail.type}</td>
        <td id=${'mail-send-time-' + mail.id}>${mail.sendTime}</td>
        <td id=${'mail-retry-num-' + mail.id}>${mail.retryNum}</td>
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
  document.getElementById('mail-data-table-body').appendChild(tableNode)

  const totalPage = Math.ceil(total / pageSize)
  document.getElementById('mail-total-page').textContent = `??? ${totalPage} ???`
  document.getElementById('mail-current-page').textContent = pageNum
  document.getElementById('mail-total-size').textContent = `??? ${total} ???`
  document.getElementById('mail-prev-page').innerHTML =
          `<button onclick='loadPrevPageMailData()' ${(pageNum === 1 || total === 0) ? 'disabled' : ''}>?????????</button>`
  document.getElementById('mail-page-jump').innerHTML = `
           <label for='mail-page-jump-to'>????????????</label>
           <input id='mail-page-jump-to' onchange='loadMailTableData(false, false, true)'>???
  `
  document.getElementById('mail-next-page').innerHTML =
          `<button onclick='loadNextPageMailData()' ${(pageNum === totalPage || total === 0) ? 'disabled' : ''}>?????????</button>`
}

function clearMailTable() {
  document.getElementById('mail-data-table-body').innerHTML = ''
}

function loadPrevPageMailData() {
  // ???????????? +1
  const pageNumNode = document.getElementById('mail-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) - 1}`
  loadMailTableData(true)
}

function loadNextPageMailData() {
  // ???????????? +1
  const pageNumNode = document.getElementById('mail-current-page');
  pageNumNode.textContent = `${parseInt(pageNumNode.textContent) + 1}`
  loadMailTableData(false, true)
}
