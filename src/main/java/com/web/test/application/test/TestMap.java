package com.web.test.application.test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TestMap {
    public static void main(String[] args) {
        ConcurrentMap concurrentMap = new ConcurrentHashMap();
        ConcurrentMap concurrentMap1 = new ConcurrentHashMap();
        concurrentMap.put("123", "123");
        concurrentMap1.putAll(concurrentMap);
        concurrentMap.clear();
        concurrentMap1.forEach((k, v) -> {
            System.out.println(k + " " + v);
        });

        concurrentMap.put("456", "456");
        concurrentMap.forEach((k, v) -> {
            System.out.println(k + " " + v);
        });

    }
}
