function subAndAppend(str, len, append) {
  if (typeof str !== 'string') {
    return ''
  }
  if (str.length <= len) {
    return str
  }
  return str.substring(0, len) + append
}

function encode(str) {
  return encodeURIComponent(str)
          .replace(/'/g, '%27')
          .replace(/\(/g, '%28')
          .replace(/\)/g, '%29');
}

function decode(str) {
  return decodeURIComponent(str)
          .replace(/%27/g, "'")
          .replace(/%22/g, '"')
          .replace(/%28/g, '(')
          .replace(/%29/g, ')');
}
