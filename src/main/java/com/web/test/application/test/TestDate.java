package com.web.test.application.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.test
 * @Project：application
 * @name：TestDate
 * @Date：2023/1/31 8:15
 * @Filename：TestDate
 */
public class TestDate {
    public static void main(String[] args) {
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy/M/d");
        System.out.println(dateFormat.format(date));



            Date date1 = null;
            //实现将字符串转成⽇期类型
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/M/d");
            try {
                date1 = dateFormat1.parse("2023/1/31");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(dateFormat.format(date1));
            System.out.println(date1.after(date));
            System.out.println(date1.before(date));
            System.out.println(dateFormat.format(date1).equals(dateFormat.format(date)));
    }
}
