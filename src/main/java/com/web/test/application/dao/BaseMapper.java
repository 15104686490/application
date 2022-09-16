package com.web.test.application.dao;


// import org.apache.ibatis.annotations.Mapper;
// import org.apache.ibatis.annotations.Select;

import com.web.test.application.model.CollectRuleSingleton;
import com.web.test.application.model.RuleSingleton;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 */
@Mapper
public interface BaseMapper {

    @Select("select cn_name, full_code, full_name, rule_type from full_rules")
    public List<RuleSingleton> queryRulesList();


    @Select("select cn_name, full_code, full_name, rule_type from full_rules")
    public List<RuleSingleton> queryRulesListWithType();


    @Insert({"insert into full_rules(cn_name, full_name, full_code) values(#{cnName}, #{fullName}, #{fullCode})"})
    public int add(RuleSingleton ruleSingleton);


    @Select("select distinct rule_type from full_rules;")
    public List<String> queryTypes();

    @Select("select rule_string, insert_time from collect_rules")
    public List<CollectRuleSingleton> queryCollectRules();

    @Insert({"insert into collect_rules(rule_string, insert_time) values(#{ruleString}, #{insertTime})"})
    public int addCollect(CollectRuleSingleton ruleSingleton);



}
