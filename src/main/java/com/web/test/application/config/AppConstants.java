package com.web.test.application.config;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.config
 * @Project：application
 * @name：AppConstants
 * @Date：2022/11/9 13:21
 * @Filename：AppConstants
 */
public class AppConstants {
    /**
     * 验证码key前缀
     */
    public final static String MAIL_CODE_KEY_PRE = "MAIL_CODE_KEY_PRE_";

    /**
     * 用户不存在
     */
    public final static int SERVER_NO_SUCH_USERNAME = 50011;

    /**
     * 验证码过期
     */
    public final static int CLINETS_CODE_OVERTIME = 40011;

    /**
     * 验证码错误
     */
    public final static int CLINETS_CODE_ERROR = 40012;

    /**
     * 更新密码成功
     */
    public final static int UPDATE_PASSWORD_SUCCESS = 20001;

    /**
     * 用户信息存在空值
     */
    public final static int CLIENT_USER_INFO_NULL = 40010;


    /**
     * 登录失败
     */
    public final static int SERVER_USER_LOGIN_FAIL = 50015;


    /**
     * 密码错误
     */
    public final static int CLIENT_USER_PASSWORD_ERROR = 40015;

    /**
     * 用户名重复
     */
    public final static int CLIENT_USERNAME_REPEATABLE = 40013;

    /**
     * 邮箱格式错误
     */
    public final static int CLIENT_USER_MAIl_ERROR = 40018;
}
