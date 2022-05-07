package com.web.test.application.test;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5Test {
    public static void main(String[] args) {
        String str = DigestUtils.md5Hex("admin" + "fileName") + System.currentTimeMillis();
        System.out.println(str);
    }
}
