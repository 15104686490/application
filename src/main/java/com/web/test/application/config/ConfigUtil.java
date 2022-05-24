package com.web.test.application.config;

import com.web.test.application.test.ConfigTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigUtil {
    public static String getStringConfig(String name) {
        return ConfigTest.getConfig(name);
    }

    public static int getIntegerConfig(String name) {
        return Integer.valueOf(ConfigTest.getConfig(name));
    }

    public static List<String> getStringConfigList(String name) {
        String config = ConfigTest.getConfig(name);
        List<String> configList = Arrays.asList(config.split(";"));
        return configList;
    }

}
