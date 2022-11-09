package com.web.test.application.model;

import lombok.Data;
import lombok.ToString;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.model
 * @Project：application
 * @name：CodeUpdatePwdVo
 * @Date：2022/11/9 9:59
 * @Filename：CodeUpdatePwdVo
 */
@Data
@ToString
public class CodeUpdatePwdVo {
    String userName;
    String code;
    String updatePassword;
}
