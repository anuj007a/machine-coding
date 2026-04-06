package org.wraith.model;

public class DoublyLinkedList {

    Node head;
    Node tail;
    int size;

    public DoublyLinkedList() {
        head = new Node(-1, -1);
        tail = new Node(-1, -1);

        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    public void add(Node node) {
        Node next = head.next;
        head.next = node;
        node.prev = head;

        node.next = next;
        next.prev = node;

        size++;
    }

    public void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    public Node removeLast() {
        if (size == 0) return null;
        Node node = tail.prev;
        remove(node);
        return node;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}