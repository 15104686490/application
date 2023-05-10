package com.web.test.application.test;

public class GetTimeFromCodeTest {
    public static void main(String[] args) {
        String str = "GB/T11828.5-2011";
        int index = str.lastIndexOf("-");
        System.out.println(str.length());
        System.out.println(index);
        System.out.println(str.substring(index + 1, str.length()));
        System.out.println(str.substring(0, index));
        System.out.println(str.substring(index + 1, str.length()).length() == 4);
        String[] array = str.split("-");
        System.out.println(array[array.length - 1]);
        System.out.println(array[array.length - 1].length() == 4);
    }
}
