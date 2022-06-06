package com.web.test.application.test;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 测试字符串 MD5 转换
 */
public class Md5Test {
    public static void main(String[] args) {
        String str = DigestUtils.md5Hex("admin" + "fileName") + System.currentTimeMillis();
        System.out.println(str);
    }
}
