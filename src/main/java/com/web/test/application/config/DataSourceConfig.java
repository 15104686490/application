package com.web.test.application.config;


import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DataSourceConfig {

    /**
     * 利用 nacos 中配置初始化 datasource
     * @return
     */
    @Bean
    public DataSource dataSource(){

        String url = ConfigUtil.getStringConfig("mysql-url");
        String driverClass = ConfigUtil.getStringConfig("mysql-driver-class");
        String userName = ConfigUtil.getStringConfig("mysql-user-name");
        String password = ConfigUtil.getStringConfig("mysql-password");
        String poolName = ConfigUtil.getStringConfig("mysql-pool-name");
        HikariDataSource hikariDataSource = new HikariDataSource();
        try {

            hikariDataSource.setJdbcUrl(url);
            hikariDataSource.setDriverClassName(driverClass);
            hikariDataSource.setUsername(userName);
            hikariDataSource.setPassword(password);
            hikariDataSource.setPoolName(poolName);
            return hikariDataSource;
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("连接池初始化失败");
            log.error(e.getMessage());
        }
        return hikariDataSource;
    }
}
