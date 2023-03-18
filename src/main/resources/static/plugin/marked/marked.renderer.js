/* 对 marked.js 的渲染规则改写 */
const renderer = {
  image(href, title, text) {
    return `<figure><img class='zoom-img' data-src='${href}' src='/img/loading.gif' alt='${text}'/><figcaption>${text}</figcaption></figure>`
  },
  link(href, title, text) {
    return `<a href='${href}' target='_blank' rel='noopener noreferrer nofollow'>${text}</a>`
  }
}

marked.use({renderer})
