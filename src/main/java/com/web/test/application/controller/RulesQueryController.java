package com.web.test.application.controller;

import com.web.test.application.other.PageQuery;
import com.web.test.application.other.PageResult;
import com.web.test.application.other.ResultTest;
import com.web.test.application.service.RulesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 处理查询类请求
 */
@Slf4j
@RequestMapping("/query")
@RestController
public class RulesQueryController {


    /*static{
        ZipSecureFile.setMinInflateRatio(-1D);
    }*/


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

    //@RequestMapping(value = "/queryRulesPages", method = RequestMethod.POST)
    @PostMapping(value = "/queryRulesPages")
    public PageResult queryRules(@RequestBody PageQuery pageQuery) {
        if (pageQuery == null) {
            return new PageResult(null, 500, 1, 0, 0, "查询异常，查询条件为空"
                    , 0);
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
                    + e.getMessage(), 0);
        }
    }

    @ResponseBody
    @GetMapping(value = "/getRulesPages")
    public PageResult getRules(//@RequestBody PageQuery pageQuery,
                               @RequestParam int currentPage, @RequestParam int pageCapacity, @RequestParam String types,
                               @RequestParam String queryName) {
        List<String> typesList = new ArrayList<>(Arrays.asList(types.split(";")));
        PageQuery pageQuery = new PageQuery();
        pageQuery.setQueryName(queryName);
        pageQuery.setCurrentPage(currentPage);
        pageQuery.setPageCapacity(pageCapacity);
        pageQuery.setTypes(typesList);
        if (pageQuery == null) {
            return new PageResult(null, 500, 1, 0, 0, "查询异常，查询条件为空",
                    0);
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
                    + e.getMessage(), 0);
        }
    }


    @PostMapping("/postMethod")
    public String testPostMothod() {
        log.error("test post method...");
        return "ok!!";
    }


    @GetMapping("/getMethod")
    public String insertMothod(@RequestParam String type) {
        // log.error("test post method...");
        if(type == null|| type.isEmpty()){
            type = "待分类";
        }
        rulesService.getDataFromExcel("C:\\updateExcel\\update.xls", type);
        return "ok!!";
    }


    @GetMapping("/TxtFileMethod")
    public String txtFileMothod() {
        // log.error("test post method...");
        // rulesService.getDataFromExcel("C:\\updateExcel\\update.xls");
        rulesService.checkDataFromTxt("D:\\光伏加空格.txt");
        return "ok!!";
    }


}
