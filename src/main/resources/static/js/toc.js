function setupToc() {
  const article = document.getElementsByTagName("article")[0]
  if (article) {
    const headings = article.querySelectorAll(".article-body h1, .article-body h2, .article-body h3, .article-body h4, .article-body h5, .article-body h6")

    // 检查是否有任何标题元素，如果没有则返回，不生成目录
    if (headings.length === 0) {
      return
    }

    const t = document.createElement("div")
    t.classList.add("toc-wrapper")
    document.getElementsByClassName("container")[0].insertBefore(t, article)

    const o = document.createElement("div")
    const n = "toc-inner"

    let tocContent = '<div class="' + n + '"><ul>'
    headings.forEach(function (heading) {
      let id = heading.id
      if (!id) {
        id = 'toc-' + Math.random().toString(36).substring(2, 11)
        heading.id = id
      }
      const tagName = heading.tagName.toLowerCase()
      const indentClass = 'toc-' + tagName
      tocContent += '<li class="' + indentClass + '"><a href="#' + id + '"><span class="box"></span>' + heading.innerText + '</a></li>'
    })
    tocContent += '</ul></div>'

    o.classList.add("toc");
    o.innerHTML = '<div class="toc-head"><b class="toc-head-title">目录</b><a class="toc-head-top" href="javascript:void(0);" onclick="window.scrollTo({top: 0});">↑ 顶部</a></div>' + tocContent + '<div class="toc-tail"><a class="toc-tail-comment" href="javascript:void(0);" onclick="goCommentAndLoad()">评论</a><a class="toc-tail-contact" href="/about#contact">找到我</a></div><div class="toc-widget"><ul><li> <a href="javascript:void(0);" onclick="jumpToMoreArticle()">更多文章</a> </li></ul></div>'
    t.append(o)

    let isClicking = false
    let intersectionObserver

    const observeHeadings = () => {
      intersectionObserver = new IntersectionObserver(function (entries) {
        if (isClicking) return
        entries.forEach(function (entry) {
          const t = '.toc li a[href="#' + entry.target.getAttribute("id") + '"]'
          const tocLink = document.querySelector(t)
          if (tocLink && entry.isIntersecting) {
            document.querySelectorAll(".toc li.active").forEach(function (activeItem) {
              activeItem.classList.remove("active")
            })
            tocLink.parentElement.classList.add("active")
            tocLink.scrollIntoView({block: "center"})
          }
        })
      })

      headings.forEach(function (heading) {
        intersectionObserver.observe(heading)
      })
    }

    observeHeadings()

    document.querySelectorAll(".toc li a").forEach(function (link) {
      link.addEventListener("click", function (event) {
        event.preventDefault()
        const targetId = link.getAttribute("href").substring(1)
        const targetElement = document.getElementById(targetId)
        if (targetElement) {
          isClicking = true
          intersectionObserver.disconnect()
          targetElement.scrollIntoView({behavior: "smooth", block: "start"})
          document.querySelectorAll(".toc li.active").forEach(function (activeItem) {
            activeItem.classList.remove("active")
          })
          link.parentElement.classList.add("active")

          setTimeout(function () {
            isClicking = false
            observeHeadings()
          }, 1000)

          history.pushState(null, null, '#' + targetId)
        }
      })
    })

    window.addEventListener("hashchange", function () {
      const hash = window.location.hash.substring(1)
      const targetElement = document.getElementById(hash)
      if (targetElement) {
        targetElement.scrollIntoView({behavior: "smooth", block: "start"})

        document.querySelectorAll(".toc li.active").forEach(function (activeItem) {
          activeItem.classList.remove("active")
        })
        const activeLink = document.querySelector('.toc li a[href="#' + hash + '"]')
        if (activeLink) {
          activeLink.parentElement.classList.add("active")
        }
      }
    })
  }
}

function goCommentAndLoad() {
  document.getElementById('comment-textarea-0').focus()
}

function jumpToMoreArticle() {
  window.location.href = '/archive'
}
