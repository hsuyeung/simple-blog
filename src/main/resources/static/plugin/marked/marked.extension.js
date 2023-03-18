const caret = /(^|[^\[])\^/g
const _label = /(?:\[(?:\\.|[^\[\]\\])*\]|\\.|`[^`]*`|[^\[\]\\`])*?/
const _href = /<(?:\\.|[^\n<>\\])+>|[^\s\x00-\x1f]*/
const _iframeCode = /<iframe(([\s\S])*?)<\/iframe>/

function edit(regex, opt) {
  regex = typeof regex === 'string' ? regex : regex.source
  opt = opt || ''
  const obj = {
    replace: (name, val) => {
      val = val.source || val
      val = val.replace(caret, '$1')
      regex = regex.replace(name, val)
      return obj
    },
    getRegex: () => {
      return new RegExp(regex, opt)
    }
  }
  return obj
}

const livePhoto = {
  name: 'livePhoto',
  level: 'block',
  start(src) {
    return src.match(/![^!\n]/)?.index
  },
  tokenizer(src, tokens) {
    let rule = /^!?\[(label)\]\(\s*(photoSrc)?\s*\)\(\s*(videoSrc)?\s*\)/
    rule = edit(rule)
      .replace('label', _label)
      .replace('photoSrc', _href)
      .replace('videoSrc', _href)
      .getRegex()
    const match = rule.exec(src)
    if (match) {
      const token = {
        type: 'livePhoto',
        raw: match[0],
        text: match[0].trim(),
        label: match[1],
        photoSrc: match[2],
        videoSrc: match[3],
        tokens: []
      }
      this.lexer.inline(token.text, token.tokens)
      return token
    }
  },
  renderer(token) {
    return `
        <figure>
          <div class='livePhotoContainer'
              data-live-photo
              data-effect-type='live'
              data-playback-style='full'
              data-proactively-loads-video='true'
              data-photo-src='${token.photoSrc}'
              data-video-src='${token.videoSrc}'></div>
          <figcaption>${token.label}</figcaption>
        </figure>
      `
  }
}

const bilibiliPlayer = {
  name: 'bilibiliPlayer',
  level: 'block',
  start(src) {
    return src.match(/!bv[^!bv\n]/)?.index
  },
  tokenizer(src, tokens) {
    let rule = /^!bv?\{\{\s*(iframeCode)?\s*\}\}/
    rule = edit(rule)
      .replace('iframeCode', _iframeCode)
      .getRegex()
    const match = rule.exec(src)
    if (match) {
      const token = {
        type: 'bilibiliPlayer',
        raw: match[0],
        text: match[0].trim(),
        iframeCode: match[1],
        tokens: []
      }
      this.lexer.inline(token.text, token.tokens)
      return token
    }
  },
  renderer(token) {
    return `
          <div class='bilibili-aspect-ratio'>${token.iframeCode}</div>
      `
  }
}

marked.use({extensions: [livePhoto, bilibiliPlayer]})

