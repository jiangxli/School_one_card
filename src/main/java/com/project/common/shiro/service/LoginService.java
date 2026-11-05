package com.project.common.shiro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.project.common.constant.ShiroConstants;
import com.project.common.constant.UserConstants;
import com.project.common.enums.UserStatus;
import com.project.common.utils.DateUtils;
import com.project.common.utils.web.ServletUtils;
import com.project.common.utils.ShiroUtils;
import com.project.common.exception.user.CaptchaException;
import com.project.common.exception.user.UserBlockedException;
import com.project.common.exception.user.UserDeleteException;
import com.project.common.exception.user.UserNotExistsException;
import com.project.common.exception.user.UserPasswordNotMatchException;
import com.project.system.domain.SysUser;
import com.project.system.service.ISysUserService;

import javax.servlet.http.HttpSession;

/**
 * 登录校验方法
 *
 * @author
 */
@Component
public class LoginService {
    @Autowired
    private PasswordService passwordService;

    @Autowired
    private ISysUserService userService;

    /**
     * 登录
     */
    public SysUser login(String username, String password) {
        // 验证码校验
        if (!StringUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA))) {
            throw new CaptchaException();
        }
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new UserNotExistsException();
        }
        //先判断是否是手机号登录
        HttpSession session = ServletUtils.getSession();
        if(ShiroConstants.LOGIN_TYPE_V_PHONE.equals(session.getAttribute(ShiroConstants.LOGIN_TYPE_KEY))){
            if(!password.equals(session.getAttribute(ShiroConstants.LOGIN_PHONE_CODE))){
                throw new RuntimeException("验证码不正确！");
            }
            return userService.selectUserByPhoneNumber(username);
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new UserPasswordNotMatchException();
        }

        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new UserPasswordNotMatchException();
        }

        // 查询用户信息
        SysUser user = userService.selectUserByLoginName(username);

        if (user == null && maybeMobilePhoneNumber(username)) {
            user = userService.selectUserByPhoneNumber(username);
        }

        if (user == null && maybeEmail(username)) {
            user = userService.selectUserByEmail(username);
        }

        if (user == null) {
            throw new UserNotExistsException();
        }

        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            throw new UserDeleteException();
        }

        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            throw new UserBlockedException(user.getRemark());
        }

        passwordService.validate(user, password);

        recordLoginInfo(user);
        return user;
    }

    private boolean maybeEmail(String username) {
        if (!username.matches(UserConstants.EMAIL_PATTERN)) {
            return false;
        }
        return true;
    }

    private boolean maybeMobilePhoneNumber(String username) {
        if (!username.matches(UserConstants.MOBILE_PHONE_NUMBER_PATTERN)) {
            return false;
        }
        return true;
    }

    /**
     * 记录登录信息
     */
    public void recordLoginInfo(SysUser user) {
        user.setLoginIp(ShiroUtils.getIp());
        user.setLoginDate(DateUtils.getNowDate());
        userService.updateUserInfo(user);
    }
}
