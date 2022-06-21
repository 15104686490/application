package com.web.test.application.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExeTest {

    static String path = "C:\\Users\\dell\\Desktop\\抽水蓄能一维水沙计算\\输入输出文件\\抽水蓄能电站一维水沙计算.exe";

    public static void main(String[] args) {
        try {
            //执行exe  cmd可以为字符串(exe存放路径)也可为数组，调用exe时需要传入参数时，可以传数组调用(参数有顺序要求)
            Process p = Runtime.getRuntime().exec(path);
            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = br.readLine()) != null  || (line = brError.readLine()) != null) {
                //输出exe输出的信息以及错误信息
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /*if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        }
    }
}
