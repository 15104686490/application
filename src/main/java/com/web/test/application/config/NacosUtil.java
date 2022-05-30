package com.web.test.application.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Properties;

public class NacosUtil {
    static ConfigService configService;

    static {
        Properties properties = new Properties();
        //nocas 配置中心地址
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, "localhost:8848");
        try {
            configService = NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }


    public static String getConfig(String configName) {
        try {
            long start = System.currentTimeMillis();
            String config = configService.getConfig(configName, "DEFAULT_GROUP", 5000);
            System.out.println("cost of get config :" + (System.currentTimeMillis() - start));
            return config;
        } catch (NacosException e) {
            e.printStackTrace();
            return null;
        }
    }
}
