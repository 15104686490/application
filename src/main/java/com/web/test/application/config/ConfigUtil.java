package com.web.test.application.config;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 获取配置的工具类
 */
public class ConfigUtil {
    public static String getStringConfig(String name) {
        return NacosUtil.getConfig(name);
    }

    /**
     * 获取单个字符型配置
     *
     * @param name
     * @return
     */
    public static int getIntegerConfig(String name) {
        return Integer.valueOf(NacosUtil.getConfig(name));
    }

    public static boolean getBooleanConfig(String name) {
        return Boolean.valueOf(NacosUtil.getConfig(name));
    }

    /**
     * 获取配置并进行切分
     * 格式：configString1;configString;
     *
     * @param name
     * @return
     */
    public static List<String> getStringConfigList(String name) {
        String config = NacosUtil.getConfig(name);
        List<String> configList = Arrays.asList(config.split(";"));
        return configList;
    }

}
