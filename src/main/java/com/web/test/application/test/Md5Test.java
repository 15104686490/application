package com.web.test.application.test;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 测试字符串 MD5 转换
 */
public class Md5Test {
    public static void main(String[] args) {
        /*String str = DigestUtils.md5Hex("admin" + "fileName") + System.currentTimeMillis();
        System.out.println(str);*/
        String str1 = "《规范1》（GB123123）";
        String str2 = "《规范1》（GB123123-）";
        String strM = DigestUtils.md5Hex(str1);
        String strM1 = DigestUtils.md5Hex(str1);
        String strM2 = DigestUtils.md5Hex(str2);
        System.out.println(strM);
        System.out.println(strM1);
        System.out.println(strM.equals(strM1));
        System.out.println(strM2);
    }
}
