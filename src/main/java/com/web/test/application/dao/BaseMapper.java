package com.web.test.application.dao;


// import org.apache.ibatis.annotations.Mapper;
// import org.apache.ibatis.annotations.Select;

import com.web.test.application.model.CollectRuleSingleton;
import com.web.test.application.model.CommentSingleton;
import com.web.test.application.model.RuleSingleton;
import com.web.test.application.model.UserSingleton;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Insert({"insert into full_rules(cn_name, full_name, full_code, rule_type) values(#{cnName}, #{fullName}, #{fullCode}, #{type})"})
    public int add(RuleSingleton ruleSingleton);

    @Select("select distinct rule_type from full_rules;")
    public List<String> queryTypes();

    @Select("select rule_string, insert_time from collect_rules")
    public List<CollectRuleSingleton> queryCollectRules();

    @Insert({"insert into collect_rules(rule_string, insert_time) values(#{ruleString}, #{insertTime})"})
    public int addCollect(CollectRuleSingleton ruleSingleton);

    @Insert({"insert into comment (comment_id, state, comment_value, user_id) values (#{commentId}, " +
            "#{state}, #{commentTxt}, #{userId})"})
    public int addcomment(CommentSingleton commentSingleton);

    @Select("select comment_id, state, comment_value, user_id, create_time from comment order by create_time desc")
    public List<CommentSingleton> queryComments();

    @Select("select user_name, password, mail_address from user where user_name = #{userName}")
    public List<UserSingleton> queryUsersByUserName(String userName);

    @Update("update user set password = #{password}, update_time = CURRENT_TIMESTAMP where user_name = #{userName}")
    public int updatePassword(String userName, String password);

    @Insert({"insert into user (user_name, password, mail_address) values (#{userName}, " +
            "#{password}, #{mailAddress})"})
    public int addNewUser(UserSingleton userSingleton);



}
