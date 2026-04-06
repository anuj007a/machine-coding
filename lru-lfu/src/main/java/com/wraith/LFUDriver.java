package org.wraith;

public class LFUDriver {
    public static void main(String[] args) {

        LFUCache cache = new LFUCache(2);

        cache.put(1, 1);
        cache.put(2, 2);

        System.out.println(cache.get(1)); // 1

        cache.put(3, 3); // evicts key 2

        System.out.println(cache.get(2)); // -1
        System.out.println(cache.get(3)); // 3

        cache.put(4, 4); // evicts key 1

        System.out.println(cache.get(1)); // -1
        System.out.println(cache.get(3)); // 3
        System.out.println(cache.get(4)); // 4
}
}
