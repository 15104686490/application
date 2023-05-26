package com.web.test.application.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;

/**
 * nacos工具类，初始化client，提供从nacos获取配置的工具类
 * （暂未提供修改nacos配置的方法）
 */
@Slf4j
public class NacosUtil {
    static ConfigService configService;


    static {
        log.info("nacos client is initializing");
        Properties properties = new Properties();
        //nocas 配置中心地址
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, "192.168.196.220:8848");
        try {
            configService = NacosFactory.createConfigService(properties);
            log.info("nacos client initializated successful");
        } catch (NacosException e) {
            e.printStackTrace();
            log.info("some errors occured during nacos client initializated");
        }
    }


    /**
     * 获取根据名称，从nacos获取配置
     *
     * @param configName
     * @return
     */
    public static String getConfig(String configName) {
        try {
            long start = System.currentTimeMillis();
            //配置名称，group，可根据group划分不同用途的配置，超时时间
            String config = configService.getConfig(configName, "DEFAULT_GROUP", 5000);
            // System.out.println("cost of get config :" + (System.currentTimeMillis() - start));
            //通过该日志从nacos获取配置的耗时情况
            log.info("cost of get config :" + (System.currentTimeMillis() - start));
            return config;
        } catch (NacosException e) {
            log.error("get config failed (nacos)");
            e.printStackTrace();
            return null;
        }
    }
}
