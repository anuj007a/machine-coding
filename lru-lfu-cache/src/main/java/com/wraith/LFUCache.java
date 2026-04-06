package com.wraith;


import com.wraith.model.DoublyLinkedList;
import com.wraith.model.Node;

import java.util.HashMap;
import java.util.Map;

public class LFUCache {

    private final int capacity;
    private int minFreq;
    private final Map<Integer, Node> keyMap;
    private final Map<Integer, DoublyLinkedList> freqMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.keyMap = new HashMap<>();
        this.freqMap = new HashMap<>();
    }

    public int get(int key) {
        if (!keyMap.containsKey(key)) return -1;

        Node node = keyMap.get(key);
        updateFreq(node);

        return node.getValue();
    }

    public void put(int key, int value) {
        if (capacity == 0) return;

        if (keyMap.containsKey(key)) {
            Node node = keyMap.get(key);
            node.setValue(value);
            updateFreq(node);
            return;
        }

        if (keyMap.size() == capacity) {
            DoublyLinkedList minList = freqMap.get(minFreq);
            Node evicted = minList.removeLast();
            keyMap.remove(evicted.getKey());
        }

        Node newNode = new Node(key, value);
        keyMap.put(key, newNode);

        freqMap.computeIfAbsent(1, k -> new DoublyLinkedList())
                .add(newNode);

        minFreq = 1;
    }

    private void updateFreq(Node node) {
        int freq = node.getFreq();
        DoublyLinkedList list = freqMap.get(freq);

        list.remove(node);

        if (freq == minFreq && list.isEmpty()) {
            minFreq++;
        }

        node.setFreq(++freq);

        freqMap.computeIfAbsent(node.getFreq(), k -> new DoublyLinkedList())
                .add(node);
    }
}