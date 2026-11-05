package com.project.common.exception;

import com.project.common.constant.PermissionConstants;
import com.project.common.utils.web.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.project.common.base.AjaxResult;

/**
 * 自定义异常处理器
 *
 * @author
 */
@RestControllerAdvice
public class DefaultExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    /**
     * 权限校验失败
     */
    @ExceptionHandler(AuthorizationException.class)
    public AjaxResult handleAuthorizationException(AuthorizationException e) {
        log.error(e.getMessage(), e);
        return AjaxResult.error(getMsg(e.getMessage()));
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public AjaxResult handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return AjaxResult.error("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult notFount(RuntimeException e) {
        log.error("运行时异常:", e);
        return AjaxResult.error("运行时异常:" + e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return AjaxResult.error("服务器错误，请联系管理员");
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult demoModeException(DemoModeException e) {
        return AjaxResult.error("演示模式，不允许操作");
    }

    /**
     * 权限错误消息提醒
     *
     * @param permissionsStr
     * @return 错误信息
     */
    public static String getMsg(String permissionsStr) {
        String permission = StringUtils.substringBetween(permissionsStr, "[", "]");
        String msg = MessageUtils.message("no.view.permission", permission);
        if (StringUtils.endsWithIgnoreCase(permission, PermissionConstants.ADD_PERMISSION)) {
            msg = MessageUtils.message("no.create.permission", permission);
        } else if (StringUtils.endsWithIgnoreCase(permission, PermissionConstants.EDIT_PERMISSION)) {
            msg = MessageUtils.message("no.update.permission", permission);
        } else if (StringUtils.endsWithIgnoreCase(permission, PermissionConstants.REMOVE_PERMISSION)) {
            msg = MessageUtils.message("no.delete.permission", permission);
        } else if (StringUtils.endsWithIgnoreCase(permission, PermissionConstants.EXPORT_PERMISSION)) {
            msg = MessageUtils.message("no.export.permission", permission);
        } else if (StringUtils.endsWithAny(permission,
                new String[]{PermissionConstants.VIEW_PERMISSION, PermissionConstants.LIST_PERMISSION})) {
            msg = MessageUtils.message("no.view.permission", permission);
        }
        return msg;
    }
}
