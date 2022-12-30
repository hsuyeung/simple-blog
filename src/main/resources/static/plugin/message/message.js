class Message {
  /**
   * 构造函数会在实例化的时候自动执行
   */
  constructor() {
    const containerId = 'message-container';
    // 检测下 html 中是否已经有这个 message-container 元素
    this.containerEl = document.getElementById(containerId);

    if (!this.containerEl) {
      // 创建一个 Element 对象，也就是创建一个 id 为 message-container 的 dom 结点
      this.containerEl = document.createElement('div');
      this.containerEl.id = containerId;
      // 把 message-container 元素放在 html 的 body 末尾
      document.body.appendChild(this.containerEl);
    }
  }

  show({type = 'info', text = '', duration = 2000, closeable = false}) {
    // 创建一个Element对象
    let messageEl = document.createElement('div');
    // 设置消息 class，这里加上 move-in 可以直接看到弹出效果
    messageEl.className = 'message move-in';
    // 消息内部 html 字符串
    messageEl.innerHTML = `
            <span class="icon icon-${type}"></span>
            <div class="text">${text}</div>
        `;

    // 是否展示关闭按钮
    if (closeable) {
      // 创建一个关闭按钮
      let closeEl = document.createElement('div');
      closeEl.className = 'close icon icon-close';
      // 把关闭按钮追加到message元素末尾
      messageEl.appendChild(closeEl);

      // 监听关闭按钮的 click 事件，触发后将调用我们的 close 方法
      // 我们把刚才写的移除消息封装为一个 close 方法
      closeEl.addEventListener('click', () => {
        this.close(messageEl)
      });
    }

    // 追加到 message-container 末尾
    // this.containerEl 属性是我们在构造函数中创建的 message-container 容器
    this.containerEl.appendChild(messageEl);

    // 只有当 duration 大于 0 的时候才设置定时器，这样我们的消息就会一直显示
    if (duration > 0) {
      // 用setTimeout来做一个定时器
      setTimeout(() => {
        this.close(messageEl);
      }, duration);
    }
  }

  /**
   * 关闭某个消息
   * 由于定时器里边要移除消息，然后用户手动关闭事件也要移除消息，所以我们直接把移除消息提取出来封装成一个方法
   * @param {Element} messageEl
   */
  close(messageEl) {
    // 首先把 move-in 这个弹出动画类给移除掉，要不然会有问题，可以自己测试下
    messageEl.className = messageEl.className.replace('move-in', '');
    // 增加一个 move-out 类
    messageEl.className += 'move-out';

    // move-out 动画结束后把元素的高度和边距都设置为0
    // 由于我们在 css 中设置了 transition 属性，所以会有一个过渡动画
    messageEl.addEventListener('animationend', () => {
      messageEl.setAttribute('style', 'height: 0; margin: 0');
    });

    // 这个地方是监听 transition 的过渡动画结束事件，在动画结束后把消息从 dom 树中移除。
    messageEl.addEventListener('transitionend', () => {
      // Element 对象内部有一个 remove 方法，调用之后可以将该元素从 dom 树中移除
      messageEl.remove();
    });
  }


}
