package com.web.test.application.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {
    /**
     * 正则匹配方法（固定正则表达式）,从配置中心获取正则表达式,
     * @param text
     * @return
     */
    @Deprecated
    public static List<String> getPatternStringList(String text) {
        List<String> result = new ArrayList<>();
        if (text == null || text.length() == 0) {
            // System.out.println("text is null!!!");
            return result;
        }
        String regx = ConfigTest.getConfig("regx");
        // Pattern patten = Pattern.compile("《.*(.*)》.*(.*)");//编译正则表达式
        Pattern patten = Pattern.compile(regx);//编译正则表达式
        Matcher matcher = patten.matcher(text);// 指定要匹配的字符串

        // List<String> matchStrs = new ArrayList<>();

        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            result.add(matcher.group());//获取当前匹配的值
        }

        /*for (int i = 0; i < matchStrs.size(); i++) {
            System.out.println(matchStrs.get(i));
        }*/

        return result;
    }




    /**
     * 传入正则表达式
     * @param text
     * @param regex
     * @return
     */
    public static List<String> getPatternStringList(String text, String regex) {
        List<String> result = new ArrayList<>();
        if (text == null || text.length() == 0) {
            // System.out.println("text is null!!!");
            return result;
        }
        Pattern patten = Pattern.compile(regex);//编译正则表达式
        Matcher matcher = patten.matcher(text);// 指定要匹配的字符串

        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            result.add(matcher.group());//获取当前匹配的值
        }
        return result;
    }

    /**
     * 支持从配置中心等位置获取正则表达式并进行匹配
     * @param text
     * @return
     */
    public static List<String> getPatternStringListWithConfigRules(String text){
        return null;
    }

    public static void main(String[] args) {
        for (String str : getPatternStringList("《123（123-123）》")) {
            System.out.println(str);
        }
    }
}
