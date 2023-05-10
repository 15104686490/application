package com.web.test.application.test;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.test
 * @Project：application
 * @name：Test0407
 * @Date：2023/4/7 10:40
 * @Filename：Test0407
 */
public class Test0407 {
    public static void main(String[] args) {
        String str = "C:\\Users\\lzy15\\Desktop\\建设方案0128";
        str = str.replaceAll("\\\\", "");
        str = str.replaceAll(":", "");
        System.out.println(str);
    }
}
