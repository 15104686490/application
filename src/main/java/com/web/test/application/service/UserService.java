package com.web.test.application.service;

import com.web.test.application.config.AppConstants;
import com.web.test.application.dao.BaseMapper;
import com.web.test.application.model.UserSingleton;
import com.web.test.application.model.UserVo;
import com.web.test.application.other.ResultTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.service
 * @Project：application
 * @name：UserService
 * @Date：2022/11/9 10:45
 * @Filename：UserService
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    BaseMapper baseMapper;

    /**
     * @param user
     * @return
     */
    public ResultTest userLogin(UserVo user) {
        String userName = user.getUserName();
        String password = user.getPassword();

        if (userName == null || password == null) {
            return new ResultTest<>(null, AppConstants.CLIENT_USER_INFO_NULL, "用户名或密码为空");
        }
        List<UserSingleton> list = new ArrayList<>();
        list = baseMapper.queryUsersByUserName(userName);

        log.error(userName);
        if (list.size() == 0) {
            return new ResultTest<>(null, AppConstants.SERVER_NO_SUCH_USERNAME, "此用户不存在");
        }

        if (list.size() > 1) {
            return new ResultTest<>(null, AppConstants.SERVER_USER_LOGIN_FAIL, "登录失败");
        }

        UserSingleton userSingleton = list.get(0);
        if (!password.equals(userSingleton.getPassword())) {
            return new ResultTest<>(null, AppConstants.CLIENT_USER_PASSWORD_ERROR, "密码错误，登录失败");
        }

        return new ResultTest<>(null, 200, "登录成功");
    }

    /**
     * @param user
     * @return
     */
    public ResultTest addNewUser(UserVo user) {
        String userName = user.getUserName();
        String password = user.getPassword();
        String mailAddress = user.getMailAddress();

        if (userName == null || password == null || mailAddress == null) {
            return new ResultTest<>(null, AppConstants.CLIENT_USER_INFO_NULL, "用户名或密码或邮箱为空");
        }

        List<UserSingleton> list = new ArrayList<>();
        list = baseMapper.queryUsersByUserName(userName);
        if (list.size() > 0) {
            return new ResultTest<>(null, AppConstants.CLIENT_USERNAME_REPEATABLE, "用户名已存在");
        }

        if (!emailFormat(mailAddress)) {
            return new ResultTest<>(null, AppConstants.CLIENT_USER_MAIl_ERROR, "邮箱格式存在问题");
        }



        baseMapper.addNewUser(new UserSingleton(userName, password, mailAddress));
        return new ResultTest<>(null, 200, "用户新增成功");
    }


    /**
     * @param email
     * @return
     */
    public boolean emailFormat(String email) {

        return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$",
                email);

    }


}
