package com.web.test.application.controller;

import com.web.test.application.model.CodeUpdatePwdVo;
import com.web.test.application.model.UserVo;
import com.web.test.application.other.ResultTest;
import com.web.test.application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：lzy15
 * @Package：com.web.test.application.controller
 * @Project：application
 * @name：LoginController
 * @Date：2022/11/9 10:45
 * @Filename：LoginController
 */
@Slf4j
@RequestMapping("/login")
@RestController
public class LoginController {
    @Autowired
    UserService userService;

    @PostMapping("/userLogin")
    public ResultTest userLogin(@RequestBody UserVo user) {
        try {
            return userService.userLogin(user);
        } catch (Exception e) {
            /*throw new RuntimeException(e);*/

            log.error("异常导致用户登录失败"  + e.getMessage());
            return new ResultTest(null,500,"用户登录失败");
        }
    }

    @PostMapping("/addUser")
    public ResultTest addNewUser(@RequestBody UserVo user) {
        try {
            return userService.addNewUser(user);
        } catch (Exception e) {
            /*throw new RuntimeException(e);*/
            log.error("异常导致新增用户失败" + e.getMessage());
            return new ResultTest(null,500,"用户注册失败");
        }
    }
}
