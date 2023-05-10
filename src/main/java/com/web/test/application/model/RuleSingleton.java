package com.web.test.application.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 规则封装类
 */
@Getter
@Setter
@Slf4j
@AllArgsConstructor
@ToString
public class RuleSingleton {


    /**
     * 中文名称名称
     */
    String cnName;

    /**
     * 编号
     */
    String fullCode;

    /**
     * 完整名称（中文+编号）
     */
    String fullName;


    /**
     * 标准分类
     */
    String type;


    /**
     * 标准文档地址
     */
    String documentURL;

    public RuleSingleton(String cnName, String fullCode, String fullName, String type) {
        this.cnName = cnName;
        this.fullCode = fullCode;
        this.fullName = fullName;
        this.type = type;
    }
}
