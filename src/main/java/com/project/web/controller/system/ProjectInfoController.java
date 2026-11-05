package com.project.web.controller.system;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.project.system.domain.ProjectInfo;
import com.project.system.service.IProjectInfoService;
import com.project.web.controller.common.BaseController;
import com.project.common.base.TableDataInfo;
import com.project.common.base.AjaxResult;

/**
 * 考勤项目 信息操作处理
 *
 * @author administrator
 * @date 2020-05-27
 */
@Controller
@RequestMapping("/system/projectInfo" )
public class ProjectInfoController extends BaseController {
    private String prefix = "system/projectInfo" ;

    @Autowired
    private IProjectInfoService projectInfoService;

    @RequiresPermissions("system:projectInfo:view" )
    @GetMapping()
    public String projectInfo() {
        return prefix + "/projectInfo" ;
    }

    /**
     * 查询考勤项目列表
     */
    @RequiresPermissions("system:projectInfo:list" )
    @PostMapping("/list" )
    @ResponseBody
    public TableDataInfo list(ProjectInfo projectInfo) {
        if(!getUser().isSuperAdmin()){
            projectInfo.setUserId(getUserId());
        }
        startPage();
        List<ProjectInfo> list = projectInfoService.selectProjectInfoList(projectInfo);
        return getDataTable(list);
    }

    /**
     * 新增考勤项目
     */
    @GetMapping("/add" )
    public String add() {
        return prefix + "/add" ;
    }

    /**
     * 新增保存考勤项目
     */
    @RequiresPermissions("system:projectInfo:add" )
    @PostMapping("/add" )
    @ResponseBody
    public AjaxResult addSave(ProjectInfo projectInfo) {
        projectInfo.setUserId(getUserId());
        return toAjax(projectInfoService.insertProjectInfo(projectInfo));
    }

    /**
     * 查看考勤项目
     */
    @GetMapping("/detail/{projectId}" )
    public String detail(@PathVariable("projectId" ) Long projectId, ModelMap mmap) {
        ProjectInfo projectInfo =projectInfoService.selectProjectInfoById(projectId);
        mmap.put("projectInfo" , projectInfo);
        return prefix + "/detail" ;
    }

    /**
     * 修改考勤项目
     */
    @GetMapping("/edit/{projectId}" )
    public String edit(@PathVariable("projectId" ) Long projectId, ModelMap mmap) {
        ProjectInfo projectInfo =projectInfoService.selectProjectInfoById(projectId);
        mmap.put("projectInfo" , projectInfo);
        return prefix + "/edit" ;
    }

    /**
     * 修改保存考勤项目
     */
    @RequiresPermissions("system:projectInfo:edit" )
    @PostMapping("/edit" )
    @ResponseBody
    public AjaxResult editSave(ProjectInfo projectInfo) {
        return toAjax(projectInfoService.updateProjectInfo(projectInfo));
    }

    /**
     * 删除考勤项目
     */
    @RequiresPermissions("system:projectInfo:remove" )
    @PostMapping("/remove" )
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(projectInfoService.deleteProjectInfoByIds(ids));
    }

}
