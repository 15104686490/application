package com.web.test.application.test;

public class StartStringTest {
    public static void main(String[] args) {
        String str1 = "你好";
        String str2 = "你好晚上好晚上好晚上好晚上好晚上好晚上好晚上好晚上好晚上好晚上好晚上好晚上好晚上好你好";
        System.out.println(str2.indexOf(str1));//第一次出现的位置
        System.out.println(str2.substring(str2.indexOf(str1) + str1.length()));
    }
}
