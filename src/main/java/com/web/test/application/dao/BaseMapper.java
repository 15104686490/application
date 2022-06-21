package com.web.test.application.dao;


// import org.apache.ibatis.annotations.Mapper;
// import org.apache.ibatis.annotations.Select;

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

    @Select("select cn_name, full_code, full_name from full_rules")
    public List<RuleSingleton> queryRulesList();



    @Insert({"insert into full_rules(cn_name, full_name, full_code) values(#{cnName}, #{fullName}, #{fullCode})"})
    int add(RuleSingleton ruleSingleton);
}
