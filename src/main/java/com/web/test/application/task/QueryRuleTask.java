package com.web.test.application.task;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.web.test.application.config.ConfigUtil;
import com.web.test.application.dao.BaseMapper;
import com.web.test.application.model.CollectRuleSingleton;
import com.web.test.application.model.ExpireRuleSingleton;
import com.web.test.application.model.RuleSingleton;
import com.web.test.application.service.RulesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 定时任务，定期扫表更新规则存储map
 */
@Slf4j
@Component
public class QueryRuleTask {

    @Autowired
    BaseMapper baseMapper;

    public static boolean RULES_FLAG = false;







    /*@Scheduled(cron = "0 0/5 * * * ? ")
    public void test() {
        log.error("两秒一次");
        log.error("" + baseMapper.queryRulesList().size());
    }*/


    /**
     * 定时任务更新内存中的规范存储map
     */
    @Scheduled(cron = "0 */3 * * * ?")
    public void updateMap() {
        boolean executeFlag = true;
        executeFlag = ConfigUtil.getBooleanConfig("execute_flag");

        if (executeFlag) {
            try {
                log.error("更新规范Map");
                List<RuleSingleton> list = baseMapper.queryRulesList();
                ConcurrentMap<String, RuleSingleton> tempMap = new ConcurrentHashMap<>();
                list.forEach(r -> {
                    tempMap.put(r.getFullName(), r);
                });
                RulesService.setRulesMap(tempMap);
                if (!RulesService.getRulesMap().isEmpty()) {
                    RULES_FLAG = true;
                }
            } catch (Exception e) {
                log.error("updateMap 任务执行异常终止");
                log.error(e.getMessage());
            }
        } else {
            log.error("Map暂停更新");
        }
    }

    @Scheduled(cron = "0 */3 * * * ?")
    public void updateExpireRulesMap() {
        boolean executeFlag = true;
        executeFlag = ConfigUtil.getBooleanConfig("execute_expire_flag");

        if (executeFlag) {
            try {
                log.error("更新过期规范Map");
                List<ExpireRuleSingleton> list = baseMapper.queryExpireRulesList();
                ConcurrentMap<String, ExpireRuleSingleton> tempMap = new ConcurrentHashMap<>();
                list.forEach(r -> {
                    tempMap.put(r.getFullName(), r);
                });
                RulesService.setExpireRulesMap(tempMap);
                if (!RulesService.getRulesMap().isEmpty()) {
                    RULES_FLAG = true;
                }
            } catch (Exception e) {
                log.error("updateExpireRulesMap 任务执行异常终止");
                log.error(e.getMessage());
            }
        } else {
            log.error("过期标准Map暂停更新");
        }
    }

    /**
     *
     */
    /*@Scheduled(cron = "")
    public void checkRules(){

    }*/

    /*public static void main(String[] args) {

    }*/

    // 0 */1 * * * ?
    @Scheduled(cron = "0 */1 * * * ?")
    public void updataCollectRules() {
        boolean executeFlag = true;
        executeFlag = ConfigUtil.getBooleanConfig("execute_flag");

        if (executeFlag) {
            try {
                log.error("更新Collect Rules Map");
                List<CollectRuleSingleton> list = baseMapper.queryCollectRules();
                ConcurrentMap<String, CollectRuleSingleton> tempMap = new ConcurrentHashMap<>();

                list.forEach(r -> {
                    tempMap.put(r.getRuleString(), r);
                });

                RulesService.setCollectRulesMap(tempMap);
            } catch (Exception e) {
                log.error("updataCollectRules 任务执行异常终止");
                log.error(e.getMessage());
            }
        } else {
            log.error("Collect Rules Map 暂停更新");
        }
    }


    @Scheduled(cron = "0 */3 * * * ?")
    public void insertCollectRules() {
        boolean executeFlag = true;
        executeFlag = ConfigUtil.getBooleanConfig("execute_flag");
        if (executeFlag) {
            try {
                log.error("执行收集标准入库任务 " + System.currentTimeMillis());
                if (RulesService.getCollectRulesMap().isEmpty()) {
                    log.error("当前收集的标准为空，终止插入任务 " + System.currentTimeMillis());
                } else {
                    RulesService.getCollectRulesMap().forEach((k, v) -> {
                        baseMapper.addCollect(v);
                    });
                    log.error("本次任务共收集问题标准" + RulesService.getCollectRulesMap().size() + "条");
                }
                RulesService.clearCollectRulesMap();
            } catch (Exception e) {
                log.error("insertCollectRules 任务执行异常终止");
                log.error(e.getMessage());
            }
        }
    }


}
