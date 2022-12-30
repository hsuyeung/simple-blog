package com.hsuyeung.blog.cache.lfu;

/**
 * 双层双向链表的内层双向链表中的单个结点的定义
 * <p>这一个结点即表示一个缓存数据</p>
 *
 * @author hsuyeung
 * @date 2022/12/04
 */
class Node {
    /**
     * 缓存的 key
     */
    String key;

    /**
     * 缓存的值
     */
    byte[] value;

    /**
     * 该缓存的访问频次
     */
    int freq;

    /**
     * 当前结点的前一个结点
     */
    Node prevNode;

    /**
     * 当前结点的下一个结点
     */
    Node nextNode;

    /**
     * 该结点所属的外层链表引用
     */
    DoubleLinkedList doubleLinkedList;


    /**
     * 创建一个空的 Node
     */
    Node() {
    }

    /**
     * 创建一个带缓存数据的 Node
     *
     * @param key   缓存的 key
     * @param value 缓存的 value
     */
    Node(String key, byte[] value) {
        this.key = key;
        this.value = value;
    }
}
