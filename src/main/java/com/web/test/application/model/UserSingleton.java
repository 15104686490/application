package com.web.test.application.model;

import lombok.Data;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.model
 * @Project：application
 * @name：UserSingleton
 * @Date：2022/11/9 10:13
 * @Filename：UserSingleton
 */
@Data
public class UserSingleton {
    String userName;
    String password;
    String mailAddress;
    String createdTime;
    String updatedTime;

    public UserSingleton(String userName, String password, String mailAddress) {
        this.userName = userName;
        this.password = password;
        this.mailAddress = mailAddress;
    }
}
