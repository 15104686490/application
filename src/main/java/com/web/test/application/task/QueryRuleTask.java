package com.web.test.application.task;

import com.web.test.application.dao.BaseMapper;
import com.web.test.application.model.RuleSingleton;
import com.web.test.application.service.RulesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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


    @Scheduled(cron = "0 0/5 * * * ? ")
    public void updateMap() {
        log.error("更新规范Map");
        List<RuleSingleton> list = baseMapper.queryRulesList();
        list.forEach(r -> {
            log.error("task   " + r.getFullName());
            RulesService.getRulesMap().put(r.getFullName(), r);
        });
    }

    public static void main(String[] args) {

    }
}
