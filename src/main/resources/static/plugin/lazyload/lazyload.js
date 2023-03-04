/**
 * 懒加载。可加载 <img>、<video>、<audio> 等一些引用资源路径的标签
 * @param {object} options 懒加载配置对象
 * @param {string?} options.lazyAttr 自定义加载的属性（可选）
 * @param {'src'|'background'} options.loadType 加载的类型（默认为 src）
 * @param {string?} options.errorPath 加载失败时显示的资源路径，仅在 loadType 设置为 src 中可用（可选）
 */
function lazyLoad(options) {
  const attr = options.lazyAttr || 'data-src'
  const type = options.loadType || 'src'

  /**
   * 更新整个文档的懒加载结点
   */
  function observeLazyLoadNode() {
    const els = document.querySelectorAll(`[${attr}]`)
    for (let i = 0; i < els.length; i++) {
      const el = els[i]
      observer.observe(el)
    }
  }

  /**
   * 加载图片
   * @param {HTMLImageElement} el 图片结点
   */
  function loadImage(el) {
    const cache = el.src // 缓存当前 src 加载失败时候用
    el.src = el.getAttribute(attr)
    el.onerror = function () {
      el.src = options.errorPath || cache
    }
  }

  /**
   * 加载单个结点
   * @param {HTMLElement} el
   */
  function loadElement(el) {
    switch (type) {
      case 'src':
        loadImage(el)
        break
      case 'background':
        el.style.backgroundImage = `url(${el.getAttribute(attr)})`
        break
    }
    el.removeAttribute(attr)
    observer.unobserve(el)
  }

  /**
   * 监听器
   * [MDN 说明](https://developer.mozilla.org/zh-CN/docs/Web/API/IntersectionObserver)
   */
  const observer = new IntersectionObserver(function (entries) {
    for (let i = 0; i < entries.length; i++) {
      const item = entries[i]
      if (item.isIntersecting) {
        loadElement(item.target)
      }
    }
  })

  return {
    observeLazyLoadNode
  }
}
