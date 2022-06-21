package com.web.test.application.test;

import com.web.test.application.config.NacosUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式测试
 */
public class PatternTest {
    public static void main(String[] args) {

        String txt = "我的天这真是个水电工程哎呀我天呐啊啊啊啊啊编制规程AB/S12345-2022红红火火恍恍惚惚和我的天这真是个水电工程哎呀我天呐啊啊啊啊啊编制规程AB/S12345-2022红红火火恍恍惚惚";
        String regx = "水电工程[\\u4e00-\\u9fa5]{0,20}编制规程[A-Z]{2}/[A-Z]{1}[0-9]{5}-[0-9]{4}";


        String txt1 = "我 的  天这   真 是   个   水电   工程";
        String regx1 = "[ ]{0,20}";

        // Pattern patten = Pattern.compile("《.*(.*)》.*(.*)");//编译正则表达式
        Pattern patten = Pattern.compile(regx);//编译正则表达式
        Matcher matcher = patten.matcher(txt);// 指定要匹配的字符串

        // List<String> matchStrs = new ArrayList<>();

        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            // result.add(matcher.group());//获取当前匹配的值
            System.out.println(matcher.group());
        }


        // Pattern patten = Pattern.compile("《.*(.*)》.*(.*)");//编译正则表达式
        Pattern patten1 = Pattern.compile(regx1);//编译正则表达式
        Matcher matcher1 = patten.matcher(txt1);// 指定要匹配的字符串

        // List<String> matchStrs = new ArrayList<>();

        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            // result.add(matcher.group());//获取当前匹配的值
            // System.out.println(matcher.group().replaceAll(,""));
        }
    }
}
