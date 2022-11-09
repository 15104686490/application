package com.web.test.application.model;

import lombok.Data;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.model
 * @Project：application
 * @name：UserVo
 * @Date：2022/11/9 10:46
 * @Filename：UserVo
 */
@Data
public class UserVo {
    String userName;
    String password;
    String mailAddress;
}
