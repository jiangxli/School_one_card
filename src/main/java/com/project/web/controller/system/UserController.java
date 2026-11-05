package com.project.web.controller.system;

import java.util.List;

import com.project.common.utils.TimeUuidUtil;
import com.project.common.utils.file.FileUploadUtils;
import com.project.system.domain.UserSecretProtect;
import com.project.system.service.IUserSecretProtectService;
import com.project.web.controller.common.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.project.common.base.AjaxResult;
import com.project.common.utils.ExcelUtil;
import com.project.common.utils.str.StringUtils;
import com.project.common.shiro.service.PasswordService;
import com.project.common.utils.ShiroUtils;
import com.project.common.base.TableDataInfo;
import com.project.system.domain.SysUser;
import com.project.system.service.ISysRoleService;
import com.project.system.service.ISysUserService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户信息
 *
 * @author
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {
    @Autowired private com.project.system.service.CardService cardService;
    private String prefix = "system/user";

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;


    @Autowired
    private PasswordService passwordService;
    @Autowired
    private IUserSecretProtectService secretProtectService;

    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user() {
        return prefix + "/user";
    }

    @RequiresPermissions("system:user:edit")
    @GetMapping("/card/{userId}")
    public String card(@PathVariable Long userId,ModelMap model){
        SysUser user=userService.selectUserById(userId);
        if(user==null||!"0".equals(user.getDelFlag()))throw new IllegalArgumentException("用户不存在");
        Object token=ShiroUtils.getSession().getAttribute("cardToken");
        if(token==null){token=java.util.UUID.randomUUID().toString();ShiroUtils.getSession().setAttribute("cardToken",token);}
        model.put("user",user);model.put("card",cardService.info(userId));model.put("history",cardService.history(userId));model.put("cancelled",cardService.cancelled(userId));model.put("token",token);model.put("requestKey",java.util.UUID.randomUUID().toString());return prefix+"/card";
    }
    @RequiresPermissions("system:user:edit")
    @PostMapping("/card/{userId}") @ResponseBody
    public AjaxResult cardAction(@PathVariable Long userId,String token,String action,String requestKey){
        if(token==null||!token.equals(ShiroUtils.getSession().getAttribute("cardToken")))return error("页面过期，请刷新");
        try{if("issue".equals(action))return AjaxResult.success("开卡成功").put("card",cardService.issue(userId));
        if("write".equals(action))return AjaxResult.success("写卡成功（未连接实体设备）").put("receipt",cardService.write(userId,requestKey,ShiroUtils.getUserId()));
        if("cancel".equals(action)){cardService.cancel(userId,ShiroUtils.getUserId());return success("卡片已注销，钱包余额及流水保留");}
        return error("无效操作");}catch(IllegalArgumentException e){return error(e.getMessage());}
    }

    @RequiresPermissions("system:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @PostMapping("/listAll")
    @ResponseBody
    public TableDataInfo listAll(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }
    @RequiresPermissions("system:user:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "user");
    }
    /**
     * 新增用户
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("roles", roleService.selectRoleAll());
        return prefix + "/add";
    }

    /**
     * 新增保存用户
     */
    @RequiresPermissions("system:user:add")
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public AjaxResult addSave(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && SysUser.isSuperAdmin(user.getUserId())) {
            return error("不允许修改超级管理员用户");
        }
        user.setUserId(TimeUuidUtil.get16UUID());
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(userService.insertUser(user));
    }
    /**
     * 修改用户
     */
    @GetMapping("/detail/{userId}")
    public String detail(@PathVariable("userId") Long userId, ModelMap mmap) {
        mmap.put("user", userService.selectUserById(userId));
        mmap.put("roles", roleService.selectRolesByUserId(userId));
        return prefix + "/detail";
    }
    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap) {
        mmap.put("user", userService.selectUserById(userId));
        mmap.put("roles", roleService.selectRolesByUserId(userId));
        return prefix + "/edit";
    }

    /**
     * 修改保存用户
     */
    @PostMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public AjaxResult editSave(SysUser user) {
        if (StringUtils.isNotNull(user.getUserId()) && SysUser.isSuperAdmin(user.getUserId())) {
            return error("不允许修改超级管理员用户");
        }
        user.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(userService.updateUser(user));
    }

    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap) {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/resetPwd";
    }

    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwdSave(SysUser user) {
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        return toAjax(userService.resetUserPwd(user));
    }

    @RequiresPermissions("system:user:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(userService.deleteUserByIds(ids));
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 校验用户名
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(SysUser user) {
        return userService.checkLoginNameUnique(user.getLoginName());
    }

    /**
     * 校验手机号码
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(SysUser user) {
        return userService.checkPhoneUnique(user);
    }

    /**
     * 校验email邮箱
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(SysUser user) {
        return userService.checkEmailUnique(user);
    }




    /**
     * 个人信息修改保存
     * @param mmap
     * @return
     */
    @GetMapping("/updateSelf")
    public String updateSelf( ModelMap mmap) {
        mmap.put("user", userService.selectUserById(this.getUserId()));
        mmap.put("roles", roleService.selectRolesByUserId(this.getUserId()));
        return prefix + "/profile/updateSelf";
    }
    @PostMapping("/updateSelf")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public AjaxResult updateSelf(SysUser user, MultipartFile file) throws Exception {
        user.setRoleId(null);user.setStatus(null);user.setDelFlag(null);user.setLoginName(null);user.setPassword(null);user.setSalt(null);
        user.setUserId(this.getUserId());
        user.setUpdateBy(ShiroUtils.getLoginName());
        if(file!=null){
            user.setAvatar(FileUploadUtils.upload(file,null));
        }
        AjaxResult ajaxResult = toAjax(userService.updateUser(user));
        ShiroUtils.setUser(userService.selectUserById(user.getUserId()));
        return ajaxResult;
    }

    /**
     * 密保信息修改
     * @param mmap
     * @return
     */
    @GetMapping("/updateSecret")
    public String updateSecret( ModelMap mmap) {
        UserSecretProtect protect = new UserSecretProtect();
        protect.setUserId(this.getUserId());
        List<UserSecretProtect> list = secretProtectService.selectUserSecretProtectList(protect);
        while (list.size()<2){
            list.add(new UserSecretProtect());
        }
        mmap.put("secrets",list);
        mmap.put("user",this.getUser());
        return prefix + "/profile/updateSecret";
    }
    @PostMapping("/updateSecret")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public AjaxResult updateSecret(String question0,String answer0,String question1,String answer1) throws Exception {
        secretProtectService.deleteUserSecretProtectByUserId(this.getUserId());
        return toAjax(secretProtectService.insertUserSecretProtect(new String[]{question0,question1},new String[]{answer0,answer1},this.getUserId()));
    }

    /**
     * 检查密码是否正确
     * @param password
     * @return
     */
    @GetMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password) {
        SysUser user = getUser();
        String encrypt = new Md5Hash(user.getLoginName() + password + user.getSalt()).toHex().toString();
        if (user.getPassword().equals(encrypt)) {
            return true;
        }
        return false;
    }

    /**
     * 个人修改密码
     * @param mmap
     * @return
     */
    @GetMapping("/updatePwd")
    public String updatePwd(ModelMap mmap) {
        mmap.put("user", userService.selectUserById(this.getUserId()));
        return prefix + "/profile/resetPwd";
    }

    /**
     * 个人修改密码保存
     * @param user
     * @return
     */
    @PostMapping("/updatePwd")
    @ResponseBody
    public AjaxResult updatePwd(SysUser user) {
        user.setUserId(getUserId());user.setLoginName(getUser().getLoginName());
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        int rows = userService.resetUserPwd(user);
        if (rows > 0) {
            setUser(userService.selectUserById(user.getUserId()));
            return success();
        }
        return error();
    }
}