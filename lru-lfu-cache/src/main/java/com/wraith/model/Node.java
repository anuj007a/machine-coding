package com.wraith.model;

public class Node {
    int key;
    int value;
    int freq;

    Node prev;
    Node next;

    public Node(int key, int value) {
        this.key = key;
        this.value = value;
        this.freq = 1;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getFreq() {
        return freq;
    }

    public int getKey() {
        return key;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }
}