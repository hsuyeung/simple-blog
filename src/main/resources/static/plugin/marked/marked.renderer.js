/* 对 marked.js 的渲染规则改写 */
const renderer = {
  image(href, title, text) {
    return `<figure><img class='zoom-img' data-src='${href}' src='/img/loading.gif' alt='${text ? text : "default-alt"}'/><figcaption>${text}</figcaption></figure>`
  },
  link(href, title, text) {
    return `<a href='${href}' target='_blank' rel='noopener noreferrer nofollow'>${text}</a>`
  },
  heading(text, level) {
    // 生成 ID
    const uuid = crypto.randomUUID();
    return `<h${level} id="${uuid}">${text}</h${level}>`;
  }
}

marked.use({renderer})
