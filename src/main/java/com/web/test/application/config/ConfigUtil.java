package com.web.test.application.config;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigUtil {
    public static String getStringConfig(String name) {
        return NacosUtil.getConfig(name);
    }

    public static int getIntegerConfig(String name) {
        return Integer.valueOf(NacosUtil.getConfig(name));
    }

    public static List<String> getStringConfigList(String name) {
        String config = NacosUtil.getConfig(name);
        List<String> configList = Arrays.asList(config.split(";"));
        return configList;
    }

}
