package com.web.test.application.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class NacosUtil {
    static ConfigService configService;

    static {
        log.info("nacos client is initializing");
        Properties properties = new Properties();
        //nocas 配置中心地址
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, "localhost:8848");
        try {
            configService = NacosFactory.createConfigService(properties);
            log.info("nacos client initializated successful");
        } catch (NacosException e) {
            e.printStackTrace();
            log.info("some errors occured during nacos client initializated");
        }
    }


    public static String getConfig(String configName) {
        try {
            long start = System.currentTimeMillis();
            String config = configService.getConfig(configName, "DEFAULT_GROUP", 5000);
            // System.out.println("cost of get config :" + (System.currentTimeMillis() - start));
            log.info("cost of get config :" + (System.currentTimeMillis() - start));
            return config;
        } catch (NacosException e) {
            log.error("get config failed (nacos)");
            e.printStackTrace();
            return null;
        }
    }
}
