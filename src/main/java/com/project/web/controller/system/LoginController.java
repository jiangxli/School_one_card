package com.project.web.controller.system;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.project.common.config.Global;
import com.project.common.constant.ShiroConstants;
import com.project.common.shiro.service.PasswordService;
import com.project.common.shiro.web.filter.CaptchaValidateFilter;
import com.project.common.utils.SendEmailUtil;
import com.project.common.utils.TimeUuidUtil;
import com.project.system.domain.UserSecretProtect;
import com.project.system.service.ISysUserService;
import com.project.system.service.IUserSecretProtectService;
import com.project.web.controller.common.BaseController;
import com.project.common.utils.ShiroUtils;
import com.project.system.domain.SysUser;
import com.project.system.service.ISysRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.project.common.base.AjaxResult;
import com.project.common.utils.str.StringUtils;
import com.project.common.utils.web.ServletUtils;
import org.springframework.web.servlet.ModelAndView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * 登录验证
 *
 * @author
 */
@Controller
public class LoginController extends BaseController {
    @Autowired
    ISysRoleService roleService;

    @Autowired
    private PasswordService passwordService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IUserSecretProtectService userSecretProtectService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, ModelMap mmap) {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request)) {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }
        mmap.put("copyrightYear", Global.getCopyrightYear());
        mmap.put("logoText",Global.getLogoText());
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe,HttpServletRequest request) {
        request.getSession().setAttribute(ShiroConstants.LOGIN_TYPE_KEY,ShiroConstants.LOGIN_TYPE_V_UNAME);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            SysUser user = ShiroUtils.getUser(subject);
            ShiroUtils.setUser(user);
            return success();
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    @GetMapping("/unauth")
    public String unauth() {
        return "/error/unauth";
    }

    @PostMapping("/getSecretProtect")
    @ResponseBody
    public List<UserSecretProtect> getSecretProtect(String loginName) {
        UserSecretProtect protect = new UserSecretProtect();
        protect.getParams().put("loginName",loginName);
        return userSecretProtectService.selectUserSecretProtectList(protect);
    }



    @GetMapping("/forgetPwd")
    public String forgetPwd(ModelMap mmap) {
        mmap.put("copyrightYear", Global.getCopyrightYear());
        mmap.put("logoText",Global.getLogoText());
        return "forgetPwd";
    }

    @PutMapping("/forgetPwd")
    @ResponseBody
    public AjaxResult forgetPwd(String loginName,String password,Long question,String answer) {
        UserSecretProtect protect = new UserSecretProtect();
        protect.setId(question);
        protect.setAnswer(answer);
        if(userSecretProtectService.selectUserSecretProtectList(protect).isEmpty()){
            return error("回答不正确！");
        }
        SysUser user = new SysUser();
        user.setLoginName(loginName);
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(password);
        user.getParams().put("resetPwdByLoginName","1");
        return toAjax(userService.resetUserPwd(user));
    }



    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;
    /**
     * 验证码生成
     */
    @GetMapping(value = "/captcha/captchaImage")
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            HttpSession session = request.getSession();
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setContentType("image/jpeg");

            String type = request.getParameter("type");
            String capStr = null;
            String code = null;
            BufferedImage bi = null;
            if ("math".equals(type)) {
                String capText = captchaProducerMath.createText();
                capStr = capText.substring(0, capText.lastIndexOf("@"));
                code = capText.substring(capText.lastIndexOf("@") + 1);
                bi = captchaProducerMath.createImage(capStr);
            } else if ("char".equals(type)) {
                capStr = code = captchaProducer.createText();
                bi = captchaProducer.createImage(capStr);
            }
            session.setAttribute(Constants.KAPTCHA_SESSION_KEY, code);
            out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
