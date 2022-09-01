package com.web.test.application.test;

import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

public class TestCopy {
    public static void main(String[] args) {
       /* ArrayList arrayList = new ArrayList();
        LinkedList list = new LinkedList();
        Queue queue = new LinkedList();
        queue.offer(new Object());// 队列 stack不建议再使用
        Deque deque = new LinkedList();// 双端队列
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayList.add(new Object());
        int[] array = {0, 1, 2, 3};
        System.arraycopy(array, 2, array, 1, 2);
        for (int i : array) {
            System.out.print(i + " ");
        }


        DelayQueue delayQueue = new DelayQueue();
        //delayQueue.offer(new Delayed());*/

        // java 7 _
        int a = 123_123;
        System.out.println(a);
        System.out.println(1 & 1);
        System.out.println(Integer.toBinaryString(1 << 3));
        System.out.println(Integer.toBinaryString(-1 >>> 10));
        int i = 9;
        System.out.println(i < 10 ? i * 100 : i + 1);
    }
}
