package com.web.test.application.controller;

import com.web.test.application.other.PageQuery;
import com.web.test.application.other.PageResult;
import com.web.test.application.other.ResultTest;
import com.web.test.application.service.RulesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理查询类请求
 */
@Slf4j
@RequestMapping("/query")
@RestController
public class RulesQueryController {


    @Autowired
    RulesService rulesService;

    @ResponseBody
    @GetMapping("/queryTypes")
    public List queryRuleTypes() {
        return rulesService.queryRuleType();
    }


    @ResponseBody
    @GetMapping("/queryAllRulesWithType")
    public ResultTest queryAllRulesWithType() {
        return new ResultTest(rulesService.queryAllRulesWithType(), 200, "ok");
    }


    @ResponseBody
    @GetMapping("/queryRulesType")
    public ResultTest queryRulesType() {
        List<String> res = new ArrayList<>();
        try {
            res = rulesService.getRulesType();
            return new ResultTest(res, 200, "");
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("查询标准类型异常： " + e.getMessage());
            return new ResultTest(res, 500, "查询标准类型异常");
        }
    }

    @RequestMapping(value = "/queryRulesPages", method = RequestMethod.POST)
    public PageResult queryRules(@RequestBody PageQuery pageQuery) {
        if (pageQuery == null) {
            return new PageResult(null, 500, 1, 0, 0, "查询异常，查询条件为空");
        }

        if (pageQuery.getCurrentPage() <= 0) {
            pageQuery.setCurrentPage(1);
        }

        if (pageQuery.getPageCapacity() < 10) {
            pageQuery.setPageCapacity(10);
        }
        /*rulesService.queryFullNameList();*/
        if (pageQuery.getQueryName() == null) {
            pageQuery.setQueryName("");
        }
        try {
            PageResult res = rulesService.queryRulePage(pageQuery);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("标准分页查询异常 :" + e.getMessage());
            return new PageResult(null, 500, 1, 1, 10, "标准分页查询异常 : "
                    + e.getMessage());
        }
    }


}
