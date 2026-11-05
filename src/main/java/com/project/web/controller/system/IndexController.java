package com.project.web.controller.system;

import java.util.List;

import com.project.common.base.AjaxResult;
import com.project.common.utils.ShiroUtils;
import com.project.system.domain.ProjectAttend;
import com.project.system.domain.ProjectInfo;
import com.project.system.service.IGateInOutService;
import com.project.system.service.IProjectInfoService;
import com.project.system.service.ISysUserService;
import com.project.web.controller.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import com.project.common.config.Global;
import com.project.system.domain.SysMenu;
import com.project.system.domain.SysUser;
import com.project.system.service.ISysMenuService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 首页 业务处理
 *
 * @author
 */
@Controller
public class IndexController extends BaseController {
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private IGateInOutService gateInOutService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IProjectInfoService projectInfoService;
    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap) {
        // 取身份信息
        SysUser user = getUser();
        if(user == null){
            return "login";
        }
        // 根据用户id取出菜单
        List<SysMenu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("copyrightYear", Global.getCopyrightYear());
        mmap.put("logoText",Global.getLogoText());
        Integer gateKind = gateInOutService.selectKindLastTime(getUserId());
        mmap.put("gateKind",gateKind==null?2:gateKind);
        return "index";
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        return "main";
    }


    @GetMapping("/system/joinProject")
    public String joinProject(ModelMap mmap) {
        return "joinProject";
    }

    @PostMapping("/system/joinProject" )
    @ResponseBody
    public AjaxResult joinProject(Long projectId) {
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
        if(projectInfo == null){
            return error("该考勤码不存在！");
        }
        SysUser user = getUser();
        user.setProjectId(projectId);
        AjaxResult result =toAjax(userService.updateUser(user));
        ShiroUtils.setUser(user);
        return result;
    }
}
