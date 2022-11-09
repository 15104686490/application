package com.web.test.application.controller;


import com.web.test.application.model.CodeUpdatePwdVo;
import com.web.test.application.other.ResultTest;
import com.web.test.application.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/mail")
@RestController
public class MailController {

    @Autowired
    MailService mailService;


    @GetMapping("/getCode")
    public ResultTest getCode(@RequestParam String userName) {
        try {
            return mailService.sendMail(userName);
        } catch (Exception e) {
            //throw new RuntimeException(e);
            log.error("验证码发送过程存在异常 " + e.getMessage());
            return new ResultTest<>(null, 500, "验证码发送异常");
        }
        /*return new ResultTest<>(null,200,"验证码发送成功");*/
    }


    @PostMapping("/codeUpdatePwd")
    public ResultTest codeUpdatePwd(@RequestBody CodeUpdatePwdVo codeUpdatePwdVo) {
        try {
            return mailService.codeUpdatePwd(codeUpdatePwdVo);
        } catch (Exception e) {
            log.error("更新密码失败，存在异常 " + e.getMessage());
            return new ResultTest<>(null, 500, "更新密码失败");
        }
    }


}
