package com.web.test.application.model;

import lombok.Data;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.model
 * @Project：application
 * @name：MailVerifyVo
 * @Date：2022/11/10 13:28
 * @Filename：MailVerifyVo
 */
@Data
public class MailVerifyVo {
    String mailAddress;
    String code;
}
