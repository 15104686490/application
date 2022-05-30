package com.web.test.application.test;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Properties;

/**
 * 从nacos获取配置（测试）
 */
@Deprecated
public class ConfigTest {
    /*static ConfigService configService;

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

    private static String getConfig(String configName) {
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

    public static void main(String[] args) {
        for (int i = 0; i <= 10; i++) {
            long start = System.currentTimeMillis();
            System.out.println(getConfig("test"));
            System.out.println("cost of get config :" + (System.currentTimeMillis() - start));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/
}
