package com.web.test.application.task;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.web.test.application.config.ConfigUtil;
import com.web.test.application.dao.BaseMapper;
import com.web.test.application.model.RuleSingleton;
import com.web.test.application.service.RulesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务，定期扫表更新规则存储map
 */
@Slf4j
@Component
public class QueryRuleTask {

    @Autowired
    BaseMapper baseMapper;



    /*@Scheduled(cron = "0 0/5 * * * ? ")
    public void test() {
        log.error("两秒一次");
        log.error("" + baseMapper.queryRulesList().size());
    }*/


    /**
     * 定时任务更新内存中的规范存储map
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void updateMap() {
        log.error("更新规范Map");
        List<RuleSingleton> list = baseMapper.queryRulesList();
        list.forEach(r -> {
           /* if (ConfigUtil.getBooleanConfig("")) {
                log.error("task   " + r.getCnName());
            }*/
            RulesService.getRulesMap().put(r.getFullName(), r);
        });
    }

    public static void main(String[] args) {
        while(true){
            Object o = new Object();
        }
    }
}
