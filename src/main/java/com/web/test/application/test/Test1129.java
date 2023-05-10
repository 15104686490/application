package com.web.test.application.test;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.test
 * @Project：application
 * @name：Test1129
 * @Date：2022/11/29 16:51
 * @Filename：Test1129
 */
public class Test1129 {
    public static void main(String[] args) {
        String str = "《公共建筑节能设计标准》（GB50189-2015）";
        System.out.println(str.split("《公共建筑节能设计标准》").length);
        String[] strList = str.split("《公共建筑节能设计标准》");
        for (String str1 : strList) {
            System.out.println(str1);
        }
        System.out.println(str.replace("《公共建筑节能设计标准》", ""));
    }
}
