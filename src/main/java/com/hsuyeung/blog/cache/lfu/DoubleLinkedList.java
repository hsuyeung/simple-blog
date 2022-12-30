package com.hsuyeung.blog.cache.lfu;

/**
 * 双层双向链表的外层双向链表的单个结点定义
 * <p>这一个结点上存储的内层 Node 双向链表的访问频次均相同</p>
 *
 * @author hsuyeung
 * @date 2022/12/04
 */
class DoubleLinkedList {
    /**
     * 当前外层双向链表上的内层 Node 双向链表上所有 Node 的访问频次
     */
    int freq;
    /**
     * 哨兵结点，当前外层双向链表上的内层 Node 双向链表的头结点
     */
    Node headNode;
    /**
     * 哨兵结点，当前外层双向链表上的内层 Node 双向链表的尾结点
     */
    Node tailNode;
    /**
     * 当前外层双向链表的前一个双向链表
     */
    DoubleLinkedList prevList;
    /**
     * 当前外层双向链表的后一个双向链表
     */
    DoubleLinkedList nextList;

    /**
     * 创建一个空的外层双向链表
     */
    DoubleLinkedList() {
        // 初始化内部链表的头尾结点，并作为哨兵结点
        headNode = new Node();
        tailNode = new Node();
        headNode.nextNode = tailNode;
        tailNode.prevNode = headNode;
    }

    /**
     * 创建一个带初始访问频次的外层双向链表
     *
     * @param freq 该条双向链表上的数据的访问频次
     */
    DoubleLinkedList(int freq) {
        this();
        this.freq = freq;
    }

    /**
     * 删除当前外层双向链表中上的某个 Node 结点
     *
     * @param node 要删除的 Node 结点
     */
    void removeNode(Node node) {
        node.prevNode.nextNode = node.nextNode;
        node.nextNode.prevNode = node.prevNode;
    }

    /**
     * 头插法将新的 Node 结点插入到当前外层双向链表中，并在新 Node 结点中记录当前双向链表的引用
     *
     * @param node 新插入的 Node 结点
     */
    void addNode(Node node) {
        node.prevNode = headNode;
        node.nextNode = headNode.nextNode;
        headNode.nextNode.prevNode = node;
        headNode.nextNode = node;
        node.doubleLinkedList = this;
    }

    /**
     * 判断当前双向链表中是否存在有效结点
     *
     * @return 当头结点的 next 指向尾结点则返回 false，否则返回 true
     */
    boolean isEmpty() {
        return headNode.nextNode == tailNode;
    }
}
