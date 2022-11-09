package com.web.test.application.service;


import com.web.test.application.config.AppConstants;
import com.web.test.application.config.ConfigUtil;
import com.web.test.application.dao.BaseMapper;
import com.web.test.application.dao.RedisBaseDao;
import com.web.test.application.model.CodeUpdatePwdVo;
import com.web.test.application.model.UserSingleton;
import com.web.test.application.other.ResultTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String mailUserName;

    @Autowired
    RedisBaseDao redisBaseDao;

    @Autowired
    BaseMapper baseMapper;


    public ResultTest sendMail(String userName) {
        if (userName == null) {
            return new ResultTest(null, AppConstants.CLIENT_USER_INFO_NULL, "用户名为空");
        }
        List<UserSingleton> list = new ArrayList<>();
        list = baseMapper.queryUsersByUserName(userName);
        if(list.size() == 0){
            return new ResultTest<>(null, AppConstants.SERVER_NO_SUCH_USERNAME, "此用户不存在");
        }
        String mailAddress = list.get(0).getMailAddress();
        String key = AppConstants.MAIL_CODE_KEY_PRE + userName;
        Integer overtime = ConfigUtil.getIntegerConfig("code_over_time");
        StringBuilder stringBuilder = new StringBuilder();
        String code = getVerifyCode();
        stringBuilder.append("<html><head><title></title></head><body>");
        stringBuilder.append("您好<br/>");
        stringBuilder.append("您的验证码是：").append(code).append("<br/>");
        stringBuilder.append("您可以复制此验证码并返回至检测系统找回密码页面，以验证您的邮箱。<br/>");
        stringBuilder.append("此验证码只能使用一次，在");
        stringBuilder.append(overtime.toString());
        stringBuilder.append("分钟内有效。验证成功则自动失效。<br/>");
        stringBuilder.append("如果您没有进行上述操作，请忽略此邮件。");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        redisBaseDao.set(key, overtime * 60, code);
        log.error(redisBaseDao.get(key));
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(mailUserName);
            mimeMessageHelper.setTo(mailAddress);
            mimeMessage.setSubject("数字与智慧工程院-规范检测系统");
            mimeMessageHelper.setText(stringBuilder.toString(), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return new ResultTest<>(null, 200, "发送验证码成功");
    }


    public ResultTest codeUpdatePwd(CodeUpdatePwdVo codeUpdatePwdVo) {
        String userName = codeUpdatePwdVo.getUserName();
        String code = codeUpdatePwdVo.getCode();
        String password = codeUpdatePwdVo.getUpdatePassword();
        if (userName == null || code == null || password == null) {
            return new ResultTest<>(null, AppConstants.CLIENT_USER_INFO_NULL, "用户名、验证码、密码存在空值");
        } else {
            List<UserSingleton> list = new ArrayList<>();
            list = baseMapper.queryUsersByUserName(userName);
            if (list.size() == 0) {
                return new ResultTest<>(null, AppConstants.SERVER_NO_SUCH_USERNAME, "用户名不存在，请检查");
            }
            String key = AppConstants.MAIL_CODE_KEY_PRE + userName;
            String codeInCache = redisBaseDao.get(key);
            if (codeInCache == null) {
                return new ResultTest<>(null, AppConstants.CLINETS_CODE_OVERTIME, "验证码已过期");
            }

            if (!codeInCache.equals(code)) {
                return new ResultTest<>(null, AppConstants.CLINETS_CODE_ERROR, "验证码错误");
            }

            baseMapper.updatePassword(userName, password);
            redisBaseDao.expire(key);
            return new ResultTest<>(null, AppConstants.UPDATE_PASSWORD_SUCCESS, "密码更新成功");
        }
    }


    public String getVerifyCode() {
        long time = System.currentTimeMillis();
        String code = String.format("%06d", time % 10000L);
        return code;
    }
}
