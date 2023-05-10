package com.web.test.application.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.test
 * @Project：application
 * @name：ThreadTest
 * @Date：2023/1/4 14:07
 * @Filename：ThreadTest
 */
public class ThreadTest {
    static  int i = 0;
    static AtomicInteger j = new AtomicInteger(0);

    static  int k = 0;
    static int a = 0;
    static int b = 0;

    public static synchronized void add(){
        i++;
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (a < 100000) {
                    a++;
                    k++;
                    add();
                    j.incrementAndGet();
                    System.out.println(Thread.currentThread().getName() + " " + i);

                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (b < 100000) {
                    b++;
                    k++;
                    add();
                    j.incrementAndGet();
                    System.out.println(Thread.currentThread().getName() + " " + i);
                }
            }
        });
        t1.start();
        t2.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("------------------"+i);
        System.out.println("******************"+j.toString());
        System.out.println("******************"+k);

    }
}
