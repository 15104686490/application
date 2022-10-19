package com.web.test.application.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 对检测过程中问题结果的封装
 */
@Getter
@Setter
@Slf4j
@AllArgsConstructor
@ToString
public class CheckResultSingleton {


    /**
     * 文本内容
     */
    String txtValue;


    /**
     * 原因
     */
    String reason;



    String md5OfString;
}
