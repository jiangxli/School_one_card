package com.project.web.controller.system;

import java.util.Date;
import java.util.List;

import com.project.common.utils.DateUtils;
import com.project.common.utils.ExcelUtil;
import com.project.system.domain.ProjectInfo;
import com.project.system.domain.SysUser;
import com.project.system.service.IProjectInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.project.system.domain.ProjectAttend;
import com.project.system.service.IProjectAttendService;
import com.project.web.controller.common.BaseController;
import com.project.common.base.TableDataInfo;
import com.project.common.base.AjaxResult;

/**
 * 考勤记录 信息操作处理
 *
 * @author administrator
 * @date 2020-05-27
 */
@Controller
@RequestMapping("/system/projectAttend" )
public class ProjectAttendController extends BaseController {
    private String prefix = "system/projectAttend" ;

    @Autowired
    private IProjectAttendService projectAttendService;
    @Autowired
    private IProjectInfoService projectInfoService;

    @GetMapping()
    public String projectAttend(Long projectId,ModelMap modelMap) {
        modelMap.put("projectId",projectId);
        return prefix + "/projectAttend" ;
    }

    @GetMapping("/my")
    public String projectAttendMy() {
        return prefix + "/projectAttendMy" ;
    }

    @PostMapping("/exportMy")
    @ResponseBody
    public AjaxResult exportMy(ProjectAttend projectAttend) {
        projectAttend.setUserId(getUserId());
        List<ProjectAttend> list = projectAttendService.selectProjectAttendList(projectAttend);
        ExcelUtil<ProjectAttend> util = new ExcelUtil<ProjectAttend>(ProjectAttend.class);
        return util.exportExcel(list, "我的考勤");
    }
    /**
     * 查询考勤记录列表
     */
    @RequiresPermissions("system:projectAttend:list" )
    @PostMapping("/list" )
    @ResponseBody
    public TableDataInfo list(ProjectAttend projectAttend) {
        startPage();
        List<ProjectAttend> list = projectAttendService.selectProjectAttendList(projectAttend);
        return getDataTable(list);
    }

    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProjectAttend projectAttend) {
        List<ProjectAttend> list = projectAttendService.selectProjectAttendList(projectAttend);
        ExcelUtil<ProjectAttend> util = new ExcelUtil<ProjectAttend>(ProjectAttend.class);
        return util.exportExcel(list, "考勤");
    }

    @PostMapping("/listMy" )
    @ResponseBody
    public TableDataInfo listMy(ProjectAttend projectAttend) {
        projectAttend.setUserId(getUserId());
        startPage();
        List<ProjectAttend> list = projectAttendService.selectProjectAttendList(projectAttend);
        return getDataTable(list);
    }

    /**
     * 新增考勤记录
     */
    @GetMapping("/add" )
    public String add(ModelMap modelMap) {
        modelMap.put("userName",getUser().getUserName());
        modelMap.put("loginName",getUser().getLoginName());
        ProjectInfo projectInfo =projectInfoService.selectProjectInfoById(getUser().getProjectId());
        modelMap.put("projectInfo" , projectInfo);
        return prefix + "/add" ;
    }

    /**
     * 新增保存考勤记录
     */
    @RequiresPermissions("system:projectAttend:add" )
    @PostMapping("/add" )
    @ResponseBody
    public AjaxResult addSave(ProjectAttend projectAttend) {
        projectAttend.setUserId(getUserId());
        ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(getUser().getProjectId());
        if(projectInfo == null){
            return error("您还未加入考勤项目！");
        }
        String date = DateUtils.getDate()+" "+projectInfo.getStartTime();
        if(new Date().getTime()> DateUtils.parseDate(date).getTime()){
            //迟到
            projectAttend.setStatus(1);
        }else {
            //正常
            projectAttend.setStatus(0);
        }
        projectAttend.setProjectId(projectInfo.getProjectId());
        return toAjax(projectAttendService.insertProjectAttend(projectAttend));
    }

    /**
     * 查看考勤记录
     */
    @GetMapping("/detail/{id}" )
    public String detail(@PathVariable("id" ) Long id, ModelMap mmap) {
        ProjectAttend projectAttend =projectAttendService.selectProjectAttendById(id);
        mmap.put("projectAttend" , projectAttend);
        return prefix + "/detail" ;
    }

    /**
     * 修改考勤记录
     */
    @GetMapping("/edit/{id}" )
    public String edit(@PathVariable("id" ) Long id, ModelMap mmap) {
        ProjectAttend projectAttend =projectAttendService.selectProjectAttendById(id);
        mmap.put("projectAttend" , projectAttend);
        return prefix + "/edit" ;
    }

    /**
     * 修改保存考勤记录
     */
    @RequiresPermissions("system:projectAttend:edit" )
    @PostMapping("/edit" )
    @ResponseBody
    public AjaxResult editSave(ProjectAttend projectAttend) {
        return toAjax(projectAttendService.updateProjectAttend(projectAttend));
    }

    /**
     * 删除考勤记录
     */
    @RequiresPermissions("system:projectAttend:remove" )
    @PostMapping("/remove" )
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(projectAttendService.deleteProjectAttendByIds(ids));
    }

}
