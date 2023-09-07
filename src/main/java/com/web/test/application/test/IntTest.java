package com.web.test.application.test;


public class IntTest {


    /**
     * @param args
     */
    public static void main(String[] args) {
        int i = (int) System.currentTimeMillis();
        long l = System.currentTimeMillis();
        System.out.println(i);
        System.out.println((int) l);

        String str = "XX=XX";
        String[] strs = str.split("=");
        strs[0].toCharArray();
        
    }
}
