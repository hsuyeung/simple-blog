package com.hsuyeung.blog.cache.lfu;

import java.util.HashMap;
import java.util.Map;

import static com.hsuyeung.blog.constant.CommonConstants.ONE_MB;

/**
 * LFU 缓存算法实现
 *
 * @author hsuyeung
 * @date 2022/11/27
 */
public class LFUCache {
    /**
     * 默认缓存空间大小 32MB
     */
    private static final int DEFAULT_SIZE = 32 * ONE_MB;

    /**
     * 缓存数据
     */
    private final Map<String, Node> cache;

    /**
     * 哨兵结点，由 DoubleLinkedList 组成的双向链表的头结点
     */
    private final DoubleLinkedList headList;

    /**
     * 哨兵结点，由 DoubleLinkedList 组成的双向链表的尾结点
     */
    private final DoubleLinkedList tailList;

    /**
     * 总的缓存空间，单位 Byte
     */
    private final int totalSize;

    /**
     * 剩余的缓存空间，单位 Byte
     */
    private int unusedSize;

    /**
     * 创建一个 LFU 缓存
     *
     * @param totalSize 分配的缓存空间，单位 MB
     */
    public LFUCache(int totalSize) {
        if (totalSize <= 0) {
            totalSize = DEFAULT_SIZE;
        }
        this.unusedSize = this.totalSize = totalSize * ONE_MB;
        // 估计每个缓存大小为 1MB
        this.cache = new HashMap<>(totalSize / ONE_MB);
        // 初始化外层链表的头尾结点，作为哨兵结点
        headList = new DoubleLinkedList();
        tailList = new DoubleLinkedList();
        headList.nextList = tailList;
        tailList.prevList = headList;
    }

    /**
     * 获取缓存数据
     *
     * @param key 缓存的 key
     * @return 缓存的 value
     */
    public byte[] get(String key) {
        Node node = cache.get(key);
        if (node == null) {
            return new byte[0];
        }
        this.freqInc(node);
        return node.value;
    }

    /**
     * 设置新的缓存，如果 key 已经存在，则更新以前的缓存数据
     *
     * @param key   缓存的 key
     * @param value 缓存数据
     */
    public synchronized void put(String key, byte[] value) {
        int valLen = value.length;
        if (valLen > totalSize) {
            // 如果放入的缓存数据比总缓存空间还大，则直接返回
            return;
        }
        Node node = cache.get(key);
        if (node != null) {
            node.value = value;
            return;
        }
        while (unusedSize < valLen) {
            // 如果缓存空间不足，则需要把访问频次最小的，且最久未访问的结点删除
            // 由 DoubleLinkedList 组成的外层双向链表访问频次从前往后依次减小，故最小频次的外层双向链表结点是 tailList.prevList
            // DoubleLinkedList 中的内层双向 Node 链表采用的是头插法，因此最小频次且最久未访问的元素是 tailList.prevList.tailNode.prevNode
            // 最小频次 DoubleLinkedList
            DoubleLinkedList minFreqList = tailList.prevList;
            // 最久未访问的元素，需要删除
            Node deadNode = minFreqList.tailNode.prevNode;
            // 删除缓存
            cache.remove(deadNode.key);
            // 删除内层链表结点
            minFreqList.removeNode(deadNode);
            // 缓存空间增加
            unusedSize += deadNode.value.length;
            // 如果删除内层链表结点之后，外层双向链表空了，则删除此外层双向链表结点
            if (minFreqList.isEmpty()) {
                this.removeDoubleLinkedList(minFreqList);
            }
            if (this.isEmpty()) {
                // 如果外层双向链表为空了都还没腾出足够的缓存空间，则直接返回
                return;
            }
        }
        // 有足够的缓存空间，则新建一个 Node
        Node newNode = new Node(key, value);
        cache.put(key, newNode);
        // 判断频次为 1 的外层双向链表是否存在，不存在则新建
        DoubleLinkedList lastList = tailList.prevList;
        if (lastList.freq != 1) {
            DoubleLinkedList newList = new DoubleLinkedList(1);
            this.addDoubleLinkedList(newList, lastList);
            newList.addNode(newNode);
        } else {
            lastList.addNode(newNode);
        }
        // 可用空间减少
        unusedSize -= valLen;
    }

    // --------------------------------------------- PRIVATE METHOD REGION ---------------------------------------------

    /**
     * 缓存的访问频次 +1
     *
     * @param node 访问的结点
     */
    private void freqInc(Node node) {
        DoubleLinkedList outerList = node.doubleLinkedList;
        if (outerList == null) {
            return;
        }
        // 从当前频次的 outerList 中移除当前 Node
        outerList.removeNode(node);
        // 如果当前 outerList 中的双向 Node 链表没有数据了，则删除此 outerList
        if (outerList.isEmpty()) {
            this.removeDoubleLinkedList(outerList);
        }
        // 当前 Node 频次加 1
        node.freq++;
        // 找到当前 outerList 前面的 outerList，并把当前 Node 加入进去
        DoubleLinkedList prevList = outerList.prevList;
        // 如果前面的 outerList 不存在，则新建一个，并插入到由 outerList 组成的双向链表中
        // 前 outerList 的频次不等于当前 node 频次，则说明不存在
        if (prevList.freq != node.freq) {
            DoubleLinkedList newList = new DoubleLinkedList(node.freq);
            this.addDoubleLinkedList(newList, prevList);
            newList.addNode(node);
        } else {
            prevList.addNode(node);
        }
    }

    /**
     * 从外层双向链表中删除一个结点
     *
     * @param list 要删除的外层双向链表结点
     */
    private void removeDoubleLinkedList(DoubleLinkedList list) {
        list.prevList.nextList = list.nextList;
        list.nextList.prevList = list.prevList;
    }

    /**
     * 将新的外层双向链表插入到指定结点后面
     *
     * @param newList    新结点
     * @param targetList 目标结点
     */
    private void addDoubleLinkedList(DoubleLinkedList newList, DoubleLinkedList targetList) {
        newList.prevList = targetList;
        newList.nextList = targetList.nextList;
        targetList.nextList.prevList = newList;
        targetList.nextList = newList;
    }

    /**
     * 判断当前外层双向链表是否为空
     *
     * @return 当外层双向链表的头结点指向尾结点时返回 true，否则返回 false
     */
    private boolean isEmpty() {
        return this.headList.nextList == this.tailList;
    }
}
