package com.project.common.constant;

/**
 * Shiro通用常量
 * 
 * @author
 */
public interface ShiroConstants
{
    /**
     * 当前登录的用户
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     * 用户名
     */
    public static final String CURRENT_USERNAME = "username";

    /**
     * 登录类型存储在session的键
     */
    public static final String LOGIN_TYPE_KEY = "login_type";
    /**
     * 手机号登录类型
     */
    public static final String LOGIN_TYPE_V_PHONE = "login_type_phone";
    /**
     * 用户名登录类型
     */
    public static final String LOGIN_TYPE_V_UNAME = "login_type_username";
    public static final String LOGIN_PHONE_CODE = "phone_code";

    public static final String RESET_PASSWORD_CODE = "reset_password_code";

    /**
     * 消息key
     */
    public static String MESSAGE = "message";

    /**
     * 错误key
     */
    public static String ERROR = "errorMsg";

    /**
     * 编码格式
     */
    public static String ENCODING = "UTF-8";

    /**
     * 当前在线会话
     */
    public String ONLINE_SESSION = "online_session";

    /**
     * 验证码key
     */
    public static final String CURRENT_CAPTCHA = "captcha";

    /**
     * 验证码开关
     */
    public static final String CURRENT_ENABLED = "captchaEnabled";

    /**
     * 验证码开关
     */
    public static final String CURRENT_TYPE = "captchaType";

    /**
     * 验证码
     */
    public static final String CURRENT_VALIDATECODE = "validateCode";

    /**
     * 验证码错误
     */
    public static final String CAPTCHA_ERROR = "captchaError";
}
