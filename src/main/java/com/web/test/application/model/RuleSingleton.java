package com.web.test.application.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

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

}
