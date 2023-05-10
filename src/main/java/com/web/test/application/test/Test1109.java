package com.web.test.application.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1109 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        String code = String.format("%06d", time%10000L);
        System.out.println(code);


        String email = "455627872@qq.com";
        if ((email != null) && (!email.isEmpty())) {
            System.out.println(Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$",
                    email));
        }

        String str = "";



    }
}
