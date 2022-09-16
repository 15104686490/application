package com.web.test.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class CollectRuleSingleton {


    /**
     * 收集标准的文本内容
     */
    String ruleString;


    /**
     * 插入的时间戳
     */
    String insertTime;
}
