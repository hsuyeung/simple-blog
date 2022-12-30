function subAndAppend(str, len, append) {
  if (typeof str !== 'string') {
    return ''
  }
  if (str.length <= len) {
    return str
  }
  return str.substring(0, len) + append
}
